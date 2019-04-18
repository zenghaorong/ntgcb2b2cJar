package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

import java.util.List;

/**
 * 平台商品分类DTO
 *
 * Created by Aebiz_yjq on 2017/1/13.
 */
public class ProductCategoryDTO {

    /* 分类uuid */
    private String categoryUuid;

    /* 分类位置*/
    private int position;

    /* 分类父类uuid*/
    private String parentUuid;

    /* 分类编号*/
    private String categoryNo;

    /* 分类名称*/
    private String categoryName;

    private String categoryUrl;

    private List<ProductCategoryDTO> subCategoryList;

    public ProductCategoryDTO() {
    }

    public ProductCategoryDTO(String categoryUuid, int position, String parentUuid, String categoryNo, String categoryName, String categoryUrl) {
        this.categoryUuid = categoryUuid;
        this.position = position;
        this.parentUuid = parentUuid;
        this.categoryNo = categoryNo;
        this.categoryName = categoryName;
        this.categoryUrl = categoryUrl;
    }

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }

    public String getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(String categoryNo) {
        this.categoryNo = categoryNo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ProductCategoryDTO> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List<ProductCategoryDTO> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }


    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

    @Override
    public String toString() {
        return "ProductCategoryDTO{" +
                "categoryUuid='" + categoryUuid + '\'' +
                ", position=" + position +
                ", parentUuid='" + parentUuid + '\'' +
                ", categoryNo='" + categoryNo + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", categoryUrl='" + categoryUrl + '\'' +
                ", subCategoryList=" + subCategoryList +
                '}';
    }
}
