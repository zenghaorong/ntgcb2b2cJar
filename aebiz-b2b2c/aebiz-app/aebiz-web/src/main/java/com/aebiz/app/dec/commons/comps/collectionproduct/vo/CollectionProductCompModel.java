package com.aebiz.app.dec.commons.comps.collectionproduct.vo;


import com.aebiz.app.dec.commons.comps.collectionproduct.CollectionProductController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

public class CollectionProductCompModel extends BaseCompModel {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;










	public CollectionProductCompModel() {
		super(CollectionProductController.class,"/collectionProductComp/toParamsDesign");

	}

	private String compName = "收藏商品";
	private String compType = "collectionproduct";
	

	/**
	 * 标签名称
	 */
	private String lableName="收藏商品";
	
	
	/**
	 * 图标地址
	 */
	private String imgSrc="";
	
	private String categoryName="";
	
	private String categoryUuid="";
	
	private String parentUuid="";
	
	/**
	 * 发布主题
	 */
	private String topic="";
	
	private String collectProductUrl="";
	
	
	private String imgUuid="";
	
	
	
	
	
	
	




	public String getImgUuid() {
		return imgUuid;
	}



	public void setImgUuid(String imgUuid) {
		this.imgUuid = imgUuid;
	}



	public String getCollectProductUrl() {
		return collectProductUrl;
	}



	public void setCollectProductUrl(String collectProductUrl) {
		this.collectProductUrl = collectProductUrl;
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



	public String getLableName() {
		return lableName;
	}



	public void setLableName(String lableName) {
		this.lableName = lableName;
	}



	public String getImgSrc() {
		return imgSrc;
	}



	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}





	public String getTopic() {
		return topic;
	}



	public void setTopic(String topic) {
		this.topic = topic;
	}



	
	
	


	
	
	
	


}
