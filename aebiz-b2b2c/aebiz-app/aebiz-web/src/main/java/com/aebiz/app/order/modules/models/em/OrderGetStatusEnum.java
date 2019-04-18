package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 订单发货状态(0待收货  1已收货)
 * Created by wizzer on 2017/3/30.
 */
public enum OrderGetStatusEnum {
    NONE(0, Message.getMessage("order.enum.getstatus.none")),
    ALL(1, Message.getMessage("order.enum.getstatus.all"));

    private int key;
    private String value;

    private OrderGetStatusEnum(int key, String value) {
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
        for (OrderGetStatusEnum ft : OrderGetStatusEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
