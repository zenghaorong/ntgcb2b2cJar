package com.aebiz.app.goods.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 销售终端
 * Created by wizzer on 2017/3/20.
 */
public enum GoodsSaleClientEnum {
    ALL(0, Message.getMessage("goods.enum.saleclient.all")),
    PC(1, Message.getMessage("goods.enum.saleclient.pc")),
    WAP(2, Message.getMessage("goods.enum.saleclient.wap")),
    TV(3, Message.getMessage("goods.enum.saleclient.tv"));

    private int key;
    private String value;

    private GoodsSaleClientEnum(int key, String value) {
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
        for (GoodsSaleClientEnum ft : GoodsSaleClientEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
