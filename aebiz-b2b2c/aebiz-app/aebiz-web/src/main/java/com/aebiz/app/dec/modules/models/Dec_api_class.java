package com.aebiz.app.dec.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by yewei on 2017/4/14.
 */
@Table("dec_api_class")
public class Dec_api_class extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("中文名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private  String groupZhName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupZhName() {
        return groupZhName;
    }

    public void setGroupZhName(String groupZhName) {
        this.groupZhName = groupZhName;
    }

    public String getGroupEnName() {
        return groupEnName;
    }

    public void setGroupEnName(String groupEnName) {
        this.groupEnName = groupEnName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column
    @Comment("英文名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String groupEnName;

    @Column
    @Comment("分组描述")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String description;

}
