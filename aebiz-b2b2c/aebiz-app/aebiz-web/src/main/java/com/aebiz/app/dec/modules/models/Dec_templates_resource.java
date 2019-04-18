package com.aebiz.app.dec.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by xupeng on 2017/4/27.
 */
@Table("dec_templates_resource")
public class Dec_templates_resource extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("所属模板文件夹")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String folderUuid;

    @Column
    @Comment("文件名")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String fileName;

    @Column
    @Comment("后缀")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String suffix;

    @Column
    @Comment("文件类型")
    @ColDefine(type = ColType.INT)
    private Integer fileType;

    @Column
    @Comment("资源保存的key")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String resourceKey;

    @Column
    @Comment("备注")
    @ColDefine(type = ColType.VARCHAR, width = 500)
    private String note;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFolderUuid() {
        return folderUuid;
    }

    public void setFolderUuid(String folderUuid) {
        this.folderUuid = folderUuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
