package com.aebiz.app.dec.commons.comps.usefullinks.vo;


import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.comps.usefullinks.UsefulLinksCompController;

/**
 * 友情链接Model
 * @author hyl
 * 2017年1月11日
 */
public class UsefulLinksCompModel extends BaseCompModel {

	public UsefulLinksCompModel() {
		super(UsefulLinksCompController.class, "/usefulLinksComp/toParamsDesign");
	}

	private static final long serialVersionUID = -8272873530799558683L;

	//组件名称
	private String compName = "友情链接组件";
	//组件类型
	private String compType = "usefulLinksComp";
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
	
}
