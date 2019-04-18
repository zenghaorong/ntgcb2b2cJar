package com.aebiz.app.dec.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by yewei on 2017/4/24.
 */
@Table("dec_templates_style")
public class Dec_templates_style extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment(" 模板uuid")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String templateUuid;

    @Column
    @Comment(" 皮肤编号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String styleNo;

    @Column
    @Comment(" 皮肤名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String styleName;

    @Column
    @Comment(" 皮肤文件名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String styleFileName;

    @Column
    @Comment(" 图标")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String styleImage = "";

    /* 是否启用 */
    @Column
    @Comment(" 模板uuid")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    @Comment(" 模板名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String templateName;

    /* 皮肤图标路径 */
    @Comment(" 皮肤图标路径")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String styleImageUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateUuid() {
        return templateUuid;
    }

    public void setTemplateUuid(String templateUuid) {
        this.templateUuid = templateUuid;
    }

    public String getStyleNo() {
        return styleNo;
    }

    public void setStyleNo(String styleNo) {
        this.styleNo = styleNo;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getStyleFileName() {
        return styleFileName;
    }

    public void setStyleFileName(String styleFileName) {
        this.styleFileName = styleFileName;
    }

    public String getStyleImage() {
        return styleImage;
    }

    public void setStyleImage(String styleImage) {
        this.styleImage = styleImage;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getStyleImageUrl() {
        return styleImageUrl;
    }

    public void setStyleImageUrl(String styleImageUrl) {
        this.styleImageUrl = styleImageUrl;
    }
}
