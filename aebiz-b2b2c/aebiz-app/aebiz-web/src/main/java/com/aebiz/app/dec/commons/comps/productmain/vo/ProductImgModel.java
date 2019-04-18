package com.aebiz.app.dec.commons.comps.productmain.vo;

import java.io.Serializable;

public class ProductImgModel implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String bigImage;

	private String smallImageUrl;
	
	private String centerImageUrl;
	
	private String bigImageUrl;

	public ProductImgModel() {
	}

	public ProductImgModel(String bigImage, String smallImageUrl,
			String centerImageUrl, String bigImageUrl) {
		this.bigImage = bigImage;
		this.smallImageUrl = smallImageUrl;
		this.centerImageUrl = centerImageUrl;
		this.bigImageUrl = bigImageUrl;
	}

	public String getBigImage() {
		return bigImage;
	}

	public void setBigImage(String bigImage) {
		this.bigImage = bigImage;
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
	
	
}
