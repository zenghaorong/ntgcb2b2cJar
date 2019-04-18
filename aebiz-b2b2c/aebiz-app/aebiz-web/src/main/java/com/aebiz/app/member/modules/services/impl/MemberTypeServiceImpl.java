package com.aebiz.app.member.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.member.modules.models.Member_type;
import com.aebiz.app.member.modules.services.MemberTypeService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MemberTypeServiceImpl extends BaseServiceImpl<Member_type> implements MemberTypeService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    public List<Member_type> getMemberTypeList() {
        return this.query(Cnd.where("delFlag", "=", false).asc("id"));
    }
}
