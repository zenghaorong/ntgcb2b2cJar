package com.aebiz.app.dec.commons.comps.producttab;


import com.aebiz.app.dec.commons.comps.producttab.service.ProductTabCompService;
import com.aebiz.app.dec.commons.comps.producttab.vo.ProductTabCompModel;
import com.aebiz.app.dec.commons.comps.producttab.vo.ProductTabDetailModel;
import com.aebiz.app.dec.commons.comps.producttab.vo.ProductTabModel;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductCategoryDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("producttab")
public class ProductTabController extends BaseCompController {
	
	@Autowired
	ProductTabCompService productTabCompService;
	
	@Autowired
	private ProductForCompService productForCompService;

	@Override
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm){
		return productTabCompService.genCompHtml(pageViewHtml, wpm, bcm);
	}
	
	
	/**
	 * 根据tabsJson获取商品信息
	 * @param tabJson
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getInitData")
	public List<ProductTabModel> ajaxLoadData(@RequestParam("tabJson")String tabJson, HttpServletRequest req){
		return productTabCompService.getProductTabData(tabJson);
	}
	
	@ResponseBody
	@RequestMapping("getProducts")
	public List<ProductTabDetailModel> getProducts(@RequestParam("productUuids")String productUuids){
		if(!productUuids.isEmpty()){
			List<String> uuids = Arrays.asList(productUuids.split(","));
			return productTabCompService.getProductDetailsByUuids(uuids);
		}else{
			return null;
		}
	}
	
	@Override
	@RequestMapping("/toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
		String toUrl = (String) request.getAttribute("toParamsJspURL");
		List<ProductCategoryDTO> firstLevelCategory2 = productForCompService.getFirstLevelCategory();
		request.setAttribute("prodList", firstLevelCategory2);
		return toUrl;
	}
	
	
	@RequestMapping("/totest")
	public String totest(HttpServletResponse response, HttpServletRequest request) {
		return "/producttab/paramsDefine";
	}
	
	
	@Override
	public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
		ProductTabCompModel compModel = (ProductTabCompModel) bcm;
		String tabsJson = compModel.getTabsJson();
		//这里解析会出现问题。nutz框架会忽略空值的转换
		((ProductTabCompModel) bcm).setTabsJson("");
		String js = designJs.replaceAll("\\$_compConfig_\\$",Json.toJson(bcm));
		//tabsJson本来就是JSON字符串，不用转
		js = js.replaceAll("\\$_tabsJson_\\$",tabsJson);
		js = js.replaceAll("\\$_compId", compModel.getCompId());
		return js;
	}
	
}
