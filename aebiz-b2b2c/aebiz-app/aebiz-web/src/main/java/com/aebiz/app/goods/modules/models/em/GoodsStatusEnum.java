package com.aebiz.app.goods.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 商品状态(1 草稿  2 待上架  3 在售  4 已下架  5 审核中 6 审核未通过)
 * Created by hechao on 2017/6/5.
 */
public enum GoodsStatusEnum {
    DRAFT(1, Message.getMessage("goods.enum.status.draft")),
    WAITSALE(2, Message.getMessage("goods.enum.status.waitsale")),
    SALE(3, Message.getMessage("goods.enum.status.sale")),
    OFFSALE(4, Message.getMessage("goods.enum.status.offsale")),
    CHECKING(5, Message.getMessage("goods.enum.status.checking")),
    NOPASS(6, Message.getMessage("goods.enum.status.nopass"));

    private int key;
    private String value;

    private GoodsStatusEnum(int key, String value) {
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
        for (GoodsStatusEnum ft : GoodsStatusEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
