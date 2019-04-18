package com.aebiz.app.order.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.order.modules.models.Order_pay_refunds;
import com.aebiz.app.order.modules.services.OrderPayRefundsService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderPayRefundsServiceImpl extends BaseServiceImpl<Order_pay_refunds> implements OrderPayRefundsService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
