package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 订单评价状态(0待评价  1已评价)
 * Created by wizzer on 2017/3/30.
 */
public enum OrderFeedStatusEnum {
    WAIT(0, Message.getMessage("order.enum.feedstatus.wait")),
    FEED(1, Message.getMessage("order.enum.feedstatus.feed"));

    private int key;
    private String value;

    private OrderFeedStatusEnum(int key, String value) {
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
        for (OrderFeedStatusEnum ft : OrderFeedStatusEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
