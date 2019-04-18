package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 退款状态：
 * 0 原路返回
 * 1 线下转账
 * Created by zhangyi on 2017/6/22.
 */
public enum OrderAfterBackMoneyStyleEnum {
    // 原路返回
    RETRACE(0, Message.getMessage("order.after.backMoneyStyle.enum.retrace")),
    // 线下转账
    OFFLINE_TRANSFER(1, Message.getMessage("order.after.backMoneyStyle.enum.offlineTransfer"));


    private int key;
    private String value;

    OrderAfterBackMoneyStyleEnum(int key, String value) {
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
        for (OrderAfterBackMoneyStyleEnum backMoneyStyleEnum : OrderAfterBackMoneyStyleEnum.values()) {
            if (key == backMoneyStyleEnum.key) {
                return backMoneyStyleEnum.value;
            }
        }
        return "";
    }
}
