package com.aebiz.app.member.modules.models;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 会员账户表
 * Created by wizzer on 2017/3/5.
 */
@Table("member_account")
public class Member_account extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("会员余额")
    @ColDefine(type = ColType.INT)
    private Integer money = 0;

    @Column
    @Comment("冻结余额")
    @ColDefine(type = ColType.INT)
    private Integer freezeMoney = 0;

    @Column
    @Comment("支付密码")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String payPassword;

    @Column
    @Comment("支付密码盐")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String payPasswordSalt;

    @Column
    @Comment("启用支付密码")
    @ColDefine(type = ColType.BOOLEAN)
    private Boolean payPasswordEnabled;

    @Column
    @Comment("会员积分")
    @ColDefine(type = ColType.INT)
    private Integer score = 0;

    @Column
    @Comment("冻结积分")
    @ColDefine(type = ColType.INT)
    private Integer freezeScore = 0;

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

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getFreezeMoney() {
        return freezeMoney;
    }

    public void setFreezeMoney(Integer freezeMoney) {
        this.freezeMoney = freezeMoney;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getFreezeScore() {
        return freezeScore;
    }

    public void setFreezeScore(Integer freezeScore) {
        this.freezeScore = freezeScore;
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

    public String getPayPasswordSalt() {
        return payPasswordSalt;
    }

    public void setPayPasswordSalt(String payPasswordSalt) {
        this.payPasswordSalt = payPasswordSalt;
    }

    public Boolean isPayPasswordEnabled() {
        return payPasswordEnabled;
    }

    public void setPayPasswordEnabled(Boolean payPasswordEnabled) {
        this.payPasswordEnabled = payPasswordEnabled;
    }
}
