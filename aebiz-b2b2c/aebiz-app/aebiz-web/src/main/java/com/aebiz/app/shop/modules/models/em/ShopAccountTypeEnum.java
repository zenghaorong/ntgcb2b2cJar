package com.aebiz.app.shop.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 收款账户类型(0对内 1对外)
 * Created by wizzer on 2017/3/30.
 */
public enum ShopAccountTypeEnum {
    IN(0, Message.getMessage("shop.enum.account.in")),
    OUT(1, Message.getMessage("shop.enum.account.out"));

    private int key;
    private String value;

    private ShopAccountTypeEnum(int key, String value) {
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
        for (ShopAccountTypeEnum ft : ShopAccountTypeEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
