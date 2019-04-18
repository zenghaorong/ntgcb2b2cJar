package com.aebiz.app.wx.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.wx.modules.models.Wx_msg_reply;
import com.aebiz.app.wx.modules.services.WxMsgReplyService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WxMsgReplyServiceImpl extends BaseServiceImpl<Wx_msg_reply> implements WxMsgReplyService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
