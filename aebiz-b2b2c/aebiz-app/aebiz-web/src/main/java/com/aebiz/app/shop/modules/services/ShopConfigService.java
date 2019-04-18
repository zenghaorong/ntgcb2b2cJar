package com.aebiz.app.shop.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.shop.modules.models.Shop_config;

public interface ShopConfigService extends BaseService<Shop_config>{
    void clearCache();

    /**
     * 获取缓存shopConfig
     * @return
     */
    Shop_config getShopConfig();

    /**
     * 商品发布是否需要审核
     * @return true 需要审核， false 不需要
     */
    boolean checkOnGoodsPublish();

}
