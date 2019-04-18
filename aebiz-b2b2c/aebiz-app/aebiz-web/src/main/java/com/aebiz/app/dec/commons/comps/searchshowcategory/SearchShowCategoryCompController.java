package com.aebiz.app.dec.commons.comps.searchshowcategory;

import com.aebiz.app.dec.commons.comps.searchshowcategory.vo.SearchShowCategoryCompModel;
import com.aebiz.app.dec.commons.service.CategoryForCompService;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.utils.CommonUtil;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductCategoryDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜搜显示分类组件
 * @author wgh
 * 2017年2月22日
 */
@Controller
@RequestMapping("/searchShowCategoryComp")
public class SearchShowCategoryCompController extends BaseCompController {

	@Autowired
	private CategoryForCompService categoryForCompService;
	@Autowired
	private ProductForCompService productForCompService;
	
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm){
		Document doc= Jsoup.parse(pageViewHtml);
		super.delCompLoading(doc, bcm);
		SearchShowCategoryCompModel sscm = (SearchShowCategoryCompModel)bcm;
		String compId = bcm.getCompId();
		//判断显示搜索名称
		String searchName = getSearchName(wpm);
		if(searchName != null && !"".equals(searchName)){
			doc.select("#"+compId+"_searchname").html(searchName);
		}
		//调用接口返回显示分类
		List<ProductCategoryDTO> subCate = getSubCate(wpm,sscm);
		String contextPath = (String)wpm.getMapPageParams().get(DecorateCommonConstant.COMPONENT_REQUEST_CONTEXTPATH);
		//初始化页面数据
		initViewHtml(doc,subCate,sscm,contextPath);
		return doc.html();
	}
	
	/**
	 * 获取搜索名称
	 * @param wpm
	 * @return
	 * String
	 */
	private  String  getSearchName(WebPageModel wpm){
		String keyword = "";
		Map<String,Object> params = wpm.getMapPageParams();
		if(params != null && params.get("keyword") != null){
			keyword = params.get("keyword").toString();
		}
		String categoryUuid = ""; 
		if(params != null && params.get("categoryUuid") != null){
			categoryUuid = params.get("categoryUuid").toString();
		}
		// 获取搜索分类名称
		String searchName = "";
		ProductCategoryDTO categoryDto = categoryForCompService.getCategoryByUuid(categoryUuid);
		if (Strings.isEmpty(keyword)){
			if (categoryDto != null) {
				searchName = categoryDto.getCategoryName();
			}
		} else {
			searchName = keyword;
		}
		
		return searchName;
	}
	
	/**
	 * 调用接口返回显示分类
	 * @param wpm
	 * @param sscm
	 * @return
	 * List<ProductCategoryDTO>
	 */
	private List<ProductCategoryDTO> getSubCate(WebPageModel wpm,SearchShowCategoryCompModel sscm){
		List<ProductCategoryDTO> subCate = new ArrayList<>();
		Map params = wpm.getMapPageParams();
		//从共享数据中取出店铺uuids
		Map<String,Object> resultMap = (Map<String,Object>)params.get(sscm.getShareDataName()+"_searchResult");
		if(resultMap != null && resultMap.get("subCate") != null){
			subCate = (List<ProductCategoryDTO>) resultMap.get("subCate");
		}else{
			//共享数据中没有，自己查询数据并共享
			params.put("nowPage", "1");
			params.put("pageShow", "500");
			Map<String,Object> dataMap = productForCompService.searchProductAndScreenAttr(params);
			if(dataMap != null){
				params.put(sscm.getShareDataName()+"_searchResult", dataMap);
				subCate = (List<ProductCategoryDTO>) dataMap.get("subCate");
			}
		}
		
		return subCate;
	}
	
	/**
	 * 初始化页面数据
	 * @param doc
	 * @param subCate
	 * @param sscm
	 * @param contextPath
	 * void
	 */
	private void initViewHtml(Document doc, List<ProductCategoryDTO> subCate, SearchShowCategoryCompModel sscm, String contextPath){
		String compId = sscm.getCompId();
		Element cloneEle = doc.select("#"+compId+"_panel").first().clone();
		doc.select("#"+compId+"_panel").remove();
		if(subCate != null && subCate.size() > 0){
			List<ProductCategoryDTO> subCategoryList = null;
			ProductCategoryDTO p = null;
			for(int i=0;i<subCate.size();i++){
				p = subCate.get(i);
				if(p != null){
					Element newEle = cloneEle;
					if(i == 0){
						newEle.select(".cat-list-cla").addClass("in");
					}else{
						newEle.select(".cat-list-cla").removeClass("in");
					}
					newEle.select(".cat-list-cla").attr("id", "category-list-"+i);
					Elements h3aEle = newEle.select("#"+compId+"_h3_a")
							.attr("href", CommonUtil.getProductListUrl(contextPath,sscm.getProductListUrl(),p.getCategoryUuid()))
							.html(p.getCategoryName());
					newEle.select("#"+compId+"_panel_h3").attr("data-target", "#category-list-"+i)
						.html(h3aEle.outerHtml());
					subCategoryList = p.getSubCategoryList();
					if(subCategoryList != null && subCategoryList.size() > 0){
						Element liEle = newEle.select("#"+compId+"_ul_li").first().clone();
						newEle.select("#"+compId+"_ul_li").remove();
						for(ProductCategoryDTO spc : subCategoryList){
							Elements aEle = liEle.select("#"+compId+"_ul_a")
									.attr("href", CommonUtil.getProductListUrl(contextPath,sscm.getProductListUrl(),spc.getCategoryUuid()))
									.html(spc.getCategoryName());
							liEle.select("#"+compId+"_ul_li").html(aEle.outerHtml());
							newEle.select("#"+compId+"_list_ul").append(liEle.outerHtml());
						}
					}else{
						newEle.select("#"+compId+"_list_ul").remove();
					}
					doc.select("#"+compId+"_category-filter").append(newEle.outerHtml());
				}
			}
		}else{
			doc.select("#"+compId+"_category-filter").remove();
		}
	}
	
	/**
	 * 初始化属性编辑页面
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
	 */
	public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
		String js = designJs.replaceAll("\\$_compId",bcm.getCompId());
		js = js.replaceAll("\\$_compConfig_\\$", Json.toJson(bcm));
		return js;
	}
	
	/**
	 * ajax调用初始化页面
	 */
	@RequestMapping(value = "/ajaxLoadData", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String ajaxLoadData(HttpServletRequest request){
		String keyword = request.getParameter("keyword");
		if(keyword != null){
			try {
				keyword = new String(keyword.getBytes("ISO-8859-1"),"UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String categoryUuid = request.getParameter("categoryUuid");
		// 获取搜索分类名称
		String searchName = "";
		ProductCategoryDTO categoryDto = categoryForCompService.getCategoryByUuid(categoryUuid);
		if (Strings.isEmpty(keyword)){
			if (categoryDto != null) {
				searchName = categoryDto.getCategoryName();
			}
		} else {
			searchName = keyword;
		}
		
		List<ProductCategoryDTO> subCate = new ArrayList<>();
		Map<String,String> queryParam = new HashMap<>();
		queryParam.put("keyword", keyword);
		queryParam.put("categoryUuid", categoryUuid);
		queryParam.put("nowPage", "1");
		queryParam.put("pageShow", "500");
		//调用接口查询显示分类
		Map<String,Object> dataMap = productForCompService.searchProductAndScreenAttr(queryParam);
		if(dataMap != null){
			subCate = (List<ProductCategoryDTO>) dataMap.get("subCate");
		}
		
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("searchName", searchName);
		resultMap.put("subCate", subCate);
		return Json.toJson(resultMap);
	}
}
