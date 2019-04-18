package com.aebiz.app.dec.commons.comps.hotproduct;

import com.aebiz.app.dec.commons.comps.hotproduct.vo.HotProductCompModel;
import com.aebiz.app.dec.commons.comps.hotproduct.vo.HotProductModel;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductCategoryDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductDetailDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/hotProductComp")
public class HotProductController  extends BaseCompController {

	
	

	@Autowired
	private ProductForCompService productForCompService;
	@RequestMapping("/toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request) {
		String toUrl = (String) request.getAttribute("toParamsJspURL");
		
		String pageJson = request.getParameter("pageJson");
		
		Object parseObject = Json.fromJson(pageJson);
		Map obj=(Map)parseObject;
		

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
		request.setAttribute("param_inProductUuids", obj.get("param_inProductUuids"));

		return toUrl;

	}


	@Override
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm) {

		Document doc = Jsoup.parse(pageViewHtml);
		
		
		
		
		Element compEle = doc.select("#" + bcm.getCompId()).first();
		
		
		
		this.delCompLoading(doc, bcm);
		

		HotProductCompModel scm = (HotProductCompModel) bcm;

		// 根据属性获取数据
		String param_inProductUuids = scm.getParam_inProductUuids();

		String labelName = scm.getLableName();

		HotProductModel hotProductData = this
				.getHotProductData(scm.getCompId(), labelName, param_inProductUuids);

		scm.setLableName(hotProductData.getLableName());

		compEle.select("#" + bcm.getCompId()).first();

		if (StringUtils.isEmpty(scm.getLableName())) {

			compEle.select(".j_hotproduct_lable").first().text("推荐商品");
		} else {

			compEle.select(".j_hotproduct_lable").first().text(scm.getLableName());

		}

		Element compElediv = compEle.select(".j_hotproduct_all").first();
		
		int i=1;

		for (HotProductModel rp : hotProductData.getRows()) {

			Element clonehtml = compEle.select(".j_hotproduct_list").clone().first();
			
			//设置排位
			
			clonehtml.select(".j_hotproduct_num").first().text(i+"");
			
			i=i+1;


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
		compEle.select(".j_hotproduct_list").first().remove();

		return doc.html();

	}
	
	private void setTitleWithUuid(Element clonehtml, HotProductModel rp) {
		clonehtml.select(".j_hotproduct_title").attr("uuid", rp.getProductUuid());

	}

	private void setPriceHtml(Element clonehtml, HotProductModel rp) {
		clonehtml.select(".j_hotproduct_price").first().text("￥"+ MoneyUtil.fenToYuan((int)rp.getPrice())+ "");

	}

	private void setTitleHtml(Element clonehtml, HotProductModel rp) {
		clonehtml.select(".j_hotproduct_title").first().text(rp.getName());

	}

	/**
	 * 设置图片属性
	 * 
	 * @param rp
	 */
	private void setImgHtml(Element clonehtml, HotProductModel rp) {

		clonehtml.select(".j_hotproduct_img").attr("src", rp.getImgsrc());

	}

	private HotProductModel getHotProductData(String compId, String labelName, String uuids) {
		
		
		
		HotProductModel scm =new HotProductModel();
		/**
		 * 批量获取数据
		 */
		try {
			List<HotProductModel> list= new ArrayList<>();
		
			
			if(uuids!=null){
				
				
				String[] arr=uuids.substring(1).split(",");
				
				List<ProductDetailDTO> batchProductSample = productForCompService.getBatchProductSample(arr);
				
				
				if(batchProductSample!=null&&batchProductSample.size()>0){
					
					for(ProductDetailDTO productDetailDTO: batchProductSample ){
						HotProductModel scm1 =new HotProductModel();
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
		HotProductCompModel sc = (HotProductCompModel) bcm;
		String compId = sc.getCompId();
		
		designJs = designJs.replaceAll("\\$_compId", compId);

		designJs = designJs.replaceAll("\\$_compConfig_\\$", Json.toJson(sc));

		

		return designJs;
	}

	@RequestMapping("/ajaxLoadData")
	@SJson
	public String ajaxLoadData(String compId, String lableName, String productUuid) {
		
		HotProductModel recommendProductData =

				this.getHotProductData(compId, lableName, productUuid);

		String jsonString =Json.toJson(recommendProductData);

		return jsonString;
	}
}
