package com.aebiz.app.shop.modules.services;

import com.aebiz.app.shop.modules.models.Shop_area_management;
import com.aebiz.baseframework.base.service.BaseService;

import java.util.List;
import java.util.Map;

public interface ShopAreaManagementService extends BaseService<Shop_area_management>{
    void clearCache();

    /**
     * 获取片区列表
     * @return
     */
    List<Shop_area_management> getShopAreaManagementList();

    /**
     * 获取全部的片区map<code, name>
     * @return
     */
    Map<String, String> getShopAreaManagementMap();

}
