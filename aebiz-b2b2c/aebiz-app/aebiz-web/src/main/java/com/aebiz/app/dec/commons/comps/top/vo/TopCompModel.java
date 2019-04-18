package com.aebiz.app.dec.commons.comps.top.vo;


import com.aebiz.app.dec.commons.comps.top.TopCompController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

/**
 * 顶部组件Model
 * @author hyl
 * 2016年12月19日
 */
public class TopCompModel extends BaseCompModel {

	private static final long serialVersionUID = 3544531146543959849L;
	private String compName = "顶部组件";
	private String compType = "topComp";
	private String nickName = "";
	
	private String txt_customerIndexUrl = "";
	private String txt_loginUrl = "";
	private String lbl_loginName = "请登录";
	private String txt_regUrl = "";
	private String lbl_regName = "注册";
	private String txt_myorderUrl = "";
	private String lbl_myorderName = "我的订单";
	private String txt_logoutUrl = "";
	private String lbl_logoutName = "退出";
	private String txt_indexUrl = "";
	private String lbl_indexName = "商城首页";
	private String txt_favProductUrl = "";
	private String lbl_favProductName = "收藏的商品";
	private String txt_favStoreUrl = "";
	private String lbl_favStoreName = "收藏的店铺";
	private String txt_storeUrl = "";
	private String lbl_storeName = "商家中心";
	private String txt_toApplyUrl = "";
	private String lbl_toApplyName = "商家入驻";
	private String txt_helpCenterUrl = "";
	private String lbl_helpCenterName = "帮助中心";
	private String subTopic="loginSuccess";
	
	public TopCompModel() {
		super(TopCompController.class,"/topComp/toParamsDesign");
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

	public String getTxt_customerIndexUrl() {
		return txt_customerIndexUrl;
	}

	public void setTxt_customerIndexUrl(String txt_customerIndexUrl) {
		this.txt_customerIndexUrl = txt_customerIndexUrl;
	}

	public String getTxt_loginUrl() {
		return txt_loginUrl;
	}

	public void setTxt_loginUrl(String txt_loginUrl) {
		this.txt_loginUrl = txt_loginUrl;
	}

	public String getLbl_loginName() {
		return lbl_loginName;
	}

	public void setLbl_loginName(String lbl_loginName) {
		this.lbl_loginName = lbl_loginName;
	}

	public String getTxt_regUrl() {
		return txt_regUrl;
	}

	public void setTxt_regUrl(String txt_regUrl) {
		this.txt_regUrl = txt_regUrl;
	}

	public String getLbl_regName() {
		return lbl_regName;
	}

	public void setLbl_regName(String lbl_regName) {
		this.lbl_regName = lbl_regName;
	}

	public String getTxt_myorderUrl() {
		return txt_myorderUrl;
	}

	public void setTxt_myorderUrl(String txt_myorderUrl) {
		this.txt_myorderUrl = txt_myorderUrl;
	}

	public String getLbl_myorderName() {
		return lbl_myorderName;
	}

	public void setLbl_myorderName(String lbl_myorderName) {
		this.lbl_myorderName = lbl_myorderName;
	}

	public String getTxt_logoutUrl() {
		return txt_logoutUrl;
	}

	public void setTxt_logoutUrl(String txt_logoutUrl) {
		this.txt_logoutUrl = txt_logoutUrl;
	}

	public String getLbl_logoutName() {
		return lbl_logoutName;
	}

	public void setLbl_logoutName(String lbl_logoutName) {
		this.lbl_logoutName = lbl_logoutName;
	}

	public String getTxt_indexUrl() {
		return txt_indexUrl;
	}

	public void setTxt_indexUrl(String txt_indexUrl) {
		this.txt_indexUrl = txt_indexUrl;
	}

	public String getLbl_indexName() {
		return lbl_indexName;
	}

	public void setLbl_indexName(String lbl_indexName) {
		this.lbl_indexName = lbl_indexName;
	}

	public String getTxt_favProductUrl() {
		return txt_favProductUrl;
	}

	public void setTxt_favProductUrl(String txt_favProductUrl) {
		this.txt_favProductUrl = txt_favProductUrl;
	}

	public String getLbl_favProductName() {
		return lbl_favProductName;
	}

	public void setLbl_favProductName(String lbl_favProductName) {
		this.lbl_favProductName = lbl_favProductName;
	}

	public String getTxt_favStoreUrl() {
		return txt_favStoreUrl;
	}

	public void setTxt_favStoreUrl(String txt_favStoreUrl) {
		this.txt_favStoreUrl = txt_favStoreUrl;
	}

	public String getLbl_favStoreName() {
		return lbl_favStoreName;
	}

	public void setLbl_favStoreName(String lbl_favStoreName) {
		this.lbl_favStoreName = lbl_favStoreName;
	}

	public String getTxt_storeUrl() {
		return txt_storeUrl;
	}

	public void setTxt_storeUrl(String txt_storeUrl) {
		this.txt_storeUrl = txt_storeUrl;
	}

	public String getLbl_storeName() {
		return lbl_storeName;
	}

	public void setLbl_storeName(String lbl_storeName) {
		this.lbl_storeName = lbl_storeName;
	}

	public String getTxt_toApplyUrl() {
		return txt_toApplyUrl;
	}

	public void setTxt_toApplyUrl(String txt_toApplyUrl) {
		this.txt_toApplyUrl = txt_toApplyUrl;
	}

	public String getLbl_toApplyName() {
		return lbl_toApplyName;
	}

	public void setLbl_toApplyName(String lbl_toApplyName) {
		this.lbl_toApplyName = lbl_toApplyName;
	}

	public String getTxt_helpCenterUrl() {
		return txt_helpCenterUrl;
	}

	public void setTxt_helpCenterUrl(String txt_helpCenterUrl) {
		this.txt_helpCenterUrl = txt_helpCenterUrl;
	}

	public String getLbl_helpCenterName() {
		return lbl_helpCenterName;
	}

	public void setLbl_helpCenterName(String lbl_helpCenterName) {
		this.lbl_helpCenterName = lbl_helpCenterName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSubTopic() {
		return subTopic;
	}

	public void setSubTopic(String subTopic) {
		this.subTopic = subTopic;
	}
	
}
