package com.aebiz.app.member.modules.services;

import com.aebiz.app.member.modules.models.Member_account_money;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;

import java.util.List;

public interface MemberAccountMoneyService extends BaseService<Member_account_money>{
    NutMap moneyInfo(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName);
    void updateMoney(Member_account_money memberAccountMoney, String accountId);
}
