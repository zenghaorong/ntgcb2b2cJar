package com.aebiz.app.dec.commons.comps.choosewellproduct;

import com.aebiz.app.dec.commons.comps.choosewellproduct.vo.ChooseWellProductCompModel;
import com.aebiz.app.dec.commons.comps.choosewellproduct.vo.ChooseWellProductModel;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductCategoryDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductDetailDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.page.Pagination;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/chooseWellProductComp")
public class ChooseWellProductController extends BaseCompController {

	@Autowired
	private ProductForCompService productForCompService;
	@RequestMapping("/toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
		String toUrl = (String) request.getAttribute("toParamsJspURL");
		
		String pageJson = request.getParameter("pageJson");
		
		Map parseObject =(Map)Json.fromJson(pageJson);

		

		String nowPage = request.getParameter("nowPage");
		String pageShow = request.getParameter("pageShow");
		String searchKey = request.getParameter("searchKey");
		String categoryUuid = request.getParameter("categoryUuid");
		String startPrice = request.getParameter("startPrice");
		String endPrice = request.getParameter("endPrice");
		String param_inProductUuids = request.getParameter("param_inProductUuids");


		
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
		ChooseWellProductModel scm = this.getSearchData(nowPage, pageShow, searchKey,
				categoryUuid, startPrice, endPrice);
		
		scm.setParam_inProductUuids(param_inProductUuids);

		String jsonString = Json.toJson(scm);

		return jsonString;
	}

	private ChooseWellProductModel getSearchData(String nowPage, String pageShow, String searchKey, String categoryUuid,
			String startPrice, String endPrice) {
		
		
		

		if(StringUtils.isBlank(nowPage)){
			nowPage="1";
		}
		
		if(StringUtils.isBlank(pageShow)){
			pageShow="5";	
		}
		
	
	Pagination storeSearch = productForCompService.search("", searchKey, categoryUuid,
				Integer.valueOf(pageShow), Integer.valueOf(nowPage), "", "", startPrice, endPrice);
	/*
	 CompWebModel compWebModel = (CompWebModel) storeSearch.get("wm");*/
		
		
	 ChooseWellProductModel wm = new ChooseWellProductModel();

		List<ChooseWellProductModel> list = new ArrayList<>();
		
		List<ProductDTO> rows=(List<ProductDTO> )storeSearch.getList();



				wm.setPageShow(Integer.parseInt(pageShow));

				wm.setNowPage(Integer.parseInt(nowPage));

				wm.setTotalNum(storeSearch.getTotalCount());

				for (ProductDTO product : rows) {

					ChooseWellProductModel scmchild = new ChooseWellProductModel();

					scmchild.setProductUuid(product.getUuid());

					scmchild.setName(product.getName());

					scmchild.setImgsrc(product.getPic());

					scmchild.setPrice(product.getPrice());

					list.add(scmchild);
				}



		wm.setRows(list);
		
		return wm;
	}

	@Override
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {

		Document doc = Jsoup.parse(pageViewHtml);
		
		
		
		
		Element compEle = doc.select("#" + bcm.getCompId()).first();
		
		
		
		this.delCompLoading(doc, bcm);
		

		ChooseWellProductCompModel scm = (ChooseWellProductCompModel) bcm;

		// 根据属性获取数据
		String param_inProductUuids = scm.getParam_inProductUuids();

		String labelName = scm.getLableName();

		ChooseWellProductModel chooseWellProductModel = this
				.getHotProductData(scm.getCompId(), labelName, param_inProductUuids);

		scm.setLableName(chooseWellProductModel.getLableName());

		compEle.select("#" + bcm.getCompId()).first();

		if (StringUtils.isEmpty(scm.getLableName())) {

			compEle.select(".j_choosewellproduct_lable").first().text("商品精选");
		} else {

			compEle.select(".j_choosewellproduct_lable").first().text(scm.getLableName());

		}

		Element compElediv = compEle.select(".j_choosewellproduct_all").first();
		

		for (ChooseWellProductModel rp : chooseWellProductModel.getRows()) {

			Element clonehtml = compEle.select(".j_choosewellproduct_list").clone().first();
			
			
			//oneProductHtml.find(".j_choosewellproduct_collection").attr("uuid",listdata[i].productUuid);

			
			clonehtml.select(".j_choosewellproduct_collection").attr("uuid", rp.getProductUuid());

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
		compEle.select(".j_choosewellproduct_list").first().remove();

		return doc.html();

	}
	
	private void setTitleWithUuid(Element clonehtml, ChooseWellProductModel rp) {
		clonehtml.select(".j_choosewellproduct_title").attr("uuid", rp.getProductUuid());

	}

	private void setPriceHtml(Element clonehtml, ChooseWellProductModel rp) {
		clonehtml.select(".j_choosewellproduct_price").first().text("￥"+ MoneyUtil.fenToYuan((int)rp.getPrice() )+ "");

	}

	private void setTitleHtml(Element clonehtml, ChooseWellProductModel rp) {
		clonehtml.select(".j_choosewellproduct_title").first().text(rp.getName());

	}

	/**
	 * 设置图片属性
	 * 
	 * @param rp
	 */
	private void setImgHtml(Element clonehtml, ChooseWellProductModel rp) {

		clonehtml.select(".j_choosewellproduct_img").attr("src", rp.getImgsrc());

	}

	private ChooseWellProductModel getHotProductData(String compId, String labelName, String uuids) {
		
		
		
		ChooseWellProductModel scm =new ChooseWellProductModel();
		/**
		 * 批量获取数据
		 */
		try {
			List<ChooseWellProductModel> list= new ArrayList<>();
		
			
			if(uuids!=null&&uuids.contains(",")){
				
				
				String[] arr=uuids.substring(1).split(",");
				
				List<ProductDetailDTO> batchProductSample = productForCompService.getBatchProductSample(arr);
				
				
				if(batchProductSample!=null&&batchProductSample.size()>0){
					
					for(ProductDetailDTO productDetailDTO: batchProductSample ){
						ChooseWellProductModel scm1 =new ChooseWellProductModel();
						scm1.setProductUuid(productDetailDTO.getProductUuid());
						
							
							scm1.setPrice(productDetailDTO.getShopPrice());
							
							scm1.setName(productDetailDTO.getProductName());
							
							scm1.setImgsrc(productDetailDTO.getCenterImageUrl());

							list.add(scm1);
						
					}
					
				}
				
			}
			
			scm.setRows(list);

			scm.setLableName(labelName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return scm;
	}


	@Override
	public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
		
		
		ChooseWellProductCompModel sc = (ChooseWellProductCompModel) bcm;
		String compId = sc.getCompId();
		
		designJs = designJs.replaceAll("\\$_compId", compId);

		designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(sc));

		

		return designJs;
	}

	@RequestMapping("/ajaxLoadData")
	@SJson
	public String ajaxLoadData(String compId, String lableName, String productUuid) {
		
		ChooseWellProductModel recommendProductData =

				this.getHotProductData(compId, lableName, productUuid);

		String jsonString = Json.toJson(recommendProductData);

		return jsonString;
	}
}
