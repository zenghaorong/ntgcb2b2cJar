package com.aebiz.app.msg.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.msg.modules.models.Msg_conf_sms_tpl;
import com.aebiz.app.msg.modules.services.MsgConfSmsTplService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MsgConfSmsTplServiceImpl extends BaseServiceImpl<Msg_conf_sms_tpl> implements MsgConfSmsTplService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
