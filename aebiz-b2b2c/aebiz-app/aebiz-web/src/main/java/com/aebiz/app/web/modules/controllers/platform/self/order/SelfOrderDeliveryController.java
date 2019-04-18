package com.aebiz.app.web.modules.controllers.platform.self.order;

import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.order.modules.models.Order_delivery;
import com.aebiz.app.order.modules.models.Order_delivery_detail;
import com.aebiz.app.order.modules.models.Order_goods;
import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.models.em.OrderDeliveryStatusEnum;
import com.aebiz.app.order.modules.models.em.OrderExpressStatusEnum;
import com.aebiz.app.order.modules.services.OrderDeliveryDetailService;
import com.aebiz.app.order.modules.services.OrderDeliveryService;
import com.aebiz.app.order.modules.services.OrderGoodsService;
import com.aebiz.app.order.modules.services.OrderMainService;
import com.aebiz.app.shop.modules.models.Shop_express;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.shop.modules.services.ShopExpressService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
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

/**
 * 自营平台-发货单管理
 */
@Controller
@RequestMapping("/platform/self/order/delivery")
public class SelfOrderDeliveryController {

    private static final Log log = Logs.get();

    @Autowired
	private OrderDeliveryService orderDeliveryService;

    @Autowired
    private OrderMainService orderMainService;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private ShopExpressService shopExpressService;

    @Autowired
    private OrderDeliveryDetailService orderDeliveryDetailService;

    @Autowired
    private ShopAreaService shopAreaService;

    @Autowired
    private GoodsProductService goodsProductService;

    @RequestMapping("")
    @RequiresPermissions("self.order.delivery")
	public String index(HttpServletRequest req) {
        String storeId  = Strings.isNotBlank(StringUtil.getStoreId()) ? StringUtil.getStoreId() : "2017060000000001";
        //查询物流信息
        List<Shop_express> shopExpressList = shopExpressService.query(Cnd.where("delFlag","=",false));
        req.setAttribute("expressList",shopExpressList);
        //查询未发货单数量
        Integer noSendNum = orderDeliveryService.count(Cnd.where("expressStatus","=",OrderExpressStatusEnum.NOSEND.getKey()).and("delFlag","=",false).and("storeId","=",storeId));
        //查询已发货的数量
        Integer sendNum = orderDeliveryService.count(Cnd.where("expressStatus","=",OrderExpressStatusEnum.SEND.getKey()).and("delFlag","=",false).and("storeId","=",storeId));
        NutMap map = new NutMap();
        map.put("noSendNum",noSendNum);
        map.put("sendNum",sendNum);
        req.setAttribute("obj",map);
        return "pages/platform/self/order/delivery/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("self.order.delivery")
    public Object data(@RequestParam(value = "deliveryName",required = false) String deliveryName,
                       @RequestParam(value = "deliveryMobile",required = false) String deliveryMobile,
                       @RequestParam(value = "expressNo",required = false) String expressNo,
                       @RequestParam("expressStatus") int expressStatus, DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
		if(Strings.isNotBlank(deliveryName)){
		    cnd.and("deliveryName","like",deliveryName.trim()+"%");
        }
        if(Strings.isNotBlank(deliveryMobile)){
            cnd.and("deliveryMobile","like",deliveryMobile.trim()+"%");
        }
        if(Strings.isNotBlank(expressNo)){
		    cnd.and("expressNo","like","%"+expressNo.trim()+"%");
        }
        String storeId  = Strings.isNotBlank(StringUtil.getStoreId()) ? StringUtil.getStoreId() : "2017060000000001";
		cnd.and("expressStatus","=",expressStatus).and("delFlag","=",false).and("storeId","=",storeId);

		//未发货Tab,按操作时间降序排列
		if(OrderExpressStatusEnum.NOSEND.getKey() == expressStatus){
            cnd.desc("opAt");
        }

		NutMap map = orderDeliveryService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, "detailList");
        List<Order_delivery> orderDeliveryList = (List<Order_delivery>) map.get("data");
        if(orderDeliveryList != null && orderDeliveryList.size() > 0){
            for(Order_delivery orderDelivery : orderDeliveryList){
                if(orderDelivery != null){
                    List<Order_delivery_detail> orderDeliveryDetailList = orderDelivery.getDetailList();
                    if(orderDeliveryDetailList != null && orderDeliveryDetailList.size() > 0){
                        for(Order_delivery_detail detail : orderDeliveryDetailList){
                            orderDeliveryDetailService.fetchLinks(detail,"orderGoods");
                            Order_goods orderGoods = detail.getOrderGoods();
                            if(orderGoods != null){
                                orderGoods.setAccountUser(accountUserService.getField("^(loginname|mobile)$",Cnd.where("accountId","=",orderGoods.getAccountId())));
                            }
                            orderGoodsService.fetchLinks(detail.getOrderGoods(),"orderMain");
                            orderGoods.setImgUrl(goodsProductService.getProductImage(orderGoods.getSku()));
                        }
                    }
                }

            }
        }
    	return map;
    }

