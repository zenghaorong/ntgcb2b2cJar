package com.aebiz.app.msg.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.msg.modules.models.Msg_info_tpl;
import com.aebiz.app.msg.modules.services.MsgInfoTplService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MsgInfoTplServiceImpl extends BaseServiceImpl<Msg_info_tpl> implements MsgInfoTplService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
