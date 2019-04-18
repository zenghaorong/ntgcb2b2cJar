package com.aebiz.app.member.modules.services.impl;

import com.aebiz.app.member.modules.models.Member_level;
import com.aebiz.app.member.modules.services.MemberLevelService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MemberLevelServiceImpl extends BaseServiceImpl<Member_level> implements MemberLevelService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    public void updateDefault(Member_level memberlevel){
        dao().execute(Sqls.create("update Member_level set defaultValue=0 where id <> @id").setParam("id", memberlevel.getId()));
    }

    @Override
    public void updateLevelDefaultByTypeId(Member_level memberLevel) {
        boolean isDefault = memberLevel.isDefaultValue();
        if (isDefault){
            this.update(Chain.make("defaultValue",0), Cnd.where("typeId","=",memberLevel.getTypeId()).and("id","!=",memberLevel.getId()));
        }
    }

}
