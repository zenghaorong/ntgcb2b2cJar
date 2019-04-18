package com.aebiz.app.shop.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.shop.modules.models.Shop_currency;
import com.aebiz.app.shop.modules.services.ShopCurrencyService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@CacheConfig(cacheNames = "shopCache")
public class ShopCurrencyServiceImpl extends BaseServiceImpl<Shop_currency> implements ShopCurrencyService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    /**
     * 清除货币缓存
     */
    @CacheEvict(key = "#root.targetClass.getName()+'*'")
    @Async
    public void clearCache() {

    }

    /**
     * 设置默认货币
     * @param shopCurrency 实体类
     */
    public void updateDefaultCurrency(Shop_currency shopCurrency) {
        dao().execute(Sqls.create("update shop_currency set defaultValue=0 where id <> @id").setParam("id", shopCurrency.getId()));
    }

    /**
     * 获取默认货币
     * @return 实体类
     */
    @Cacheable
    public Shop_currency getDefaultCurrency() {
        return this.fetch(Cnd.where("defaultValue", "=", true));
    }
}
