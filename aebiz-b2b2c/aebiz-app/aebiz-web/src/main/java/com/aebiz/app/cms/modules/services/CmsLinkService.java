package com.aebiz.app.cms.modules.services;

import com.aebiz.app.cms.modules.models.Cms_link;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.baseframework.page.Pagination;

import java.util.List;

public interface CmsLinkService extends BaseService<Cms_link>{
    void clearCache();
    List<Cms_link> list(String classId);
    Cms_link get(String id);
    Pagination listPage(String pageNumber, String pageSize, String channelId);
}
