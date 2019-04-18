package com.aebiz.app.dec.commons.comps.category.vo;

import java.util.List;

/**
 * Created by 金辉 on 2016/12/16.
 */
public class CategoryModel {
    /*分类名称*/
    private String categoryName;
    /*分类链接*/
    private String url;
    /*分类图标*/
    private String img;
    /*子分类*/
    private List subCategory;
    //分类id
    private String categoryUuid;

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(List subCategory) {
        this.subCategory = subCategory;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
