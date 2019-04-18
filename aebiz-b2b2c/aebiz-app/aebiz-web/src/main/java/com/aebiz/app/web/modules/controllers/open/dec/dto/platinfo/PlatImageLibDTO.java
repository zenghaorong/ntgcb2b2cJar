package com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo;

/**
 * 平台图片库DTO
 *
 * Created by Aebiz_yjq on 2017/1/11.
 */
public class PlatImageLibDTO {

    /* 图片uuid */
    private String imgUuid = "";

    /* 所属分类 */
    private String categoryUuid = "";

    /* 图片名称 */
    private String imageName = "";

    /* 图片路径 */
    private String imagePath = "";

    /* 图片尺寸 *//*
    private String imageSize = "";

    *//* 图片大小 *//*
    private String imageSpace = "";

    *//* 后缀 *//*
    private String suffix = "";*/

    public PlatImageLibDTO(){}

    public PlatImageLibDTO(String imgUuid, String categoryUuid, String imageName, String imagePath) {
        this.imgUuid = imgUuid;
        this.categoryUuid = categoryUuid;
        this.imageName = imageName;
        this.imagePath = imagePath;
       /* this.imageSize = imageSize;
        this.imageSpace = imageSpace;
        this.suffix = suffix;*/
    }

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

  /*  public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String getImageSpace() {
        return imageSpace;
    }

    public void setImageSpace(String imageSpace) {
        this.imageSpace = imageSpace;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }*/

    public String getImgUuid() {
        return imgUuid;
    }

    public void setImgUuid(String imgUuid) {
        this.imgUuid = imgUuid;
    }

    @Override
    public String toString() {
        return "PlatImageLibDTO{" +
                "imgUuid='" + imgUuid + '\'' +
                "categoryUuid='" + categoryUuid + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imagePath='" + imagePath + '\'' +
                               '}';
    }
}
