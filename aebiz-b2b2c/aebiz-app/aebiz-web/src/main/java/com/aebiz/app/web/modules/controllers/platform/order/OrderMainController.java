package com.aebiz.app.web.modules.controllers.platform.order;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.order.commons.vo.OrderCheckBoxStatus;
import com.aebiz.app.order.modules.models.*;
import com.aebiz.app.order.modules.models.em.OrderDeliveryStatusEnum;
import com.aebiz.app.order.modules.models.em.OrderPayStatusEnum;
import com.aebiz.app.order.modules.models.em.OrderPayTypeEnum;
import com.aebiz.app.order.modules.models.em.OrderStatusEnum;
import com.aebiz.app.order.modules.services.*;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.sys.modules.models.Sys_dict;
import com.aebiz.app.sys.modules.services.SysDictService;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/platform/order/main")
public class OrderMainController {
    private static final Log log = Logs.get();
    @Autowired
	private OrderMainService orderMainService;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private OrderAfterMainService orderAfterMainService;

    @Autowired
    private OrderDeliveryDetailService orderDeliveryDetailService;

    @Autowired
    private OrderPayTransferService orderPayTransferService;

    @Autowired
    private OrderLogService orderLogService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private ShopAreaService shopAreaService;


    @RequestMapping("")
    @RequiresPermissions("order.manage.main")
	public String index(HttpServletRequest req) {
        NutMap map = new NutMap();
        //查询待发货的订单数量
        Integer waitDeliveryNum = orderMainService.count(Cnd.where("deliveryStatus","<",OrderDeliveryStatusEnum.ALL.getKey())
                .and("payStatus","=",OrderPayStatusEnum.PAYALL.getKey()).and("payType"," in",OrderPayTypeEnum.ONLINE.getKey()+","+OrderPayTypeEnum.TRANSFER.getKey())
                .and("orderStatus","=",OrderStatusEnum.ACTIVE.getKey()).and("delFlag","=",false));
        //查询待审核的订单数量
        Integer waitVerifyNum = orderMainService.count(Cnd.where("orderStatus","=",OrderStatusEnum.WAITVERIFY.getKey()).and("delFlag","=",false));
        //查询待支付的数量
        Integer waitPayNum = orderMainService.count(Cnd.where("payStatus","<",OrderPayStatusEnum.PAYALL.getKey()).and("payType"," in",OrderPayTypeEnum.ONLINE.getKey()+","+OrderPayTypeEnum.TRANSFER.getKey()).and("orderStatus","=",OrderStatusEnum.ACTIVE.getKey()).and("delFlag","=",false));
        //货到付款的订单数量
        Integer deliveryPayNum = orderMainService.count(Cnd.where("payType","in", OrderPayTypeEnum.CASH.getKey()+","+OrderPayTypeEnum.POS.getKey()+","+OrderPayTypeEnum.ALIQRCODE.getKey()).and("orderStatus","=",OrderStatusEnum.ACTIVE.getKey()).and("delFlag","=",false));
        //关闭订单原因
        List<Sys_dict> sysDictList = sysDictService.query(Cnd.where("delFlag","=",false).and("code","=","order_close_reason"));
        if(sysDictList != null && sysDictList.size() > 0){
            Sys_dict sysDict = sysDictList.get(0);
            List<Sys_dict> reasonList = sysDictService.query(Cnd.where("delFlag","=",false).and("parentId","=",sysDict.getId()));
            req.setAttribute("reasonList",reasonList);
        }
        map.put("waitDeliveryNum",waitDeliveryNum);
        map.put("waitVerifyNum",waitVerifyNum);
        map.put("waitPayNum",waitPayNum);
        map.put("deliveryPayNum",deliveryPayNum);
        // TODO: 2017/5/25 超时订单和预售订单未知
        req.setAttribute("obj",map);
        return "pages/platform/order/main/index";
	}


