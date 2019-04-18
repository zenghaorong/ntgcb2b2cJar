package com.aebiz.app.msg.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.msg.modules.models.Msg_type;
import com.aebiz.app.msg.modules.services.MsgTypeService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MsgTypeServiceImpl extends BaseServiceImpl<Msg_type> implements MsgTypeService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
