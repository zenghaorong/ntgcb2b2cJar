package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 订单支付状态(0待支付 1待确认 2部分支付 3已支付 4待退款 5部分退款 6已退款)
 * Created by wizzer on 2017/3/30.
 */
public enum OrderPayStatusEnum {
    NO(0, Message.getMessage("order.enum.paystatus.nopay")),
    WAITVERIFY(1, Message.getMessage("order.enum.paystatus.waitverify")),
    PAYSOME(2, Message.getMessage("order.enum.paystatus.paysome")),
    PAYALL(3, Message.getMessage("order.enum.paystatus.payall")),
    REFUNDWAIT(4, Message.getMessage("order.enum.paystatus.refundwait")),
    REFUNDSOME(5, Message.getMessage("order.enum.paystatus.refundsome")),
    REFUNDALL(6, Message.getMessage("order.enum.paystatus.refundall"));

    private int key;
    private String value;

    private OrderPayStatusEnum(int key, String value) {
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
        for (OrderPayStatusEnum ft : OrderPayStatusEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
