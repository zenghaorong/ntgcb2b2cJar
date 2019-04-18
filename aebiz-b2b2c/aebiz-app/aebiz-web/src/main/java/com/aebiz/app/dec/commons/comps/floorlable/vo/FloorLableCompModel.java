package com.aebiz.app.dec.commons.comps.floorlable.vo;


import com.aebiz.app.dec.commons.comps.floorlable.FloorLableController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

public class FloorLableCompModel extends BaseCompModel {






	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public FloorLableCompModel() {
		super(FloorLableController.class,"/floorLableComp/toParamsDesign");

	}

	private String compName = "楼层标签";
	private String compType = "floorlable";
	
	

	/**
	 * 标签名称
	 */
	private String lableName="";
	
	
	/**
	 * 楼层名称
	 */
	private String floorName="";



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






	
	
	


	
	
	
	


}
