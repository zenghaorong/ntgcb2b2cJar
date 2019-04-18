package com.aebiz.app.dec.commons.comps.floor;
import com.aebiz.app.dec.commons.comps.floor.vo.FloorCompModel;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.view.annotation.SJson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/floorComp")
public class FloorController extends BaseCompController {

	@RequestMapping("/toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
		
		String toUrl = (String) request.getAttribute("toParamsJspURL");
		
		return toUrl;
	}

	@Override
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {

		Document doc = Jsoup.parse(pageViewHtml);

			
		return doc.html();

	}


	public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
		FloorCompModel sc = (FloorCompModel) bcm;
		
		sc.setNeedAsyncInit(false);
        String compId = sc.getCompId();
/*
        designJs = designJs.replaceAll("\\$_compId", compId);
		designJs = designJs.replaceAll(compId + "_value", compId);
		designJs = designJs.replaceAll(compId + "_showtype_value", sc.getShowtype());*/

        designJs = designJs.replaceAll("\\$_compId", compId);

		designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(sc));
        
        return designJs;
	}

	@RequestMapping("/ajaxLoadData")
	@SJson
	public String ajaxLoadData(String showtype) {
		
		NutMap map =new NutMap();

		map.put("showtype", showtype);
		
		return Json.toJson(map);
	}



}
