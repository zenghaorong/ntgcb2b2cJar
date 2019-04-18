package com.aebiz.app.shop.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.shop.modules.models.Shop_adv_main;

import java.util.List;

public interface ShopAdvMainService extends BaseService<Shop_adv_main>{
    //根据广告位的id获取下面的所有的广告图片
    public List<Shop_adv_main> getAdvMainByPositionId(String positionId);
}
