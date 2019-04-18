package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 退款状态：
 * 0 等待退款
 * 1 审核确认
 * 2 退款成功
 * 3 退款失败
 * 4 退款关闭
 * Created by yj on 2017/5/27.
 */
public enum OrderAfterRefundStateEnum {
    // 等待退款
    REFUND_WAIT(0, Message.getMessage("order.after.refund.enum.refundWait")),
    // 审核确认
    REFUND_CONFIRM(1, Message.getMessage("order.after.refund.enum.refundConfirm")),
    // 退款成功
    REFUND_SUCCESS(2, Message.getMessage("order.after.refund.enum.refundSuccess")),
    // 退款失败
    REFUND_FAIL(3, Message.getMessage("order.after.refund.enum.refundFail")),
    // 退款关闭
    REFUND_CLOSE(4, Message.getMessage("order.after.refund.enum.refundClose"));

    private int key;
    private String value;

    OrderAfterRefundStateEnum(int key, String value) {
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
        for (OrderAfterRefundStateEnum orderAfterRefundEnum : OrderAfterRefundStateEnum.values()) {
            if (key == orderAfterRefundEnum.key) {
                return orderAfterRefundEnum.value;
            }
        }
        return "";
    }
}
