package com.aebiz.app.dec.commons.comps.screen.vo;

import java.util.List;

/**
 * Created by 金辉 on 2016/12/25.
 */
public class AttributeModel {
    private String attributeUuid;
    private String attributeName;
    private String attributeEnName;
    private String unit;
    private String type;
    private List selectValue;

    public String getAttributeUuid() {
        return attributeUuid;
    }

    public void setAttributeUuid(String attributeUuid) {
        this.attributeUuid = attributeUuid;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeEnName() {
        return attributeEnName;
    }

    public void setAttributeEnName(String attributeEnName) {
        this.attributeEnName = attributeEnName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List getSelectValue() {
        return selectValue;
    }

    public void setSelectValue(List selectValue) {
        this.selectValue = selectValue;
    }
}
