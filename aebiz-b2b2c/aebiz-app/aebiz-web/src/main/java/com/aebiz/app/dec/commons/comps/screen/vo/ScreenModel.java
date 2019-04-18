package com.aebiz.app.dec.commons.comps.screen.vo;

import java.util.List;

/**
 * Created by 金辉 on 2016/12/21.
 */
public class ScreenModel {
    private List brands;
    private List prices;
    private List attributeList;
    private List cateList;

    public List getBrands() {
        return brands;
    }

    public void setBrands(List brands) {
        this.brands = brands;
    }

    public List getPrices() {
        return prices;
    }

    public void setPrices(List prices) {
        this.prices = prices;
    }

    public List getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List attributeList) {
        this.attributeList = attributeList;
    }

    public List getCateList() {
        return cateList;
    }

    public void setCateList(List cateList) {
        this.cateList = cateList;
    }
}
