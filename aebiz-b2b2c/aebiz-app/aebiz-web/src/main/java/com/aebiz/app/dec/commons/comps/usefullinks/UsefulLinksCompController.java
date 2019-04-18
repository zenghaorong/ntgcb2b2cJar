package com.aebiz.app.dec.commons.comps.usefullinks;


import com.aebiz.app.dec.commons.service.PlatInfoForCompService;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo.PartnerDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 友情链接组件
 * @author hyl
 * 2016年12月22日
 */
@Controller
@RequestMapping("/usefulLinksComp")
public class UsefulLinksCompController extends BaseCompController {

	@Autowired
	private PlatInfoForCompService platInfoForCompService;
	private static final Log log = Logs.get();
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm){
		Document doc = Jsoup.parse(pageViewHtml);
		super.delCompLoading(doc, bcm);
		String compId = bcm.getCompId();
		//调用接口返回友情链接数据
		List<PartnerDTO> linkList = platInfoForCompService.getPartnerSites();
		Element element = doc.select("#"+compId+"_usefulLinks").first();
		//生成友情链接html内容
		initPageViewHtml(element,compId,linkList);
		return doc.html();
	}
	
	/**
	 * 跳转到参数设计页面
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request){
		String toUrl = (String) request.getAttribute("toParamsJspURL");
		return toUrl;
	}
	
	/**
	 * 替换js
	 * @param designJs
	 * @param wpm
	 * @param bcm
	 * @return
	 */
	public String genJs(String designJs,WebPageModel wpm,BaseCompModel bcm){
		designJs = designJs.replaceAll("\\$_compId",bcm.getCompId());
		String js = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(bcm));
		return js;
	}
	
	/**
	 * 异步初始化页面数据
	 * @return
	 */
	@RequestMapping(value="ajaxLoadData")
	public void ajaxLoadData(HttpServletResponse response){
		//调用接口返回友情链接数据
		List<PartnerDTO> linkList = platInfoForCompService.getPartnerSites();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		try {
			response.getWriter().print(Json.toJson(linkList));
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	
	/**
	 * 生成友情链接html内容
	 * @param element
	 * @param compId
	 * @param linkList
	 */
	private void initPageViewHtml(Element element, String compId, List<PartnerDTO> linkList){
		if(element != null){
			element.empty();
			for(PartnerDTO m : linkList){
				element.appendElement("a").attr("target", m.getSiteTarget()).attr("href", m.getSiteLinks()).html(m.getSiteName());
			}
		}
	}
	
}
