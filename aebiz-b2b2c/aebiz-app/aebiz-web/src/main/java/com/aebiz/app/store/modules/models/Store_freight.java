package com.aebiz.app.store.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 商家物流运费主表
 * Created by wizzer on 2017/3/1.
 */
@Table("store_freight")
public class Store_freight extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("模板名称")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String templateName;

    @Column
    @Comment("商户id")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String storeId;

    @Column
    @Comment("模板说明")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String templateExplain;

    @Column
    @Comment("是否默认")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean defaulted;

    @Column
    @Comment("是否启用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean enabled;

    @Column
    @Comment("计价方式")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String billingType;

    @Column
    @Comment("默认首件")
    @ColDefine(type = ColType.INT)
    private Integer defaultUnit;

    @Column
    @Comment("默认首费")
    @ColDefine(type = ColType.INT)
    private Integer defautAffix;

    @Column
    @Comment("默认续件")
    @ColDefine(type = ColType.INT)
    private Integer addUnit;

    @Column
    @Comment("默认续费")
    @ColDefine(type = ColType.INT)
    private Integer addCost;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public boolean isDefaulted() {
        return defaulted;
    }

    public void setDefaulted(boolean defaulted) {
        this.defaulted = defaulted;
    }

    public String getTemplateExplain() {
        return templateExplain;
    }

    public void setTemplateExplain(String templateExplain) {
        this.templateExplain = templateExplain;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getDefaultUnit() {
        return defaultUnit;
    }

    public void setDefaultUnit(Integer defaultUnit) {
        this.defaultUnit = defaultUnit;
    }

    public Integer getDefautAffix() {
        return defautAffix;
    }

    public void setDefautAffix(Integer defautAffix) {
        this.defautAffix = defautAffix;
    }

    public Integer getAddUnit() {
        return addUnit;
    }

    public void setAddUnit(Integer addUnit) {
        this.addUnit = addUnit;
    }

    public Integer getAddCost() {
        return addCost;
    }

    public void setAddCost(Integer addCost) {
        this.addCost = addCost;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
