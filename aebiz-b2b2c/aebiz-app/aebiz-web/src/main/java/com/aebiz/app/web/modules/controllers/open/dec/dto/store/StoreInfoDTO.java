package com.aebiz.app.web.modules.controllers.open.dec.dto.store;

/**
 * 店铺详细信息DTO
 *
 * Created by Aebiz_yjq on 2017/1/19.
 */
public class StoreInfoDTO {

    /* 商户uuid */
    private String storeUuid = "";

    /*商户名称*/
    private String storeName;

    /* 关联的商户的uuid */
    private String accountUuid = "";

    /*商户logo*/
    private String logo;

    /* 商户类型 */
    private String storeType = "";

    /* 商户所属角色 */
    private String roleUuid = "";

    /*店铺简介*/
    private String companyIntroduction;

    /*开店时间*/
    private String openTime;

    /* 品牌故事 */
    private String brandStory = "";

    /* 品牌视频 */
    private String brandVideo = "";

    /* 商户保障 */
    private String storeService = "";

    /* 是否支持自提 0 :支持 1 不支持 */
    private String supportPickup = "";

    /* 是否支持门店 0 :支持 1 不支持 */
    private String supportSubShop = "";

    /* 是否支持批发 0 :支持 1 不支持 */
    private String supportWholesale = "";

    /* 商户是否启用货到付款 0:启用 1:不启用 */
    private String enableCod = "";

    /* 商户是否启用自提 0:启用 1:不启用 */
    private String enablePickUp = "";

    /* 商户是否启用门店 0:启用 1:不启用 */
    private String enableSubStore = "";

    /* 商户是否启用批发 0:启用 1:不启用 */
    private String enableWholesale = "";

    /*商户宝贝数量*/
    private int productCount;

    /*商户销量*/
    private int saleCount;

    /*商户被收藏次数*/
    private int favoriteCount;

    /*商户被收藏状态 0-未收藏；1-已收藏*/
    private String favoriteState;

    /*商户收藏记录uuid*/
    private String storeFavoriteUuid;

    /* 客服QQ */
    private String qq = "";

    /*客服电话*/
    private String customerService;

    /*客服昵称*/
    private String csNickName;

    /*商户综合评分*/
    private double storeScore;

    /*物流速度评分*/
    private double logisticSpeedScore;

    /*服务态度评分*/
    private double serviceAttitudeScore;

    /*商品描述评分*/
    private double prodDescScore;

