package com.aebiz.app.dec.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by yewei on 2017/4/21.
 */
@Table("dec_component_resource")
public class Dec_component_resource  extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;


    @Column
    @Comment("组件uuid")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String compUuid = "";

    /* 验证号 用来验证jsp资源文件当前使用版本 */
    @Column
    @Comment("验证号")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String versionNo = "";

    /* 资源类型 */
    @Column
    @Comment("资源类型")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String resourceType;

    /* 文件保存的key */
    @Column
    @Comment("文件保存的key")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String resourceKey;

    /* 是否正在使用 */
    @Column
    @Comment("是否正在使用")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer disabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompUuid() {
        return compUuid;
    }

    public void setCompUuid(String compUuid) {
        this.compUuid = compUuid;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public Integer isDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }
}
