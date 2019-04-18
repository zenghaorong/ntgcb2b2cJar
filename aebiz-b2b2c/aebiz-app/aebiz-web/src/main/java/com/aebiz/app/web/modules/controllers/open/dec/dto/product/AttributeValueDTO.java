package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

/**
 * 属性值Json DTO
 *
 * Created by Aebiz_yjq on 2017/1/20.
 */
public class AttributeValueDTO {

    /* 属性值uuid */
    private String valueUuid;

    /* 属性值 */
    private String value;

    public AttributeValueDTO() {
    }

    public AttributeValueDTO(String valueUuid, String value) {
        this.valueUuid = valueUuid;
        this.value = value;
    }

    public String getValueUuid() {
        return valueUuid;
    }

    public void setValueUuid(String valueUuid) {
        this.valueUuid = valueUuid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AttributeValueDTO{" +
                "valueUuid='" + valueUuid + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
