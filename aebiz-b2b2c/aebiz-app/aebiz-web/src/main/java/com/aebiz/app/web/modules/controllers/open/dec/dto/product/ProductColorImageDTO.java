package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

public class ProductColorImageDTO {
	
	private String uuid;

	/* 商品uuid*/
	private String productUuid;
	/* 属性uuid*/
	private String attributeUuid;
	/* 属性值uuid*/
	private String attrValueUuid;
	/* 属性值名称*/
	private String colorName;
	
	private String bigImageUrl;
	
	private String centerImageUrl;
	
	private String smallImageUrl;
	
	public ProductColorImageDTO() {
		
	}
	
	/* 位置*/
	private int position=0;
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
	public String getAttributeUuid() {
		return attributeUuid;
	}
	public void setAttributeUuid(String attributeUuid) {
		this.attributeUuid = attributeUuid;
	}
	public String getAttrValueUuid() {
		return attrValueUuid;
	}
	public void setAttrValueUuid(String attrValueUuid) {
		this.attrValueUuid = attrValueUuid;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
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
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
	
}
