package com.aebiz.app.order.modules.services.impl;

import com.aebiz.app.order.modules.models.Order_log;
import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.services.OrderLogService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderLogServiceImpl extends BaseServiceImpl<Order_log> implements OrderLogService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    /**
     * 创建订单日志
     * @param orderMain
     * @param note
     * @param behavior
     */
    public void createLog(Order_main orderMain,String opByName, String note, Integer behavior) {
        Order_log orderLog = new Order_log();
        orderLog.setOrderId(orderMain.getId());
        orderLog.setAccountId(orderMain.getAccountId());
        orderLog.setStoreId(orderMain.getStoreId());
        orderLog.setNote(note);
        orderLog.setBehavior(behavior);
        orderLog.setSuccess(true);
        orderLog.setOpBy(orderMain.getOpBy());
        orderLog.setOpByName(opByName);
        orderLog.setOpAt((int)(System.currentTimeMillis()/1000));
        orderLog.setDelFlag(false);
        this.insert(orderLog);
    }
}
