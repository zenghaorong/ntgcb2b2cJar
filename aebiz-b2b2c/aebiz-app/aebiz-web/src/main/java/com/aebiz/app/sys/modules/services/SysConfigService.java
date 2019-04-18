package com.aebiz.app.sys.modules.services;

import com.aebiz.app.sys.modules.models.Sys_config;
import com.aebiz.baseframework.base.service.BaseService;

import java.util.List;

/**
 * Created by wizzer on 2016/12/23.
 */
public interface SysConfigService extends BaseService<Sys_config> {
    List<Sys_config> getAllList();
}
