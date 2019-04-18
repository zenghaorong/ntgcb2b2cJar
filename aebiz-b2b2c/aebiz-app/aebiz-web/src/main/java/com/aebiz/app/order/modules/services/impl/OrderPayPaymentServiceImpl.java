package com.aebiz.app.order.modules.services.impl;

import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.models.Order_pay_payment;
import com.aebiz.app.order.modules.services.OrderPayPaymentService;
import com.aebiz.app.shop.modules.models.Shop_account;
import com.aebiz.app.shop.modules.services.ShopAccountService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderPayPaymentServiceImpl extends BaseServiceImpl<Order_pay_payment> implements OrderPayPaymentService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private ShopAccountService shopAccountService;

    @Override
    public void add(List<Order_pay_payment> orderPayPaymentList, Order_main orderMain,boolean isPaySuccess) {
        for (Order_pay_payment orderPayPayment : orderPayPaymentList) {
            orderPayPayment.setGroupId(orderMain.getGroupId());
            orderPayPayment.setOrderId(orderMain.getId());
            orderPayPayment.setStoreId(orderMain.getStoreId());
            //
            Shop_account shop_account = shopAccountService.fetch(orderPayPayment.getAccountId());
            if (shop_account != null) {
                orderPayPayment.setPayAccount(shop_account.getAccountName());
            }
            orderPayPayment.setPayAt((int) (System.currentTimeMillis() * 1000));
            if(isPaySuccess){
                orderPayPayment.setFinishAt((int) (System.currentTimeMillis() * 1000));
            }
            orderPayPayment.setPaySucess(isPaySuccess);
            orderPayPayment.setOpBy(StringUtil.getUsername());
            orderPayPayment.setOpAt((int) (System.currentTimeMillis() * 1000));
            orderPayPayment.setDelFlag(false);
            this.insert(orderPayPayment);
        }
    }
}
