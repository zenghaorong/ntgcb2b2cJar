package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

import java.util.List;

/**
 * Created by Aebiz_yjq on 2017/2/8.
 */
public class SelectAttributeJsonDTO {

    /* 属性uuid*/
    private String attributeUuid;

    /* 属性名称*/
    private String attributeName;

    /* 属性英文名*/
    private String attributeEnName;

    /* 单位*/
    private String unit;

    /* 是否区间*/
    private String type;

    /* 位置*/
    private int position;

    /* 选择的属性值*/
    private List<String> selectValue;

    public SelectAttributeJsonDTO() {
    }

    public SelectAttributeJsonDTO(String attributeUuid, String attributeName, String attributeEnName, String unit,
                                  String type, int position, List<String> selectValue) {
        this.attributeUuid = attributeUuid;
        this.attributeName = attributeName;
        this.attributeEnName = attributeEnName;
        this.unit = unit;
        this.type = type;
        this.position = position;
        this.selectValue = selectValue;
    }

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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<String> getSelectValue() {
        return selectValue;
    }

    public void setSelectValue(List<String> selectValue) {
        this.selectValue = selectValue;
    }

    @Override
    public String toString() {
        return "SelectAttributeJsonDTO{" +
                "attributeUuid='" + attributeUuid + '\'' +
                ", attributeName='" + attributeName + '\'' +
                ", attributeEnName='" + attributeEnName + '\'' +
                ", unit='" + unit + '\'' +
                ", type='" + type + '\'' +
                ", position=" + position +
                ", selectValue=" + selectValue +
                '}';
    }
}
