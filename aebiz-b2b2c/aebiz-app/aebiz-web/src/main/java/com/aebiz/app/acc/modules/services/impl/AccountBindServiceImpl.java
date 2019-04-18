package com.aebiz.app.acc.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.acc.modules.models.Account_bind;
import com.aebiz.app.acc.modules.services.AccountBindService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AccountBindServiceImpl extends BaseServiceImpl<Account_bind> implements AccountBindService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
