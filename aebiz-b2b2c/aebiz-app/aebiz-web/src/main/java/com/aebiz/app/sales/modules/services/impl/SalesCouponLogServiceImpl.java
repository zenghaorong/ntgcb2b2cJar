package com.aebiz.app.sales.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.sales.modules.models.Sales_coupon_log;
import com.aebiz.app.sales.modules.services.SalesCouponLogService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SalesCouponLogServiceImpl extends BaseServiceImpl<Sales_coupon_log> implements SalesCouponLogService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
