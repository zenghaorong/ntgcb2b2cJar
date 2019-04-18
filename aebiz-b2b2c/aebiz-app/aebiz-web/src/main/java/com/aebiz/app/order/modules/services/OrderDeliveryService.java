package com.aebiz.app.order.modules.services;

import com.aebiz.app.order.modules.models.Order_delivery;
import com.aebiz.app.order.modules.models.Order_goods;
import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.baseframework.base.service.BaseService;

import java.util.List;

public interface OrderDeliveryService extends BaseService<Order_delivery>{

    /**
     * 创建发货单
     * @param orderGoodsList
     * @param remark
     */
    void saveOrderDelivery(List<Order_goods> orderGoodsList, String remark, Order_main order_main);

    /**
     * 取消发货（单个）
     * @param id
     */
    void cancelOrderDelivery(String id);

    /**
     * 取消发货（批量）
     * @param ids
     */
    void cancelOrderDelivery(String[] ids);

    /**
     * 更新物流状态
     * @param id
     * @param status
     */
    void updateExpressStatus(String id,Integer status);

    /**
     * 确认发货(单个)
     * @param orderDelivery
     */
    void confirmOrderDelivery(Order_delivery orderDelivery);

}
