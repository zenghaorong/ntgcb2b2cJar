package com.aebiz.app.dec.commons.comps.picturead.vo;

import java.util.List;

public class PictureAdModel {

	
	
	
	
	
	
	

	/**
	 * 图片地址
	 */
	
	private String imgSrc="";
	
	
	/**
	 * 图标名称
	 */
	private String imgName="";
	
	/**
	 * 图标uuid
	 */
	
	private String imgUuid="";
	
	
	
	
	

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

	private int pageShow;
	private int nowPage;
	private int totalNum;
	private int totalPage;
	private List<PictureAdModel> rows;



	public int getFromNum() {
		return ((getNowPage() - 1) * this.pageShow);
	}



	public List<PictureAdModel> getRows() {
		return rows;
	}

	public void setRows(List<PictureAdModel> rows) {
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
}
