package com.aebiz.app.store.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.store.modules.models.Store_menu;

public interface StoreMenuService extends BaseService<Store_menu> {
    Store_menu save(Store_menu sysMenu);

    Store_menu saveData(Store_menu sysMenu);

    void deleteAndChild(Store_menu sysMenu);

    Store_menu addChildData(String pid, String name, String permission, String system);
}
