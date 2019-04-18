package com.aebiz.app.dec.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 组件分类
 * Created by yewei on 2017/4/13.
 */
@Table("dec_component_class")
public class Dec_component_class  extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column

    @Comment("分类名称")
    @ColDefine(type = ColType.VARCHAR,width = 255)
    private String categoryName;

    @Column
    @Comment("分类描述")
    @ColDefine(type = ColType.VARCHAR,width = 255)
    private String description;
}
