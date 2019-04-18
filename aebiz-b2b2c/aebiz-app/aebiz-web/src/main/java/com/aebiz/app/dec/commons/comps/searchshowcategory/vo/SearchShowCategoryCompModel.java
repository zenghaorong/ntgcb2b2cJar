package com.aebiz.app.dec.commons.comps.searchshowcategory.vo;


import com.aebiz.app.dec.commons.comps.searchshowcategory.SearchShowCategoryCompController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

/**
 * 搜索显示分类组件
 * @author hyl
 * 2017年2月20日
 */
public class SearchShowCategoryCompModel extends BaseCompModel {

	public SearchShowCategoryCompModel() {
		super(SearchShowCategoryCompController.class, "/searchShowCategoryComp/toParamsDesign");
	}

	private static final long serialVersionUID = 7872251110303236806L;
	//组件名称
	private String compName = "搜索显示分类组件";
	//组件类型
	private String compType = "searchShowCategoryComp";
	//商品列表页地址
	private String productListUrl;
	//共享数据名称
	private String shareDataName ="";
	
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
	public String getShareDataName() {
		return shareDataName;
	}
	public void setShareDataName(String shareDataName) {
		this.shareDataName = shareDataName;
	}
	public String getProductListUrl() {
		return productListUrl;
	}
	public void setProductListUrl(String productListUrl) {
		this.productListUrl = productListUrl;
	}

}
