package com.aebiz.app.order.modules.services.impl;

import com.aebiz.app.order.modules.models.Order_delivery;
import com.aebiz.app.order.modules.models.Order_delivery_detail;
import com.aebiz.app.order.modules.models.Order_goods;
import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.models.em.OrderDeliveryStatusEnum;
import com.aebiz.app.order.modules.models.em.OrderExpressStatusEnum;
import com.aebiz.app.order.modules.models.em.OrderLogBehaviorEnum;
import com.aebiz.app.order.modules.services.*;
import com.aebiz.app.shop.modules.models.Shop_express;
import com.aebiz.app.shop.modules.services.ShopExpressService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDeliveryServiceImpl extends BaseServiceImpl<Order_delivery> implements OrderDeliveryService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private OrderMainService orderMainService;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private OrderDeliveryDetailService orderDeliveryDetailService;

    @Autowired
    private OrderLogService orderLogService;

    @Autowired
    private ShopExpressService shopExpressService;

    /**
     * 创建发货单
     * @param orderGoodsList
     * @param remark
     */
    @Override
    @Transactional
    public void saveOrderDelivery(List<Order_goods> orderGoodsList, String remark, Order_main order_main){
        //1、生成发货单主表信息（order_delivery）
        Order_delivery order_delivery = new Order_delivery();
        //order_delivery.setCheckNo("checkNo");
        order_delivery.setDeliveryName(order_main.getDeliveryName());
        order_delivery.setDeliveryProvince(order_main.getDeliveryProvince());
        order_delivery.setDeliveryCity(order_main.getDeliveryCity());
        order_delivery.setDeliveryCounty(order_main.getDeliveryCounty());
        order_delivery.setDeliveryTown(order_main.getDeliveryTown());
        order_delivery.setDeliveryAddress(order_main.getDeliveryAddress());
        order_delivery.setDeliveryMobile(order_main.getDeliveryMobile());
        order_delivery.setDeliveryPhone(order_main.getDeliveryPhone());
        order_delivery.setDeliveryPostcode(order_main.getDeliveryPostcode());
        order_delivery.setExpressId(order_main.getExpressId());
        order_delivery.setExpressName(order_main.getExpressName());
        order_delivery.setStoreId(order_main.getStoreId());
        order_delivery = this.insert(order_delivery);
        String deliveryId = order_delivery.getId();
        order_delivery.setCheckNo("No"+deliveryId);
        order_delivery.setOpAt((int)(System.currentTimeMillis()/1000));
        order_delivery.setOpBy(StringUtil.getUid());
        this.update(order_delivery);
        //2、生成发货单详细信息（order_delivery_detail）
        List<String> orderIds = new ArrayList<String>();
        for (Order_goods orderGoods : orderGoodsList){
            //页面传递过来的格式：orderGoodsId1|sendNum1,orderGoodsId2|sendNum2
            //String[] datas = orderGoodsId.split("\\|");
            int sendNum = orderGoods.getSendNum(); //当前商品的发货数量

            //获取订单明细信息
            Order_goods order_goods = orderGoodsService.fetch(orderGoods.getId());
            String orderId = order_goods.getOrderId();

            //拼接OrderId，去除重复的id，用来更新订单的状态
            if(!orderIds.contains(orderId)){
                orderIds.add(orderId);
            }

            //保存发货单明细信息
            Order_delivery_detail order_delivery_detail = new Order_delivery_detail();
            order_delivery_detail.setDeliveryId(deliveryId);
            order_delivery_detail.setOrderId(orderId);
            order_delivery_detail.setOrderGoodsId(orderGoods.getId());
            order_delivery_detail.setGoodsId(order_goods.getGoodsId());
            order_delivery_detail.setProductId(order_goods.getProductId());
            order_delivery_detail.setName(order_goods.getName());
            order_delivery_detail.setSku(order_goods.getSku());
            order_delivery_detail.setSpec(order_goods.getSpec());
            //order_delivery_detail.setProductNo(order_goods.get);
            order_delivery_detail.setSendNum(sendNum);
            order_delivery_detail.setOpAt((int)(System.currentTimeMillis()/1000));
            order_delivery_detail.setOpBy(StringUtil.getUid());
            orderDeliveryDetailService.insert(order_delivery_detail);

            //更新订单详情里面的发货数量（sendNum）
            order_goods.setSendNum(order_goods.getSendNum()+sendNum);
            order_goods.setOpAt((int)(System.currentTimeMillis()/1000));
            order_goods.setOpBy(StringUtil.getUid());
            orderGoodsService.update(order_goods);
        }

        //3、操作记录记录（变更状态为：配货中 并记录日志）
        for (String orderId : orderIds){

            Order_main main = orderMainService.fetch(orderId);
            main.setDeliveryStatus(OrderDeliveryStatusEnum.WAIT.getKey()); //配货中
            main.setOpAt((int)(System.currentTimeMillis()/1000));
            main.setOpBy(StringUtil.getUid());
            main.setMark(remark);
            orderMainService.update(main);

            //创建操作日志
            orderLogService.createLog(main,StringUtil.getUsername(),"更改订单发货状态为配货中", OrderLogBehaviorEnum.UPDATE.getKey());
        }
    }

    /**
     * 取消发货(单个取消)
     * @param id
     */
    @Override
    @Transactional
    public void cancelOrderDelivery(String id){
        this.cancel(id);
    }

    /**
     * 取消发货（批量）
     * @param ids
     */
    @Override
    @Transactional
    public void cancelOrderDelivery(String[] ids){
        for (String id : ids){
            this.cancel(id);
        }
    }

    /**
     * 确认收货
     * @param orderDelivery
     */
    @Override
    @Transactional
    public void confirmOrderDelivery(Order_delivery orderDelivery){
        this.confirm(orderDelivery);
    }



    /**
     * 更新物流状态
     * @param id
     * @param status
     */
    @Override
    @Transactional
    public void updateExpressStatus(String id, Integer status) {
        Order_delivery orderDelivery =  this.fetch(id);
        orderDelivery.setExpressStatus(status);
        this.update(orderDelivery);
    }

    /**
     * 取消发货调用方法
     * @param id
     */
    private void cancel(String id){
        //获取发货单信息
        Order_delivery obj = this.fetch(id);

        //获取发货单明细信息
        List<Order_delivery_detail> detailList =
                orderDeliveryDetailService.query(Cnd.where("deliveryId", "=", obj.getId()));

        List<String> orderIds = new ArrayList<String>();

        //更新发货单明细中对应的订单明细表中的sendNum
        for (Order_delivery_detail detail : detailList){
            Order_goods order_goods = orderGoodsService.fetch(detail.getOrderGoodsId());
            String orderId = order_goods.getOrderId();

            order_goods.setSendNum(order_goods.getSendNum() - detail.getSendNum());
            orderGoodsService.update(order_goods);

            //拼接OrderId，去除重复的id，用来更新订单的状态
            if(!orderIds.contains(orderId)){
                orderIds.add(orderId);
            }
        }

        //更新订单的状态
        for (String orderId : orderIds){
            Order_main order_main = orderMainService.fetch(orderId);
            List<Order_goods> goodsList =
                    orderGoodsService.query(Cnd.where("orderId", "=", order_main.getId()));
            //根据订单下的订单明细记录的sendNum来判断将订单的状态更新成什么
            //TODO
            int deliveryStatus =  getDeliveryStatusBySendNum(goodsList);
            order_main.setDeliveryStatus(deliveryStatus);
            order_main.setDeliveryAt((int)(System.currentTimeMillis()/1000));
            orderMainService.update(order_main);
        }

        //删除发货单明细信息
        orderDeliveryDetailService.clear(Cnd.where("deliveryId", "=", id));

        //删除发货单主信息
        this.delete(id);
    }

    /**
     * 确认收货调用方法
     * @param orderDelivery
     */
    @Transactional
    private  void confirm(Order_delivery orderDelivery){
        Order_delivery newOrderDelivery = this.fetch(Cnd.NEW().and("delFlag","=",false).and("id","=",orderDelivery.getId()));
        String expressId = orderDelivery.getExpressId();
        Shop_express shopExpress =  shopExpressService.fetch(expressId);
        newOrderDelivery.setExpressId(expressId);
        newOrderDelivery.setExpressName(shopExpress.getName());
        newOrderDelivery.setExpressNo(orderDelivery.getExpressNo());
        newOrderDelivery.setExpressStatus( OrderExpressStatusEnum.SEND.getKey());
        //更新发货单信息
        this.update(newOrderDelivery);
        this.fetchLinks(newOrderDelivery,"detailList");
        //获取发货详情表
        List<Order_delivery_detail> orderDeliveryDetailList = newOrderDelivery.getDetailList();
        //更新订单状态
        List<String> orderIds = new ArrayList<String>();

        //更新发货单明细中对应的订单明细表中的sendNum
        for (Order_delivery_detail detail : orderDeliveryDetailList){
            Order_goods order_goods = orderGoodsService.fetch(detail.getOrderGoodsId());
            String orderId = order_goods.getOrderId();
            //拼接OrderId，去除重复的id，用来更新订单的状态
            if(!orderIds.contains(orderId)){
                orderIds.add(orderId);
            }
        }
        //更新订单的状态
        for (String orderId : orderIds){
            Order_main order_main = orderMainService.fetch(orderId);
            List<Order_goods> goodsList = orderGoodsService.query(Cnd.where("orderId", "=", order_main.getId()));
            //根据订单下的订单明细记录的sendNum来判断将订单的状态更新成什么
            int deliveryStatus =  getDeliveryStatusBySendNum(goodsList);
            order_main.setDeliveryStatus(deliveryStatus);
            order_main.setExpressId(expressId);
            order_main.setExpressName(shopExpress.getName());
            order_main.setExpressNo(orderDelivery.getExpressNo());
            order_main.setDeliveryAt((int)(System.currentTimeMillis()/1000));
            orderMainService.update(order_main);
        }
    }

    private  int getDeliveryStatusBySendNum(List<Order_goods> goodsList){
        if(goodsList == null){
            return OrderDeliveryStatusEnum.NONE.getKey();
        }
        Integer buyNum =0;//订单总的购买数量
        Integer sendNum = 0;//订单总的发货数量
        for(Order_goods goods: goodsList){
            buyNum += goods.getBuyNum();
            sendNum += goods.getSendNum();
        }
        if(sendNum == 0){
            return  OrderDeliveryStatusEnum.NONE.getKey();
        }else if(buyNum > sendNum){
            return  OrderDeliveryStatusEnum.SOME.getKey();
        }
        return  OrderDeliveryStatusEnum.ALL.getKey();
    }
}
