package com.aebiz.app.dec.commons.comps.picturead;

import com.aebiz.app.dec.commons.comps.picturead.vo.PictureAdCompModel;
import com.aebiz.app.dec.commons.comps.picturead.vo.PictureAdModel;
import com.aebiz.app.dec.commons.service.PlatInfoForCompService;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo.PlatImageCategoryDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo.PlatImageLibDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.page.Pagination;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/pictureAdComp")
public class PictureAdController extends BaseCompController {

	@Autowired
	private PlatInfoForCompService platInfoForCompService;

	@Override
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {

		Document doc = Jsoup.parse(pageViewHtml);
		this.delCompLoading(doc, bcm);

		Element compEle = doc.select("#" + bcm.getCompId()).first();

		PictureAdCompModel scm = (PictureAdCompModel) bcm;

		compEle.select("#" + bcm.getCompId() + "_img").attr("src", scm.getImgSrc() + "");
		compEle.select("#" + bcm.getCompId() + "_img").attr("width", scm.getDefineWidth() + "");
		compEle.select("#" + bcm.getCompId() + "_img").attr("height", scm.getDefineHeight() + "");
		return doc.html();

	}

	@RequestMapping("/toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
		String toUrl = (String) request.getAttribute("toParamsJspURL");

		List<PlatImageCategoryDTO> list = platInfoForCompService.getImageLibCategoryById();
		

		request.setAttribute("list", list);
		return toUrl;

	}

	/**
	 * 获取图片的分页数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getPageData")
	@SJson
	public String getPageData(HttpServletRequest request) {

		String nowPage = request.getParameter("nowPage");
		String pageShow = request.getParameter("pageShow");
		String categoryUuid = request.getParameter("categoryUuid");
		// 根据分类获取图片 默认是第一个分类 不为空
		PictureAdModel scp = this.getImgeData(nowPage, pageShow, categoryUuid);

		String jsonString = Json.toJson(scp);

		return jsonString;
	}

	/**
	 * 根据分类获取图片数据
	 * 
	 * @param nowPage
	 * @param pageShow
	 * @param categoryUuid
	 * @return
	 */
	private PictureAdModel getImgeData(String nowPage, String pageShow, String categoryUuid) {

		PictureAdModel parent = new PictureAdModel();

		List<PictureAdModel> list = new ArrayList<>();

		if (StringUtils.isEmpty(nowPage)) {
			nowPage = "1";
		}

		if (StringUtils.isEmpty(pageShow)) {
			pageShow = "5";
		}



		Pagination imageLigByCateId = platInfoForCompService.getImageLigByCateId(categoryUuid, Integer.valueOf(nowPage), Integer.valueOf(pageShow));
		
				
	
	parent.setTotalNum(imageLigByCateId.getTotalCount());
	
	
	
	List<PlatImageLibDTO> platImageLibDTOs = (List<PlatImageLibDTO>)imageLigByCateId.getList();
	
	if(platImageLibDTOs!=null&&platImageLibDTOs.size()>0){
		
	
	for(PlatImageLibDTO image:platImageLibDTOs){
		
		PictureAdModel child = new PictureAdModel();
		child.setImgSrc(image.getImagePath());
		
		
		child.setImgUuid(image.getImgUuid());
		
		child.setImgName(image.getImageName());
		
		list.add(child);
		
	}
	
	}
		
	parent.setRows(list);

		return parent;
	}

	public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
		PictureAdCompModel sc = (PictureAdCompModel) bcm;
		String compId = sc.getCompId();

		designJs = designJs.replaceAll("\\$_compId", compId);

		designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(sc));

		return designJs;
	}

	@RequestMapping("/ajaxLoadData")
	@SJson
	public String ajaxLoadData(String compId, String imgSrc, String defineUrl, int defineWidth, int defineHeight) {

		PictureAdCompModel scm = getData(imgSrc, defineUrl, defineWidth, defineHeight);

		String jsonString = Json.toJson(scm);
		return jsonString;
	}

	public PictureAdCompModel getData(String imgSrc, String defineUrl, int defineWidth, int defineHeight) {

		// 得到类型

		PictureAdCompModel comp = new PictureAdCompModel();

		comp.setDefineUrl(defineUrl);

		comp.setDefineWidth(defineWidth);

		comp.setDefineHeight(defineHeight);

		comp.setImgSrc(imgSrc);

		return comp;
	}
}
