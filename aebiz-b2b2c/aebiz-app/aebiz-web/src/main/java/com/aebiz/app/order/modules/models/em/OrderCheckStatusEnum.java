package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 订单审核状态(0 待审核 1 通过  2 不通过)
 * Created by wizzer on 2017/3/20.
 */
public enum OrderCheckStatusEnum {
    WAIT(0, Message.getMessage("goods.enum.checkstatus.wait")),
    PASS(1, Message.getMessage("goods.enum.checkstatus.pass")),
    NOPASS(2, Message.getMessage("goods.enum.checkstatus.nopass"));

    private int key;
    private String value;

    private OrderCheckStatusEnum(int key, String value) {
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
        for (OrderCheckStatusEnum ft : OrderCheckStatusEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
