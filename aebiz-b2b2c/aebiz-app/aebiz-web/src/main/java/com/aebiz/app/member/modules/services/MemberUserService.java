package com.aebiz.app.member.modules.services;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.member.modules.models.Member_user;
import com.aebiz.baseframework.base.service.BaseService;

public interface MemberUserService extends BaseService<Member_user>{
    void addMemberUser(Member_user memberUser, Account_user accountUser, Account_info accountInfo);
    void updateMemberUser(Member_user memberUser, Account_user accountUser, Account_info accountInfo);
    boolean checkUnique(String fieldName,String fieldValue);

    /**
     * 根据手机号注册成会员
     *
     * @param mobile 手机号
     * @param password 密码
     * @param username 用户名
     *
     * @return true 注册成功，false 注册失败
     */
    boolean memberRegister(String mobile,String password,String username);
}
