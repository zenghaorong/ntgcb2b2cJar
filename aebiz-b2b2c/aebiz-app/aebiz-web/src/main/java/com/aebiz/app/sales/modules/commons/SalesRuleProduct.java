package com.aebiz.app.sales.modules.commons;

/**
 * 货品信息
 * Created by hechao on 2017/7/21.
 */
public class SalesRuleProduct {

    private String sku;
    private Integer num;
    private Integer price;
    private String salesId;

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }
}