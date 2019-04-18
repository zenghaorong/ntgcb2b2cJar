package com.aebiz.app.dec.commons.comps.qrcode;


import com.aebiz.app.dec.commons.comps.qrcode.vo.QRCodeCompModel;
import com.aebiz.app.dec.commons.comps.qrcode.vo.QRCodeModel;
import com.aebiz.app.dec.commons.service.PlatInfoForCompService;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo.PlatImageCategoryDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo.PlatImageLibDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.page.Pagination;
import org.apache.commons.lang3.StringUtils;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/qrCodeComp")
public class QRCodeController extends BaseCompController {

	
	@Autowired
	private PlatInfoForCompService platInfoForCompService;

	@RequestMapping("/toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
		String toUrl = (String) request.getAttribute("toParamsJspURL");
				
		List<PlatImageCategoryDTO> list = platInfoForCompService.getImageLibCategoryById();
		

		request.setAttribute("list", list);

		return toUrl;

	}

	@RequestMapping("/getPageData")
	@ResponseBody
	public String getPageData(HttpServletRequest request) {

		String nowPage = request.getParameter("nowPage");
		String pageShow = request.getParameter("pageShow");
		String categoryUuid = request.getParameter("categoryUuid");

		// 根据分类获取图片 默认是第一个分类 不为空
		QRCodeModel scp = this.getImgeData(nowPage, pageShow, categoryUuid);

		String jsonString = Json.toJson(scp);

		return jsonString;
	}

	private QRCodeModel getImgeData(String nowPage, String pageShow, String categoryUuid) {

		QRCodeModel parent = new QRCodeModel();

		List<QRCodeModel> list = new ArrayList<>();

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
		
		QRCodeModel child = new QRCodeModel();
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
		QRCodeCompModel sc = (QRCodeCompModel) bcm;
		String compId = sc.getCompId();
		designJs = designJs.replaceAll("\\$_compId", compId);

		designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(sc));
		

		return designJs;
	}

	@RequestMapping("/ajaxLoadData")
	@ResponseBody
	public String ajaxLoadData(String compId, String qrtype, String imgSrc, String defineUrl, int defineWidth,
			int defineHeight) {

		QRCodeCompModel scm = getData(qrtype, imgSrc, defineUrl, defineWidth, defineHeight);

		String jsonString = Json.toJson(scm);

		return jsonString;
	}

	public QRCodeCompModel getData(String imgSrc, String qrtype, String defineUrl, int defineWidth, int defineHeight) {

		// 得到类型

		QRCodeCompModel comp = new QRCodeCompModel();

		comp.setDefineUrl(defineUrl);

		comp.setDefineWidth(defineWidth);

		comp.setDefineHeight(defineHeight);

		comp.setImgSrc(imgSrc);

		return comp;
	}
}
