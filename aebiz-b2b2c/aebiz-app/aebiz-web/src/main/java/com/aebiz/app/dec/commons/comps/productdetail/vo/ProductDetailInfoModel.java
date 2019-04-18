package com.aebiz.app.dec.commons.comps.productdetail.vo;


public class ProductDetailInfoModel {

	//商品简介
	private String desc ;
	
	//售后服务
	private String afterService;
	
	//规格与包装
	private String prodAttr;
	
	
	private String productUuid;
	
	//商品小图
	private String smallImg;
	
	//商品skunNo
	private String skuNo;
	
	//商品价格
	private String price;
	
	//商品评价
	private ProductDetailCommentModel comments;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAfterService() {
		return afterService;
	}

	public void setAfterService(String afterService) {
		this.afterService = afterService;
	}

	public ProductDetailCommentModel getComments() {
		return comments;
	}

	public void setComments(ProductDetailCommentModel comments) {
		this.comments = comments;
	}

	
	public String getProdAttr() {
		return prodAttr;
	}

	public void setProdAttr(String prodAttr) {
		this.prodAttr = prodAttr;
	}
	
	public String getProductUuid() {
		return productUuid;
	}

	public void setProductUuid(String productUuid) {
		this.productUuid = productUuid;
	}

	public String getSmallImg() {
		return smallImg;
	}

	public void setSmallImg(String smallImg) {
		this.smallImg = smallImg;
	}

	public String getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}

	
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "ProductDetailInfoModel [desc=" + desc + ", afterService="
				+ afterService + ", comments=" + comments + "]";
	}
	
}
