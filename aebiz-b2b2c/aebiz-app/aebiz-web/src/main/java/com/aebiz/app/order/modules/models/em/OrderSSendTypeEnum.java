package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 配送方式
 * Created by wizzer on 2017/4/5.
 */
public enum OrderSSendTypeEnum {
    EXPRESS(0, Message.getMessage("order.enum.ssendtype.express")),
    HAND(1, Message.getMessage("order.enum.ssendtype.hand"));

    private int key;
    private String value;

    private OrderSSendTypeEnum(int key, String value) {
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
        for (OrderSSendTypeEnum ft : OrderSSendTypeEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
