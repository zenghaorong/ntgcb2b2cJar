package com.aebiz.app.order.modules.services.impl;

import com.aebiz.app.order.modules.models.Order_main_discount;
import com.aebiz.app.order.modules.services.OrderMainDiscountService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderMainDiscountServiceImpl extends BaseServiceImpl<Order_main_discount> implements OrderMainDiscountService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
