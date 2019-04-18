package com.aebiz.app.dec.commons.comps.collectionproduct.vo;

import java.util.List;

public class CollectionProductModel {

	/**
	 * 图标地址
	 */
	private String imgSrc="";
	
	
	/**
	 * 图标名称
	 */
	private String imgName;
	
	
	private String imgUuid;
	
	
	
	private int pageShow;
	private int nowPage;
	private int totalNum;
	private int totalPage;
	private List<CollectionProductModel> rows;



	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}




	public int getFromNum() {
		return ((getNowPage() - 1) * this.pageShow);
	}



	public List<CollectionProductModel> getRows() {
		return rows;
	}

	public void setRows(List<CollectionProductModel> rows) {
		this.rows = rows;
	}

	public int getPageShow() {
		return this.pageShow;
	}

	public void setPageShow(int pageShow) {
		this.pageShow = pageShow;
	}

	public int getNowPage() {
		if (this.nowPage <= 0) {
			this.nowPage = 1;
		}
		if (this.nowPage > getTotalPage()) {
			this.nowPage = getTotalPage();
		}
		return this.nowPage;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	public int getTotalNum() {
		return this.totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getTotalPage() {
		if (this.totalNum > 0) {
			return (int) Math.ceil(this.totalNum * 1.0D / this.pageShow);
		}
		return this.totalPage;
	}




	public String getImgSrc() {
		return imgSrc;
	}




	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}




	public String getImgName() {
		return imgName;
	}




	public void setImgName(String imgName) {
		this.imgName = imgName;
	}




	public String getImgUuid() {
		return imgUuid;
	}




	public void setImgUuid(String imgUuid) {
		this.imgUuid = imgUuid;
	}
	
	
	
	
}
