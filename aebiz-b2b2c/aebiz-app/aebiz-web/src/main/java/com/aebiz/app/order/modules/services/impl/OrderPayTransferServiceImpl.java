package com.aebiz.app.order.modules.services.impl;

import com.aebiz.app.order.modules.models.Order_pay_transfer;
import com.aebiz.app.order.modules.services.OrderPayTransferService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderPayTransferServiceImpl extends BaseServiceImpl<Order_pay_transfer> implements OrderPayTransferService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
