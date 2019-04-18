package com.aebiz.app.shop.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.shop.modules.models.Shop_account;
import com.aebiz.app.shop.modules.services.ShopAccountService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ShopAccountServiceImpl extends BaseServiceImpl<Shop_account> implements ShopAccountService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
