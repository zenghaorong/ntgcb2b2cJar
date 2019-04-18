package com.aebiz.app.order.modules.services.impl;

import com.aebiz.app.order.modules.models.Order_goods_discount;
import com.aebiz.app.order.modules.services.OrderGoodsDiscountService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderGoodsDiscountServiceImpl extends BaseServiceImpl<Order_goods_discount> implements OrderGoodsDiscountService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
