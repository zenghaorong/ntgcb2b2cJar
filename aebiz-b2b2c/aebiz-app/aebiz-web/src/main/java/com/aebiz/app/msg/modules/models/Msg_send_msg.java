package com.aebiz.app.msg.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 站内信发送表
 * Created by wizzer on 2017/2/21.
 */
@Table("msg_send_msg")
public class Msg_send_msg extends BaseModel implements Serializable {
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
    @Comment("标题")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String title;

    @Column
    @Comment("内容")
    @ColDefine(type = ColType.TEXT)
    private String note;

    @Many(field = "sendId")
    private List<Msg_receive_msg> receiveMsgs;

    @One(field = "sendId")
    private Msg_send msgSend;

    //消息类型
    private String type;//system=系统消息
    //发送消息类型
    private String sendType;// MSG,SMS,EMAIL
    //发送目标对象
    private String sendTo;
    //发送者
    private String sendFrom;
    //是否发送相应平台（平台，商户，会员）下的所有人
    private Boolean sendAll;

    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Msg_receive_msg> getReceiveMsgs() {
        return receiveMsgs;
    }

    public void setReceiveMsgs(List<Msg_receive_msg> receiveMsgs) {
        this.receiveMsgs = receiveMsgs;
    }

    public Msg_send getMsgSend() {
        return msgSend;
    }

    public void setMsgSend(Msg_send msgSend) {
        this.msgSend = msgSend;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public Boolean getSendAll() {
        return sendAll;
    }

    public void setSendAll(Boolean sendAll) {
        this.sendAll = sendAll;
    }
}
