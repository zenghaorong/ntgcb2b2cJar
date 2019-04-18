package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 订单来源(0pc 1wap 2微信 3iOS 4android 5tv 6手动录单)
 * Created by wizzer on 2017/3/31.
 */
public enum OrderSourceEnum {
    PC(0, Message.getMessage("order.enum.source.pc")),
    WAP(1, Message.getMessage("order.enum.source.wap")),
    WECHAT(2, Message.getMessage("order.enum.source.wechat")),
    IOS(3, Message.getMessage("order.enum.source.ios")),
    ANDROID(4, Message.getMessage("order.enum.source.android")),
    TV(5, Message.getMessage("order.enum.source.tv")),
    HAND(6, Message.getMessage("order.enum.source.hand"));

    private int key;
    private String value;

    private OrderSourceEnum(int key, String value) {
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
        for (OrderSourceEnum ft : OrderSourceEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