    /**
     * 订单查询
     * @param id
     * @param loginname
     * @param checkedStatus
     * @param status
     * @param length
     * @param start
     * @param draw
     * @param order
     * @param columns
     * @return
     */
	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("order.manage.main")
    public Object data(@RequestParam(value = "id",required = false) String id,
                       @RequestParam(value = "loginname",required = false) String loginname,
                       @RequestParam(value = "checkedStatus",required = false) String checkedStatus,
                       @RequestParam("status") Integer status,
                       @RequestParam("length") int length,
                       @RequestParam("start") int start,
                       @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if(Strings.isNotBlank(id)){
            cnd.and("id","=",id.trim());
        }
        if(Strings.isNotBlank(loginname)){
            String accId="";
            List<Account_user> accountUserList = accountUserService.query("accountId",Cnd.where("loginname","=",loginname));
            if(accountUserList != null && accountUserList.size() > 0){
                for(Account_user accountUser:accountUserList){
                    accId = accountUser.getAccountId();
                }
                cnd.and("accountId","=",accId);
            }else{
                cnd.and("accountId","=","-1");
            }
        }
        if(Strings.isNotBlank(checkedStatus)){
            //选中的状态
            List<OrderCheckBoxStatus> checkBoxStatusList = Json.fromJsonAsList(OrderCheckBoxStatus.class,checkedStatus);
            SqlExpressionGroup el = new SqlExpressionGroup();
            for(int i = 0;i<checkBoxStatusList.size();i++){
                el.or(checkBoxStatusList.get(i).getName(),"=",checkBoxStatusList.get(i).getVal());
            }
            cnd.and(el);
            if(status == 0 ){
                cnd.and("orderStatus","=",OrderStatusEnum.ACTIVE.getKey());
            }
        }
        cnd.and("delFlag","=",false);
        switch (status){
            case 0:
                cnd.desc("opAt");
                break;
            case 1:
                cnd.and("deliveryStatus","<",OrderDeliveryStatusEnum.ALL.getKey())
                        .and("payStatus","=",OrderPayStatusEnum.PAYALL.getKey()).and("payType"," in",OrderPayTypeEnum.ONLINE.getKey()+","+OrderPayTypeEnum.TRANSFER.getKey())
                        .and("orderStatus","=",OrderStatusEnum.ACTIVE.getKey()).desc("payAt");
                break;
            case 2:
                cnd.and("orderStatus","=",OrderStatusEnum.WAITVERIFY.getKey());
                break;
            case 3:
                cnd.and("payStatus","<",OrderPayStatusEnum.PAYALL.getKey()).and("payType"," in",OrderPayTypeEnum.ONLINE.getKey()+","+OrderPayTypeEnum.TRANSFER.getKey()).and("orderStatus","=",OrderStatusEnum.ACTIVE.getKey());
                break;
            case 4:
                cnd.and("payType","in", OrderPayTypeEnum.CASH.getKey()+","+OrderPayTypeEnum.POS.getKey()+","+OrderPayTypeEnum.ALIQRCODE.getKey()).and("orderStatus","=",OrderStatusEnum.ACTIVE.getKey());
                break;
            default:
        }
        //订单时间降序
        cnd.desc("orderAt");
        NutMap map = orderMainService.data(length, start, draw, order, columns, cnd, "^(accountInfo|storeMain|goodsList)$");
        List<Order_main> orderMainList = (List<Order_main>) map.get("data");
        if(orderMainList != null){
            for(Order_main orderMain :orderMainList){
                Account_info accountInfo = orderMain.getAccountInfo();
                if(accountInfo != null){
                    orderMain.setAccountUser(accountUserService.getField("^(loginname|mobile)$",Cnd.where("accountId","=",accountInfo.getId())));
                }
                List<Order_goods> orderGoodsList = orderMain.getGoodsList();
                if(orderGoodsList != null){
                    for(Order_goods orderGoods:orderGoodsList){
                        orderGoods.setImgUrl(goodsProductService.getProductImage(orderGoods.getSku()));
                    }
                }

            }
        }
        return map;
    }

    /**
     * 订单查看功能
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            Order_main orderMain = orderMainService.fetch(id);
            if(orderMain == null){
                return "redirect:/404";
            }
            orderMainService.fetchLinks(orderMain,"accountInfo");
            Account_user accountUser = accountUserService.fetch(Cnd.where("accountId","=",orderMain.getAccountId()));
            orderMain.setAccountUser(accountUser);
            req.setAttribute("obj", orderMain);
            List<Order_goods> orderGoodsList = orderGoodsService.query(Cnd.where("delFlag","=",false).and("orderId","=",id));
            //加载订单商品信息
            req.setAttribute("orderGoods",orderGoodsList);
            //物流信息
            List<Order_delivery_detail> orderDeliveryDetailList = orderDeliveryDetailService.query(Cnd.where("delFlag","=",false).and("orderId","=",id));
            String expressId ="";
            String expressName = "";
            if(orderDeliveryDetailList != null && orderDeliveryDetailList.size() > 0){
                for(Order_delivery_detail detail :orderDeliveryDetailList){
                    expressId += (expressId + ",");
                    expressName += (expressName + ",");
                }
                req.setAttribute("expressId",expressId.substring(0,expressId.lastIndexOf(",")));
                req.setAttribute("expressName",expressName.substring(0,expressName.lastIndexOf(",")));
            }
            //关闭订单原因
            List<Sys_dict> sysDictList = sysDictService.query(Cnd.where("delFlag","=",false).and("code","=","order_close_reason"));
            if(sysDictList != null && sysDictList.size() > 0){
                Sys_dict sysDict = sysDictList.get(0);
                List<Sys_dict> reasonList = sysDictService.query(Cnd.where("delFlag","=",false).and("parentId","=",sysDict.getId()));
                req.setAttribute("reasonList",reasonList);
            }
            //支付凭证信息
            List<Order_pay_transfer> orderPayTransferList = orderPayTransferService.query(Cnd.where("delFlag","=",false).and("orderId","=",id));
            req.setAttribute("payTransfers",orderPayTransferList);
            //售后信息
            List<Order_after_main> orderAfterMainList = orderAfterMainService.query(Cnd.where("orderId","=",id).and("delFlag","=",false));
            req.setAttribute("orderAfterMainList",orderAfterMainList);
            //订单日志信息
            List<Order_log> orderLogList = orderLogService.query(Cnd.where("delFlag","=",false).and("orderId","=",id).desc("opAt"));
            orderLogService.fetchLinks(orderLogList,"accountInfo");
            req.setAttribute("orderLogList",orderLogList);
            req.setAttribute("area",shopAreaService);
        }else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/order/main/detail";
    }
}
