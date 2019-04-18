package com.aebiz.app.wx.modules.services.impl;

import com.aebiz.app.wx.modules.models.Wx_tpl_id;
import com.aebiz.app.wx.modules.services.WxTplIdService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WxTplIdServiceImpl extends BaseServiceImpl<Wx_tpl_id> implements WxTplIdService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
