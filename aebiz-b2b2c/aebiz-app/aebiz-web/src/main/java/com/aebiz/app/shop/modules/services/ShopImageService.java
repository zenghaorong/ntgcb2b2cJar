package com.aebiz.app.shop.modules.services;

import com.aebiz.app.shop.modules.models.Shop_image;
import com.aebiz.baseframework.base.service.BaseService;

public interface ShopImageService extends BaseService<Shop_image>{
    void clearCache();
    Shop_image getImage(String id);
}
