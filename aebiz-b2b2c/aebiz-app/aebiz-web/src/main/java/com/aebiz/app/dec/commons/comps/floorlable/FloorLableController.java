package com.aebiz.app.dec.commons.comps.floorlable;

import com.aebiz.app.dec.commons.comps.floorlable.vo.FloorLableCompModel;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.view.annotation.SJson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.json.Json;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/floorLableComp")
public class FloorLableController extends BaseCompController {


	@Override
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {

		Document doc = Jsoup.parse(pageViewHtml);
		
		this.delCompLoading(doc, bcm);


		Element compEle = doc.select("#" + bcm.getCompId()).first();
		
		FloorLableCompModel scm = (FloorLableCompModel) bcm;
		Elements select = compEle.select("#"+scm.getCompId()+"_lablename");
		
		
				
		select.first().text(scm.getLableName());
		
		compEle.select("#"+scm.getCompId()+"_span").first().text(scm.getFloorName());
		return doc.html();

	}

	
	@RequestMapping("/toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
		
		String toUrl = (String) request.getAttribute("toParamsJspURL");
		
		return toUrl;
	}

	public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
		FloorLableCompModel sc = (FloorLableCompModel) bcm;
	        String compId = sc.getCompId();
	   /*     boolean  needAsyncInit = sc.isNeedAsyncInit();

	        designJs = designJs.replaceAll("\\$_compId", compId);
			designJs = designJs.replaceAll(compId + "_value", compId);
			designJs = designJs.replaceAll(compId + "_lableName_value", sc.getLableName());
			designJs = designJs.replaceAll(compId + "_needAsync_value", needAsyncInit + "");*/

	    	
			designJs = designJs.replaceAll("\\$_compId", compId);

			designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(sc));
	        
	        return designJs;
	}

	@RequestMapping("/ajaxLoadData")
	@SJson
	public String ajaxLoadData(String compId,String floorName,String lableName) {

		FloorLableCompModel scm = getData(floorName,lableName);

		String jsonString = Json.toJson(scm);

		return jsonString;
	}

	public  FloorLableCompModel getData(String floorName,String lableName) {

		FloorLableCompModel compModel =new FloorLableCompModel();
		
		compModel.setFloorName(floorName);
		
		compModel.setLableName(lableName);
		
		compModel.setNeedAsyncInit(false);
		
		return compModel;
	}

}
