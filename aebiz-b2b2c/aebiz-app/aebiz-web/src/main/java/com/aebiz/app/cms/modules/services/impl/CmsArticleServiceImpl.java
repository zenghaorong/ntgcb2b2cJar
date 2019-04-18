package com.aebiz.app.cms.modules.services.impl;

import com.aebiz.app.cms.modules.models.Cms_article;
import com.aebiz.app.cms.modules.services.CmsArticleService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.baseframework.page.Pagination;
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
@CacheConfig(cacheNames = "cmsCache")
public class CmsArticleServiceImpl extends BaseServiceImpl<Cms_article> implements CmsArticleService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @CacheEvict(key = "#root.targetClass.getName()+'*'")
    @Async
    public void clearCache() {

    }
    /**
     * 按照文章所属栏目信息查询文章信息列表
     */
    @Override
    @Cacheable
    public List<Cms_article> list(String channelId) {
        Cnd cnd=Cnd.NEW();
        cnd.and("disabled","=",false);
        if(!Strings.isEmpty(channelId)){
            cnd.and("channelId","=",channelId);
        }
        cnd.asc("location");
        return this.query(cnd);
    }
    /**
     * 按照文章主键信息查询文章内容
     */
    @Cacheable
    @Override
    public Cms_article get(String id) {
        return this.fetch(id);
    }


    /**
     * 按照文章所属栏目信息分页查询文章信息
     */
    @Override
    @Cacheable
    public Pagination listPage(String pageNumber, String pageSize, String channelId) {
        Cnd cnd=Cnd.NEW();
        cnd.and("disabled","=",false);
        if(!Strings.isEmpty(channelId)){
            cnd.and("channelId","=",channelId);
        }
        cnd.asc("location");
        return  this.listPage(Integer.parseInt(pageNumber),Integer.parseInt(pageSize),cnd);
    }
}
