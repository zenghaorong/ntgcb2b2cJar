package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 售后相关的状态
 * Created by yj on 2017/5/19.
 */
public enum OrderAfterStateEnum {
    // 等待卖家处理
    WAIT_SELLER_TREAT(0, Message.getMessage("order.after.enum.state.waitSellerTreat")),
    // 卖家超时未处理
    SELLER_OVERTIME_UNTREATED(1, Message.getMessage("order.after.enum.state.sellerOvertimeUntreated")),
    // 不同意
    SELLER_REFUSE(2, Message.getMessage("order.after.enum.state.sellerRefuse")),
    // 待财务审核
    WAIT_FINANCE_CHECK(3, Message.getMessage("order.after.enum.state.waitFinanceCheck")),
    // 等待买家寄回
    WAIT_BUYER_SEND_BACK(4, Message.getMessage("order.after.enum.state.waitBuyerSendBack")),
    //财务审核不通过
    FINANCE_REFUSE(5, Message.getMessage("order.after.enum.state.financeRefuse")),
    //待买家填写账号（如线下转账）
    WAIT_BUYER_ACCOUNT(6, Message.getMessage("order.after.enum.state.waitBuyerAccount")),
    //财务确认(待退款)
    FINANCE_CONFIRM(7, Message.getMessage("order.after.enum.state.financeConfirm")),
    // 买家已发货
    BUYER_DELIVERY(8, Message.getMessage("order.after.enum.state.buyerDelivery")),
    // 卖家收到货不同意，验货不通过
    SELLER_RECEIVE_REFUSE(9, Message.getMessage("order.after.enum.state.sellerReceiveRefuse")),
    // 售后成功(已退款)
    AFTER_SUCCESS(10, Message.getMessage("order.after.enum.state.afterSuccess")),
    // 售后关闭
    AFTER_CLOSE(11, Message.getMessage("order.after.enum.state.afterClose")),
    // 售后已取消
    AFTER_SALE_CANCEL(12,Message.getMessage("order.after.enum.state.afterSaleCancel"));

    private int key;
    private String value;

    OrderAfterStateEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String getValue(int key) {
        for (OrderAfterStateEnum reasonEnum : OrderAfterStateEnum.values()) {
            if (key == reasonEnum.key) {
                return reasonEnum.value;
            }
        }
        return "";
    }
}
