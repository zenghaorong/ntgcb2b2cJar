package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 订单日志行为
 * Created by wizzer on 2017/3/30.
 */
public enum OrderLogBehaviorEnum {
    CREATE(0, Message.getMessage("order.enum.logbehavior.create")),
    UPDATE(1, Message.getMessage("order.enum.logbehavior.update")),
    PAYMENT(2, Message.getMessage("order.enum.logbehavior.payment")),
    REFUND(3, Message.getMessage("order.enum.logbehavior.refund")),
    DELIVERY(4, Message.getMessage("order.enum.logbehavior.delivery")),
    RECEIVE(5, Message.getMessage("order.enum.logbehavior.receive")),
    RETURN(6, Message.getMessage("order.enum.logbehavior.return")),
    COMPLETE(7, Message.getMessage("order.enum.logbehavior.complete")),
    FINISH(8, Message.getMessage("order.enum.logbehavior.finish")),
    CANCEL(9, Message.getMessage("order.enum.logbehavior.cancel"));

    private int key;
    private String value;

    private OrderLogBehaviorEnum(int key, String value) {
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
        for (OrderLogBehaviorEnum ft : OrderLogBehaviorEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
