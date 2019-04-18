package com.aebiz.app.dec.modules.models;


import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by yewei on 2017/4/18.
 */
@Table("dec_templates_manager")
public class Dec_templates_manager  extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("模板编号")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String templateNo;

    @Column
    @Comment("模板中文名称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String templateZhName;

    @Column
    @Comment("模板英文名称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String templateEnName;

    @Column
    @Comment("模板缩略图")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String templateImage;

    @Column
    @Comment("模板使用类型")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    @Default("1")
    private String useType;//使用类型1：平台/系统，2：店铺 默认为系统模板

    @Column
    @Comment("模板描述")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String description;

    @Column
    @Comment("版本类型")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String versionType;

    @Column
    @Comment("店铺id")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String storeId;

    @Column
    @Comment("是否启用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    public String getVersionType() {
        return versionType;
    }

    public void setVersionType(String versionType) {
        this.versionType = versionType;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateNo() {
        return templateNo;
    }

    public void setTemplateNo(String templateNo) {
        this.templateNo = templateNo;
    }

    public String getTemplateZhName() {
        return templateZhName;
    }

    public void setTemplateZhName(String templateZhName) {
        this.templateZhName = templateZhName;
    }

    public String getTemplateEnName() {
        return templateEnName;
    }

    public void setTemplateEnName(String templateEnName) {
        this.templateEnName = templateEnName;
    }

    public String getTemplateImage() {
        return templateImage;
    }

    public void setTemplateImage(String templateImage) {
        this.templateImage = templateImage;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
