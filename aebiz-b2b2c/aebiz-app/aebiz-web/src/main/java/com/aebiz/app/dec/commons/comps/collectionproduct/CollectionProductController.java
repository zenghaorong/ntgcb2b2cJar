package com.aebiz.app.dec.commons.comps.collectionproduct;


import com.aebiz.app.dec.commons.comps.collectionproduct.vo.CollectionProductCompModel;
import com.aebiz.app.dec.commons.comps.collectionproduct.vo.CollectionProductModel;
import com.aebiz.app.dec.commons.service.PlatInfoForCompService;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.goods.modules.services.GoodsFavoriteService;
import com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo.PlatImageCategoryDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo.PlatImageLibDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.page.Pagination;
import com.aebiz.commons.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/collectionProductComp")
public class CollectionProductController extends BaseCompController {

	@Autowired
	private PlatInfoForCompService platInfoForCompService;
	@Autowired
	private ProductForCompService productForCompService;
	@Autowired
	private GoodsFavoriteService goodsFavoriteService;
	
	private static final String productUuid="";

	@Override
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {

		Document doc = Jsoup.parse(pageViewHtml);

		this.delCompLoading(doc, bcm);

		Element compEle = doc.select("#" + bcm.getCompId()).first();

		CollectionProductCompModel scm = (CollectionProductCompModel) bcm;

		// 收藏图标设置
		compEle.select("#" + bcm.getCompId() + "_collectionproduct_img").attr("src", scm.getImgSrc());
		// 收藏标签设置
		compEle.select("#" + bcm.getCompId() + "_collectionproduct_span").first().text(scm.getLableName());

		return doc.html();

	}

	@RequestMapping("/toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
		String toUrl = (String) request.getAttribute("toParamsJspURL");
		
		
		
		String pageJson = request.getParameter("pageJson");
		
		Object obj = Json.fromJson(pageJson);
		Map parseObject=(Map)obj;
		request.setAttribute("imgUuid",parseObject.get("imgUuid"));

		List<PlatImageCategoryDTO> list = platInfoForCompService.getImageLibCategoryById();
		

		request.setAttribute("list", list);

		return toUrl;

	}
	

	/**
	 * 属性页面获取数据
	 * 
	 * @param request
	 * @return
	 */

	@RequestMapping(value = "/getPageData", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getPageData(HttpServletRequest request) {

		String nowPage = request.getParameter("nowPage");
		String pageShow = request.getParameter("pageShow");
		String categoryUuid = request.getParameter("categoryUuid");
		// 根据分类获取图片 默认是第一个分类 不为空
		CollectionProductModel scp = this.getImgeData(nowPage, pageShow, categoryUuid);

		String jsonString = Json.toJson(scp);

		return jsonString;
	}

	/**
	 * 根据分类获取图片
	 * 
	 * @param nowPage
	 * @param pageShow
	 * @param categoryUuid
	 * @return
	 */

	private CollectionProductModel getImgeData(String nowPage, String pageShow, String categoryUuid) {

		CollectionProductModel parent = new CollectionProductModel();

		List<CollectionProductModel> list = new ArrayList<>();

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
		
		CollectionProductModel child = new CollectionProductModel();
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
		CollectionProductCompModel sc = (CollectionProductCompModel) bcm;
		String compId = sc.getCompId();
	
		designJs = designJs.replaceAll("\\$_compId", compId);

		designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(sc));
		
		
		
		
		
		
		return designJs;
	}

	@RequestMapping(value = "/ajaxLoadData", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String ajaxLoadData(String compId, String lableName, String imgSrc) {

		CollectionProductCompModel data = getData(compId, lableName, imgSrc);

		String jsonString = Json.toJson(data);

		return jsonString;
	}

	/**
	 * 获取数据
	 * 
	 * @param compId
	 * @param lableName
	 * @param imgSrc
	 * @return
	 */
	public CollectionProductCompModel getData(String compId, String lableName, String imgSrc) {

		CollectionProductCompModel comp = new CollectionProductCompModel();

		comp.setLableName(lableName);

		comp.setImgSrc(imgSrc);

		return comp;

	}

	/**
	 * 商品收藏
	 * 
	 * @param sku
	 * @return
	 */

	@RequestMapping(value = "/collectProduct", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String collectProduct(@RequestParam("sku") String sku) {
		String accountId= StringUtil.getMemberUid();
		Map json =new HashMap<>();
		
		if (StringUtils.isEmpty(sku)) {
			//TODO 
			//从接口中获取
			sku = "";
		}

		String addProductFavorite = productForCompService.addProductFavorite(sku);
		
		json.put("state", addProductFavorite);
		
		
		
		int count = productForCompService.getProductFavorite();
		json.put("count", count);




		return Json.toJson(json);
		

	}

}
