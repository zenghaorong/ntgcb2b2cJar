package com.aebiz.app.order.modules.services;

import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.order.modules.models.Order_log;

public interface OrderLogService extends BaseService<Order_log>{

    /**
     * 创建订单日志
     * @param orderMain
     * @param note
     * @param behavior
     */
    void createLog(Order_main orderMain, String opByName ,String note, Integer behavior);

}
