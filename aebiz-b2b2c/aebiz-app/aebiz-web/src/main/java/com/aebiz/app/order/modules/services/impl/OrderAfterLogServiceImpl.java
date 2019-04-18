package com.aebiz.app.order.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.order.modules.models.Order_after_log;
import com.aebiz.app.order.modules.services.OrderAfterLogService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderAfterLogServiceImpl extends BaseServiceImpl<Order_after_log> implements OrderAfterLogService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
