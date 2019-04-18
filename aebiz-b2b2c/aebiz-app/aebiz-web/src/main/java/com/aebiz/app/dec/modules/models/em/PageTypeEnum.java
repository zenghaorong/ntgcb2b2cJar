package com.aebiz.app.dec.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * 页面类型： <br/>
 * 
 * 1. INDEX 首页 <br/>
 * 2. LIST 商品列表页 <br/>
 * 3. DETAIL 商品详情页 <br/>
 * 4. CUSTOM 自定义页 <br/>
 * 5. CHANNEL 频道页
 * 
 * @author yewei
 * 
 */
public enum PageTypeEnum {
	BASE(1, Message.getMessage("pages.pageType.index")), LIST(2,
			Message.getMessage("pages.pageType.list")), DETAIL(3,
			Message.getMessage("pages.pageType.detail")), CUSTOM(4,
			Message.getMessage("pages.pageType.custom")), CHANNEL(5,
			Message.getMessage("pages.pageType.channel"));

	private int key ;
	private String value;

	private PageTypeEnum(int key, String value) {
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

	public static String getValue(String key) {
		for (PageTypeEnum pe : PageTypeEnum.values()) {
			if (key.equals(pe.key)) {
				return pe.value;
			}
		}
		return "";
	}
}
