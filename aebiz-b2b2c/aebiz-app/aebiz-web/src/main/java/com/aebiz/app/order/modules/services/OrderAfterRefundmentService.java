package com.aebiz.app.order.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.order.modules.models.Order_after_refundment;

public interface OrderAfterRefundmentService extends BaseService<Order_after_refundment>{

    /**
     * 财务处理
     *
     * @param refundmentId 售后退款单号
     * @param flag 自定义标识，用于区分具体的处理操作
     * @param refundStyle 退款方式
     * @param note 卖家填写的备注说明
     * @param vouchers 凭证
     * @param opBy 操作人
     * @param handleType 处理人类型
     * @param handleMan 处理人
     * @return
     */
    public void financeHandle(String refundmentId , int flag, String refundStyle, String note,String[] vouchers, String opBy, Integer handleType, String handleMan);

    /**
     * 填写售后退款银行卡信息
     *
     * @param afterSaleId
     * @param bankCard
     * @param bankName
     * @param name
     */
    public void addBankCardDo(String afterSaleId, String bankCard, String bankName, String name);

}
