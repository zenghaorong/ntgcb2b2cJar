package com.aebiz.app.dec.commons.comps.producttab.vo;

import java.io.Serializable;

public class ProductTabDetailModel implements Serializable{

	private static final long serialVersionUID = 1L;

	private String productUuid;
	
	private String price;
	
	private String productName;
	
	private String pic;

	public String getProductUuid() {
		return productUuid;
	}

	public void setProductUuid(String productUuid) {
		this.productUuid = productUuid;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	
	
	
	
}
