package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 物流状态：0:未发货 1:已发货
 * Created by wizzer on 2017/4/5.
 */
public enum OrderExpressStatusEnum {
    NOSEND(0, Message.getMessage("order.enum.expressstatus.nosend")),
    SEND(1, Message.getMessage("order.enum.expressstatus.send"));

    private int key;
    private String value;

    private OrderExpressStatusEnum(int key, String value) {
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
        for (OrderExpressStatusEnum ft : OrderExpressStatusEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
