package com.aebiz.app.shop.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.shop.modules.models.Shop_payment;
import com.aebiz.app.shop.modules.services.ShopPaymentService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ShopPaymentServiceImpl extends BaseServiceImpl<Shop_payment> implements ShopPaymentService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
