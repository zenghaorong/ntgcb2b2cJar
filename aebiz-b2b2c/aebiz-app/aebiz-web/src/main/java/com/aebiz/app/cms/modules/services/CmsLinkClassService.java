package com.aebiz.app.cms.modules.services;

import com.aebiz.app.cms.modules.models.Cms_link_class;
import com.aebiz.baseframework.base.service.BaseService;

import java.util.List;

public interface CmsLinkClassService extends BaseService<Cms_link_class>{
    void clearCache();
    List<Cms_link_class> list();
}
