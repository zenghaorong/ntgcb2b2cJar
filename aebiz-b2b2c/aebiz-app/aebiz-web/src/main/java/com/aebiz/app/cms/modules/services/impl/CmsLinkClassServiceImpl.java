package com.aebiz.app.cms.modules.services.impl;

import com.aebiz.app.cms.modules.models.Cms_link_class;
import com.aebiz.app.cms.modules.services.CmsLinkClassService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@CacheConfig(cacheNames = "cmsCache")
public class CmsLinkClassServiceImpl extends BaseServiceImpl<Cms_link_class> implements CmsLinkClassService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @CacheEvict(key = "#root.targetClass.getName()+'*'")
    @Async
    public void clearCache() {

    }



    /**
     * 根据parentId查询
     */
    @Cacheable
    public List<Cms_link_class> list(){
        return this.query(Cnd.NEW());
    }


}
