package com.aebiz.app.web.modules.controllers.front.pc.member;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.member.modules.models.Member_cart;
import com.aebiz.app.member.modules.services.MemberCartService;
import com.aebiz.app.order.commons.utils.OrderStatusUtil;
import com.aebiz.app.order.modules.models.Order_goods;
import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.models.em.*;
import com.aebiz.app.order.modules.services.OrderGoodsService;
import com.aebiz.app.order.modules.services.OrderLogService;
import com.aebiz.app.order.modules.services.OrderMainService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.shop.modules.services.ShopExpressService;
import com.aebiz.app.sys.modules.models.Sys_dict;
import com.aebiz.app.sys.modules.services.SysDictService;
import com.aebiz.app.web.commons.utils.KuaiDi100Util;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.DateUtil;
import com.aebiz.commons.utils.SpringUtil;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/member/order")
public class PcMemberOrderController {
    private Log log = Logs.getLog(PcMemberOrderController.class);
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private OrderMainService orderMainService;
    @Autowired
    private AccountUserService accountUserService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private MemberCartService memberCartService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private ShopAreaService shopAreaService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private GoodsProductService goodsProductService;
    @Autowired
    private ShopExpressService shopExpressService;
    @Autowired
    private PropertiesProxy config;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(/*@RequestParam(value = "length",required = false) int length,
                        @RequestParam(value = "start",required = false) int start,
                        @RequestParam(value = "draw",required = false) int draw,*/ ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns, HttpServletRequest req) {

        //获取当前会员的Id
        String accountId = StringUtil.getMemberUid();
        //
        Account_user accountUser = accountUserService.fetch(Cnd.where("accountId", "=", accountId));
        if (accountUser.getLoginname() != null) {
            req.setAttribute("memberUsername", accountUser.getLoginname());
        } else if (accountUser.getEmail() != null) {
            req.setAttribute("memberUsername", accountUser.getEmail());
        } else {
            req.setAttribute("memberUsername", accountUser.getMobile());
        }
        List<Member_cart> cartList = memberCartService.query(Cnd.where("accountId", "=", accountId));
        req.setAttribute("accountId", accountId);
        req.setAttribute("cartList", cartList.size());
        Account_info info = accountInfoService.fetch(Cnd.where("id", "=", accountId));
        req.setAttribute("accountInfo", info);

        //订单取消原因，通过数据字典获取，传递到页面供选择
        req.setAttribute("cancelReasonList", sysDictService.getSubListByCode("order_cancel_reason"));
        return "pages/front/pc/member/order/order";
    }

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    public String memberOrder(@RequestParam("pageNo") Integer pageNo, @RequestParam("status") Integer status, @RequestParam(value = "id", required = false) String id, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns, HttpServletRequest req) {
        //获取当前会员的Id
        String accountId = StringUtil.getMemberUid();
        //查询当前会员的订单
        Cnd cnd = Cnd.NEW();
        cnd.and("accountId", "=", accountId).and("payType", "!=", 4).and("delFlag", "=", false).desc("orderAt");
        if (Strings.isNotBlank(id)) {
            SqlExpressionGroup el = new SqlExpressionGroup();
            el.or("id", "=", id);
            cnd.and(el);
        }
        switch (status) {
            case 0://查询全部订单
                break;
            case 1://查询待付款的订单
                cnd.and("payStatus", "=", 0).and("orderStatus", "=", 1);
                break;
            case 2://查询待发货的订单
                cnd.and("payStatus", "=", 3).and("deliveryStatus", "=", 0).and("orderStatus", "=", 1);
                break;
            case 3://查询待收货的订单
                cnd.and("deliveryStatus", "=", 3).and("getStatus", "=", 0).and("orderStatus", "=", 1);
                break;
            case 4://查询待评价的订单
                cnd.and("getStatus", "=", 1).and("feedStatus", "=", 0).and("orderStatus", "=", 1);
                break;
            default:
        }
        int pageSize = 10;
        int start = (pageNo - 1) * pageSize;
        NutMap map = orderMainService.data(10, start, 0, order, columns, cnd, "^(storeMain|goodsList|deliveryDetailList)$");
        int totalCount = (int) map.get("recordsFiltered");//count总数
        int pageTotal = totalCount / pageSize + 1;
        List<Order_main> orderMainList = (List<Order_main>) map.get("data");
        if (orderMainList != null) {
            for (Order_main orderMain : orderMainList) {
                //计算订单待支付的剩余时间
                if (orderMain.getPayStatus() == OrderPayStatusEnum.NO.getKey() && orderMain.getOrderStatus() == OrderStatusEnum.ACTIVE.getKey()) {
                    Integer payAt = orderMain.getOrderAt();
                    Integer nowAt = DateUtil.getTime(new Date());
                    int endTime = nowAt - payAt;
                    int limitTime = 60 * 60 * 2;
                    if (endTime < limitTime) {
                        //在两个小时之内,计算订单剩余时间
                        int hour = (limitTime - endTime) / (60 * 60);
                        int min = (limitTime - endTime - hour * 60 * 60) / 60;
                        orderMain.setEndTime(hour + "时" + min + "分");
                    }
                }
                List<Order_goods> orderGoodsList = orderMain.getGoodsList();
                if (orderGoodsList != null) {
                    for (Order_goods orderGoods : orderGoodsList) {
                        orderGoods.setImgUrl(goodsProductService.getProductImage(orderGoods.getSku()));
                    }
                }
                //订单物流信息
                if (Strings.isNotBlank(orderMain.getExpressId()) && Strings.isNotBlank(orderMain.getExpressNo())) {
                    orderMain.setExpressInfo(KuaiDi100Util.getInfoUrl(shopExpressService.fetch(orderMain.getExpressId()).getCode(), orderMain.getExpressNo(), config.get("ext.kuaidi100_key", "")));
                }
            }
        }
        req.setAttribute("obj", map);
        req.setAttribute("pageTotal", pageTotal);
        req.setAttribute("orderStatus", SpringUtil.getBean(OrderStatusUtil.class));
        req.setAttribute("area", shopAreaService);
        return "pages/front/pc/member/order/order_data";
    }

