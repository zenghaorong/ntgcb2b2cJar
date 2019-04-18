package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 订单状态(0待审核订单 1活动订单 2无效订单 3取消订单 4关闭订单 5完成订单)
 * Created by wizzer on 2017/3/30.
 */
public enum OrderStatusEnum {
    WAITVERIFY(0, Message.getMessage("order.enum.status.waitverify")),
    ACTIVE(1, Message.getMessage("order.enum.status.active")),
    INVALID(2, Message.getMessage("order.enum.status.invalid")),
    CANCEL(3, Message.getMessage("order.enum.status.cancel")),
    DEAD(4, Message.getMessage("order.enum.status.dead")),
    FINISH(5, Message.getMessage("order.enum.status.finish"));

    private int key;
    private String value;

    private OrderStatusEnum(int key, String value) {
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
        for (OrderStatusEnum ft : OrderStatusEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
