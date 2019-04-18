package com.aebiz.app.dec.commons.comps.recommendproduct.vo;

import java.util.List;

public class RecommendProductModel implements java.io.Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	/**
	 * 商品价格
	 */
	private double price=0.00;

	/**
	 * 商品标题
	 */
	private String name="";
	
	
	
	/**
	 * 商品地址
	 */
	private String imgsrc="";
	
	
	/**
	 * 商品uuid
	 */
	private String productUuid="";
	
	
	/**
	 * 推荐商品的uuid集合
	 */
	private String param_inProductUuids="";
	
	
	
	
	private String lableName;
	
	private String sku;


	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getLableName() {
		return lableName;
	}



	public void setLableName(String lableName) {
		this.lableName = lableName;
	}



	public String getParam_inProductUuids() {
		return param_inProductUuids;
	}



	public void setParam_inProductUuids(String param_inProductUuids) {
		this.param_inProductUuids = param_inProductUuids;
	}



	public double getPrice() {
		return price;
	}



	public void setPrice(double price) {
		this.price = price;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getImgsrc() {
		return imgsrc;
	}



	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}



	public String getProductUuid() {
		return productUuid;
	}



	public void setProductUuid(String productUuid) {
		this.productUuid = productUuid;
	}

	private int pageShow;
	private int nowPage;
	private int totalNum;
	private int totalPage;
	private List<RecommendProductModel> rows;



	public int getFromNum() {
		return ((getNowPage() - 1) * this.pageShow);
	}



	public List<RecommendProductModel> getRows() {
		return rows;
	}

	public void setRows(List<RecommendProductModel> rows) {
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
