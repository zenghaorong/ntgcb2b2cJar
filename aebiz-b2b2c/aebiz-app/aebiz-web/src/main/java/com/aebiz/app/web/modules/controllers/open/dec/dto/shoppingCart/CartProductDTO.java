package com.aebiz.app.web.modules.controllers.open.dec.dto.shoppingCart;

/**
 * 用作购物车数据在前台的展示。这里展示的只是商品，不包含商户。
 * Created by yewei on 2017/6/17.
 */
public class CartProductDTO {
    //货品id
    private String productId;
    //货品图片
    private String productImg;
    //购买数量
    private  Integer buyNum;
    //单价（）
    private  Integer price;
    //货品的sku
    private  String sku;
    //货品的商品名称
    private String productName;
    //库存
    private int stock;
   //购买单个购物车大的价格（购买价*购买数量）
    private int totalPrice;

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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

    public Integer getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "cartProductDTO{" +
                "productId='" + productId + '\'' +
                ", productImg='" + productImg + '\'' +
                ", buyNum=" + buyNum +
                ", price=" + price +
                ", sku='" + sku + '\'' +
                ", productName='" + productName + '\'' +
                ", stock=" + stock +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
