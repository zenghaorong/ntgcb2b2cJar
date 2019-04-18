package com.aebiz.app.dec.commons.service.impl;

import com.aebiz.app.dec.commons.service.CategoryForCompService;
import com.aebiz.app.store.modules.models.Store_goodsclass;
import com.aebiz.app.store.modules.services.StoreGoodsclassService;
import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 前台分类业务
 * @author hyl
 * 2017年2月21日
 */
@Service
public class CategoryForCompServiceImpl implements CategoryForCompService {

	 @Autowired
	 private StoreGoodsclassService storeGoodsclassService;
	/**
	 * 根据分类Uuid查询分类
	 * @param categoryUuid
	 * @return
	 * ProductCategoryDTO
	 */
	public ProductCategoryDTO getCategoryByUuid(String categoryUuid){
		Store_goodsclass model = storeGoodsclassService.fetch(categoryUuid);
		ProductCategoryDTO category = new ProductCategoryDTO();
		if(model != null){
        	category.setCategoryUuid(model.getId());
        	category.setPosition(model.getLocation());
        	category.setCategoryNo("");
        	category.setCategoryName(model.getName());
        	category.setParentUuid(model.getParentId());
        }
		return category;
	}
	
	/**
	 * 对外接口：获取所有店铺分类
	 * @param parentUuid
	 * 			父类uuid
	 * @return
	 */
	/*public List<ProductCategoryDTO> getAllProductCategoryFront(String parentUuid){
		List<ProductCategoryFrontInteractiveModel> subCate = productCategoryFrontInteractiveService
				.getAllProductCategoryFront(parentUuid);
		List<ProductCategoryDTO> cateDtoList = new ArrayList<>();
		if(subCate != null && subCate.size() > 0){
			ProductCategoryDTO cateDto = null;
			for(ProductCategoryFrontInteractiveModel p : subCate){
				if(p != null){
					cateDto = new ProductCategoryDTO();
					cateDto.setCategoryUuid(p.getUuid());
					cateDto.setCategoryName(p.getCategoryName());
					cateDto.setCategoryNo(p.getCategoryNo());
					cateDto.setCategoryUrl(p.getCategoryUrl());
					List<ProductCategoryFrontInteractiveModel> subCategoryPlatformModels = p.getSubProductCategoryFront();
					ProductCategoryDTO productSubCategoryDTO;
		            List<ProductCategoryDTO> productCategoryDTOList = new ArrayList<>();
		            if(subCategoryPlatformModels != null && subCategoryPlatformModels.size() > 0){
		                for(ProductCategoryFrontInteractiveModel subCategoryModel : subCategoryPlatformModels){
		                    productSubCategoryDTO = new ProductCategoryDTO(subCategoryModel.getUuid(), subCategoryModel.getPosition(),
		                            subCategoryModel.getParentUuid(), subCategoryModel.getCategoryNo(), subCategoryModel.getCategoryName(),
		                            subCategoryModel.getIconPath(), subCategoryModel.getCategoryUrl());
		                    productCategoryDTOList.add(productSubCategoryDTO);
		                }
		            }
		            cateDto.setSubCategoryList(productCategoryDTOList);
		            cateDtoList.add(cateDto);
				}
			}
		}
		return cateDtoList;
	}*/
}
