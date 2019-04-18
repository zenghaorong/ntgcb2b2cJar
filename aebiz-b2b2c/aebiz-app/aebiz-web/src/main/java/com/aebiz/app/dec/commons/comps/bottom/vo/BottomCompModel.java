package com.aebiz.app.dec.commons.comps.bottom.vo;


import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.comps.bottom.BottomCompController;

/**
 * 底部组件Model
 * @author hyl
 * 2016年12月19日
 */
public class BottomCompModel extends BaseCompModel {

	
	private static final long serialVersionUID = -7630291852253848248L;
	//组件名称
	private String compName = "底部组件";
	//组件类型
	private String compType = "bottomComp";
	
	public BottomCompModel() {
		super(BottomCompController.class,"/bottomComp/toParamsDesign");
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
	
}
