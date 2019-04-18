package com.aebiz.app.dec.commons.comps.qrcode.vo;

import java.util.List;

public class QRCodeModel {
	
	

	
	/**
	 * 图标名称
	 */
	private String imgName="";
	
	private String imgUuid="";
	
	
	
	
	private String imgSrc="";
	
	
	
	
	private int pageShow;
	private int nowPage;
	private int totalNum;
	private int totalPage;
	private List<QRCodeModel> rows;



	public int getFromNum() {
		return ((getNowPage() - 1) * this.pageShow);
	}



	public List<QRCodeModel> getRows() {
		return rows;
	}

	public void setRows(List<QRCodeModel> rows) {
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



	public String getImgSrc() {
		return imgSrc;
	}



	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}



	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
	
	
	

}
