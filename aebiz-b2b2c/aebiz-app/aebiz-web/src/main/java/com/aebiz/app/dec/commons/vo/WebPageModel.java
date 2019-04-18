package com.aebiz.app.dec.commons.vo;

import java.util.HashMap;
import java.util.Map;

public class WebPageModel implements java.io.Serializable{
	private String pageUuid;
	private String pageType;
	private Map<String,Object> mapPageParams = new HashMap<String,Object>();
	private Map<String,BaseCompModel> mapComps = new HashMap<String,BaseCompModel>();
	public Map<String, Object> getMapPageParams() {
		return mapPageParams;
	}
	public void setMapPageParams(Map<String, Object> mapPageParams) {
		this.mapPageParams = mapPageParams;
	}
	public String getPageUuid() {
		return pageUuid;
	}
	public void setPageUuid(String pageUuid) {
		this.pageUuid = pageUuid;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	public Map<String, BaseCompModel> getMapComps() {
		return mapComps;
	}
	public void setMapComps(Map<String, BaseCompModel> mapComps) {
		this.mapComps = mapComps;
	}
	@Override
	public String toString() {
		return "WebPageModel [pageUuid=" + pageUuid + ", pageType=" + pageType + ", mapPageParams=" + mapPageParams
				+ ", mapComps=" + mapComps + "]";
	}
}
