package com.aebiz.app.store.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.store.modules.models.Store_freight_rulesdetail;
import com.aebiz.app.store.modules.services.StoreFreightRulesdetailService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StoreFreightRulesdetailServiceImpl extends BaseServiceImpl<Store_freight_rulesdetail> implements StoreFreightRulesdetailService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
