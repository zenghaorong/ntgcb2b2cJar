package com.aebiz.app.msg.modules.services.impl;

import com.aebiz.app.msg.modules.models.Msg_send;
import com.aebiz.app.msg.modules.models.Msg_send_msg;
import com.aebiz.app.sys.modules.models.Sys_user;
import com.aebiz.app.sys.modules.services.SysUserService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.msg.modules.models.Msg_receive_msg;
import com.aebiz.app.msg.modules.services.MsgReceiveMsgService;
import org.nutz.aop.interceptor.async.Async;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MsgReceiveMsgServiceImpl extends BaseServiceImpl<Msg_receive_msg> implements MsgReceiveMsgService {

    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private SysUserService sysUserService;


    //异步方法接受 消息
    @Async
    @Override
    public void saveReviceMsg(Msg_send_msg msgSendMsg) {
        if (msgSendMsg.getSendAll()) {//所有人
            if("PLATFORM".equals(msgSendMsg.getSendTo())){
                List<Sys_user>  platUser = sysUserService.query(Cnd.NEW());
                if(platUser != null && platUser.size()>0){
                    for (Sys_user user : platUser){
                        Msg_receive_msg  obj = new Msg_receive_msg();
                        obj.setSendId(msgSendMsg.getSendId());
                        obj.setMsgId(msgSendMsg.getId());
                        obj.setReceiveId(user.getId());
                        obj.setHasRead(false);
                        obj.setType(msgSendMsg.getType());
                        this.dao().insert(obj);
                    }
                }
            }else if("STORE".equals(msgSendMsg.getSendTo())){
                //TODO 发送给所有商户
            }else if("MEMBER".equals(msgSendMsg.getSendTo())){
                //TODO 发送给所有会员
            }

        } else {//部分人
            if("PLATFORM".equals(msgSendMsg.getSendTo())){
                for (Msg_receive_msg receive_msg : msgSendMsg.getReceiveMsgs()) {
                    receive_msg.setSendId(msgSendMsg.getSendId());
                    receive_msg.setMsgId(msgSendMsg.getId());
                    receive_msg.setHasRead(false);
                    receive_msg.setType(msgSendMsg.getType());
                    this.dao().insert(receive_msg);
                }
            }else if("STORE".equals(msgSendMsg.getSendTo())){
                //TODO 发送给所有商户
            }else if("MEMBER".equals(msgSendMsg.getSendTo())){
                //TODO 发送给所有会员
            }
        }
    }
}
