package com.aebiz.app.store.modules.services;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.store.modules.models.Store_user;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public interface StoreUserService extends BaseService<Store_user> {
    void fillMenu(Store_user user);

    List<String> getRoleCodeList(Store_user user);

    Store_user getUser(String accountId);

    void addDo(Account_info accountInfo, Account_user accountUser);
}
