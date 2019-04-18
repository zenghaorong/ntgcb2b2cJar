package com.aebiz.app.msg.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.msg.modules.models.Msg_conf_email;

public interface MsgConfEmailService extends BaseService<Msg_conf_email>{
    void clearCache();
    Msg_conf_email getConfEmail();
}
