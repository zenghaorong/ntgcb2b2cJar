package com.aebiz.app.order.commons.vo;

import java.io.Serializable;

/**
 * Created by ThinkPad on 2017/7/23.
 */
public class CartCountVO implements Serializable{

    private String sku;

    private String salesId;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }
}
