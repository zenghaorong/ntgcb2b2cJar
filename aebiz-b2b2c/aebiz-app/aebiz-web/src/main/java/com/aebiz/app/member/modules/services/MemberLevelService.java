package com.aebiz.app.member.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.member.modules.models.Member_level;

public interface MemberLevelService extends BaseService<Member_level>{
    /**
     * 修改默認值
     * @param memberlevel
     */
     void updateDefault(Member_level memberlevel);

     void updateLevelDefaultByTypeId(Member_level member_level);

}
