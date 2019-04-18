package com.aebiz.app.dec.commons.comps.poplogin.vo;


import com.aebiz.app.dec.commons.comps.poplogin.PopLoginCompController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

/**
 * 弹出登录框组件Model
 * @author hyl
 * 2017年1月5日
 */
public class PopLoginCompModel extends BaseCompModel {
	private static final long serialVersionUID = -4561426076077166322L;
	//组件名称
	private String compName = "弹出登录框组件";
	//组件类型
	private String compType = "popLoginComp";
	//注册名称
	private String a_regName = "注册";
	//注册Url
	private String a_regUrl = "";
	//忘记密码名称
	private String a_forgetPwdName = "忘记密码";
	//忘记密码Url
	private String a_forgetPwdUrl = "";
	//订阅主题
	private String subTopic = "";
	//发布主题
	private String pubTopic="loginSuccess";
	
	public PopLoginCompModel() {
		super(PopLoginCompController.class, "/popLoginComp/toParamsDesign");
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


	public String getSubTopic() {
		return subTopic;
	}

	public void setSubTopic(String subTopic) {
		this.subTopic = subTopic;
	}

	public String getPubTopic() {
		return pubTopic;
	}

	public void setPubTopic(String pubTopic) {
		this.pubTopic = pubTopic;
	}
	
}
