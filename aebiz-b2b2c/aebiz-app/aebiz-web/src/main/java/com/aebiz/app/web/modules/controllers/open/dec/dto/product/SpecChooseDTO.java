package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

/**
 * 商品属性选择DTO
 *
 * Created by Aebiz_yjq on 2017/1/17.
 */
public class SpecChooseDTO {

    /* 商品主图大图 */
    private String bigPictureUrl = "";

    /* 商品主图中图 */
    private String centerPictureUrl = "";

    /* 商品主图小图 */
    private String smallPictureUrl = "";

    /* 价格 */
    private double price = 0.0;

    /* 库存 */
    private int stock = 0;

    /* 商品sku编号 */
    private String skuNo = "";

    /* 选择属性值：属性值uuid-属性值uuid-属性值uuid */
    private String selectedUuids = "";

    private String productName="";

    public SpecChooseDTO() {
    }

    public SpecChooseDTO(String productName,String bigPictureUrl, String centerPictureUrl, String smallPictureUrl, double price, int stock, String skuNo, String selectedUuids) {
        this.productName=productName;
        this.bigPictureUrl = bigPictureUrl;
        this.centerPictureUrl = centerPictureUrl;
        this.smallPictureUrl = smallPictureUrl;
        this.price = price;
        this.stock = stock;
        this.skuNo = skuNo;
        this.selectedUuids = selectedUuids;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBigPictureUrl() {
        return bigPictureUrl;
    }

    public void setBigPictureUrl(String bigPictureUrl) {
        this.bigPictureUrl = bigPictureUrl;
    }

    public String getCenterPictureUrl() {
        return centerPictureUrl;
    }

    public void setCenterPictureUrl(String centerPictureUrl) {
        this.centerPictureUrl = centerPictureUrl;
    }

    public String getSamllPictureUrl() {
        return smallPictureUrl;
    }

    public void setSmallPictureUrl(String smallPictureUrl) {
        this.smallPictureUrl = smallPictureUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public String getSelectedUuids() {
        return selectedUuids;
    }

    public void setSelectedUuids(String selectedUuids) {
        this.selectedUuids = selectedUuids;
    }

    @Override
    public String toString() {
        return "SpecChooseDTO{" +
                "bigPictureUrl='" + bigPictureUrl + '\'' +
                ", centerPictureUrl='" + centerPictureUrl + '\'' +
                ", smallPictureUrl='" + smallPictureUrl + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", skuNo='" + skuNo + '\'' +
                ", selectedUuids='" + selectedUuids + '\'' +
                '}';
    }
}
