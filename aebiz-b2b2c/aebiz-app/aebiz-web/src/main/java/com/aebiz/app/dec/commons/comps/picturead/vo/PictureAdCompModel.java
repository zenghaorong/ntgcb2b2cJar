package com.aebiz.app.dec.commons.comps.picturead.vo;


import com.aebiz.app.dec.commons.comps.picturead.PictureAdController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

public class PictureAdCompModel extends BaseCompModel {

	
	private static final long serialVersionUID = 1L;



	public PictureAdCompModel() {
		super(PictureAdController.class,"/pictureAdComp/toParamsDesign");

	}
	
	/**
	 * 图片地址
	 */
	
	private String imgSrc="";
	
	
	/**
	 * 自定义宽度
	 */
	private int defineWidth=0;
	
	
	/**
	 * 自定义高度
	 */
	
	private int defineHeight=0;
	
	
	
	
	/**
	 * 自定义链接地址 
	 */
	private String defineUrl="";
	
	
	
	
	
	public int getDefineWidth() {
		return defineWidth;
	}



	public void setDefineWidth(int defineWidth) {
		this.defineWidth = defineWidth;
	}



	public int getDefineHeight() {
		return defineHeight;
	}



	public void setDefineHeight(int defineHeight) {
		this.defineHeight = defineHeight;
	}



	public String getDefineUrl() {
		return defineUrl;
	}



	public void setDefineUrl(String defineUrl) {
		this.defineUrl = defineUrl;
	}



	public String getImgSrc() {
		return imgSrc;
	}



	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	
	
	
	
	
	
	
	
	
}
