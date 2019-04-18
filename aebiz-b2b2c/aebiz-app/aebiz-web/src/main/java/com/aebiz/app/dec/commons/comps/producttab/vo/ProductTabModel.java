package com.aebiz.app.dec.commons.comps.producttab.vo;

import java.io.Serializable;
import java.util.List;

public class ProductTabModel implements Serializable{

	private static final long serialVersionUID = 1L;

	//标签页名称
	private String tabName;
	
	//标签页对应的商品ID
	private List<ProductTabDetailModel> products;

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public List<ProductTabDetailModel> getProducts() {
		return products;
	}

	public void setProducts(List<ProductTabDetailModel> products) {
		this.products = products;
	}
	
	
}
