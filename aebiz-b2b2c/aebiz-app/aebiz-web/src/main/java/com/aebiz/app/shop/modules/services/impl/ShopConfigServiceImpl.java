package com.aebiz.app.shop.modules.services.impl;

import com.aebiz.app.sys.modules.models.Sys_dict;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.shop.modules.models.Shop_config;
import com.aebiz.app.shop.modules.services.ShopConfigService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@CacheConfig(cacheNames = "shopCache")
public class ShopConfigServiceImpl extends BaseServiceImpl<Shop_config> implements ShopConfigService {

    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    @CacheEvict(key = "#root.targetClass.getName()+'*'")
    @Async
    public void clearCache() {
    }

    /**
     * 缓存获取配置文件
     * @return
     */
    @Cacheable
    public Shop_config getShopConfig() {
        return this.fetch("system");
    }


    public boolean checkOnGoodsPublish() {
        Shop_config config = this.fetch("system");
        return !Lang.isEmpty(config) && config.isGoodsNeedCheck();
    }

}
