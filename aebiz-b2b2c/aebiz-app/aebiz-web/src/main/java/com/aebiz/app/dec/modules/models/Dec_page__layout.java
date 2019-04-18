package com.aebiz.app.dec.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by yewei on 2017/6/30.
 */
@Table("dec_page_layout")
public class Dec_page__layout extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("布局名称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String layoutName = "";

    @Column
    @Comment("布局编号")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String layoutId = "";

    @Column
    @Comment("资源保存key")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String resourceKey = "";

    @Column
    @Comment("终端类型")
    @ColDefine(type = ColType.VARCHAR, width = 1)
    @Default("1")
    private String versionType = "";//1：电脑端；2：手机端；3：微信端；4：电视端

    @Column
    @Comment("布局描述")
    @ColDefine(type = ColType.VARCHAR, width = 200)
    private String description = "";

    @Comment("页面布局")
    @ColDefine(type = ColType.VARCHAR, width = 2100)
    private String layoutContent = "";

    public String getLayoutContent() {
        return layoutContent;
    }

    public void setLayoutContent(String layoutContent) {
        this.layoutContent = layoutContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public String getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(String layoutId) {
        this.layoutId = layoutId;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getVersionType() {
        return versionType;
    }

    public void setVersionType(String versionType) {
        this.versionType = versionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
