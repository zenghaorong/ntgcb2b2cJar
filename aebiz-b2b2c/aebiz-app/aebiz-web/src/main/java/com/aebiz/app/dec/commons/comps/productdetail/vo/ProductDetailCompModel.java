package com.aebiz.app.dec.commons.comps.productdetail.vo;


import com.aebiz.app.dec.commons.comps.productdetail.ProductDetailCompController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

public class ProductDetailCompModel extends BaseCompModel {

	private static final long serialVersionUID = 1L;
	
	private String compName = "商品详情选项卡";
	
	private String compType = "productdetail";
	
	//组件的商品Uuid
	private String productUuid = "";
	
	//评价每页显示的条数
	private  int pageShow = 8;
	
	//显示售后服务
	private boolean showAfterService = true;
	
	//显示规格属性
	private boolean showAttribute = false;
	
	//显示购买咨询
	private boolean showBuyRecord = false;

	
	public ProductDetailCompModel() {
		super(ProductDetailCompController.class, "/productdetail/toParamsDesign");
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


	public boolean isShowAfterService() {
		return showAfterService;
	}

	public void setShowAfterService(boolean showAfterService) {
		this.showAfterService = showAfterService;
	}

	public boolean isShowAttribute() {
		return showAttribute;
	}

	public void setShowAttribute(boolean showAttribute) {
		this.showAttribute = showAttribute;
	}

	public boolean isShowBuyRecord() {
		return showBuyRecord;
	}

	public void setShowBuyRecord(boolean showBuyRecord) {
		this.showBuyRecord = showBuyRecord;
	}

	public String getProductUuid() {
		return productUuid;
	}

	public void setProductUuid(String productUuid) {
		this.productUuid = productUuid;
	}

	public int getPageShow() {
		return pageShow;
	}

	public void setPageShow(int pageShow) {
		this.pageShow = pageShow;
	}


}
