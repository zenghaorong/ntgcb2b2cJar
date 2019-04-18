package com.aebiz.app.shop.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.shop.modules.models.Shop_estemp;
import com.aebiz.app.shop.modules.services.ShopEstempService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ShopEstempServiceImpl extends BaseServiceImpl<Shop_estemp> implements ShopEstempService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
