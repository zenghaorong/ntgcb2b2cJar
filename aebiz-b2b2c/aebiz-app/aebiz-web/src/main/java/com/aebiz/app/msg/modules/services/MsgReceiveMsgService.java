package com.aebiz.app.msg.modules.services;

import com.aebiz.app.msg.modules.models.Msg_send_msg;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.msg.modules.models.Msg_receive_msg;
import org.nutz.aop.interceptor.async.Async;

public interface MsgReceiveMsgService extends BaseService<Msg_receive_msg>{

    //异步方法保存
    public void saveReviceMsg(Msg_send_msg msgSendMsg);

}
