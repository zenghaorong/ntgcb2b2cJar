package com.aebiz.app.wx.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.wx.modules.models.Wx_config;
import org.nutz.weixin.spi.WxApi2;

public interface WxConfigService extends BaseService<Wx_config>{
    WxApi2 getWxApi2(String wxid);
}
