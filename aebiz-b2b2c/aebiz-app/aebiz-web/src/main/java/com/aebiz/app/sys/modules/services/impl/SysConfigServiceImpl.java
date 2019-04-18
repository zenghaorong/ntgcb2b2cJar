package com.aebiz.app.sys.modules.services.impl;

import com.aebiz.app.sys.modules.models.Sys_config;
import com.aebiz.app.sys.modules.services.SysConfigService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wizzer on 2016/12/23.
 */
@Service
public class SysConfigServiceImpl extends BaseServiceImpl<Sys_config> implements SysConfigService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    public List<Sys_config> getAllList() {
        return this.query(Cnd.where("delFlag", "=", false));
    }
}