package com.aebiz.app.goods.commons.vo;

import java.io.Serializable;

/**
 * Created by ThinkPad on 2017/7/24.
 */
public class GoodsProductVO implements Serializable{

    private String sku;

    private Integer num;

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
}
