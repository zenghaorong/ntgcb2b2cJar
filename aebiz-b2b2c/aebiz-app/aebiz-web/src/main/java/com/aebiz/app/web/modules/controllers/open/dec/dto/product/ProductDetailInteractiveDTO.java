package com.aebiz.app.web.modules.controllers.open.dec.dto.product;


import com.aebiz.app.web.modules.controllers.open.dec.dto.promotion.PromotionDetailDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.store.ConsumerProtectionDTO;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 商品详情页详细信息DTO
 *
 * Created by Aebiz_yjq on 2017/1/21.
 */
public class ProductDetailInteractiveDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/* 商品uuid */
    private String productUuid="";

    /* 商品名称 */
    private String productName="";

    /*商品推荐语*/
    private String adviceNote="";

    /*商品默认SKU编号*/
    private String skuNo="";

    /*商品库存*/
    private int stock=0;

    /*商品描述*/
    private String desc="";

    /*商城价格*/
    private int shopPrice=0;

    /*市场价格*/
    private int marketPrice=0;

    /*大图url*/
    private String bigImageUrl="";

    /*中图url*/
    private String centerImageUrl="";

    /*小图url*/
    private String smallImageUrl="";

	/*收藏个数*/
    private int favoriteCount=0;

    /*销量*/
    private int saleCount=0;

    /*评论个数*/
    private int appCount=0;


    /*第一条评论对象*/
    private ProductAppraiseForCompDTO appraiseInfo=null;

    /*商品多角度视图集合*/
    private List<ProductMainMultiImageDTO> productMultiImage=null;
    
    /*商品颜色规格图片*/
    private List<ProductColorImageDTO> productColorImage = null;
    
	
    /*商户的服务集合：正品保证、七天换货等*/
    private List<ConsumerProtectionDTO> protections=null;

    /*该商品参与的单品促销集合*/
    private List<PromotionDetailDTO> productPromotions=null;

    /*商品组合信息集合*/
    private List<ProductGroupApiDTO> groupList=null;

    /*店铺促销名称集合*/
    private List<PromotionDetailDTO> storePromotions=null;

    /*供选择的前台属性、属性值*/
    private List<SelectedAttributeInValueJsonDTO> selectAttrValues=null;

    /*店铺名称*/
    private String storeName="";

    /*商户综合评分，满分5*/
    private double storeScore=0.0;

    /*店铺log图片地址*/
    private String logUrl="";

    /*店铺商品数*/
    private int productCount=0;

    /*店铺被收藏数*/
    private int storeFavoriteCount=0;

    /*当前系统时间*/
    private String nowTime="";

    /*商户uuid*/
    private String storeUuid="";

    /*商品是否已经收藏：1-已收藏;0-未收藏*/
    private String hasFavorite="0";

    /*商品收藏记录UUID*/
    private String productFavoriteUuid;

    /*店铺是否已收藏：1-已收藏;0-未收藏*/
    private String hasStoreFavorite="0";

    /*店铺收藏记录UUID*/
    private String storeFavoriteUuid;

    /*商品上架状态(1-上架；0-下架)*/
    private String state;

    /*商品删除状态(0-删除；1-未删除)*/
    private String delFlag;

    /*商品发布后生成的所有的sku库存对象List*/
    private ProductAttrStockDTO attrStock;

    /*促销活动开始时间*/
    private String startTime;

    /*促销活动结束时间*/
    private String endTime;

    /*促销活动名称*/
    private List<String> promotionName;

    /*商品参与的所有的促销活动的类型数组*/
    private String[] privilegeTypes;

    /*售后信息*/
    private String afterService;

    /* 商品规格属性值 */
    private String prodAttrs;
    
    /* 是否自营 */
    private String selfSupport;
    
    /* 最低购买积分 */
  	private int  leastIntegral;

  	/*当前商品的规格*/
  	private String attrValueId;
  	
  	/** 是否规格商品**/
  	private String spec;

    public ProductDetailInteractiveDTO() {
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

    public String getAttrValueId() {
        return attrValueId;
    }

    public void setAttrValueId(String attrValueId) {
        this.attrValueId = attrValueId;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(int shopPrice) {
        this.shopPrice = shopPrice;
    }

    public int getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(int marketPrice) {
        this.marketPrice = marketPrice;
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

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    public int getAppCount() {
        return appCount;
    }

    public void setAppCount(int appCount) {
        this.appCount = appCount;
    }

    public ProductAppraiseForCompDTO getAppraiseInfo() {
        return appraiseInfo;
    }

    public void setAppraiseInfo(ProductAppraiseForCompDTO appraiseInfo) {
        this.appraiseInfo = appraiseInfo;
    }

    public List<ProductMainMultiImageDTO> getProductMultiImage() {
        return productMultiImage;
    }

    public void setProductMultiImage(List<ProductMainMultiImageDTO> productMultiImage) {
        this.productMultiImage = productMultiImage;
    }

    public List<ConsumerProtectionDTO> getProtections() {
        return protections;
    }

    public void setProtections(List<ConsumerProtectionDTO> protections) {
        this.protections = protections;
    }

    public List<PromotionDetailDTO> getProductPromotions() {
        return productPromotions;
    }

    public void setProductPromotions(List<PromotionDetailDTO> productPromotions) {
        this.productPromotions = productPromotions;
    }

    public List<ProductGroupApiDTO> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<ProductGroupApiDTO> groupList) {
        this.groupList = groupList;
    }

    public List<PromotionDetailDTO> getStorePromotions() {
        return storePromotions;
    }

    public void setStorePromotions(List<PromotionDetailDTO> storePromotions) {
        this.storePromotions = storePromotions;
    }

    public List<SelectedAttributeInValueJsonDTO> getSelectAttrValues() {
        return selectAttrValues;
    }

    public void setSelectAttrValues(List<SelectedAttributeInValueJsonDTO> selectAttrValues) {
        this.selectAttrValues = selectAttrValues;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public double getStoreScore() {
        return storeScore;
    }

    public void setStoreScore(double storeScore) {
        this.storeScore = storeScore;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public int getStoreFavoriteCount() {
        return storeFavoriteCount;
    }

    public void setStoreFavoriteCount(int storeFavoriteCount) {
        this.storeFavoriteCount = storeFavoriteCount;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    public String getStoreUuid() {
        return storeUuid;
    }

    public void setStoreUuid(String storeUuid) {
        this.storeUuid = storeUuid;
    }

    public String getHasFavorite() {
        return hasFavorite;
    }

    public void setHasFavorite(String hasFavorite) {
        this.hasFavorite = hasFavorite;
    }

    public String getProductFavoriteUuid() {
        return productFavoriteUuid;
    }

    public void setProductFavoriteUuid(String productFavoriteUuid) {
        this.productFavoriteUuid = productFavoriteUuid;
    }

    public String getHasStoreFavorite() {
        return hasStoreFavorite;
    }

    public void setHasStoreFavorite(String hasStoreFavorite) {
        this.hasStoreFavorite = hasStoreFavorite;
    }

    public String getStoreFavoriteUuid() {
        return storeFavoriteUuid;
    }

    public void setStoreFavoriteUuid(String storeFavoriteUuid) {
        this.storeFavoriteUuid = storeFavoriteUuid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public ProductAttrStockDTO getAttrStock() {
        return attrStock;
    }

    public void setAttrStock(ProductAttrStockDTO attrStock) {
        this.attrStock = attrStock;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<String> getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(List<String> promotionName) {
        this.promotionName = promotionName;
    }

    public String[] getPrivilegeTypes() {
        return privilegeTypes;
    }

    public void setPrivilegeTypes(String[] privilegeTypes) {
        this.privilegeTypes = privilegeTypes;
    }

    public String getAfterService() {
        return afterService;
    }

    public void setAfterService(String afterService) {
        this.afterService = afterService;
    }

    public String getProdAttrs() {
        return prodAttrs;
    }

    public void setProdAttrs(String prodAttrs) {
        this.prodAttrs = prodAttrs;
    }

    
    public String isSelfSupport() {
		return selfSupport;
	}

	public void setSelfSupport(String selfSupport) {
		this.selfSupport = selfSupport;
	}

	
	public int getLeastIntegral() {
		return leastIntegral;
	}

	public void setLeastIntegral(int leastIntegral) {
		this.leastIntegral = leastIntegral;
	}

	public String getSelfSupport() {
		return selfSupport;
	}

	
	public List<ProductColorImageDTO> getProductColorImage() {
		return productColorImage;
	}

	public void setProductColorImage(List<ProductColorImageDTO> productColorImage) {
		this.productColorImage = productColorImage;
	}
	

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	@Override
    public String toString() {
        return "ProductDetailInteractiveDTO{" +
                "productUuid='" + productUuid + '\'' +
                ", productName='" + productName + '\'' +
                ", adviceNote='" + adviceNote + '\'' +
                ", skuNo='" + skuNo + '\'' +
                ", stock=" + stock +
                ", desc='" + desc + '\'' +
                ", shopPrice=" + shopPrice +
                ", marketPrice=" + marketPrice +
                ", bigImageUrl='" + bigImageUrl + '\'' +
                ", centerImageUrl='" + centerImageUrl + '\'' +
                ", smallImageUrl='" + smallImageUrl + '\'' +
                ", favoriteCount=" + favoriteCount +
                ", saleCount=" + saleCount +
                ", appCount=" + appCount +
                ", appraiseInfo=" + appraiseInfo +
                ", productMultiImage=" + productMultiImage +
                ", protections=" + protections +
                ", productPromotions=" + productPromotions +
                ", groupList=" + groupList +
                ", storePromotions=" + storePromotions +
                ", selectAttrValues=" + selectAttrValues +
                ", storeName='" + storeName + '\'' +
                ", storeScore=" + storeScore +
                ", logUrl='" + logUrl + '\'' +
                ", productCount=" + productCount +
                ", storeFavoriteCount=" + storeFavoriteCount +
                ", nowTime='" + nowTime + '\'' +
                ", storeUuid='" + storeUuid + '\'' +
                ", hasFavorite='" + hasFavorite + '\'' +
                ", productFavoriteUuid='" + productFavoriteUuid + '\'' +
                ", hasStoreFavorite='" + hasStoreFavorite + '\'' +
                ", storeFavoriteUuid='" + storeFavoriteUuid + '\'' +
                ", state='" + state + '\'' +
                ", delFlag='" + delFlag + '\'' +
                ", attrStock=" + attrStock +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", promotionName='" + promotionName + '\'' +
                ", privilegeTypes=" + Arrays.toString(privilegeTypes) +
                ", afterService='" + afterService + '\'' +
                ", prodAttrs='" + prodAttrs + '\'' +
                '}';
    }
}
