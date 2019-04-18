package com.aebiz.app.sys.modules.services.impl;

import com.aebiz.app.sys.modules.models.Sys_task;
import com.aebiz.app.sys.modules.services.SysTaskService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wizzer on 2016/12/22.
 */
@Service
public class SysTaskServiceImpl extends BaseServiceImpl<Sys_task> implements SysTaskService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
