package com.aebiz.app.acc.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 第三方登录表
 * Created by wizzer on 2017/4/8.
 */
@Table("account_bind")
@TableIndexes({@Index(name = "INDEX_ACCOUNT", fields = {"accountId"}, unique = false)})
public class Account_bind extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("第三方类型")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String bind_type;//qq  weibo  wechat

    @Column
    @Comment("第三方帐号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String bind_openid;

    @Column
    @Comment("第三方昵称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String bind_nickname;

    @Column
    @Comment("是否禁用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    @Column
    @Comment("是否绑定")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean binded;

    @Column
    @Comment("绑定时间")
    @Prev(els = @EL("$me.now()"))
    @ColDefine(type = ColType.INT)
    private Integer bindAt;

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

    public String getBind_type() {
        return bind_type;
    }

    public void setBind_type(String bind_type) {
        this.bind_type = bind_type;
    }

    public String getBind_openid() {
        return bind_openid;
    }

    public void setBind_openid(String bind_openid) {
        this.bind_openid = bind_openid;
    }

    public String getBind_nickname() {
        return bind_nickname;
    }

    public void setBind_nickname(String bind_nickname) {
        this.bind_nickname = bind_nickname;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isBinded() {
        return binded;
    }

    public void setBinded(boolean binded) {
        this.binded = binded;
    }

    public Integer getBindAt() {
        return bindAt;
    }

    public void setBindAt(Integer bindAt) {
        this.bindAt = bindAt;
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
