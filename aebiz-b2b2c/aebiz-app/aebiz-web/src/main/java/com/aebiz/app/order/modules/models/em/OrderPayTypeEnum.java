package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 订单支付状态(0网上支付 1货到付现金 2货到刷卡 3货到支付宝扫码 4线下转账)
 * Created by wizzer on 2017/3/30.
 */
public enum OrderPayTypeEnum {
    ONLINE(0, Message.getMessage("order.enum.paytype.online")),
    CASH(1, Message.getMessage("order.enum.paytype.cash")),
    POS(2, Message.getMessage("order.enum.paytype.pos")),
    ALIQRCODE(3, Message.getMessage("order.enum.paytype.aliqrcode")),
    TRANSFER(4, Message.getMessage("order.enum.paytype.transfer"));

    private int key;
    private String value;

    private OrderPayTypeEnum(int key, String value) {
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
        for (OrderPayTypeEnum ft : OrderPayTypeEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
