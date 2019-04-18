package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * Created by wy on 2017/5/23.
 */
public enum OrderAfterTypeEnum {
    //仅退款
    ONLY_REFUNDMENT(0, Message.getMessage("order.after.enum.type.onlyRefundment")),
    //退货退款
    REFUNDMENT_AND_GOODS(1,Message.getMessage("order.after.enum.type.refundmentAndGoods")),
    //换货
    EXCHANGE(2,Message.getMessage("order.after.enum.type.exchange"));


    private int key;
    private String value;

    OrderAfterTypeEnum(int key, String value) {
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
        for (OrderAfterTypeEnum typeEnum : OrderAfterTypeEnum.values()) {
            if (key == typeEnum.key) {
                return typeEnum.value;
            }
        }
        return "";
    }
}
