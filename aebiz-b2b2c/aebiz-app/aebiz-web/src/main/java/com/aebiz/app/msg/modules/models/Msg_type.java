package com.aebiz.app.msg.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 消息类型表,只可修改名称不允许修改code
 * Created by wizzer on 2017/2/21.
 */
@Table("msg_type")
public class Msg_type extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("类型标识")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String code;//system=系统消息

    @Column
    @Comment("类型名称")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
