package com.aebiz.app.wx.modules.services.impl;

import com.aebiz.app.wx.modules.models.Wx_tpl_list;
import com.aebiz.app.wx.modules.services.WxTplListService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WxTplListServiceImpl extends BaseServiceImpl<Wx_tpl_list> implements WxTplListService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
