package com.aebiz.app.acc.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AccountInfoServiceImpl extends BaseServiceImpl<Account_info> implements AccountInfoService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