    /**
     * 新增发货单和发货单明细,生成发货单后,需要把订单状态改为备货中
     * @param orderGoods
     * @param remark
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveOrderDelivery")
    @SJson
    @SLog(description = "创建配货单")
    //@RequiresPermissions("self.order.delivery.create")
    public Object saveOrderDelivery(@RequestParam("id") String id,@RequestParam("orderGoods") String  orderGoods,
           @RequestParam(value = "remark",required = false) String remark, HttpServletRequest req) throws Exception {
        try {
            Order_main order_main =
                    orderMainService.fetch(Cnd.NEW().where("delFlag","=",false).and("id","=",id));
            if(order_main == null){
                return Result.error("订单不存在");
            }
            List<Order_goods> orderGoodsList =  Json.fromJsonAsList(Order_goods.class,orderGoods);
            if(orderGoodsList != null ){
                for(Order_goods goods : orderGoodsList){
                    //查询当前订单商品发货信息
                    Order_goods oldGoods = orderGoodsService.fetch(goods.getId());
                    Integer noSendNum = oldGoods.getBuyNum() - oldGoods.getSendNum();
                    if(goods.getSendNum() > noSendNum){
                        return  Result.error("发货数量大于该订单未发货的数量");
                    }
                }
                orderDeliveryService.saveOrderDelivery(orderGoodsList, remark, order_main);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    /**
     * 到发货单查看页面
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("self.order.delivery")
    public String detail(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            Order_delivery obj = orderDeliveryService.fetch(id);
            if(obj == null){
                return  "redirect:/404";
            }
            orderDeliveryService.fetchLinks(obj,"detailList");
            req.setAttribute("obj", obj);
            if(obj.getDetailList() != null){
                //获取订单信息，并传值到页面
                Order_main orderMain = orderMainService.fetch(obj.getDetailList().get(0).getOrderId());
                if(orderMain == null){
                    return  "redirect:/404";
                }
                //加载会员信息
                Account_user accountUser = accountUserService.fetch(Cnd.where("accountId","=",orderMain.getAccountId()));
                accountUserService.fetchLinks(accountUser,"accountInfo");
                req.setAttribute("accountUser",accountUser);
                //发货单详情
                List<Order_delivery_detail> orderDeliveryDetailList = obj.getDetailList();
                String ids ="";
                if(orderDeliveryDetailList != null){
                    for(Order_delivery_detail detail :orderDeliveryDetailList){
                        ids += (detail.getOrderId()+",");
                    }
                }
                //根据条件获取符合创建配货单的订单信息
                List<Order_main> orderList = new ArrayList<>();
                if(ids.length() > 0 ){
                    orderList = orderMainService.query(Cnd.where("id","in",ids.substring(0,ids.lastIndexOf(","))));
                }
                if(orderList != null){
                    for(Order_main main : orderList){
                        orderMainService.fetchLinks(main,"goodsList");
                    }
                }
                req.setAttribute("orderList", orderList);
                req.setAttribute("area",shopAreaService);
            }
        }else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/self/order/delivery/detail";
    }

    /**
     * 取消发货：
     *    1、删除发货单、发货单明细
     *    2、更新订单明细sendNum
     *    3、更新订单发货状态
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/cancelOrderDelivery")
    @SJson
    @SLog(description = "取消发货")
    @RequiresPermissions("self.order.delivery.delete")
    public Object cancelOrderDelivery(@RequestParam("id") String id, HttpServletRequest req) {
        try {
            orderDeliveryService.cancelOrderDelivery(id);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    /**
     * 到打印配货单页面
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/printDelivery/{id}")
    @RequiresPermissions("self.order.delivery")
    public String printDelivery(@PathVariable String id, HttpServletRequest req){
        if (!Strings.isBlank(id)) {
            Order_delivery obj = orderDeliveryService.fetch(id);
            orderDeliveryService.fetchLinks(obj, "detailList");
            req.setAttribute("obj", obj);
            if (obj.getDetailList() != null) {
                //获取订单信息，并传值到页面
                Order_main orderMain = orderMainService.fetch(obj.getDetailList().get(0).getOrderId());
                //加载会员信息
                Account_user accountUser = accountUserService.fetch(orderMain.getAccountId());
                accountUserService.fetchLinks(accountUser, "accountInfo");
                req.setAttribute("accountUser", accountUser);
                //根据条件获取符合创建配货单的订单信息
                List<Order_delivery_detail> orderDeliveryDetailList = obj.getDetailList();
                String ids ="";
                if(orderDeliveryDetailList != null){
                    for(Order_delivery_detail detail :orderDeliveryDetailList){
                        ids += (detail.getOrderId()+",");
                    }
                }
                List<Order_main> orderList = new ArrayList<>();
                if(ids.length() > 0 ){
                    orderList = orderMainService.query(Cnd.where("id","in",ids.substring(0,ids.lastIndexOf(","))));
                }
                if (orderList != null) {
                    for (Order_main main : orderList) {
                        orderMainService.fetchLinks(main, "goodsList");
                    }
                }
                req.setAttribute("orderList", orderList);
            }
        }
        return  "pages/platform/self/order/delivery/printDelivery";
    }

    /**
     * 打印配货单
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/confirmPrintDelivery")
    @SJson
    @SLog(description = "打印配货单")
    public Object confirmPrintDelivery(@RequestParam("id") String id, HttpServletRequest req){
           try {
               //orderDeliveryService.updateExpressStatus(id, OrderExpressStatusEnum.PRINTEXPRESS.getKey());
               return Result.success("globals.result.success");
           }catch (Exception e){
               return Result.error("globals.result.error");
           }
    }


    /**
     * 到打印快递单页面
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/printExpress/{id}")
    @RequiresPermissions("self.order.delivery")
    public String printExpress(@PathVariable String id, HttpServletRequest req){
        if (!Strings.isBlank(id)) {
            Order_delivery obj = orderDeliveryService.fetch(id);
            orderDeliveryService.fetchLinks(obj, "detailList");
            req.setAttribute("obj", obj);
            if (obj.getDetailList() != null) {
                //获取订单信息，并传值到页面
                Order_main orderMain = orderMainService.fetch(obj.getDetailList().get(0).getOrderId());
                //加载会员信息
                Account_user accountUser = accountUserService.fetch(orderMain.getAccountId());
                accountUserService.fetchLinks(accountUser, "accountInfo");
                req.setAttribute("accountUser", accountUser);
                //根据条件获取符合创建配货单的订单信息
                List<Order_main> orderList = orderMainService.query(
                        Cnd.where("accountId", "=", orderMain.getAccountId()) //同一会员
                                .and("deliveryStatus", "<", OrderDeliveryStatusEnum.ALL.getKey())  //未全部发货(小于3)
                                .asc("id"));
                if (orderList != null) {
                    for (Order_main main : orderList) {
                        orderMainService.fetchLinks(main, "goodsList");
                    }
                }
                req.setAttribute("orderList", orderList);
            }
        }
        return  "pages/platform/self/order/delivery/printExpress";
    }

    /**
     * 打印快递单
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/confirmPrintExpress/{id}")
    @SJson
    @SLog(description = "打印快递单")
    public Object confirmPrintExpress(@PathVariable String id, HttpServletRequest req){
        try {
            //orderDeliveryService.updateExpressStatus(id, OrderExpressStatusEnum.CONFRIMSEND.getKey());
            return Result.success("globals.result.success");
        }catch (Exception e){
            return Result.error("globals.result.error");
        }
    }

    /**
     * 确认发货
     * @param req
     * @return
     */
    @RequestMapping("/confirmDelivry")
    @SJson
    @SLog(description = "确认发货")
    public Object confirmDelivry(Order_delivery orderDelivery, HttpServletRequest req){
        try {
            orderDeliveryService.confirmOrderDelivery(orderDelivery);
            return Result.success("globals.result.success");
        }catch (Exception e){
            return Result.error("globals.result.error");
        }
    }

    /**
     * 跳过打印快递单，更新发货单状态为待确认发货
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/skipPrint")
    @SJson
    @SLog(description = "跳过打印快递单")
   /* @RequiresPermissions("self.order.delivery.skipPrint")*/
    public Object skipPrint(@RequestParam("id") String id, HttpServletRequest req) {
        try {
            //orderDeliveryService.updateExpressStatus(id,OrderExpressStatusEnum.CONFRIMSEND.getKey());
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

}
