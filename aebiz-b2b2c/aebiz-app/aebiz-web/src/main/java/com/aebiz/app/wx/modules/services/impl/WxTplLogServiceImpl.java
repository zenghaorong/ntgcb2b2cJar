package com.aebiz.app.wx.modules.services.impl;

import com.aebiz.app.wx.modules.models.Wx_tpl_log;
import com.aebiz.app.wx.modules.services.WxTplLogService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WxTplLogServiceImpl extends BaseServiceImpl<Wx_tpl_log> implements WxTplLogService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
