package com.aebiz.app.dec.commons.comps.imagetexteditor.vo;


import com.aebiz.app.dec.commons.comps.imagetexteditor.ImageTextEditorCompController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

public class ImageTextEditorCompModel extends BaseCompModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageTextEditorCompModel() {
		super(ImageTextEditorCompController.class, "/imageTextEditorComp/toParamsDesign");

	}

	/**
	 * 自定义宽度
	 */
	private int defineWidth = 768;

	/**
	 * 自定义高度
	 */

	private int defineHeight = 500;

	/**
	 * 内容
	 */
	private String content="";

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

}
