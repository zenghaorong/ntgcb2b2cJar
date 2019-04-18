package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 订单发货状态(0待发货 1配货中  2部分发货  3已发货 4申请退货  5部分退货 6已退货)
 * Created by wizzer on 2017/3/30.
 */
public enum OrderDeliveryStatusEnum {
    NONE(0, Message.getMessage("order.enum.sendstatus.none")),
    WAIT(1, Message.getMessage("order.enum.sendstatus.wait")),
    SOME(2, Message.getMessage("order.enum.sendstatus.some")),
    ALL(3, Message.getMessage("order.enum.sendstatus.all")),
    RETURNWAIT(4, Message.getMessage("order.enum.sendstatus.returnwait")),
    RETURNSOME(5, Message.getMessage("order.enum.sendstatus.returnsome")),
    RETURNALL(6, Message.getMessage("order.enum.sendstatus.returnall"));

    private int key;
    private String value;

    private OrderDeliveryStatusEnum(int key, String value) {
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
        for (OrderDeliveryStatusEnum ft : OrderDeliveryStatusEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
