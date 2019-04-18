package com.aebiz.app.dec.commons.comps.recommendproduct.service.impl;

import com.aebiz.app.dec.commons.comps.recommendproduct.service.RecommendProductService;
import com.aebiz.app.dec.commons.comps.recommendproduct.vo.RecommendProductModel;
import com.aebiz.app.dec.commons.service.ProductForCompService;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductDetailDTO;
import com.aebiz.baseframework.page.Pagination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecommendProductServiceImpl implements RecommendProductService {
	
	@Autowired
	private ProductForCompService compService;
	

	
	public RecommendProductModel getRecommendProductData(String compId, String lableName, String uuids){
	
		RecommendProductModel scm =new RecommendProductModel();
		/**
		 * 批量获取数据
		 */
		try {
			List<RecommendProductModel> list= new ArrayList<>();
		
			
			if(uuids!=null && uuids !=""){
				
				
				String[] arr=uuids.substring(1).split(",");
				
				List<ProductDetailDTO> batchProductSample = compService.getBatchProductSample(arr);
				
				
				if(batchProductSample!=null&&batchProductSample.size()>0){
					
					for(ProductDetailDTO productDetailDTO: batchProductSample ){
						RecommendProductModel scm1 =new RecommendProductModel();
						scm1.setProductUuid(productDetailDTO.getProductUuid());

							scm1.setSku(productDetailDTO.getSku());

							scm1.setPrice(productDetailDTO.getShopPrice());
							
							scm1.setName(productDetailDTO.getProductName());
							
							scm1.setImgsrc(productDetailDTO.getCenterImageUrl());

							list.add(scm1);
						
					}
					
				}
				
			}
			scm.setRows(list);

			scm.setLableName(lableName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scm;
	}
	
	
	@Override
	public RecommendProductModel getSearchData(String nowPage, String pageShow, String searchKey,
			String categoryUuid, String startPrice, String endPrice) {
		if(StringUtils.isBlank(nowPage)){
			nowPage="1";
			
		}
		if(StringUtils.isBlank(pageShow)){
			pageShow="5";
		}
		Map<String, Object> map = new HashMap<>();

		map.put("nowPage", nowPage);

		map.put("pageShow", pageShow);

		map.put("searchKey", searchKey);

		map.put("categoryUuid", categoryUuid);

		map.put("startPrice", startPrice);

		map.put("endPrice", endPrice);

		map.put("sortBy", "");

		map.put("sortType", "");
		
	
		
			
	 Pagination storeSearch = compService.search("", searchKey, categoryUuid,
				Integer.valueOf(pageShow), Integer.valueOf(nowPage), "", "", startPrice, endPrice);
		RecommendProductModel wm = new RecommendProductModel();

		List<RecommendProductModel> list = new ArrayList<>();
		
		List<ProductDTO> rows=(List<ProductDTO>)storeSearch.getList();
		


				wm.setPageShow(Integer.parseInt(pageShow));

				wm.setNowPage(Integer.parseInt(nowPage));

				wm.setTotalNum(storeSearch.getTotalCount());

				for (ProductDTO product : rows) {

					RecommendProductModel scmchild = new RecommendProductModel();

					scmchild.setProductUuid(product.getUuid());

					scmchild.setName(product.getName());

					scmchild.setImgsrc(product.getPic());

					scmchild.setPrice(product.getPrice());

					list.add(scmchild);
				}
		wm.setRows(list);
		
		return wm;
	}

}
