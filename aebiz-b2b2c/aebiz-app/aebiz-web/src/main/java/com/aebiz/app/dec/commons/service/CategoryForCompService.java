package com.aebiz.app.dec.commons.service;


import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductCategoryDTO;

import java.util.List;

/**
 * 前台分类相关Service
 * @author hyl
 * 2017年2月21日
 */
public interface CategoryForCompService {

	/**
	 * 根据分类Uuid查询分类
	 * @param categoryUuid
	 * @return
	 * ProductCategoryDTO
	 */
	public ProductCategoryDTO getCategoryByUuid(String categoryUuid);
	
	/**
	 * 对外接口：获取所有店铺分类
	 * @param parentUuid
	 * 			父类uuid
	 * @return
	 */
	//public List<ProductCategoryDTO> getAllProductCategoryFront(String parentUuid);
}
