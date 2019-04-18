package com.aebiz.app.web.modules.controllers.open.dec.dto.product;


import java.util.ArrayList;
import java.util.List;

/**
 * 该类主要是用来封装商品各个属性所选属性值 并用该类转换成json
 *
 * Created by Aebiz_yjq on 2017/1/20.
 */
public class SelectedAttributeInValueJsonDTO {

    /* 属性uuid */
    private String attributeUuid="";

    /* 属性名称 */
    private String attributeName="";

    /* 是否是颜色属性 */
    private String canColor="";

    /* 属性值集合 */
    private List<AttributeValueDTO> values=new ArrayList<>();

    public SelectedAttributeInValueJsonDTO() {
    }

    public SelectedAttributeInValueJsonDTO(String attributeUuid, String attributeName, String canColor, List<AttributeValueDTO> values) {
        this.attributeUuid = attributeUuid;
        this.attributeName = attributeName;
        this.canColor = canColor;
        this.values = values;
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

    public String getCanColor() {
        return canColor;
    }

    public void setCanColor(String canColor) {
        this.canColor = canColor;
    }

    public List<AttributeValueDTO> getValues() {
        return values;
    }

    public void setValues(List<AttributeValueDTO> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "SelectedAttributeInValueJsonDTO{" +
                "attributeUuid='" + attributeUuid + '\'' +
                ", attributeName='" + attributeName + '\'' +
                ", canColor='" + canColor + '\'' +
                ", values=" + values +
                '}';
    }
}
