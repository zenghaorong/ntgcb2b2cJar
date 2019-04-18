package com.aebiz.app.web.modules.controllers.open.dec.dto.product;


import com.aebiz.app.goods.modules.models.Goods_favorite;
import com.aebiz.app.store.modules.models.Store_goodsclass;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品相关业务Model转换为DTO工具类
 *
 * Created by Aebiz_yjq on 2017/1/21.
 */
public class ProductConvertHelper {
    /**
     *将商品分类Model列表转换为DTO类表
     * @param productCategoryFrontInteractiveModels 商品model列表
     * @return 转换之后的DTO列表
     */
    public static List<ProductCategoryDTO> convertProductCategoryToDTOs(List<Store_goodsclass> productCategoryFrontInteractiveModels){
        List<ProductCategoryDTO> productCategoryDTOs = new ArrayList<>();
        ProductCategoryDTO dto;
        if( null != productCategoryFrontInteractiveModels && !productCategoryFrontInteractiveModels.isEmpty()){
            for(Store_goodsclass pcModel : productCategoryFrontInteractiveModels){
                dto=new ProductCategoryDTO();
                dto.setCategoryUuid(pcModel.getId());
                dto.setCategoryName(pcModel.getName());
                dto.setCategoryNo("");
                dto.setCategoryUrl("");
                dto.setPosition(pcModel.getLocation());
                dto.setParentUuid(pcModel.getParentId());
                /**add by wgh  on  2017-02-22  begin***********************/
                List<Store_goodsclass>  pcfList = pcModel.getClassList();
                List<ProductCategoryDTO> subCateList = new ArrayList<>();
                if(pcfList != null && pcfList.size() > 0){
                	ProductCategoryDTO subCatDto = null;
                	for(Store_goodsclass pcf : pcfList){
                		subCatDto = new ProductCategoryDTO();
                        subCatDto.setCategoryUuid(pcf.getId());
                        subCatDto.setCategoryName(pcf.getName());
                        subCatDto.setCategoryNo("");
                        subCatDto.setCategoryUrl("");
                        subCatDto.setPosition(pcf.getLocation());
                        subCatDto.setParentUuid(pcf.getParentId());
                		subCateList.add(subCatDto);
                	}
                	dto.setSubCategoryList(subCateList);
                }
                /**add by wgh  on  2017-02-22  end**********************************/
                productCategoryDTOs.add(dto);
            }
        }
        return productCategoryDTOs;
    }

    /**
     * 将商品收藏Model列表转换为收藏DTO列表
     *
     * @param productFavoriteModels 商品收藏 Model
     * @return 转换之后的DTO列表
     */
    public static List<ProductFavoriteDTO> convertProductFavorite(List<Goods_favorite> productFavoriteModels){
        List<ProductFavoriteDTO> productFavoriteDTOs = new ArrayList<>();
        ProductFavoriteDTO productFavoriteDTO;
        if(null != productFavoriteModels && !productFavoriteModels.isEmpty()){
            for (Goods_favorite model : productFavoriteModels) {
                productFavoriteDTO = new ProductFavoriteDTO(model.getId(), model.getProductId(), model.getProductName(), model.getFavoritePrice(), model.getAccountId());
                productFavoriteDTOs.add(productFavoriteDTO);
            }
        }
        return productFavoriteDTOs;
    }
}
