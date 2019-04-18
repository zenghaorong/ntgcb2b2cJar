package com.aebiz.app.dec.commons.comps.sideenterance.vo;


import com.aebiz.app.dec.commons.comps.sideenterance.SideEnteranceCompController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

/**
 * 侧边快速入口组件Model
 * @author hyl
 * 2016年12月19日
 */
public class SideEnteranceCompModel extends BaseCompModel {

	public SideEnteranceCompModel() {
		super(SideEnteranceCompController.class,"/sideEnteranceComp/toParamsDesign");
	}

	private static final long serialVersionUID = -8445782105920600494L;
    //组件名称
	private String compName = "侧边快速入口组件";
	//组件类型
	private String compType = "sideEnteranceComp";
	//登录是否显示
	private String loginIsShow = "true";
	//购物车是否显示
	private String cartIsShow = "true";
	//收藏是否显示
	private String collectionIsShow = "true";
	//调查问卷是否显示
	private String quesNaireIsShow = "true";
	//调查问卷地址
	private String quesNaireUrl = "";
	//在线咨询是否显示
	private String onlineIsShow = "true";
	//QQ咨询是否显示
	private String qqIsShow = "true";
	//注册名称
	private String a_regName = "免费注册";
	//注册url
	private String a_regUrl = "";
	//忘记密码名称
	private String a_forgetPwdName = "忘记密码";
	//忘记密码Url
	private String a_forgetPwdUrl = "";
	//会员中心url
	private String customerIndexUrl = "";
	//qq列表
	private String qqListStr = "";
	//购物车交互主题
	private String cartTopic = "";
	
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
	public String getLoginIsShow() {
		return loginIsShow;
	}
	public void setLoginIsShow(String loginIsShow) {
		this.loginIsShow = loginIsShow;
	}
	public String getCartIsShow() {
		return cartIsShow;
	}
	public void setCartIsShow(String cartIsShow) {
		this.cartIsShow = cartIsShow;
	}
	public String getCollectionIsShow() {
		return collectionIsShow;
	}
	public void setCollectionIsShow(String collectionIsShow) {
		this.collectionIsShow = collectionIsShow;
	}
	public String getQuesNaireIsShow() {
		return quesNaireIsShow;
	}
	public void setQuesNaireIsShow(String quesNaireIsShow) {
		this.quesNaireIsShow = quesNaireIsShow;
	}
	public String getQuesNaireUrl() {
		return quesNaireUrl;
	}
	public void setQuesNaireUrl(String quesNaireUrl) {
		this.quesNaireUrl = quesNaireUrl;
	}
	public String getQqIsShow() {
		return qqIsShow;
	}
	public void setQqIsShow(String qqIsShow) {
		this.qqIsShow = qqIsShow;
	}
	public String getOnlineIsShow() {
		return onlineIsShow;
	}
	public void setOnlineIsShow(String onlineIsShow) {
		this.onlineIsShow = onlineIsShow;
	}
	public String getA_regName() {
		return a_regName;
	}
	public void setA_regName(String a_regName) {
		this.a_regName = a_regName;
	}
	public String getA_regUrl() {
		return a_regUrl;
	}
	public void setA_regUrl(String a_regUrl) {
		this.a_regUrl = a_regUrl;
	}
	public String getA_forgetPwdName() {
		return a_forgetPwdName;
	}
	public void setA_forgetPwdName(String a_forgetPwdName) {
		this.a_forgetPwdName = a_forgetPwdName;
	}
	public String getA_forgetPwdUrl() {
		return a_forgetPwdUrl;
	}
	public void setA_forgetPwdUrl(String a_forgetPwdUrl) {
		this.a_forgetPwdUrl = a_forgetPwdUrl;
	}
	public String getCustomerIndexUrl() {
		return customerIndexUrl;
	}
	public void setCustomerIndexUrl(String customerIndexUrl) {
		this.customerIndexUrl = customerIndexUrl;
	}
	public String getQqListStr() {
		return qqListStr;
	}
	public void setQqListStr(String qqListStr) {
		this.qqListStr = qqListStr;
	}
	public String getCartTopic() {
		return cartTopic;
	}
	public void setCartTopic(String cartTopic) {
		this.cartTopic = cartTopic;
	}
	
}
