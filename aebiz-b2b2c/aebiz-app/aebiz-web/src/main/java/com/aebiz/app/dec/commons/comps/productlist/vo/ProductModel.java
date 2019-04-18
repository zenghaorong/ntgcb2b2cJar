package com.aebiz.app.dec.commons.comps.productlist.vo;

/**
 * Created by 金辉 on 2016/12/21.
 */
public class ProductModel {
    /*商品ID*/
    private String productId;
    /*商品名称*/
    private String productName;
    /*商品推荐语*/
    private String recommend;
    /*商品图片*/
    private String pic;
    /*商品评分*/
    private double score;
    /*商品所属商户UUID*/
    private String storeUuid;
    /*商品价格*/
    private double price;
    /*商品所属商户名称*/
    private String storeName;
    /*商品所属商户logo*/
    private String storeLogo;
    /*商品的sku*/
    private String sku;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getStoreUuid() {
        return storeUuid;
    }

    public void setStoreUuid(String storeUuid) {
        this.storeUuid = storeUuid;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }
}
