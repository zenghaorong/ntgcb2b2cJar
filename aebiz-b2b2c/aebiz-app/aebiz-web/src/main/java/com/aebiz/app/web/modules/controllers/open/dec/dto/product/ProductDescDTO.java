package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

import java.io.Serializable;

public class ProductDescDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/* 商品uuid*/
	private String productUuid;

	/* 是否使用店铺售后服务0-不使用 1-使用*/
	private String useDefult="1";

	/* 售后服务信息*/
	private String saleAfterNote;

	/* 商品描述*/
	private String description;

	/* 商品简介*/
	private String note;

	/*商品详情页中商品详细信息处，规格参数处需要展示的规格属性*/
	private String prodAttrs;
	
	

	public String getProductUuid() {
		return productUuid;
	}

	public void setProductUuid(String productUuid) {
		this.productUuid = productUuid;
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

}
