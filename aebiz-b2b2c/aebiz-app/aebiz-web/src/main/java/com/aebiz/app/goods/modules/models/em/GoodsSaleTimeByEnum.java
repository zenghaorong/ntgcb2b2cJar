package com.aebiz.app.goods.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 上架时间(0 立即上架  1 暂不上架  2 定时上架)
 * Created by hechao on 2017/06/06.
 */
public enum GoodsSaleTimeByEnum {
    NOW(0, Message.getMessage("goods.enum.saleTimeBy.now")),
    WAIT(1, Message.getMessage("goods.enum.saleTimeBy.wait")),
    TIME(2, Message.getMessage("goods.enum.saleTimeBy.time"));

    private int key;
    private String value;

    private GoodsSaleTimeByEnum(int key, String value) {
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
        for (GoodsSaleTimeByEnum ft : GoodsSaleTimeByEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
