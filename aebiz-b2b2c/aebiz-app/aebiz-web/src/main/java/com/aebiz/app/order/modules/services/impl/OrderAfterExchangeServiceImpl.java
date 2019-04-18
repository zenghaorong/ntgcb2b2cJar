package com.aebiz.app.order.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.order.modules.models.Order_after_exchange;
import com.aebiz.app.order.modules.services.OrderAfterExchangeService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderAfterExchangeServiceImpl extends BaseServiceImpl<Order_after_exchange> implements OrderAfterExchangeService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
