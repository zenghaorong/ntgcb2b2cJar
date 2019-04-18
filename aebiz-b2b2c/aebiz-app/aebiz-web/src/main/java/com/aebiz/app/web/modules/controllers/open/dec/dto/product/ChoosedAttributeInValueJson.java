package com.aebiz.app.web.modules.controllers.open.dec.dto.product;


import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 该类主要是用来封装商品各个属性所选属性值 并用该类转换成json
 * 
 * @author huangpinpin
 * 
 */
public class ChoosedAttributeInValueJson {

	public static final String YES = "1"; // 颜色
	public static final String NO = "0"; // 非颜色

	/* 属性uuid */
	private String attributeUuid="";
	/* 属性名称 */
	private String attributeName="";
	/* 属性sku */
	private String skuNo;

	/* 是否是颜色属性 */
	private String canColor="";
	/* 属性值集合 */
	private List<AttributeValue> values=new ArrayList<AttributeValue>();

	public void setAttributeUuid(String attributeUuid) {
		this.attributeUuid = attributeUuid;
	}

	public String getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}

	public String getAttributeUuid() {
		return attributeUuid;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setCanColor(String canColor) {
		this.canColor = canColor;
	}

	public String getCanColor() {
		return canColor;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public void setValues(List<AttributeValue> values) {
		this.values = values;
	}

	public List<AttributeValue> getValues() {
		return values;
	}
}
