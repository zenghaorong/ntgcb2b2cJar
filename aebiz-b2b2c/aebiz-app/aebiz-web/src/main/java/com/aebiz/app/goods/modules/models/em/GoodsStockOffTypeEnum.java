package com.aebiz.app.goods.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 商品库存减少方式(0支付时减库存 1下单时减库存)
 * Created by wizzer on 2017/3/20.
 */
public enum GoodsStockOffTypeEnum {
    PAY(0, Message.getMessage("goods.enum.stockofftype.pay")),
    ORDER(1, Message.getMessage("goods.enum.stockofftype.order"));

    private int key;
    private String value;

    private GoodsStockOffTypeEnum(int key, String value) {
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
        for (GoodsStockOffTypeEnum ft : GoodsStockOffTypeEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }
}
