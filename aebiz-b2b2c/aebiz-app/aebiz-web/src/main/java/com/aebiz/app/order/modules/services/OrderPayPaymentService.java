package com.aebiz.app.order.modules.services;

import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.models.Order_pay_payment;
import com.aebiz.baseframework.base.service.BaseService;

import java.util.List;

public interface OrderPayPaymentService extends BaseService<Order_pay_payment>{

    /**
     *订单支付信息（新增）
     * 对于订单支付信息批量新增，需要支付账号信息、订单主表信息、订单组信息
     * @param orderPayPaymentList
     * @param orderMain
     */
    void add(List<Order_pay_payment> orderPayPaymentList, Order_main orderMain,boolean isPaySuccess);

}
