package com.aebiz.app.dec.commons.vo;

import com.alibaba.fastjson.annotation.JSONField;


public class BaseCompModel implements java.io.Serializable {
	private String compId;
	private boolean needAsyncInit = true;
	@JSONField(serialize = false)
	private Class compControllerClass;
	@JSONField(serialize = false)
	private String urlToParamsJsp;

	public BaseCompModel(Class compControllerClass, String urlToParamsJsp) {
		this.compControllerClass = compControllerClass;
		this.urlToParamsJsp = urlToParamsJsp;
	}

	public String getUrlToParamsJsp() {
		return urlToParamsJsp;
	}

	public void setUrlToParamsJsp(String urlToParamsJsp) {
		this.urlToParamsJsp = urlToParamsJsp;
	}

	public Class getCompControllerClass() {
		return compControllerClass;
	}

	public void setCompControllerClass(Class compControllerClass) {
		this.compControllerClass = compControllerClass;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public boolean isNeedAsyncInit() {
		return needAsyncInit;
	}

	public void setNeedAsyncInit(boolean needAsyncInit) {
		this.needAsyncInit = needAsyncInit;
	}

	@Override
	public String toString() {
		return "BaseCompModel [compId=" + compId + ", needAsyncInit="
				+ needAsyncInit + ", compControllerClass="
				+ compControllerClass + ", urlToParamsJsp=" + urlToParamsJsp
				+ "]";
	}
}
