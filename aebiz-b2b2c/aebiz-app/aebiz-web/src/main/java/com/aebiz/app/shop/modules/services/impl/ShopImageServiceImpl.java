package com.aebiz.app.shop.modules.services.impl;

import com.aebiz.app.shop.modules.models.Shop_image;
import com.aebiz.app.shop.modules.services.ShopImageService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.aop.interceptor.async.Async;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@CacheConfig(cacheNames = "shopCache")
public class ShopImageServiceImpl extends BaseServiceImpl<Shop_image> implements ShopImageService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @CacheEvict(key = "#root.targetClass.getName()+'*'")
    @Async
    public void clearCache() {

    }

    @Cacheable
    public Shop_image getImage(String id) {
        return this.fetch(Cnd.where("id", "=", id));
    }
}
