package com.aebiz.app.dec.commons.comps.imagetexteditor;

import com.aebiz.app.dec.commons.comps.imagetexteditor.vo.ImageTextEditorCompModel;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Map;

@Controller
@RequestMapping("/imageTextEditorComp")
public class ImageTextEditorCompController extends BaseCompController {
	@Override
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {

		Document doc = Jsoup.parse(pageViewHtml);
		this.delCompLoading(doc, bcm);


		Element compEle = doc.select("#" + bcm.getCompId()).first();

		ImageTextEditorCompModel scm = (ImageTextEditorCompModel) bcm;

		compEle.select("#" + bcm.getCompId() + "_content").attr("width", scm.getDefineWidth() + "");
		compEle.select("#" + bcm.getCompId() + "_content").attr("height", scm.getDefineHeight() + "");

		String content = scm.getContent();

		if (StringUtils.isNoneBlank(content)) {

			try {
				content = URLDecoder.decode(content, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		compEle.select("#" + bcm.getCompId() + "_content").html(content + "");

		return doc.html();

	}

	@RequestMapping("/toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
		String toUrl = (String) request.getAttribute("toParamsJspURL");
		String pageJson = request.getParameter("pageJson");
		Map parseObject=(Map)Json.fromJson(pageJson);
		String str ="";
		if(parseObject.containsKey("content")){
			str =(String)parseObject.get("content");
			if (!Strings.isBlank(str)){
				try {
					str = URLDecoder.decode(str, "utf-8");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		request.setAttribute("content", str);
		return toUrl;

	}

	public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
		ImageTextEditorCompModel sc = (ImageTextEditorCompModel) bcm;
		String compId = sc.getCompId();
		designJs = designJs.replaceAll("\\$_compId", compId);

		designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(sc));

		return designJs;
	}

	@RequestMapping("/ajaxLoadData")
	@SJson
	public String ajaxLoadData(String defineUrl, int defineWidth, int defineHeight) {

		ImageTextEditorCompModel scm = getData(defineWidth, defineHeight);

		String jsonString = Json.toJson(scm);

		return "";
	}

	public ImageTextEditorCompModel getData(int defineWidth, int defineHeight) {

		ImageTextEditorCompModel comp = new ImageTextEditorCompModel();

		comp.setDefineWidth(defineWidth);

		comp.setDefineHeight(defineHeight);

		return comp;
	}

}
