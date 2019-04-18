package com.aebiz.app.sys.modules.services;

import com.aebiz.app.sys.modules.models.Sys_menu;
import com.aebiz.baseframework.base.service.BaseService;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface SysMenuService extends BaseService<Sys_menu> {

    Sys_menu save(Sys_menu sysMenu);

    Sys_menu saveData(Sys_menu sysMenu);

    void deleteAndChild(Sys_menu sysMenu);

    Sys_menu addChildData(String pid, String name, String permission,String system);
}
