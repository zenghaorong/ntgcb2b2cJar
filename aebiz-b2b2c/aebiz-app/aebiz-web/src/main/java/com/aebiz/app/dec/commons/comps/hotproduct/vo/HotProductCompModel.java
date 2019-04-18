package com.aebiz.app.dec.commons.comps.hotproduct.vo;


import com.aebiz.app.dec.commons.comps.hotproduct.HotProductController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

public class HotProductCompModel extends BaseCompModel {
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String compName = "热销商品";
	private String compType = "hotproduct";
	
	/**
	 * 标签名称
	 */
	private String lableName="商品热销";

	/**
	 * 数量
	 */
	private int count=0;

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
	
	/**
	 * 商品详情链接
	 */
	private String productUrl="";
	
	
	
	
	
	
	
	
	
	
	


	public String getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	public String getParam_inProductUuids() {
		return param_inProductUuids;
	}

	public void setParam_inProductUuids(String param_inProductUuids) {
		this.param_inProductUuids = param_inProductUuids;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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

	public HotProductCompModel() {
		
		super(HotProductController.class,"/hotProductComp/toParamsDesign");

	}


	

}
