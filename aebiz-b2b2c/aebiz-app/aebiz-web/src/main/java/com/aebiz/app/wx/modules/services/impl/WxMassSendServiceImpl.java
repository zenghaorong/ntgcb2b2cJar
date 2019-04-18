package com.aebiz.app.wx.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.wx.modules.models.Wx_mass_send;
import com.aebiz.app.wx.modules.services.WxMassSendService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WxMassSendServiceImpl extends BaseServiceImpl<Wx_mass_send> implements WxMassSendService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
