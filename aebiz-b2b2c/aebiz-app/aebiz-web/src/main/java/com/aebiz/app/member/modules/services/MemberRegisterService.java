package com.aebiz.app.member.modules.services;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.member.modules.models.Member_user;
import com.aebiz.app.msg.modules.models.Msg_conf_sms;
import com.aebiz.baseframework.base.service.BaseService;

public interface MemberRegisterService extends BaseService<Member_user>{

    public void memberRegister(String mobile, String password,String username,String passwordStrength);
}

