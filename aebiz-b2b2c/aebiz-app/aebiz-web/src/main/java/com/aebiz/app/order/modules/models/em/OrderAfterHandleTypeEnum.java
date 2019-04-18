package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 处理人类型（0平台，1自营，2商户，3会员）
 * Created by 杨剑 on 2017/5/24.
 */
public enum OrderAfterHandleTypeEnum {
    // 平台
    PLATFORM(0, Message.getMessage("order.after.enum.handleType.platform")),
    //自营
    SELF(1, Message.getMessage("order.after.enum.handleType.self")),
    // 商户
    STORE(2, Message.getMessage("order.after.enum.handleType.store")),
    // 会员
    MEMBER(3, Message.getMessage("order.after.enum.handleType.member"));
    private int key;
    private String value;

    OrderAfterHandleTypeEnum(int key, String value) {
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
        for (OrderAfterHandleTypeEnum handleTypeEnum : OrderAfterHandleTypeEnum.values()) {
            if (key == handleTypeEnum.key) {
                return handleTypeEnum.value;
            }
        }
        return "";
    }
}
