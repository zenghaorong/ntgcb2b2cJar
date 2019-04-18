package com.aebiz.app.dec.commons.comps.producttab.service;


import com.aebiz.app.dec.commons.comps.producttab.vo.ProductTabDetailModel;
import com.aebiz.app.dec.commons.comps.producttab.vo.ProductTabModel;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;

import java.util.List;

public interface ProductTabCompService {
	
	/**
	 * 生成页面HTML
	 * @param pageViewHtml
	 * @param wpm
	 * @param bcm
	 * @return
	 */
	String genCompHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm);
	
	
	/**
	 * 跟tabJson获取商品信息，tabJson下有多个商品ID
	 * @param tabJson
	 * @return
	 */
	List<ProductTabModel> getProductTabData(String tabJson);
	
	/**
	 * 通过商品UUIDS获取商品信息集合
	 * @param productUuids
	 * @return
	 */
	List<ProductTabDetailModel> getProductDetailsByUuids(List<String> productUuids);
	
}
