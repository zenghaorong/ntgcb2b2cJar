package com.aebiz.app.cms.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.cms.modules.models.Cms_site;

public interface CmsSiteService extends BaseService<Cms_site>{
    public void clearCache();
    public Cms_site getCmsSite(String id);
}
