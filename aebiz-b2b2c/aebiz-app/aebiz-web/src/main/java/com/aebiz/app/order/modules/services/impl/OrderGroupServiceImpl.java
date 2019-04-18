package com.aebiz.app.order.modules.services.impl;

import com.aebiz.app.order.modules.models.Order_group;
import com.aebiz.app.order.modules.services.OrderGroupService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderGroupServiceImpl extends BaseServiceImpl<Order_group> implements OrderGroupService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
