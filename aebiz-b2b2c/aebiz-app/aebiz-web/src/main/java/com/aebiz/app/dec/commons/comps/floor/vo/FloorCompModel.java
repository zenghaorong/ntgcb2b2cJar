package com.aebiz.app.dec.commons.comps.floor.vo;

import com.aebiz.app.dec.commons.comps.floor.FloorController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

public class FloorCompModel extends BaseCompModel {
	private static final long serialVersionUID = 1L;

	public FloorCompModel() {
		super(FloorController.class,"/floorComp/toParamsDesign");

	}

	private String compName = "楼层";
	private String compType = "floor";
	
	

	/**
	 * 标签名称
	 */
	private String lableName="";
	
	
	/**
	 * 楼层名称
	 */
	private String floorName="";
	
	
	/**
	 * 是否显示
	 */
	private boolean showtype=true;
	
	


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



	public String getFloorName() {
		return floorName;
	}



	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}



	public boolean isShowtype() {
		return showtype;
	}



	public void setShowtype(boolean showtype) {
		this.showtype = showtype;
	}




	
	
	
	

}
