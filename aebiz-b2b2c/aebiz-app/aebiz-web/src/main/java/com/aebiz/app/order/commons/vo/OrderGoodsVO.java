package com.aebiz.app.order.commons.vo;

import java.io.Serializable;

/**
 * Created by ThinkPad on 2017/7/24.
 */
public class OrderGoodsVO implements Serializable{

    private String sku;

    private Integer num;

    private String goodsSalesId;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getGoodsSalesId() {
        return goodsSalesId;
    }

    public void setGoodsSalesId(String goodsSalesId) {
        this.goodsSalesId = goodsSalesId;
    }
}
