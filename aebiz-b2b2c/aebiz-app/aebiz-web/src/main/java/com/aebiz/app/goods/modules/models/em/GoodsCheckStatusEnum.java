package com.aebiz.app.goods.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 商品审核状态(0 待审核 1 通过  2 不通过 3 违规)
 * Created by wizzer on 2017/3/20.
 */
public enum GoodsCheckStatusEnum {
    WAIT(0, Message.getMessage("goods.enum.checkstatus.wait")),
    PASS(1, Message.getMessage("goods.enum.checkstatus.pass")),
    NOPASS(2, Message.getMessage("goods.enum.checkstatus.nopass")),
    ILLEGL(3, Message.getMessage("goods.enum.checkstatus.illegl"));

    private int key;
    private String value;

    private GoodsCheckStatusEnum(int key, String value) {
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
        for (GoodsCheckStatusEnum ft : GoodsCheckStatusEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
