package com.aebiz.app.acc.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 登录日志表
 * Created by wizzer on 2017/3/5.
 */
@Table("account_login")
@TableIndexes({@Index(name = "INDEX_ACCOUNT_LOGIN", fields = {"accountId"}, unique = false)})
public class Account_login extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("IP地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String ip;

    @Column
    @Comment("登录类型")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String loginType;//member 登录会员 store 登录商户

    @Column
    @Comment("客户端类型")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String clientType;//windows mac android  ios

    @Column
    @Comment("客户端名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String clientName;

    @Column
    @Comment("浏览器版本")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String clientBrowser;

    @Column
    @Comment("客户端版本")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String clientVersion;

    @Column
    @Comment("登录时间")
    @Prev(els = @EL("$me.now()"))
    @ColDefine(type = ColType.INT)
    private Integer loginAt;

    @Column
    @Comment("帐号ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String accountId;

    @One(target = Account_info.class, field = "accountId")
    private Account_info accountInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientBrowser() {
        return clientBrowser;
    }

    public void setClientBrowser(String clientBrowser) {
        this.clientBrowser = clientBrowser;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public Integer getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(Integer loginAt) {
        this.loginAt = loginAt;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Account_info getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(Account_info accountInfo) {
        this.accountInfo = accountInfo;
    }
}

