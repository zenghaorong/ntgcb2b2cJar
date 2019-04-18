package com.aebiz.app.msg.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.msg.modules.models.Msg_send;
import com.aebiz.app.msg.modules.services.MsgSendService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MsgSendServiceImpl extends BaseServiceImpl<Msg_send> implements MsgSendService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
