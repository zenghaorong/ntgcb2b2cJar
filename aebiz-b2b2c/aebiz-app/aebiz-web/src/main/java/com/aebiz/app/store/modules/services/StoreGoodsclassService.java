package com.aebiz.app.store.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.store.modules.models.Store_goodsclass;

import java.util.List;

public interface StoreGoodsclassService extends BaseService<Store_goodsclass>{

    void clearCache();

    void save(Store_goodsclass goodsClass, String parentId, String storeId);

    //获取所有的商品分类列表
    List<Store_goodsclass> getAllClassGoodsName();


    /**
     * 根据分类uuid查询分类,三级分类
     * @return
     */
    Store_goodsclass getByCategoryUuid(String categoryUuid);

    /**
     * 对外接口：获取所有店铺分类
     *
     * @param parentUuid
     * 			父类uuid
     * @return
     */
    List<Store_goodsclass> getAllProductCategoryFront(String parentUuid);

    /**
     * 排序
     * @param menuIds
     * @param storeId
     */
    void sort(String[] menuIds, String storeId);
    /**
     * 根据商户查询商户某一分类对应的同级分类
     * @param storeUuid
     * @return
     */
    public List<Store_goodsclass> getSameLevelProductCategorysByStoreUuid(String storeUuid);
}
