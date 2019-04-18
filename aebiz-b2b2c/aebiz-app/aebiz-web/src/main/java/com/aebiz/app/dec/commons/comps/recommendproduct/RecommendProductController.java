package com.aebiz.app.dec.commons.comps.recommendproduct;


import com.aebiz.app.dec.commons.comps.recommendproduct.service.RecommendProductService;
import com.aebiz.app.dec.commons.comps.recommendproduct.vo.RecommendProductCompModel;
import com.aebiz.app.dec.commons.comps.recommendproduct.vo.RecommendProductModel;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductCategoryDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.MoneyUtil;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/recommendProductComp")
public class RecommendProductController extends BaseCompController {

	@Autowired
	private ProductForCompService productForCompService;

	@Autowired
	private RecommendProductService recommendProductService;

	@RequestMapping("/toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
		String toUrl = (String) request.getAttribute("toParamsJspURL");
		
		String pageJson = request.getParameter("pageJson");
		Map parseObject = (Map)Json.fromJson(pageJson);
		String nowPage = request.getParameter("nowPage");
		String pageShow = request.getParameter("pageShow");
		String searchKey = request.getParameter("searchKey");
		String categoryUuid = request.getParameter("categoryUuid");
		String startPrice = request.getParameter("startPrice");
		String endPrice = request.getParameter("endPrice");
		String param_inProductUuids = request.getParameter("param_inProductUuids");


		// 获取分类
		//Map<String, Object> firstLevelCategory = productForCompService.getFirstLevelCategory();
		
		List<ProductCategoryDTO> firstLevelCategory = productForCompService.getFirstLevelCategory();

	

			request.setAttribute("prodList", firstLevelCategory);
	

		request.setAttribute("nowPage", nowPage);
		request.setAttribute("pageShow", pageShow);
		request.setAttribute("searchKey", searchKey);
		request.setAttribute("categoryUuid", categoryUuid);
		request.setAttribute("startPrice", startPrice);
		request.setAttribute("endPrice", endPrice);
		request.setAttribute("param_inProductUuids", parseObject.get("param_inProductUuids"));

		return toUrl;

	}
	
	/**
	 * 属性设置页面获取分页数据
	 * @param request
	 * @return
	 */

	@RequestMapping("/getPageData")
	@SJson
	public String getPageData(HttpServletRequest request) {
		
		
													

		String nowPage = request.getParameter("nowPage");
		String pageShow = request.getParameter("pageShow");
		String searchKey = request.getParameter("searchKey");
		String categoryUuid = request.getParameter("categoryUuid");
		String startPrice = request.getParameter("startPrice");
		String endPrice = request.getParameter("endPrice");
		String param_inProductUuids = request.getParameter("param_inProductUuids");

		// 获取所有商品列表
		RecommendProductModel scm = recommendProductService.getSearchData(nowPage, pageShow, searchKey,
				categoryUuid, startPrice, endPrice);
		
		scm.setParam_inProductUuids(param_inProductUuids);

		String jsonString = Json.toJson(scm);

		return jsonString;
	}

	@Override
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {

		Document doc = Jsoup.parse(pageViewHtml);
		
		
		
		
		Element compEle = doc.select("#" + bcm.getCompId()).first();
		
		
		
		this.delCompLoading(doc, bcm);
		

		RecommendProductCompModel scm = (RecommendProductCompModel) bcm;

		// 根据属性获取数据
		String param_inProductUuids = scm.getParam_inProductUuids();

		String labelName = scm.getLableName();

		RecommendProductModel recommendProductData = recommendProductService
				.getRecommendProductData(scm.getCompId(), labelName, param_inProductUuids);

		scm.setLableName(recommendProductData.getLableName());

		compEle.select("#" + bcm.getCompId()).first();

		if (StringUtils.isEmpty(scm.getLableName())) {

			compEle.select(".j_recommendproduct_lable").first().text("推荐商品");
		} else {

			compEle.select(".j_recommendproduct_lable").first().text(scm.getLableName());

		}

		Element compElediv = compEle.select(".j_recommendproduct_all").first();

		for (RecommendProductModel rp : recommendProductData.getRows()) {

			Element clonehtml = compEle.select(".j_recommendproduct_list").clone().first();

			// 设置图片
			setImgHtml(clonehtml, rp);

			// 设置标题
			setTitleHtml(clonehtml, rp);

			// 设置价格
			setPriceHtml(clonehtml, rp);

			// 设置uuid
			setTitleWithUuid(clonehtml, rp);

			compElediv.append(clonehtml.outerHtml());
		}

		// 移除模板
		compEle.select(".j_recommendproduct_list").first().remove();

		return doc.html();

	}

	private void setTitleWithUuid(Element clonehtml, RecommendProductModel rp) {
		clonehtml.select(".j_recommendproduct_title").attr("uuid", rp.getProductUuid());

	}

	private void setPriceHtml(Element clonehtml, RecommendProductModel rp) {
		clonehtml.select(".j_recommendproduct_price").first().text("￥"+ MoneyUtil.fenToYuan((int)rp.getPrice())+ "");

	}

	private void setTitleHtml(Element clonehtml, RecommendProductModel rp) {
		clonehtml.select(".j_recommendproduct_title").first().text(rp.getName());

	}

	/**
	 * 设置图片属性
	 * 
	 * @param rp
	 */
	private void setImgHtml(Element clonehtml, RecommendProductModel rp) {

		clonehtml.select(".j_recommendproduct_img").attr("src", rp.getImgsrc());

	}

	@Override
	public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
		RecommendProductCompModel sc = (RecommendProductCompModel) bcm;
		String compId = sc.getCompId();
		
		designJs = designJs.replaceAll("\\$_compId", compId);

		designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(sc));

		

		return designJs;
	}

	@RequestMapping("/ajaxLoadData")
	@SJson
	public String ajaxLoadData(String compId, String lableName, String productUuid) {
		
		RecommendProductModel recommendProductData =

				recommendProductService.getRecommendProductData(compId, lableName, productUuid);

		String jsonString = Json.toJson(recommendProductData);

		return jsonString;
	}

}
