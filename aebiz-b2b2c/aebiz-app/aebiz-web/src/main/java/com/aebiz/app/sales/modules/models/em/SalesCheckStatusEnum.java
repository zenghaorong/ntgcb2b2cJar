package com.aebiz.app.sales.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 促销审核状态(0 未提交 1 待审核  2 审核通过 3 未通过)
 * Created by hechao on 2017/7/14.
 */
public enum SalesCheckStatusEnum {
    UNSUBMIT(0, Message.getMessage("sales.enum.checkstatus.unsubmit")),
    WAIT(1, Message.getMessage("sales.enum.checkstatus.wait")),
    PASS(2, Message.getMessage("sales.enum.checkstatus.pass")),
    NOPASS(3, Message.getMessage("sales.enum.checkstatus.nopass"));

    private int key;
    private String value;

    private SalesCheckStatusEnum(int key, String value) {
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
        for (SalesCheckStatusEnum ft : SalesCheckStatusEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
