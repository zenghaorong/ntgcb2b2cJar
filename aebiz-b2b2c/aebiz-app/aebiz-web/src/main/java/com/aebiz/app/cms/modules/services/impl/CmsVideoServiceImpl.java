package com.aebiz.app.cms.modules.services.impl;

import com.aebiz.app.cms.modules.models.Cms_site;
import com.aebiz.app.cms.modules.models.Cms_video;
import com.aebiz.app.cms.modules.services.CmsSiteService;
import com.aebiz.app.cms.modules.services.CmsVideoService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Auther: zenghaorong
 * @Date: 2019/2/28  21:42
 * @Description:
 */
@Service
public class CmsVideoServiceImpl extends BaseServiceImpl<Cms_video> implements CmsVideoService {


    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }


}
