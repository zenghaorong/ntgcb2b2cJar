package com.aebiz.app.acc.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 帐号登录用户表
 * 开发注意事项
 * 1、记得在删改此表数据的时候，调用清除缓存的方法（因为没有list缓存，所以新增的时候不用清除缓存）
 * Created by wizzer on 2017/4/8.
 */
@Table("account_user")
@TableIndexes({@Index(name = "INDEX_ACCOUNT_USER_LOGINNAMAE", fields = {"loginname"}, unique = true),
        @Index(name = "INDEX_ACCOUNT_USER_EMAIL", fields = {"email"}, unique = false),
        @Index(name = "INDEX_ACCOUNT_USER_MOBILE", fields = {"mobile"}, unique = true),
        @Index(name = "INDEX_ACCOUNT_USER_MEMBER", fields = {"accountId"}, unique = false)
})
public class Account_user extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("登录名")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String loginname;

    @Column
    @Comment("登录邮箱")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String email;

    @Column
    @Comment("登录手机号")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String mobile;

    @Column
    @Comment("密码")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String password;

    @Column
    @Comment("密码强度")
    @ColDefine(type = ColType.INT)
    private Integer passwordStrength; //PasswordStrengthEnum 密码强度Enum

    @Column
    @Comment("密码盐")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String salt;

    @Column
    @Comment("是否禁用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    @Column
    @Comment("帐号ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String accountId;

    @Column
    @Comment("会员头像")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String image;

    @One(target = Account_info.class, field = "accountId")
    private Account_info accountInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
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

    public Integer getPasswordStrength() {
        return passwordStrength;
    }

    public void setPasswordStrength(Integer passwordStrength) {
        this.passwordStrength = passwordStrength;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
