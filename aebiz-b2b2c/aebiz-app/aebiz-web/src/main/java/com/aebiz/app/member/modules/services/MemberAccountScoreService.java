package com.aebiz.app.member.modules.services;

import com.aebiz.app.member.modules.models.Member_account_score;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;

import java.util.List;
import java.util.Map;

public interface MemberAccountScoreService extends BaseService<Member_account_score>{
    NutMap viewScoreInfo(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName);
    void updateScore(Member_account_score memberAccountScore, String accountId);

    Map<String,Object> selectDataAll(String accountId, Integer page, Integer rows, Integer status);

}
