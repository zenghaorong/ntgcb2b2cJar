package com.aebiz.app.msg.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.msg.modules.models.Msg_conf_sms;

public interface MsgConfSmsService extends BaseService<Msg_conf_sms>{

    Msg_conf_sms insertWith(Msg_conf_sms conf, String many);

    void save(Msg_conf_sms conf);

    void deleteWith(String id, String many);

}
