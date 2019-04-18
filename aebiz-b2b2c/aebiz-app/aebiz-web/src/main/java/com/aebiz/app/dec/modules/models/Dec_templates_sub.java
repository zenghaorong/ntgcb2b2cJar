package com.aebiz.app.dec.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by yewei on 2017/4/24.
 */
@Table("dec_templates_sub")
public class Dec_templates_sub extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment(" 页面uuid")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String pageUuid;

    @Column
    @Comment(" 页面保存的key")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String resourceKey;

    @Column
    @Comment("版本号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String versionNo;

    @Column
    @Comment("版本描述")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String description;

    @Column
    @Comment("是否启用")
    @ColDefine(type = ColType.INT)
    private Integer disabled;

    @Column
    @Comment("创建时间")
    @ColDefine(type = ColType.INT)
    private Integer createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPageUuid() {
        return pageUuid;
    }

    public void setPageUuid(String pageUuid) {
        this.pageUuid = pageUuid;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}
