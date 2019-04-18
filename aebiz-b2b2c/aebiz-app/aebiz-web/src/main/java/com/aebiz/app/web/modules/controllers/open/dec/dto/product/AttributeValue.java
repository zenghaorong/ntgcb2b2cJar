package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

/**
 * 
 * 属性值json
 * @author huangpinpin
 *
 */
public class AttributeValue {
	
	/* 属性值uuid*/
	private String valueUuid;
	
	/* 属性值*/
	private String value;
	
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	
	public String getValueUuid() {
		return valueUuid;
	}

	public void setValueUuid(String valueUuid) {
		this.valueUuid = valueUuid;
	}

	public String toString() {
		// TODO Auto-generated method stub
		return "[valueUuid="+this.getValueUuid()+",value="+this.getValue()+"]";
	}
}
