package com.aebiz.app.dec.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 组件版本发布类型： <br/>
 * 
 * 1. 电脑版 首页 <br/>
 * 2. 微信版 <br/>
 * 3. 手机版 <br/>
 * 4. 电视版 <br/>
 * 
 * @author lq
 * 
 */
public enum ComponentVersionTypeEnum {
	PC(1, Message.getMessage("components.versionType.pc")), WEIXIN(2,
			Message.getMessage("components.versionType.weixin")), PHONE(3, Message.getMessage("components.versionType.phone")), TV(
			4, Message.getMessage("components.versionType.tv"));

	private int key;
	private String value;;

	private ComponentVersionTypeEnum(int key, String value) {
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static String getValue(int key) {
		for (ComponentVersionTypeEnum pe : ComponentVersionTypeEnum.values()) {
			if (key==pe.key) {
				return pe.value;
			}
		}
		return "";
	}
}
