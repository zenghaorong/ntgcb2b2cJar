package com.aebiz.app.msg.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.msg.modules.models.Msg_send_msg;

public interface MsgSendMsgService extends BaseService<Msg_send_msg>{


    /**
     * 保存发送的消息
     * @param msgSendMsg
     */
    void saveInfoMsg(Msg_send_msg msgSendMsg);

    /**
     * 保存发送的消息
     * @param msgSendMsg
     */
    void saveInfoMsg(Msg_send_msg msgSendMsg,String infoId);

}
