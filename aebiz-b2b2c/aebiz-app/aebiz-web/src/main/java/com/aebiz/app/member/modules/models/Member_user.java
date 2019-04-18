package com.aebiz.app.member.modules.models;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 会员表
 * Created by wizzer on 2017/3/1.
 */
@Table("member_user")
@TableIndexes({@Index(name = "INDEX_MEMBER_USER_ACCOUNT", fields = {"accountId"}, unique = true)})
public class Member_user extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("帐号ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String accountId;

    @Column
    @Comment("等级ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String levelId;

    @Column
    @Comment("类型ID")
    @ColDefine(type = ColType.INT)
    private int typeId;

    @One(target = Member_level.class, field = "levelId")
    private Member_level memberLevel;

    @One(target = Member_type.class, field = "typeId")
    private Member_type memberType;

    @One(target = Member_account.class, field = "accountId",key = "accountId")
    private Member_account memberAccount;

    @One(target = Account_info.class, field = "accountId")
    private Account_info accountInfo;

    @One(target = Account_user.class, field = "accountId",key = "accountId")
    private Account_user accountUser;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public Member_level getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(Member_level memberLevel) {
        this.memberLevel = memberLevel;
    }

    public Member_type getMemberType() {
        return memberType;
    }

    public void setMemberType(Member_type memberType) {
        this.memberType = memberType;
    }

    public Account_info getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(Account_info accountInfo) {
        this.accountInfo = accountInfo;
    }

    public Account_user getAccountUser() {
        return accountUser;
    }

    public void setAccountUser(Account_user accountUser) {
        this.accountUser = accountUser;
    }

    public Member_account getMemberAccount() {
        return memberAccount;
    }

    public void setMemberAccount(Member_account memberAccount) {
        this.memberAccount = memberAccount;
    }
}
