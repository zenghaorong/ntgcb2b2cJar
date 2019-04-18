package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

/**
 * 商品收藏DTO
 *
 * Created by Aebiz_yjq on 2017/1/16.
 */
public class ProductFavoriteDTO {

    /* 收藏uuid */
    private String uuid;

    /*商品Uuid*/
    private String productUuid;

    /*商品名称*/
    private String name;

    /*商品价格*/
    private double price;

    /*商品主图url*/
    private String imageUrl;

    public ProductFavoriteDTO() {
    }

    public ProductFavoriteDTO(String uuid, String productUuid, String name, double price, String imageUrl) {
        this.uuid = uuid;
        this.productUuid = productUuid;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getProductUuid() {
        return productUuid;
    }

    public void setProductUuid(String productUuid) {
        this.productUuid = productUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    @Override
    public String toString() {
        return "ProductFavoriteDTO{" +
                "uuid='" + uuid + '\'' +
                ", productUuid='" + productUuid + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
