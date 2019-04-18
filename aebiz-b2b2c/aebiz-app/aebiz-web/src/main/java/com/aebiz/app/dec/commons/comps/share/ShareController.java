package com.aebiz.app.dec.commons.comps.share;

import com.aebiz.app.dec.commons.comps.share.vo.ShareCompModel;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.json.Json;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/shareComp")
public class ShareController extends BaseCompController {
	@Override
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {
		Document doc = Jsoup.parse(pageViewHtml);
		this.delCompLoading(doc, bcm);
		Element compEle = doc.select("#"+bcm.getCompId()).first();
		ShareCompModel scm = (ShareCompModel)bcm;
		
		//获取数据
		String imgSize = scm.getImgSize();
		
		String lableName = scm.getLableName();
		
		//标签设置
		if(StringUtils.isEmpty(lableName)){
			compEle.select("#"+bcm.getCompId()+"_share_name").first().text("分享：");
		}
		else {
			compEle.select("#"+bcm.getCompId()+"_share_name").first().text(lableName);
		}
		//图标大小
		compEle.select("#"+bcm.getCompId()+"_share_detail").attr("imgSize",imgSize);
		return doc.html();
			}
	@RequestMapping("/toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
	
		String toUrl = (String) request.getAttribute("toParamsJspURL");
		
		return toUrl;
		
	}
	@Override
	public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
		ShareCompModel sc = (ShareCompModel) bcm;
        String compId = sc.getCompId();
        
      /*  boolean  needAsyncInit = sc.isNeedAsyncInit();
        
        String labelName = sc.getLableName();
        
        String imgSize = sc.getImgSize();        
        
        designJs=  designJs.replaceAll("\\$_compId",compId);
        designJs=  designJs.replaceAll(compId+"_value",compId);
        designJs=  designJs.replaceAll(compId+"_imgSize_value",imgSize+"");
        designJs=  designJs.replaceAll(compId+"_needAsync_value",needAsyncInit+"");
        designJs=  designJs.replaceAll(compId+"_lableName_value",labelName+"");*/
  
        
        
        designJs = designJs.replaceAll("\\$_compId", compId);

		designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(sc));
        return designJs;
	}

	@RequestMapping("/ajaxLoadData")
	@SJson
	public String ajaxLoadData(String compId,String imgSize,String lableName) {
		ShareCompModel data = getData(compId,imgSize,lableName);
		String jsonString = Json.toJson(data);
		return jsonString;
	}
	
	/**
	 * 获取分享数据
	 * @param compId
	 * @param imgSize
	 * @param lableName
	 * @return
	 */
	public ShareCompModel getData(String compId,String imgSize,String lableName){
		ShareCompModel comp =new ShareCompModel();
		comp.setImgSize(imgSize);
		comp.setLableName(lableName);
		return  comp;
		 
	}
}