    /**
     * 取消订单将订单状态改成“取消订单”
     *
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @SJson
    public Object update(@RequestParam("id") String id, @RequestParam("dictId") String dictId, HttpServletRequest req) {
        //通过数据字典，获取订单取消的原因
        Sys_dict sysDict = sysDictService.fetch(dictId);
        String dictName = sysDict.getName();

        //拼写日志取消原因
        StringBuilder note = new StringBuilder("会员取消订单：");
        note.append(dictName);

        int time = (int) (System.currentTimeMillis() / 1000);
        String opBy = StringUtil.getMemberUid();
        Order_main orderMain = orderMainService.fetch(id);
        orderMain.setOpAt(time);
        orderMain.setOpBy(opBy);
        orderMain.setOrderStatus(OrderStatusEnum.CANCEL.getKey());
        orderMain.setMark(dictName);
        orderMainService.update(orderMain);
        orderLogService.createLog(orderMain, StringUtil.getMemberUsername(), note.toString(), OrderLogBehaviorEnum.CANCEL.getKey());
        return Result.success("globals.result.success");
    }

    @RequestMapping(value = "takeDelivery", method = RequestMethod.POST)
    @SJson
    @SLog(description = "确认收货")
    public Object takeDelivery(@RequestParam("orderId") String orderId) {
        try {
            StringBuilder note = new StringBuilder("确认收货");
            //更新订单收货状态
            Order_main order = orderMainService.fetch(orderId);
            Map map = new HashMap();
            if (order.getPayType() == OrderPayTypeEnum.ONLINE.getKey()) {
                orderMainService.update(Chain.make("getStatus", OrderGetStatusEnum.ALL.getKey()).add("getAt", (int) (System.currentTimeMillis() / 1000)), Cnd.where("id", "=", orderId).and("delFlag", "=", false));
                map.put("type", "0");
                map.put("at", "");
            } else {
                //不是线上转账的收货的同时更新支付时间
                int time = (int) (System.currentTimeMillis() / 1000);
                orderMainService.update(Chain.make("getStatus", OrderGetStatusEnum.ALL.getKey()).add("getAt", time).add("payAt", time), Cnd.where("id", "=", orderId).and("delFlag", "=", false));
                map.put("type", "1");
                map.put("at", time);
            }
            Order_main orderMain = orderMainService.fetch(orderId);
            //记录日志
            orderLogService.createLog(orderMain, StringUtil.getMemberUsername(), note.toString(), OrderLogBehaviorEnum.UPDATE.getKey());
            return Result.success("globals.result.success", map);
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }

    }


    /**
     * 跳转订单支付页面
     *
     * @param id
     * @param req
     * @return
     */
    @RequestMapping(value = "/count/{id}", method = RequestMethod.GET)
    public String toCount(@PathVariable("id") String id, HttpServletRequest req) {
        Order_main orderMain = orderMainService.fetch(id);
        if (orderMain == null || orderMain.getDelFlag() || orderMain.getPayStatus() >= 3) {
            return "redirect:/404";//非法的支付链接或者支付的订单信息不存在
        }
        Order_goods goods = new Order_goods();
        //计算商品件数总数量
        int goodsNum = 0;
        orderMainService.fetchLinks(orderMain, "^(accountInfo|goodsList)$");
        List<Order_goods> orderGoodsList = orderMain.getGoodsList();
        if (orderGoodsList != null) {
            goods = orderGoodsList.get(0);
            orderGoodsService.fetchLinks(goods, "storeMain");
            for (Order_goods orderGoods : orderGoodsList) {
                goodsNum += orderGoods.getBuyNum();
            }
        }
        Account_user accountUser = accountUserService.fetch(Cnd.where("accountId", "=", orderMain.getAccountId()));
        req.setAttribute("orderId", id);
        req.setAttribute("order", orderMain);
        req.setAttribute("accountUser", accountUser);
        req.setAttribute("goods", goods);
        req.setAttribute("payMoney", orderMain.getPayMoney());
        req.setAttribute("goodsNum", goodsNum);
        req.setAttribute("area", shopAreaService);
        return "pages/front/pc/member/order/orderPay";
    }


    /**
     * 根据订单号查询订单是否支付成功
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/isPaySuccess", method = RequestMethod.POST)
    @SJson
    public Object isPaySuccess(@RequestParam("orderId") String orderId) {
        try {
            Order_main orderMain = orderMainService.fetch(Cnd.where("delFlag", "=", false).and("id", "=", orderId));
            if (orderMain == null) {
                return Result.error("订单不存在");
            }
            if (OrderPayStatusEnum.PAYALL.getKey() != orderMain.getPayStatus()) {
                return Result.error("订单未支付");
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return Result.error("globals.result.error");
        }

    }
}
