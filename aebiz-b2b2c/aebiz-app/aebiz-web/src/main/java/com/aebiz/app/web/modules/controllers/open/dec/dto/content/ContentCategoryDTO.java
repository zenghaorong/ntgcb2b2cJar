package com.aebiz.app.web.modules.controllers.open.dec.dto.content;

/**
 * 内容分类DTO
 *
 * Created by Aebiz_yjq on 2016/12/28.
 */
public class ContentCategoryDTO {

    /*内容分类uuid*/
    private String categoryUuid ;

    /*内容分类名称*/
    private String categoryName;

    /*内容位置*/
    private int position;

    /* 图标地址 */
    private String iconPath;


    public ContentCategoryDTO() {
    }

    public ContentCategoryDTO(String categoryUuid, String categoryName, int position, String iconPath) {
        this.categoryUuid = categoryUuid;
        this.categoryName = categoryName;
        this.position = position;
        this.iconPath = iconPath;
    }

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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    @Override
    public String toString() {
        return "ContentCategoryDTO{" +
                "categoryUuid='" + categoryUuid + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", position=" + position +
                ", iconPath='" + iconPath + '\'' +
                '}';
    }
}
