package com.aebiz.app.store.modules.services;

import com.aebiz.app.store.modules.models.Store_menu;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.store.modules.models.Store_role;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface StoreRoleService extends BaseService<Store_role> {
    void add(Store_role storeRole);

    void edit(Store_role storeRole);

    List<String> getPermissionNameList(Store_role role);

    void del(String roleid);

    List<Store_menu> getMenusAndButtons(String roleId);

    List<Store_menu> getDatas(String id);
    void editMenuDo(String ids,String roleid);
}
