package com.aebiz.app.goods.modules.services;

import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.goods.modules.models.Goods_favorite;

import java.util.Map;

/**
 * 商品收藏service
 */
public interface GoodsFavoriteService extends BaseService<Goods_favorite>{

    /**
     * 保存商品收藏信息(调用方法前要先验证accountId、goods_product对应的记录是否存在)
     * @param accountId 会员id
     * @param goods_product 货品
     * @return
     */
    Goods_favorite saveData(String accountId, Goods_product goods_product);

    Map<String,Object> selectDataAll(String accountId, Integer page, Integer rows, Integer status);

    Map<String,Object> selectData(String content, String accountId, Integer page, Integer rows, Integer status);
}
