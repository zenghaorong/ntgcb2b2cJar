package com.aebiz.app.dec.commons.comps.bottom;

import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.cms.modules.services.CmsSiteService;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.view.annotation.SJson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 底部组件
 * @author hyl
 * 2016年12月14日
 */
@Controller
@RequestMapping("/bottomComp")
public class BottomCompController extends BaseCompController {
	@Autowired
	public CmsSiteService cmsSiteService;

	private static final Log log = Logs.get();
	public String executeCompViewHtml(String pageViewHtml,WebPageModel wpm,BaseCompModel bcm){
		Document doc = Jsoup.parse(pageViewHtml);
		super.delCompLoading(doc, bcm);
		//调用接口获取底部数据
		String bottomInfo = getBottomInfo();
		//初始化底部页面
		doc.select("#"+bcm.getCompId()+"_content").html(bottomInfo);
		return doc.html();
	}

	/*
	 * ajax调用初始化页面
	 * @param response
	 */
	@RequestMapping("/ajaxLoadData")
	@SJson
	public String ajaxLoadData(){
		return getBottomInfo();
	}

	/**
	 * 替换js
	 */
	public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm){
		designJs = designJs.replaceAll("\\$_compId",bcm.getCompId());
		String js = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(bcm));
		return js;
	}

	/**
	 * 初始化属性编辑页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request){
		String toUrl = Strings.sNull(request.getAttribute("toParamsJspURL")) ;
		return toUrl;
	}

	/*
	 *
	 * 调用接口返回底部信息
	 * @return
	 *
	*/
	private String getBottomInfo(){
		try {
			return cmsSiteService.fetch(Cnd.where("id","=","site")).getFooter_content();
		} catch (Exception e) {
			log.error(e);
			return "";
		}
	}

}


