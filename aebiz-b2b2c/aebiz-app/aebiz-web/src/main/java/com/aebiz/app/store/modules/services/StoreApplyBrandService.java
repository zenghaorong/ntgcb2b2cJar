package com.aebiz.app.store.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.store.modules.models.Store_apply_brand;

public interface StoreApplyBrandService extends BaseService<Store_apply_brand>{

    void add(Store_apply_brand storeApplyBrand, String[] type);

    void edit(Store_apply_brand storeApplyBrand, String[] type);

    void verify(Store_apply_brand storeApplyBrand);
}
