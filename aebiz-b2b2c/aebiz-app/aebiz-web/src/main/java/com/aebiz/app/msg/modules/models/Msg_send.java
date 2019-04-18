package com.aebiz.app.msg.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 消息发送表(通知公告要支持撤回功能,即伪删除)
 * Created by wizzer on 2017/2/21.
 */
@Table("msg_send")
public class Msg_send extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public static enum SendToEnum {
        PLATFORM("PLATFORM"), STORE("STORE"), MEMBER("MEMBER");  // 接受者  平台，商户 ,会员

        private final String name;

        SendToEnum(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static enum SendFromEnum {
        PLATFORM, ADMIN  //发送者    平台，管理员
    }

    public static enum SendTypeEnum{
        MSG("MSG"),SMS("SMS"),EMAIL("EMAIL");

        private final String name;

        SendTypeEnum(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Column
    @Name
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("消息类型")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String type;//system=系统消息

    @Column
    @Comment("系统消息ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String infoId;

    @Column
    @Comment("发送目标对象")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String sendTo;//platform  store  member

    @Column
    @Comment("发送者")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String sendFrom;//platform  store  member

    @Column
    @Comment("发送者ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String sendBy;//id

    @Column
    @Comment("发送时间")
    @Prev(els = @EL("$me.now()"))
    @ColDefine(type = ColType.INT)
    private Integer sendAt;//id

    @Column
    @Comment("发送消息类型")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String sendType;

    @Column
    @Comment("是否发送成功")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean sendResult;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    public Integer getSendAt() {
        return sendAt;
    }

    public void setSendAt(Integer sendAt) {
        this.sendAt = sendAt;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public boolean isSendResult() {
        return sendResult;
    }

    public void setSendResult(boolean sendResult) {
        this.sendResult = sendResult;
    }
}
