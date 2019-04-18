package com.aebiz.app.goods.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.goods.modules.models.Goods_spec_values;
import com.aebiz.app.goods.modules.services.GoodsSpecValuesService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@CacheConfig(cacheNames = "goodsCache")
public class GoodsSpecValuesServiceImpl extends BaseServiceImpl<Goods_spec_values> implements GoodsSpecValuesService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @CacheEvict(key = "#root.targetClass.getName()+'*'")
    @Async
    public void clearCache() {

    }

    @Cacheable
    public List<Goods_spec_values> getValueBySpecId(String specId) {
        Cnd cnd=Cnd.NEW();
        if(!Strings.isEmpty(specId)){
            cnd.and("specId","=",specId);
        }

        cnd.asc("opAt");
        return this.query(cnd);
    }
}
