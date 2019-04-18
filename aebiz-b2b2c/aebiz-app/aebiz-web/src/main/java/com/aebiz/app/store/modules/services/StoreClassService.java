package com.aebiz.app.store.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.store.modules.models.Store_class;

public interface StoreClassService extends BaseService<Store_class>{
    void save(Store_class c, String pid);
    void deleteAndChild(Store_class c);
    String getClassOption();
}
