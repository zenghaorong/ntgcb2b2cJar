package com.aebiz.app.store.modules.commons.vo;

import java.io.Serializable;

public class StoreFreightProduct implements Serializable{

    private String sku;

    private int num;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}
