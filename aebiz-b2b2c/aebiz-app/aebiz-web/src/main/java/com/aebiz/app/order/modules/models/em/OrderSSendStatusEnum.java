package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 配送单据状态(0等待 1成功 2失败 3取消 4丢失 5进程中)
 * Created by wizzer on 2017/4/5.
 */
public enum OrderSSendStatusEnum {
    READY(0, Message.getMessage("order.enum.ssendstatus.ready")),
    SUCCESS(1, Message.getMessage("order.enum.ssendstatus.success")),
    FAIL(2, Message.getMessage("order.enum.ssendstatus.fail")),
    CANCEL(3, Message.getMessage("order.enum.ssendstatus.cancel")),
    LOST(4, Message.getMessage("order.enum.ssendstatus.lost")),
    PROGRESS(5, Message.getMessage("order.enum.ssendstatus.progress"));

    private int key;
    private String value;

    private OrderSSendStatusEnum(int key, String value) {
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
        for (OrderSSendStatusEnum ft : OrderSSendStatusEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
