package com.aebiz.app.web.modules.controllers.platform.order;

import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.order.modules.models.Order_delivery;
import com.aebiz.app.order.modules.models.Order_delivery_detail;
import com.aebiz.app.order.modules.models.Order_goods;
import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.models.em.OrderExpressStatusEnum;
import com.aebiz.app.order.modules.services.OrderDeliveryDetailService;
import com.aebiz.app.order.modules.services.OrderDeliveryService;
import com.aebiz.app.order.modules.services.OrderGoodsService;
import com.aebiz.app.order.modules.services.OrderMainService;
import com.aebiz.app.shop.modules.models.Shop_express;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.shop.modules.services.ShopExpressService;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
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
@RequestMapping("/platform/order/delivery")
public class OrderDeliveryController {
    private static final Log log = Logs.get();
    @Autowired
	private OrderDeliveryService orderDeliveryService;

    @Autowired
    private OrderDeliveryDetailService orderDeliveryDetailService;

    @Autowired
    private OrderMainService orderMainService;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private ShopExpressService shopExpressService;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private ShopAreaService shopAreaService;

    @RequestMapping("")
    @RequiresPermissions("order.delivery.main")
	public String index(HttpServletRequest req) {
        //查询物流信息
        List<Shop_express> shopExpressList = shopExpressService.query(Cnd.where("delFlag","=",false));
        req.setAttribute("expressList",shopExpressList);
        //查询未发货单数量
        Integer noSendNum = orderDeliveryService.count(Cnd.where("expressStatus","=", OrderExpressStatusEnum.NOSEND.getKey()).and("delFlag","=",false));
        //查询已发货的数量
        Integer sendNum = orderDeliveryService.count(Cnd.where("expressStatus","=",OrderExpressStatusEnum.SEND.getKey()).and("delFlag","=",false));
        NutMap map = new NutMap();
        map.put("noSendNum",noSendNum);
        map.put("sendNum",sendNum);
        req.setAttribute("obj",map);

        return "pages/platform/order/delivery/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("order.delivery.main")
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
        cnd.and("expressStatus","=",expressStatus).and("delFlag","=",false);

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



    @RequestMapping("/detail/{id}")
    @RequiresPermissions("order.delivery.main")
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
        return "pages/platform/order/delivery/detail";
    }


}
