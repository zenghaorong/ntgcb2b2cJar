package com.aebiz.app.shop.modules.services.impl;

import com.aebiz.app.shop.modules.models.Shop_area_management;
import com.aebiz.app.shop.modules.services.ShopAreaManagementService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.lang.util.NutMap;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@CacheConfig(cacheNames = "shopCache")
@Service
public class ShopAreaManagementServiceImpl extends BaseServiceImpl<Shop_area_management> implements ShopAreaManagementService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @CacheEvict(key = "#root.targetClass.getName()+'*'")
    @Async
    public void clearCache() {

    }

    @Cacheable
    public List<Shop_area_management> getShopAreaManagementList() {
        return this.query(Cnd.where("delFlag", "=", false).asc("code"));
    }

    @Cacheable
    public Map<String, String> getShopAreaManagementMap() {
        return this.getMap(Sqls.create("select code,name from shop_area_management order by code asc"));
    }
}
