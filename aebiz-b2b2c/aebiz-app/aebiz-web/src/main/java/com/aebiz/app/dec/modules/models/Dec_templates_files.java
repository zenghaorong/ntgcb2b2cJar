package com.aebiz.app.dec.modules.models;

import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by yewei on 2017/4/26.
 */
@Table("dec_templates_files")
public class Dec_templates_files extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("所属模板")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String templateUuid;


    /* 文件类型 */
    @Column
    @Comment("文件类型")
    @ColDefine(type = ColType.INT)
    private Integer fileType;

    @Column
    @Comment(" 文件夹/页面显示名称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String showName;

    @Column
    @Comment("页面uuid")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String pageUuid;

    @Column
    @Comment("父文件夹uuid")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String parentUuid;

    @Column
    @Comment("备注")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String note;

    @Column
    @Comment("是否系统默认")
    @ColDefine(type = ColType.INT)
    private int isDefault = DecorateCommonConstant.TEMPLATE_FOLDER_DEFAULT_NO;

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

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getPageUuid() {
        return pageUuid;
    }

    public void setPageUuid(String pageUuid) {
        this.pageUuid = pageUuid;
    }

    public String getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}
