package com.aebiz.app.cms.modules.services.impl;

import com.aebiz.app.cms.modules.models.Cms_site;
import com.aebiz.app.cms.modules.services.CmsSiteService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@CacheConfig(cacheNames = "cmsCache")
public class CmsSiteServiceImpl extends BaseServiceImpl<Cms_site> implements CmsSiteService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    /**
     * 清除站点缓存
     */
    @CacheEvict(key = "#root.targetClass.getName()+'*'")
    @Async
    public void clearCache() {

    }

    /**
     * 根据ID获取站点信息，ID为空则获取默认站点
     * @return 实体类
     */
    @Cacheable
    public  Cms_site getCmsSite(String id ){
        if (Strings.isEmpty(id)){
            id="site";
        }
        return this.fetch(id);
    }
}
