package com.aebiz.app.order.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.order.modules.models.Order_delivery_detail;
import com.aebiz.app.order.modules.services.OrderDeliveryDetailService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderDeliveryDetailServiceImpl extends BaseServiceImpl<Order_delivery_detail> implements OrderDeliveryDetailService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