    public StoreInfoDTO() {
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

    public String getAccountUuid() {
        return accountUuid;
    }

    public void setAccountUuid(String accountUuid) {
        this.accountUuid = accountUuid;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getRoleUuid() {
        return roleUuid;
    }

    public void setRoleUuid(String roleUuid) {
        this.roleUuid = roleUuid;
    }

    public String getCompanyIntroduction() {
        return companyIntroduction;
    }

    public void setCompanyIntroduction(String companyIntroduction) {
        this.companyIntroduction = companyIntroduction;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getBrandStory() {
        return brandStory;
    }

    public void setBrandStory(String brandStory) {
        this.brandStory = brandStory;
    }

    public String getBrandVideo() {
        return brandVideo;
    }

    public void setBrandVideo(String brandVideo) {
        this.brandVideo = brandVideo;
    }

    public String getStoreService() {
        return storeService;
    }

    public void setStoreService(String storeService) {
        this.storeService = storeService;
    }

    public String getSupportPickup() {
        return supportPickup;
    }

    public void setSupportPickup(String supportPickup) {
        this.supportPickup = supportPickup;
    }

    public String getSupportSubShop() {
        return supportSubShop;
    }

    public void setSupportSubShop(String supportSubShop) {
        this.supportSubShop = supportSubShop;
    }

    public String getSupportWholesale() {
        return supportWholesale;
    }

    public void setSupportWholesale(String supportWholesale) {
        this.supportWholesale = supportWholesale;
    }

    public String getEnableCod() {
        return enableCod;
    }

    public void setEnableCod(String enableCod) {
        this.enableCod = enableCod;
    }

    public String getEnablePickUp() {
        return enablePickUp;
    }

    public void setEnablePickUp(String enablePickUp) {
        this.enablePickUp = enablePickUp;
    }

    public String getEnableSubStore() {
        return enableSubStore;
    }

    public void setEnableSubStore(String enableSubStore) {
        this.enableSubStore = enableSubStore;
    }

    public String getEnableWholesale() {
        return enableWholesale;
    }

    public void setEnableWholesale(String enableWholesale) {
        this.enableWholesale = enableWholesale;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getFavoriteState() {
        return favoriteState;
    }

    public void setFavoriteState(String favoriteState) {
        this.favoriteState = favoriteState;
    }

    public String getStoreFavoriteUuid() {
        return storeFavoriteUuid;
    }

    public void setStoreFavoriteUuid(String storeFavoriteUuid) {
        this.storeFavoriteUuid = storeFavoriteUuid;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getCustomerService() {
        return customerService;
    }

    public void setCustomerService(String customerService) {
        this.customerService = customerService;
    }

    public String getCsNickName() {
        return csNickName;
    }

    public void setCsNickName(String csNickName) {
        this.csNickName = csNickName;
    }

    public double getStoreScore() {
        return storeScore;
    }

    public void setStoreScore(double storeScore) {
        this.storeScore = storeScore;
    }

    public double getLogisticSpeedScore() {
        return logisticSpeedScore;
    }

    public void setLogisticSpeedScore(double logisticSpeedScore) {
        this.logisticSpeedScore = logisticSpeedScore;
    }

    public double getServiceAttitudeScore() {
        return serviceAttitudeScore;
    }

    public void setServiceAttitudeScore(double serviceAttitudeScore) {
        this.serviceAttitudeScore = serviceAttitudeScore;
    }

    public double getProdDescScore() {
        return prodDescScore;
    }

    public void setProdDescScore(double prodDescScore) {
        this.prodDescScore = prodDescScore;
    }

    @Override
    public String toString() {
        return "StoreInfoDTO{" +
                "storeUuid='" + storeUuid + '\'' +
                ", storeName='" + storeName + '\'' +
                ", accountUuid='" + accountUuid + '\'' +
                ", logo='" + logo + '\'' +
                ", storeType='" + storeType + '\'' +
                ", roleUuid='" + roleUuid + '\'' +
                ", companyIntroduction='" + companyIntroduction + '\'' +
                ", openTime='" + openTime + '\'' +
                ", brandStory='" + brandStory + '\'' +
                ", brandVideo='" + brandVideo + '\'' +
                ", storeService='" + storeService + '\'' +
                ", supportPickup='" + supportPickup + '\'' +
                ", supportSubShop='" + supportSubShop + '\'' +
                ", supportWholesale='" + supportWholesale + '\'' +
                ", enableCod='" + enableCod + '\'' +
                ", enablePickUp='" + enablePickUp + '\'' +
                ", enableSubStore='" + enableSubStore + '\'' +
                ", enableWholesale='" + enableWholesale + '\'' +
                ", productCount=" + productCount +
                ", saleCount=" + saleCount +
                ", favoriteCount=" + favoriteCount +
                ", favoriteState='" + favoriteState + '\'' +
                ", storeFavoriteUuid='" + storeFavoriteUuid + '\'' +
                ", qq='" + qq + '\'' +
                ", customerService='" + customerService + '\'' +
                ", csNickName='" + csNickName + '\'' +
                ", storeScore=" + storeScore +
                ", logisticSpeedScore=" + logisticSpeedScore +
                ", serviceAttitudeScore=" + serviceAttitudeScore +
                ", prodDescScore=" + prodDescScore +
                '}';
    }
}
