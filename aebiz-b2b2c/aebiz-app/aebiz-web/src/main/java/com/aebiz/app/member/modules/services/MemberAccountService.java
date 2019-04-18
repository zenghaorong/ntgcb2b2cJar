package com.aebiz.app.member.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.member.modules.models.Member_account;

public interface MemberAccountService extends BaseService<Member_account>{

    /**
     * 获取会员账号评分[0,100]
     *
     * [0, 50) 弱
     * [50, 80) 中
     * [80, 100] 强
     *
     * @param accountId 会员ID
     * @return
     */
    int getSecurityScore(String accountId);

}
