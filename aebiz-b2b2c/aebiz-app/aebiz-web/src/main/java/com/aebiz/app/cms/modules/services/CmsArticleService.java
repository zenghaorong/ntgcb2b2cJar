package com.aebiz.app.cms.modules.services;

import com.aebiz.app.cms.modules.models.Cms_article;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.baseframework.page.Pagination;

import java.util.List;

public interface CmsArticleService extends BaseService<Cms_article>{
    void clearCache();
    List<Cms_article> list(String channelId);
    Cms_article get(String id);
    Pagination listPage(String pageNumber, String pageSize, String channelId);
}
