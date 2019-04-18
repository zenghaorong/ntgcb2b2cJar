package com.aebiz.app.wx.modules.services.impl;

import com.aebiz.app.wx.modules.models.Wx_reply;
import com.aebiz.app.wx.modules.models.Wx_reply_txt;
import com.aebiz.app.wx.modules.services.WxReplyService;
import com.aebiz.app.wx.modules.services.WxReplyTxtService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WxReplyServiceImpl extends BaseServiceImpl<Wx_reply> implements WxReplyService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
