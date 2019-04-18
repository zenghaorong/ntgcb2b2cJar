package com.aebiz.app.dec.commons.comps.recommendproduct.service;


import com.aebiz.app.dec.commons.comps.recommendproduct.vo.RecommendProductModel;

public interface RecommendProductService  {
	
	
	
	
	
	public RecommendProductModel getRecommendProductData(String compId, String labelName, String productuuid);






	/**
	 * 平台推荐商品
	 * @param nowPage
	 * @param pageShow
	 * @param searchKey
	 * @param categoryUuid
	 * @param startPrice
	 * @param endPrice
	 * @param search
	 * @return
	 */
	RecommendProductModel getSearchData(String nowPage, String pageShow, String searchKey, String categoryUuid,
                                        String startPrice, String endPrice);
	
	

}
