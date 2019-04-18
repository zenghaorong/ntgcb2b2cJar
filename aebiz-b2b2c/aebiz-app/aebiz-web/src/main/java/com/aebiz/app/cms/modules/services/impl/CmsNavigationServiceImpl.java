package com.aebiz.app.cms.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.cms.modules.models.Cms_navigation;
import com.aebiz.app.cms.modules.services.CmsNavigationService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsNavigationServiceImpl extends BaseServiceImpl<Cms_navigation> implements CmsNavigationService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    /**
     * 获取所有发布的频道
     */
    public List<Cms_navigation> getPublishedChannel(){
        return this.query(Cnd.where("disabled","=",true));
    }
}
