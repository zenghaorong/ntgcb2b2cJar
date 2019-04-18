package com.aebiz.app.store.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.store.modules.models.Store_type;

public interface StoreTypeService extends BaseService<Store_type> {
    void add(Store_type storeRole);

    void edit(Store_type storeRole);
}
