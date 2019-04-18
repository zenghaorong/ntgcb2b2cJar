package com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo;

/**
 *图片库分类DTO
 *
 * Created by Aebiz_yjq on 2017/1/13.
 */
public class PlatImageCategoryDTO {

    /* 分类名称 */
    private String categoryName = "";

    /* 分类uuid */
    private String categoryUuid = "";

    /* 所在位置 *//*
    private int position;*/

   /* *//* 上级分类uuid *//*
    private String parentUuid = "";*/


    public PlatImageCategoryDTO(){}

    public PlatImageCategoryDTO(String categoryUuid, String categoryName) {
        this.categoryUuid = categoryUuid;
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    @Override
    public String toString() {
        return "PlatImageCategoryDTO{" +
                "categoryName='" + categoryName + '\'' +
                ", categoryUuid='" + categoryUuid + '\'' +
                '}';
    }
}
