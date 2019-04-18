package com.aebiz.app.dec.commons.comps.cartcomp.vo;

import java.util.List;

/**
 * Created by 金辉 on 2016/12/16.
 */
public class CartModel {
    /*购物车总价*/
    private double totalPrice;
    /*商品总数*/
    private int totalNum;
    /*商品list*/
    private List productList;


    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List getProductList() {
        return productList;
    }

    public void setProductList(List productList) {
        this.productList = productList;
    }
}
