package com.aebiz.app.member.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.member.modules.models.Member_type;

import java.util.List;

public interface MemberTypeService extends BaseService<Member_type>{

    /**
     * 取会员类型list
     * @return
     */
    List<Member_type> getMemberTypeList();
}
