package com.aebiz.app.cms.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.cms.modules.models.Cms_navigation;

import java.util.List;

public interface CmsNavigationService extends BaseService<Cms_navigation>{

    /**
     * 获取所有发布的频道
     */
    public List<Cms_navigation> getPublishedChannel();
}
