package com.aebiz.app.cms.modules.services.impl;

import com.aebiz.app.cms.modules.models.Cms_link;
import com.aebiz.app.cms.modules.services.CmsLinkService;
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
public class CmsLinkServiceImpl extends BaseServiceImpl<Cms_link> implements CmsLinkService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @CacheEvict(key = "#root.targetClass.getName()+'*'")
    @Async
    public void clearCache() {

    }

    /**
     *
     * 按照链接类型查看所有链接信息
     */
    @Cacheable
    public List<Cms_link> list(String classId) {
        Cnd cnd=Cnd.NEW();
        if(!Strings.isEmpty(classId)){
            cnd.and("classId","=",classId);
        }
        cnd.asc("opAt");
        return this.query(cnd);
    }

    /**
     * 按照链接主键信息查询文章内容
     */
    @Cacheable

    public Cms_link get(String id) {
        return this.fetch(id);
    }


    /**
     * 按照链接所属栏目信息分页查询文章信息
     */

    @Cacheable
    public Pagination listPage(String pageNumber, String pageSize, String classId) {
        Cnd cnd=Cnd.NEW();
        if(!Strings.isEmpty(classId)){
            cnd.and("classId","=",classId);
        }
        cnd.asc("opAt");
        return  this.listPage(Integer.parseInt(pageNumber),Integer.parseInt(pageSize),cnd);
    }


}
