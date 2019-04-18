package com.aebiz.app.store.modules.services.impl;

import com.aebiz.app.store.modules.models.Store_logistics_shipping_method;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.store.modules.models.Store_logistics_mode;
import com.aebiz.app.store.modules.services.StoreLogisticsModeService;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StoreLogisticsModeServiceImpl extends BaseServiceImpl<Store_logistics_mode> implements StoreLogisticsModeService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

}
