package com.aebiz.app.cms.modules.services;

import com.aebiz.app.cms.modules.models.Cms_channel;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.baseframework.page.Pagination;

import java.util.List;

public interface CmsChannelService extends BaseService<Cms_channel>{
	void deleteAndChild(Cms_channel dict);

	void save(Cms_channel cmsChannel, String parentId);
	void clearCache();
	List<Cms_channel> list( String parentId);
	Cms_channel get(String id);
	Pagination listPage(String pageNumber, String pageSize, String parentId);
	void editDo(Cms_channel cmsChannel);
}
