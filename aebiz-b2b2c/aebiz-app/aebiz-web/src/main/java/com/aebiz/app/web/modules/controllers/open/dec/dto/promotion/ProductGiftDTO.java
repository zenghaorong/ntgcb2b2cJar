package com.aebiz.app.web.modules.controllers.open.dec.dto.promotion;

/**
 * 赠品DTO
 *
 * Created by Aebiz_yjq on 2017/1/21.
 */
public class ProductGiftDTO {

    /* 商品uuid*/
    private String productUuid;

    /* 商品名称*/
    private String productName;

    /* 商品价格*/
    private double price;

    /* 商品小图片*/
    private String smallImage;

    /* 商品小图片地址*/
    private String smallImageUrl;

    /* 是否赠送完  1-未赠完,0-赠完了*/
    private String canGive;

    /* 库存*/
    private int stock;

    public ProductGiftDTO() {
    }

    public ProductGiftDTO(String productUuid, String productName, double price, String smallImage,
                          String smallImageUrl, String canGive, int stock) {
        this.productUuid = productUuid;
        this.productName = productName;
        this.price = price;
        this.smallImage = smallImage;
        this.smallImageUrl = smallImageUrl;
        this.canGive = canGive;
        this.stock = stock;
    }

    public String getProductUuid() {
        return productUuid;
    }

    public void setProductUuid(String productUuid) {
        this.productUuid = productUuid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public String getCanGive() {
        return canGive;
    }

    public void setCanGive(String canGive) {
        this.canGive = canGive;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "ProductGiftDTO{" +
                "productUuid='" + productUuid + '\'' +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", smallImage='" + smallImage + '\'' +
                ", smallImageUrl='" + smallImageUrl + '\'' +
                ", canGive='" + canGive + '\'' +
                ", stock=" + stock +
                '}';
    }
}
