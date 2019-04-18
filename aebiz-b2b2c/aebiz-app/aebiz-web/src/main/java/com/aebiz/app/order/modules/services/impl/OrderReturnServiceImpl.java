package com.aebiz.app.order.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.order.modules.models.Order_return;
import com.aebiz.app.order.modules.services.OrderReturnService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderReturnServiceImpl extends BaseServiceImpl<Order_return> implements OrderReturnService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
