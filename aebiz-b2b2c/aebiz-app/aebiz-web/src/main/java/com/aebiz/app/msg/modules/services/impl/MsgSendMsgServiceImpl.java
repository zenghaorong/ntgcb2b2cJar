package com.aebiz.app.msg.modules.services.impl;

import com.aebiz.app.msg.modules.models.Msg_send;
import com.aebiz.app.msg.modules.models.Msg_send_msg;
import com.aebiz.app.msg.modules.models.Msg_type;
import com.aebiz.app.msg.modules.services.MsgReceiveMsgService;
import com.aebiz.app.msg.modules.services.MsgSendMsgService;
import com.aebiz.app.msg.modules.services.MsgSendService;
import com.aebiz.app.msg.modules.services.MsgTypeService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class MsgSendMsgServiceImpl extends BaseServiceImpl<Msg_send_msg> implements MsgSendMsgService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private MsgTypeService msgTypeService;
    @Autowired
    private MsgSendService msgSendService;
    @Autowired
    private MsgReceiveMsgService msgReceiveMsgService;
    /**
     * 保存发送信息，保存信息详情，保存（关联消息代码）
     * @param msgSendMsg
     */
    @Transactional
    @Override
    public void saveInfoMsg(Msg_send_msg msgSendMsg,String infoId) {
            String localuid  = "PLATFORM";
            if(!"PLATFORM".equals(msgSendMsg.getSendFrom())){
                localuid = StringUtil.getUid();
            }
            Msg_send msgSend = new Msg_send();
            //消息类型
            Msg_type msgType = msgTypeService.fetch(Cnd.where("code","=",msgSendMsg.getType()));
            msgSend.setInfoId(infoId);
            msgSend.setType(msgType.getName());
            msgSend.setSendType(msgSendMsg.getSendType());
            msgSend.setSendTo(msgSendMsg.getSendTo());
            msgSend.setSendFrom(msgSendMsg.getSendFrom());
            msgSend.setSendBy(localuid);
            msgSend.setSendResult(true);
            msgSendService.insert(msgSend);
            msgSendMsg.setMsgSend(msgSend);

            msgSendMsg.setSendId(msgSend.getId());
            this.dao().insert(msgSendMsg);
            //将消息发送给接受者
            msgReceiveMsgService.saveReviceMsg(msgSendMsg);
    }


    /**
     * 保存发送信息，保存信息详情，保存(不关联消息代码)
     * @param msgSendMsg
     */
    @Transactional
    @Override
    public void saveInfoMsg(Msg_send_msg msgSendMsg) {
        String localuid  = "PLATFORM";
        if(!"PLATFORM".equals(msgSendMsg.getSendFrom())){
            localuid = StringUtil.getUid();
        }
        Msg_send msgSend = new Msg_send();
        //消息类型
        Msg_type msgType = msgTypeService.fetch(Cnd.where("code","=",msgSendMsg.getType()));
        msgSend.setType(msgType.getName());
        msgSend.setSendType(msgSendMsg.getSendType());
        msgSend.setSendTo(msgSendMsg.getSendTo());
        msgSend.setSendFrom(msgSendMsg.getSendFrom());
        msgSend.setSendBy(localuid);
        msgSend.setSendResult(true);
        msgSendService.insert(msgSend);
        msgSendMsg.setMsgSend(msgSend);

        msgSendMsg.setSendId(msgSend.getId());
        this.dao().insert(msgSendMsg);
        //将消息发送给接受者
        msgReceiveMsgService.saveReviceMsg(msgSendMsg);
    }
}
