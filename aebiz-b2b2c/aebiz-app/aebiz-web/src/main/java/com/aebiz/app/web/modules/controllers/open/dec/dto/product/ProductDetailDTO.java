package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

/**
 * 商品详细信息DTO
 *
 * Created by Aebiz_yjq on 2017/1/18.
 */
public class ProductDetailDTO {

    /* 商品uuid */
    private String productUuid;

    /* 商品名称 */
    private String productName;

    /* 商品推荐语 */
    private String adviceNote;

    /* 所属分类uuid */
    private String categoryUuid;

    /* 所属分类名称 */
    private String categoryName;

    /* 所属店铺uuid */
    private String storeUuid;

    /* 所属店铺名称 */
    private String storeName;

    /* 所属店铺Logo */
    private String storeLogo;

    /* 品牌uuid */
    private String brandUuid;

    /* 品牌名称 */
    private String brandName;

    /* 市场价 */
    private double marketPrice;

    /* 商城价 */
    private double shopPrice;

    /* 商品库存 */
    private int actualStock;

    /* 主图 */
    private String mainImageUrl;

    /* 大图 */
    private String bigImageUrl;

    /* 中图 */
    private String centerImageUrl;

    /* 小图 */
    private String smallImageUrl;

    /* 推荐图1 */
    private String popImage1Url;

    /* 推荐图2 */
    private String popImage2Url;

    /* 推荐图3 */
    private String popImage3Url;

    /* 推荐图4 */
    private String popImage4Url;

    /* 推荐图5 */
    private String popImage5Url;

    /* 推荐图6 */
    private String popImage6Url;

    /* 推荐图7 */
    private String popImage7Url;

    /* 是否使用店铺售后服务0-不使用 1-使用 */
    private String useDefult="1";

    /* 售后服务信息 */
    private String saleAfterNote;

    /* 商品描述 */
    private String description;

    /* 商品简介 */
    private String note;

    /* 商品详情页中商品详细信息处，规格参数处需要展示的规格属性 */
    private String prodAttrs;
    //商品的sku
    private String sku;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public ProductDetailDTO() {
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

    public String getAdviceNote() {
        return adviceNote;
    }

    public void setAdviceNote(String adviceNote) {
        this.adviceNote = adviceNote;
    }

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStoreUuid() {
        return storeUuid;
    }

    public void setStoreUuid(String storeUuid) {
        this.storeUuid = storeUuid;
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

    public String getBrandUuid() {
        return brandUuid;
    }

    public void setBrandUuid(String brandUuid) {
        this.brandUuid = brandUuid;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public double getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(double shopPrice) {
        this.shopPrice = shopPrice;
    }

    public int getActualStock() {
        return actualStock;
    }

    public void setActualStock(int actualStock) {
        this.actualStock = actualStock;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public String getBigImageUrl() {
        return bigImageUrl;
    }

    public void setBigImageUrl(String bigImageUrl) {
        this.bigImageUrl = bigImageUrl;
    }

    public String getCenterImageUrl() {
        return centerImageUrl;
    }

    public void setCenterImageUrl(String centerImageUrl) {
        this.centerImageUrl = centerImageUrl;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public String getPopImage1Url() {
        return popImage1Url;
    }

    public void setPopImage1Url(String popImage1Url) {
        this.popImage1Url = popImage1Url;
    }

    public String getPopImage2Url() {
        return popImage2Url;
    }

    public void setPopImage2Url(String popImage2Url) {
        this.popImage2Url = popImage2Url;
    }

    public String getPopImage3Url() {
        return popImage3Url;
    }

    public void setPopImage3Url(String popImage3Url) {
        this.popImage3Url = popImage3Url;
    }

    public String getPopImage4Url() {
        return popImage4Url;
    }

    public void setPopImage4Url(String popImage4Url) {
        this.popImage4Url = popImage4Url;
    }

    public String getPopImage5Url() {
        return popImage5Url;
    }

    public void setPopImage5Url(String popImage5Url) {
        this.popImage5Url = popImage5Url;
    }

    public String getPopImage6Url() {
        return popImage6Url;
    }

    public void setPopImage6Url(String popImage6Url) {
        this.popImage6Url = popImage6Url;
    }

    public String getPopImage7Url() {
        return popImage7Url;
    }

    public void setPopImage7Url(String popImage7Url) {
        this.popImage7Url = popImage7Url;
    }

    public String getUseDefult() {
        return useDefult;
    }

    public void setUseDefult(String useDefult) {
        this.useDefult = useDefult;
    }

    public String getSaleAfterNote() {
        return saleAfterNote;
    }

    public void setSaleAfterNote(String saleAfterNote) {
        this.saleAfterNote = saleAfterNote;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getProdAttrs() {
        return prodAttrs;
    }

    public void setProdAttrs(String prodAttrs) {
        this.prodAttrs = prodAttrs;
    }

    @Override
    public String toString() {
        return "ProductDetailDTO{" +
                "productUuid='" + productUuid + '\'' +
                ", productName='" + productName + '\'' +
                ", adviceNote='" + adviceNote + '\'' +
                ", categoryUuid='" + categoryUuid + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", storeUuid='" + storeUuid + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storeLogo='" + storeLogo + '\'' +
                ", brandUuid='" + brandUuid + '\'' +
                ", brandName='" + brandName + '\'' +
                ", marketPrice=" + marketPrice +
                ", shopPrice=" + shopPrice +
                ", actualStock=" + actualStock +
                ", mainImageUrl='" + mainImageUrl + '\'' +
                ", bigImageUrl='" + bigImageUrl + '\'' +
                ", centerImageUrl='" + centerImageUrl + '\'' +
                ", smallImageUrl='" + smallImageUrl + '\'' +
                ", popImage1Url='" + popImage1Url + '\'' +
                ", popImage2Url='" + popImage2Url + '\'' +
                ", popImage3Url='" + popImage3Url + '\'' +
                ", popImage4Url='" + popImage4Url + '\'' +
                ", popImage5Url='" + popImage5Url + '\'' +
                ", popImage6Url='" + popImage6Url + '\'' +
                ", popImage7Url='" + popImage7Url + '\'' +
                ", useDefult='" + useDefult + '\'' +
                ", saleAfterNote='" + saleAfterNote + '\'' +
                ", description='" + description + '\'' +
                ", note='" + note + '\'' +
                ", prodAttrs='" + prodAttrs + '\'' +
                ", sku='" + sku + '\'' +
                '}';
    }
}
