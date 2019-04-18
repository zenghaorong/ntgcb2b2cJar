package com.aebiz.app.msg.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 电子邮件配置表
 * Created by wizzer on 2017/2/21.
 */
@Table("msg_conf_email")
public class Msg_conf_email extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("发送方式")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String sendType;//mail本服务器 smtp外部服务

    @Column
    @Comment("发件人邮箱")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String sendEmail;

    @Column
    @Comment("smtp服务器地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String smtpUrl;

    @Column
    @Comment("smtp服务器端口")
    @ColDefine(type = ColType.INT)
    private Integer smtpPort;

    @Column
    @Comment("smtp用户名")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String smtpUsername;


    @Column
    @Comment("smtp密码")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String smtpPassword;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(String sendEmail) {
        this.sendEmail = sendEmail;
    }

    public String getSmtpUrl() {
        return smtpUrl;
    }

    public void setSmtpUrl(String smtpUrl) {
        this.smtpUrl = smtpUrl;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getSmtpUsername() {
        return smtpUsername;
    }

    public void setSmtpUsername(String smtpUsername) {
        this.smtpUsername = smtpUsername;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }
}
