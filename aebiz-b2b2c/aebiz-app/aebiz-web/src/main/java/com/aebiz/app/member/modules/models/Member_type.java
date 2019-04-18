package com.aebiz.app.member.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 会员类型表
 * Created by wizzer on 2017/3/5.
 */
@Table("member_type")
public class Member_type extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Id
    @Comment("ID")
    @ColDefine(type = ColType.INT)
    private int id;

    @Column
    @Comment("类型名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String name;

    @Column
    @Comment("类型别名")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String aliasName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
}
