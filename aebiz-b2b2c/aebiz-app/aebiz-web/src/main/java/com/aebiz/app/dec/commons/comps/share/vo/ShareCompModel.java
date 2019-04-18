package com.aebiz.app.dec.commons.comps.share.vo;

import com.aebiz.app.dec.commons.comps.share.ShareController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

public class ShareCompModel extends BaseCompModel {



	public ShareCompModel() {
		super(ShareController.class,"/shareComp/toParamsDesign");

	}

	private String compName = "分享";
	private String compType = "share";
	
	

	/**
	 * 标签名称
	 */
	private String lableName="分享";
	
	
	/**
	 * 图标大小
	 */
	private String imgSize="";



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



	public String getImgSize() {
		return imgSize;
	}



	public void setImgSize(String imgSize) {
		this.imgSize = imgSize;
	}
	
	
	


	
	
	
	


}
