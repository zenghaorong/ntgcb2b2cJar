package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

/**
 * 商品属性DTO
 *
 * Created by Aebiz_yjq on 2017/1/20.
 */
public class SpecJsonDTO {

    /* 属性名称 */
    private String name;

    /* 属性英文名 */
    private String enName;

    /* 属性值uuid */
    private String valueUuid;

    /* 属性值 */
    private String value;

    public SpecJsonDTO() {
    }

    public SpecJsonDTO(String name, String enName, String valueUuid, String value) {
        this.name = name;
        this.enName = enName;
        this.valueUuid = valueUuid;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
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
        return "SpecJsonDTO{" +
                "name='" + name + '\'' +
                ", enName='" + enName + '\'' +
                ", valueUuid='" + valueUuid + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
