package com.aebiz.app.member.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 会员等级表
 * Created by wizzer on 2017/3/5.
 */
@Table("member_level")
public class Member_level extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("等级名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String name;

    @Column
    @Comment("Logo")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String imgurl;

    @Column
    @Comment("折扣率")
    @ColDefine(type = ColType.INT)
    private Integer dis_count;

    @Column
    @Comment("所需积分")
    @ColDefine(type = ColType.INT)
    private Integer point;

    @Column
    @Comment("是否默认")
    private boolean defaultValue;

    @Column
    @Comment("类型ID")
    @ColDefine(type = ColType.INT)
    private int typeId;

    @One(target = Member_type.class, field = "typeId")
    private Member_type memberType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Integer getDis_count() {
        return dis_count;
    }

    public void setDis_count(Integer dis_count) {
        this.dis_count = dis_count;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public Member_type getMemberType() {
        return memberType;
    }

    public void setMemberType(Member_type memberType) {
        this.memberType = memberType;
    }
}


