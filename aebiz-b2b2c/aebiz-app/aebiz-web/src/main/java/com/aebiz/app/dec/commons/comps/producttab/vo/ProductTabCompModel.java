package com.aebiz.app.dec.commons.comps.producttab.vo;


import com.aebiz.app.dec.commons.comps.producttab.ProductTabController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.alibaba.fastjson.annotation.JSONField;

public class ProductTabCompModel extends BaseCompModel {
	
	private static final long serialVersionUID = 1L;

	//展示方式 ，1：普通展示，2：轮播展示
	private String showStyle = "1";
	
	@JSONField(serialize = false)
	private String tabsJson ="[]"; 
	
	private String productDetailUrl = "";
	
	private String loginTopic = "";
	
	private String collectUrl = "";
	

	public ProductTabCompModel() {
		super(ProductTabController.class, "/producttab/toParamsDesign");
		
	}


	public String getShowStyle() {
		return showStyle;
	}


	public void setShowStyle(String showStyle) {
		this.showStyle = showStyle;
	}


	public String getTabsJson() {
		return tabsJson;
	}


	public void setTabsJson(String tabsJson) {
		this.tabsJson = tabsJson;
	}


	public String getProductDetailUrl() {
		return productDetailUrl;
	}


	public void setProductDetailUrl(String productDetailUrl) {
		this.productDetailUrl = productDetailUrl;
	}

	public String getLoginTopic() {
		return loginTopic;
	}

	public void setLoginTopic(String loginTopic) {
		this.loginTopic = loginTopic;
	}


	public String getCollectUrl() {
		return collectUrl;
	}


	public void setCollectUrl(String collectUrl) {
		this.collectUrl = collectUrl;
	}

}
