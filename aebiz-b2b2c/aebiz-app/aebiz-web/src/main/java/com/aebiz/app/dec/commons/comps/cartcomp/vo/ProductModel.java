package com.aebiz.app.dec.commons.comps.cartcomp.vo;

/**
 * Created by 金辉 on 2016/12/16.
 */
public class ProductModel {
    /*商品ID*/
    private String productId;
    private String attrId;
    /*商品名称*/
    private String productName;
    /*商品数量*/
    private String productNum;
    /*商品单价*/
    private String productPrice;
    /*商品数量*商品单价*/
    private String totalPrice;
    /*商品图片路径*/
    private String productImg;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }


    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }
}
