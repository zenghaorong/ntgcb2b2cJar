package com.aebiz.app.web.modules.controllers.platform.dec;


import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import org.jsoup.nodes.Document;
import org.nutz.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseCompController {
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm){
		return "";
	}
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request){
		return "";
	}
	public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
		String js = designJs.replaceAll("\\$_compId",bcm.getCompId());
		js = js.replaceAll("\\$_compConfig_\\$", Json.toJson(bcm));
		return js;
	}
	public String ajaxLoadData(String compId){
		return "";
	}
	
	/**
	 * 删除文档中遮罩层
	 * @param doc
	 * @param bcm
	 */
	public void delCompLoading(Document doc , BaseCompModel bcm){
		String compId = bcm.getCompId();
		doc.select("#"+compId+"_comsloading").remove();
	}
}
