package com.aebiz.app.dec.commons.comps.producttab.service.impl;


import com.aebiz.app.dec.commons.comps.producttab.service.ProductTabCompService;
import com.aebiz.app.dec.commons.comps.producttab.vo.ProductTabCompModel;
import com.aebiz.app.dec.commons.comps.producttab.vo.ProductTabDetailModel;
import com.aebiz.app.dec.commons.comps.producttab.vo.ProductTabModel;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.utils.CommonUtil;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductDetailDTO;
import com.aebiz.commons.utils.MoneyUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductTabCompServiceImpl implements ProductTabCompService {
	
	@Autowired
	ProductForCompService productForCompService;
	
	private String contextPath;
	

	@Override
	public String genCompHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {
		ProductTabCompModel compModel = (ProductTabCompModel) bcm;
		Document doc = Jsoup.parse(pageViewHtml);
		String compId = bcm.getCompId();
		doc.select("#"+compId+"_comsloading").remove();
		String tabJson = compModel.getTabsJson();
		contextPath = (String) wpm.getMapPageParams().get(DecorateCommonConstant.COMPONENT_REQUEST_CONTEXTPATH);
		//通过商品tabJson获取商品信息
		List<ProductTabModel>  productInfo = getProductTabData(tabJson);
		if(productInfo != null && productInfo.size() > 0){
			 //初始化模板，给模板赋值保存，后面使用模板
			 Map<String,Element> templateMap =  initTemplate(doc, compModel);
			 
			//生成HTML
			genHtml(doc, wpm, compModel, productInfo,templateMap);
		}
		
		return doc.html();
	}

	//初始化模板
	private Map<String,Element> initTemplate(Document doc, ProductTabCompModel  compModel){
		Map<String,Element> res = new HashMap<String,Element>();
		//保存 详情模板，防止在嵌套循环中模板被删除掉
		String compId = compModel.getCompId();
		Element root = doc.select("#"+compId+"-products").first();
		
		Element productDeail = root.select(".j_product-detail").first();
		
		Element productTmplate = productDeail;
		productDeail.remove();
		res.put("productTmplate", productTmplate);
		
		//商品组 模板，8个商品一组，产生轮播效果（8个商品在一个LI内）
		Element productLi = root.select(".j_tab-product-page-group").first();
		Element productLiTmplate = productLi;
		productLi.remove();
		res.put("productLiTmplate", productLiTmplate);
		
		//标签页内容模板，每个标签页对应一个标签页内容
		Element tabContent = root.select(".j_tab-content-wrap").first();
		Element tabContentTemplate = tabContent;
		tabContent.remove();
		res.put("tabContentTemplate", tabContentTemplate);
		return res;
	}
	
	
	/**
	 * 生成页面HTML
	 * @param doc 整个页面文档
	 * @param wpm
	 * @param compModel 该组件model
	 * @param productInfo 商品信息
	 * @param templateMap 模板
	 */
	private void genHtml(Document doc, WebPageModel wpm, ProductTabCompModel  compModel, List<ProductTabModel> productInfo, Map<String,Element> templateMap ){
		String compId = compModel.getCompId();
		//标签页tab
		Element tabs = doc.select("#"+compId+"-tabs").first();
		Element liEl = tabs.select(".j_product-tab").first();
		liEl.remove();
		Element tabContents = doc.select("#"+compId+"-products").first();
		Element tabContentTemplate  = templateMap.get("tabContentTemplate");
		for(int i =0, len = productInfo.size(); i < len ; i++){
			ProductTabModel tabData = productInfo.get(i);
			
			String tabContentId = compId+"-tab-content-"+i;
			//生成一个标签页
			Element tmpLi = liEl.clone();
			tmpLi.attr("href","#"+tabContentId);//标签页锚向标签页内容容器
			tmpLi.select(".j_tab-name").html(tabData.getTabName());
			if(i==0)tmpLi.addClass("on");
			tabs.append(tmpLi.outerHtml());
			//生成一个标签页对应的内容容器
			Element tmpTabContent  = tabContentTemplate.clone();
			tmpTabContent.attr("id",tabContentId);
			//常规展示 不需添加slide
			if(compModel.getShowStyle().equals("1")){
				tmpTabContent.addClass("m-noslide");
			}
			//向容器里面添加数据
			genTabProducts(tabData.getProducts(),tmpTabContent,compModel,templateMap);
			tabContents.append(tmpTabContent.outerHtml());
		}
	
	}
	
	/**
	 * 生成每个tab页签内容
	 * @param products,每个页签内的商品
	 * @param container,页签的外容器
	 * @param compModel
	 */
	private void genTabProducts(List<ProductTabDetailModel> products, Element container, ProductTabCompModel  compModel, Map<String,Element> templateMap){
		Element pagingContainerUl = container.select("ul.j_tab-product-page-wrap").first();
		Element productGroupLi = templateMap.get("productLiTmplate");
		//给商品分组，8个一组 对应一个LI模板内容
		List<List<ProductTabDetailModel>> groups = this.groupProducts(products);
		for(int j = 0 ; j < groups.size() ; j++){
			//循环每个分组
			List<ProductTabDetailModel> groupProducts = groups.get(j);//每个分组内的商品
			//创建分组
			Element tmpGroupLi = productGroupLi.clone();
			//往分组内添加 商品数据
			this.genOneGroupHtml(groupProducts,tmpGroupLi,compModel,templateMap);
			//将新生成的li追加到Ul下
			pagingContainerUl.append(tmpGroupLi.outerHtml());
		}
	}
	
	//将 商品分组，8个商品为一组，获得一个 商品组 数组
	private List<List<ProductTabDetailModel>> groupProducts(List<ProductTabDetailModel> products){
		List<List<ProductTabDetailModel>> res = new ArrayList<List<ProductTabDetailModel>>();
		for(int i = 0 ,len = products.size() ; i < len ; i++){
			List<ProductTabDetailModel> arr = null;
			if((i+1) % 8 == 1){
				arr  = new ArrayList<ProductTabDetailModel>();
				res.add(arr);
			}else{
				arr = res.get(res.size()-1);
			}
			arr.add(products.get(i));
		}
		return res;
	}
	
	//生成一个 商品
	private void genOneGroupHtml(List<ProductTabDetailModel> products, Element tmpGroupLi , ProductTabCompModel  compModel, Map<String,Element> templateMap){
		//商品模板
		Element productDetail = templateMap.get("productTmplate");
		for(int i = 0 ,len = products.size() ; i < len ; i++){
			ProductTabDetailModel product = products.get(i);
			Element tmpProduct = productDetail.clone();
			tmpProduct.select(".j_product-name").html(product.getProductName());
			tmpProduct.select(".j_product-price").html(MoneyUtil.fenToYuan(Integer.parseInt(product.getPrice()))+"");
			//================连接 待定===================//
			//String link = contextPath + compModel.getProductDetailUrl()+"&productUuid="+product.getProductUuid();
			String link = CommonUtil.getProductDetailUrl(contextPath, compModel.getProductDetailUrl(), product.getProductUuid());
			tmpProduct.select("a.j_product-link").attr("href",link);
			tmpProduct.select("a.j_product-collect").attr("data-id",product.getProductUuid());
			tmpProduct.select(".j_product-photo").attr("src",product.getPic());
			
			tmpGroupLi.append(tmpProduct.outerHtml());
		}
	}
	
	
	@Override
	public List<ProductTabModel> getProductTabData(String tabJson) {
 		List<ProductTabModel>  products = new ArrayList<ProductTabModel>();
		JSONArray jsonArr = JSON.parseArray(tabJson);
		//依次获取每个tab的数据
		for(int i = 0 ,len = jsonArr.size() ; i < len ; i ++){
			JSONObject tab = (JSONObject) jsonArr.get(i);
			ProductTabModel productTab  = new ProductTabModel();
			productTab.setTabName(tab.getString("tabName"));
			JSONArray productUuidsJsonArr = tab.getJSONArray("productUuids");
			List<String> productUuids = new ArrayList<String>();
			for(int j = 0 ,length = productUuidsJsonArr.size() ; j < length ; j ++){
				String productUuid = productUuidsJsonArr.getString(j);
				productUuids.add(productUuid);
			}
			List<ProductTabDetailModel> details = getProductDetailModelByUuids(productUuids);
			productTab.setProducts(details);
			products.add(productTab);
		}
		
		return products;
	}
	
	/**
	 * 通过商品tab下的商品UUid获取商品详情
	 * @param productUuids
	 * @return
	 */
	private List<ProductTabDetailModel> getProductDetailModelByUuids(List<String> productUuids){

		
	
    	String[] arr = (String[])productUuids.toArray(new String[productUuids.size()]);
		
		
		
		
		List<ProductTabDetailModel>  list = new ArrayList<ProductTabDetailModel>();

    	List<ProductDetailDTO> res =productForCompService.getBatchProductSample(arr);
    	
    	for(ProductDetailDTO dto:res){
			ProductTabDetailModel  product = new ProductTabDetailModel();
			product.setProductName(dto.getProductName());
			product.setProductUuid(dto.getProductUuid());
			product.setPrice((long)dto.getShopPrice()+"");
			product.setPic(dto.getCenterImageUrl());
			list.add(product);
    	}
		return list;
	}
	@Override
	public List<ProductTabDetailModel> getProductDetailsByUuids( List<String> productUuids) {
		return  getProductDetailModelByUuids(productUuids);
	}

}
