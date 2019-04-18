package com.aebiz.app.acc.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 密码强度：
 * 0 弱
 * 1 中
 * 2 强
 * Created by Renjm on 2017/7/26.
 */
public enum PasswordStrengthEnum {
    // 弱
    WEAK(0, Message.getMessage("account.user.passwordStrength.enum.weak")),
    // 中
    MIDDLE(1, Message.getMessage("account.user.passwordStrength.enum.middle")),
    // 强
    STRONG(2, Message.getMessage("account.user.passwordStrength.enum.strong"));

    private int key;
    private String value;

    PasswordStrengthEnum(int key, String value) {
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
        for (PasswordStrengthEnum backMoneyStyleEnum : PasswordStrengthEnum.values()) {
            if (key == backMoneyStyleEnum.key) {
                return backMoneyStyleEnum.value;
            }
        }
        return "";
    }
}
