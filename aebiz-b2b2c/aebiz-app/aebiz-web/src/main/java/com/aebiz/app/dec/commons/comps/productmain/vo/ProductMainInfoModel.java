package com.aebiz.app.dec.commons.comps.productmain.vo;

import com.aebiz.app.web.modules.controllers.open.dec.dto.product.SelectedAttributeInValueJsonDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.promotion.PromotionDetailDTO;

import java.io.Serializable;
import java.util.List;

public class ProductMainInfoModel implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String productUuid;

	//商品名称
	private String productName;
	
	//是否自营
	private boolean isSelfStore;
	
	//商品推荐语
	private String adviceNote;
	
	//市场价格
	private String marketPrice;
	
	//商城价格
	private String shopPrice;
	
	//商品评分
	private double productScore;
	
	//总评价数
	private int appCount;

	//当前货品的规格属性
	private String attrValueId;
	//库存
	private int stock;
	
	//默认skuNo
	private String skuNo;
	
	private String smallImageUrl;
	
	private String centerImageUrl;
	
	private String bigImageUrl;
	
	
	private String productType ;
	
	private String canShowBuy;
	
	private int saleCount = 0;
	
	/**
	 * 是否规格商品
	 */
	private String Spec;
	
	//商品的规格属性
	private List<SelectedAttributeInValueJsonDTO>  productAttributes;
	
	
	//多角度图片列表
	private List<ProductImgModel> imgList;
	
	//初始化 默认选择的商品属性值的 valueUuid
	private List<String> initSelectSpecUuids;
	
	
	//最低购买积分
	private int  leastIntegral;
	
	//促销名称
	private List<String>  promotionName;
	
	//单品促销活动
	private List<PromotionDetailDTO> productPromotions;
	
	//店铺促销活动
	private List<PromotionDetailDTO> storePromotions;

	public String getAttrValueId() {
		return attrValueId;
	}

	public void setAttrValueId(String attrValueId) {
		this.attrValueId = attrValueId;
	}

	public String getShopPrice() {
		return shopPrice;
	}

	public void setShopPrice(String shopPrice) {
		this.shopPrice = shopPrice;
	}

	public String getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}

	

	public List<SelectedAttributeInValueJsonDTO> getProductAttributes() {
		return productAttributes;
	}

	public void setProductAttributes(
			List<SelectedAttributeInValueJsonDTO> productAttributes) {
		this.productAttributes = productAttributes;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public boolean isSelfStore() {
		return isSelfStore;
	}

	public void setSelfStore(boolean isSelfStore) {
		this.isSelfStore = isSelfStore;
	}

	public String getAdviceNote() {
		return adviceNote;
	}

	public void setAdviceNote(String adviceNote) {
		this.adviceNote = adviceNote;
	}

	public String getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}


	public double getProductScore() {
		return productScore;
	}

	public void setProductScore(double productScore) {
		this.productScore = productScore;
	}

	public int getAppCount() {
		return appCount;
	}

	public void setAppCount(int appCount) {
		this.appCount = appCount;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getSmallImageUrl() {
		return smallImageUrl;
	}

	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}

	public String getCenterImageUrl() {
		return centerImageUrl;
	}

	public void setCenterImageUrl(String centerImageUrl) {
		this.centerImageUrl = centerImageUrl;
	}

	public String getBigImageUrl() {
		return bigImageUrl;
	}

	public void setBigImageUrl(String bigImageUrl) {
		this.bigImageUrl = bigImageUrl;
	}

	public List<ProductImgModel> getImgList() {
		return imgList;
	}

	public void setImgList(List<ProductImgModel> imgList) {
		this.imgList = imgList;
	}

	public List<String> getInitSelectSpecUuids() {
		return initSelectSpecUuids;
	}

	public void setInitSelectSpecUuids(List<String> initSelectSpecUuids) {
		this.initSelectSpecUuids = initSelectSpecUuids;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getCanShowBuy() {
		return canShowBuy;
	}

	public void setCanShowBuy(String canShowBuy) {
		this.canShowBuy = canShowBuy;
	}

	public String getProductUuid() {
		return productUuid;
	}

	public void setProductUuid(String productUuid) {
		this.productUuid = productUuid;
	}

	public int getSaleCount() {
		return saleCount;
	}

	public void setSaleCount(int saleCount) {
		this.saleCount = saleCount;
	}

	public List<PromotionDetailDTO> getProductPromotions() {
		return productPromotions;
	}

	public void setProductPromotions(List<PromotionDetailDTO> productPromotions) {
		this.productPromotions = productPromotions;
	}

	public List<PromotionDetailDTO> getStorePromotions() {
		return storePromotions;
	}

	public void setStorePromotions(List<PromotionDetailDTO> storePromotions) {
		this.storePromotions = storePromotions;
	}

	public int getLeastIntegral() {
		return leastIntegral;
	}

	public void setLeastIntegral(int leastIntegral) {
		this.leastIntegral = leastIntegral;
	}

	public List<String> getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(List<String> promotionName) {
		this.promotionName = promotionName;
	}

	public String getSpec() {
		return Spec;
	}

	public void setSpec(String spec) {
		Spec = spec;
	}
	
	
}
