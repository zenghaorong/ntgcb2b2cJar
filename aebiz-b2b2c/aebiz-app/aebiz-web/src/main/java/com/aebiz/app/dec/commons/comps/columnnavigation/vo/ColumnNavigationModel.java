package com.aebiz.app.dec.commons.comps.columnnavigation.vo;

import java.util.HashMap;
import java.util.List;

public class ColumnNavigationModel {

	
	
	private String categoryName=""; //名称
    
    private String categoryUuid="";
    
    private String parentUuid="";
    
    private String content="";
	
	
    
    private HashMap<ColumnNavigationModel,List<ColumnNavigationModel>> map =new HashMap<>();



	public String getCategoryName() {
		return categoryName;
	}



	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}



	public String getCategoryUuid() {
		return categoryUuid;
	}



	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}



	public String getParentUuid() {
		return parentUuid;
	}



	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}



	public HashMap<ColumnNavigationModel, List<ColumnNavigationModel>> getMap() {
		return map;
	}



	public void setMap(HashMap<ColumnNavigationModel, List<ColumnNavigationModel>> map) {
		this.map = map;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}
    
    
    
	

}
