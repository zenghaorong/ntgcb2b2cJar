package com.aebiz.app.web.modules.controllers.open.dec.dto.store;

/**
 * 商品品牌DTO
 *
 * Created by Aebiz_yjq on 2017/1/19.
 */
public class BrandDTO {

    /* 品牌uuid */
    private String brandUuid = "";

    /* 品牌名称*/
    private String brandName = "";

    /* 品牌英文名*/
    private String brandEnName="";

    /* 图片1-正方形*/
    private String imgUrl1="";

    /* 图片2-长方形*/
    private String imgUrl2="";

    /* 父品牌Id*/
    private String parentUuid = "";

    /* 介绍*/
    private String note = "";

    public BrandDTO() {
    }

    public BrandDTO(String brandUuid, String brandName, String brandEnName, String imgUrl1, String imgUrl2, String parentUuid, String note) {
        this.brandUuid = brandUuid;
        this.brandName = brandName;
        this.brandEnName = brandEnName;
        this.imgUrl1 = imgUrl1;
        this.imgUrl2 = imgUrl2;
        this.parentUuid = parentUuid;
        this.note = note;
    }

    @Override
    public String toString() {
        return "BrandDTO{" +
                "brandUuid='" + brandUuid + '\'' +
                "brandName='" + brandName + '\'' +
                ", brandEnName='" + brandEnName + '\'' +
                ", imgUrl1='" + imgUrl1 + '\'' +
                ", imgUrl2='" + imgUrl2 + '\'' +
                ", parentUuid='" + parentUuid + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

    public String getBrandUuid() {
        return brandUuid;
    }

    public void setBrandUuid(String brandUuid) {
        this.brandUuid = brandUuid;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandEnName() {
        return brandEnName;
    }

    public void setBrandEnName(String brandEnName) {
        this.brandEnName = brandEnName;
    }

    public String getImgUrl1() {
        return imgUrl1;
    }

    public void setImgUrl1(String imgUrl1) {
        this.imgUrl1 = imgUrl1;
    }

    public String getImgUrl2() {
        return imgUrl2;
    }

    public void setImgUrl2(String imgUrl2) {
        this.imgUrl2 = imgUrl2;
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
}
