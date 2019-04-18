package com.aebiz.app.dec.commons.comps.qrcode.vo;


import com.aebiz.app.dec.commons.comps.qrcode.QRCodeController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

public class QRCodeCompModel extends BaseCompModel {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public QRCodeCompModel() {
		super(QRCodeController.class,"/qrCodeComp/toParamsDesign");

	}

	private String compName = "二维码";
	private String compType = "twocode";
	
	

	

	/**
	 * 当选择自定义二维码下方文字
	 */
	private String explain="";
	
	
	
	/**
	 * 当选择自定义二维码logo
	 */
	
	private String imgSrc="";
	
	
	
	/**
	 * 当选择自定义二维码宽度
	 */
	private int defineWidth=0;
	
	
	/**
	 * 当选择自定义二维码高度
	 */
	
	private int defineHeight=0;
	
	
	
	
	/**
	 * 当选择自定义二维码链接
	 */
	private String defineUrl="";
	
	
	
	/**
	 * 二维码类型
	 */
	private String qrtype="";
	
	
	/**
	 * 二维码商品链接地址
	 */
	private String productUrl="";
	
	
	/**
	 * 二维码店铺链接地址
	 */
	private String shopUrl="";
	

	public String getQrtype() {
		return qrtype;
	}




	public void setQrtype(String qrtype) {
		this.qrtype = qrtype;
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




	public String getExplain() {
		return explain;
	}




	public void setExplain(String explain) {
		this.explain = explain;
	}








	public String getImgSrc() {
		return imgSrc;
	}




	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}




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


	public String getProductUrl() {
		return productUrl;
	}



	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}



	public String getShopUrl() {
		return shopUrl;
	}



	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}


	
	
	
	
	

	
	
	
	


	


}
