package com.aebiz.app.sys.modules.services;

import com.aebiz.app.sys.modules.models.Sys_menu;
import com.aebiz.app.sys.modules.models.Sys_role;
import com.aebiz.baseframework.base.service.BaseService;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface SysRoleService extends BaseService<Sys_role> {
    List<String> getPermissionNameList(Sys_role role);

    List<Sys_menu> getMenusAndButtons(String id);

    List<Sys_menu> getDatas(String id);

    void del(String roleid);

    void del(String[] roleids);
}
