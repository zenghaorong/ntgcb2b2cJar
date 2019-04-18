package com.aebiz.app.sys.modules.services;

import com.aebiz.app.sys.modules.models.Sys_menu;
import com.aebiz.app.sys.modules.models.Sys_user;
import com.aebiz.baseframework.base.service.BaseService;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface SysUserService extends BaseService<Sys_user> {
    List<String> getRoleCodeList(Sys_user user);
    void fillMenu(Sys_user user);

    void deleteById(String userId);

    void deleteByIds(String[] userIds);

    List<Sys_menu> getMenusAndButtons(String id);

    List<Sys_menu> getDatas(String id);
}
