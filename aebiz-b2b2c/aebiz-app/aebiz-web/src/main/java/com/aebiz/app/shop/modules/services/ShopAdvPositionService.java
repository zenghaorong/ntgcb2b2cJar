package com.aebiz.app.shop.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.shop.modules.models.Shop_adv_position;

import java.util.List;

public interface ShopAdvPositionService extends BaseService<Shop_adv_position>{
    void clearCache();

    //获取所有的广告位管理（前台用）
    public List<Shop_adv_position> getAll(int pageShow, int pageNo);
}
