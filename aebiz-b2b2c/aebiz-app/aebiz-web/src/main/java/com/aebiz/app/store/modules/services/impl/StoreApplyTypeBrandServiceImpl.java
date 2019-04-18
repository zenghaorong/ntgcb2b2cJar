package com.aebiz.app.store.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.store.modules.models.Store_apply_type_brand;
import com.aebiz.app.store.modules.services.StoreApplyTypeBrandService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StoreApplyTypeBrandServiceImpl extends BaseServiceImpl<Store_apply_type_brand> implements StoreApplyTypeBrandService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
