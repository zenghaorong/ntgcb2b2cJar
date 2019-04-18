package com.aebiz.app.dec.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 *  * 模板的页面表
 *
 * 一个模板可能有一套页面，例如包含首页、商品详情页、商品列表页、频道页等一套页面
 *
 * Created by yewei on 2017/4/18.
 */
@Table("dec_templates_pages")
public class Dec_templates_pages extends BaseModel implements Serializable {
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
    @Comment(" 页面编号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String pageNo;

    @Column
    @Comment(" 页面名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String pageName;

    @Column
    @Comment(" 文件名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String pageFileName;

    @Column
    @Comment(" 页面类型")
    @ColDefine(type =ColType.INT)
    private Integer pageType;

    @Column
    @Comment(" 页面描述")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String description;

    @Comment(" 所属模板名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String templateName;

    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String pageTypeName = "";

    public String getId() {
        return id;
    }
    public String getTemplateUuid() {
        return templateUuid;
    }

    public void setTemplateUuid(String templateUuid) {
        this.templateUuid = templateUuid;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getPageTypeName() {
        return pageTypeName;
    }

    public void setPageTypeName(String pageTypeName) {
        this.pageTypeName = pageTypeName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageFileName() {
        return pageFileName;
    }

    public void setPageFileName(String pageFileName) {
        this.pageFileName = pageFileName;
    }

    public Integer getPageType() {
        return pageType;
    }

    public void setPageType(Integer pageType) {
        this.pageType = pageType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
