package com.aebiz.app.dec.modules.models;

import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.modules.models.em.ComponentVersionTypeEnum;
import com.aebiz.baseframework.base.model.BaseModel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.nutz.dao.entity.annotation.*;
import org.nutz.lang.Strings;

import java.io.Serializable;

/**
 *组件管理
 * Created by yewei on 2017/4/13.
 */
@Table("dec_component")
public class Dec_component extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("版本类型")
    @ColDefine(type = ColType.INT)
    @Default("1")
    private Integer versionType= ComponentVersionTypeEnum.PC.getKey();;//1：电脑版；2：微信版；3：手机版；4：电视版

    @Column
    @Comment("所属组件分类")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String compCategoryUuid;

    @Column
    @Comment("组件类型")
    @ColDefine(type = ColType.INT)
    @Default("1")
    private Integer compType;//1：系统组件，2：自定义组件*/

    @Column
    @Comment("组件使用类型")
    @ColDefine(type = ColType.INT)
    @Default("1")
    private int useType= DecorateCommonConstant.DECORATE_USETYPE_SYSTEM;//1：系统，2：店铺 默认：系统

    @Column
    @Comment("组件编号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String compId;

    @Column
    @Comment("组件名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String compName;
    @Column
    @Comment("组件权限")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String competence;

    @Column
    @Comment("组件图标")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String compImage;

    @Column
    @Comment("组件类的全路径")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String classFullName;

    @Column
    @Comment("组件描述")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String description;

    @Column
    @Comment("弹层高度")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer pHeight;


    @Column
    @Comment("弹层宽度")
    @ColDefine(type = ColType.INT)
    @Default("0")

    private Integer pWidth;

    @Column
    @Comment("是否启用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    @Comment("组件分类名称")
    @ColDefine(type =ColType.VARCHAR, width = 255)
    private String categoryName;

    @Comment("组件显示的html")
    @ColDefine(type =ColType.VARCHAR, width = 255)
    private String compHtml;

    @Comment("组件的model")
    @ColDefine(type =ColType.VARCHAR, width = 255)
    private String compModelStr;

    public String getCompetence() {
        return competence;
    }

    public void setCompetence(String competence) {
        this.competence = competence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersionType() {
        return versionType;
    }

    public void setVersionType(Integer versionType) {
        this.versionType = versionType;
    }

    public String getCompCategoryUuid() {
        return compCategoryUuid;
    }

    public void setCompCategoryUuid(String compCategoryUuid) {
        this.compCategoryUuid = compCategoryUuid;
    }

    public Integer getCompType() {
        return compType;
    }

    public void setCompType(Integer compType) {
        this.compType = compType;
    }

    public Integer getUseType() {
        return useType;
    }

    public void setUseType(Integer useType) {
        this.useType = useType;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompImage() {
        return compImage;
    }

    public void setCompImage(String compImage) {
        this.compImage = compImage;
    }

    public String getClassFullName() {
        return classFullName;
    }

    public void setClassFullName(String classFullName) {
        this.classFullName = classFullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getpHeight() {
        return pHeight;
    }

    public void setpHeight(Integer pHeight) {
        this.pHeight = pHeight;
    }

    public Integer getpWidth() {
        return pWidth;
    }

    public void setpWidth(Integer pWidth) {
        this.pWidth = pWidth;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCompHtml() {
        return compHtml;
    }

    public void setCompHtml(String compHtml) {
        this.compHtml = compHtml;
    }

    public String getCompModelStr() {

        if (!Strings.isEmpty(this.getClassFullName())) {
            try {
                Class compClass = Class.forName(this.getClassFullName());
                Object obj = compClass.newInstance();
                return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return "";
    }

    public void setCompModelStr(String compModelStr) {
        this.compModelStr = compModelStr;
    }
}
