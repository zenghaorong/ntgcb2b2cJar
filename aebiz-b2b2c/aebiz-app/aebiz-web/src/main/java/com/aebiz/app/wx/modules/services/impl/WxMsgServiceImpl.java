package com.aebiz.app.wx.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.wx.modules.models.Wx_msg;
import com.aebiz.app.wx.modules.services.WxMsgService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WxMsgServiceImpl extends BaseServiceImpl<Wx_msg> implements WxMsgService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
