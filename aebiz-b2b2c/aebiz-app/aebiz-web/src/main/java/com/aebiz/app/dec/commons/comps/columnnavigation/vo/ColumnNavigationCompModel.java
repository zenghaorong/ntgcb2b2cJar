package com.aebiz.app.dec.commons.comps.columnnavigation.vo;


import com.aebiz.app.dec.commons.comps.columnnavigation.ColumnNavigationController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

public class ColumnNavigationCompModel extends BaseCompModel {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;




	public ColumnNavigationCompModel() {
		super(ColumnNavigationController.class,"/columnNavigationComp/toParamsDesign");

	}

	private String compName = "店铺简介";
	private String compType = "ColumnNavigationCompModel";
	
	
    private String categoryName=""; //名称
    
    private String categoryUuid="";
    
    private String parentUuid="";
    
    private String content="";
    
    private String url="";
    
    
    
    //private List<ColumnNavigationCompModel> list =new ArrayList<>();
       
    
    
    


	public String getCompName() {
		return compName;
	}


	public void setCompName(String compName) {
		this.compName = compName;
	}


	public String getCompType() {
		return compType;
	}


	public void setCompType(String compType) {
		this.compType = compType;
	}








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


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


    




	
	


	


}
