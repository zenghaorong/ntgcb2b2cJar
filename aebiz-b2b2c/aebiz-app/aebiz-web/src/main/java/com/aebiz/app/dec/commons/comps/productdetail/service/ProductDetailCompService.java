package com.aebiz.app.dec.commons.comps.productdetail.service;


import com.aebiz.app.dec.commons.comps.productdetail.vo.ProductDetailInfoModel;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.baseframework.page.Pagination;
import org.jsoup.nodes.Document;

public interface ProductDetailCompService {
	
	ProductDetailInfoModel getProductDetailInfo(String sku, String pageShow);


	Pagination getProductComment(String sku, String type, String nowPage, String pageShow);
	
	
	Document genCompHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm);

}
