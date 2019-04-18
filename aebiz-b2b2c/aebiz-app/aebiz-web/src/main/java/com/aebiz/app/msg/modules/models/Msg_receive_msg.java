package com.aebiz.app.msg.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 站内信接收表
 * Created by wizzer on 2017/2/21.
 */
@Table("msg_receive_msg")
public class Msg_receive_msg extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("发送ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String sendId;

    @Column
    @Comment("站内信ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String msgId;

    @Column
    @Comment("消息类型")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String type;

    @Column
    @Comment("接收人")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String receiveId;

    @Column
    @Comment("是否查看")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean hasRead;

    @Column
    @Comment("查看时间")
    @ColDefine(type = ColType.INT)
    private Integer readAt;

    @One(field = "sendId")
    private Msg_send msgSend;
    @One(field = "msgId")
    private Msg_send_msg msgSendMsg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    public Integer getReadAt() {
        return readAt;
    }

    public void setReadAt(Integer readAt) {
        this.readAt = readAt;
    }

    public Msg_send getMsgSend() {
        return msgSend;
    }

    public void setMsgSend(Msg_send msgSend) {
        this.msgSend = msgSend;
    }

    public Msg_send_msg getMsgSendMsg() {
        return msgSendMsg;
    }

    public void setMsgSendMsg(Msg_send_msg msgSendMsg) {
        this.msgSendMsg = msgSendMsg;
    }
}
