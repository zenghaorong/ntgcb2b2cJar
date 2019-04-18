package com.aebiz.app.shop.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 商城图片设置
 * Created by wizzer on 2017/3/27.
 */
@Table("shop_image")
public class Shop_image extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String id;//pc wap tv


    @Column
    @Comment("列表图宽")
    @ColDefine(type = ColType.INT)
    private Integer imgList_width;

    @Column
    @Comment("列表图高")
    @ColDefine(type = ColType.INT)
    private Integer imgList_height;

    @Column
    @Comment("相册图宽")
    @ColDefine(type = ColType.INT)
    private Integer imgAlbum_width;

    @Column
    @Comment("相册图高")
    @ColDefine(type = ColType.INT)
    private Integer imgAlbum_height;

    @Column
    @Comment("相册图水印类型")
    @ColDefine(type = ColType.VARCHAR, width = 10)
    private String imgAlbum_wmtype;//none  text  image

    @Column
    @Comment("相册图水印文字")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String imgAlbum_wmtext;

    @Column
    @Comment("相册图水印图片")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String imgAlbum_wmimg;

    @Column
    @Comment("缩略图宽")
    @ColDefine(type = ColType.INT)
    private Integer imgThumb_width;

    @Column
    @Comment("缩略图高")
    @ColDefine(type = ColType.INT)
    private Integer imgThumb_height;

    @Column
    @Comment("缩略图水印类型")
    @ColDefine(type = ColType.VARCHAR, width = 10)
    private String imgThumb_wmtype;//none  text  image

    @Column
    @Comment("缩略图水印文字")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String imgThumb_wmtext;

    @Column
    @Comment("缩略图水印图片")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String imgThumb_wmimg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getImgList_width() {
        return imgList_width;
    }

    public void setImgList_width(Integer imgList_width) {
        this.imgList_width = imgList_width;
    }

    public Integer getImgList_height() {
        return imgList_height;
    }

    public void setImgList_height(Integer imgList_height) {
        this.imgList_height = imgList_height;
    }

    public Integer getImgAlbum_width() {
        return imgAlbum_width;
    }

    public void setImgAlbum_width(Integer imgAlbum_width) {
        this.imgAlbum_width = imgAlbum_width;
    }

    public Integer getImgAlbum_height() {
        return imgAlbum_height;
    }

    public void setImgAlbum_height(Integer imgAlbum_height) {
        this.imgAlbum_height = imgAlbum_height;
    }

    public String getImgAlbum_wmtype() {
        return imgAlbum_wmtype;
    }

    public void setImgAlbum_wmtype(String imgAlbum_wmtype) {
        this.imgAlbum_wmtype = imgAlbum_wmtype;
    }

    public String getImgAlbum_wmtext() {
        return imgAlbum_wmtext;
    }

    public void setImgAlbum_wmtext(String imgAlbum_wmtext) {
        this.imgAlbum_wmtext = imgAlbum_wmtext;
    }

    public String getImgAlbum_wmimg() {
        return imgAlbum_wmimg;
    }

    public void setImgAlbum_wmimg(String imgAlbum_wmimg) {
        this.imgAlbum_wmimg = imgAlbum_wmimg;
    }

    public Integer getImgThumb_width() {
        return imgThumb_width;
    }

    public void setImgThumb_width(Integer imgThumb_width) {
        this.imgThumb_width = imgThumb_width;
    }

    public Integer getImgThumb_height() {
        return imgThumb_height;
    }

    public void setImgThumb_height(Integer imgThumb_height) {
        this.imgThumb_height = imgThumb_height;
    }

    public String getImgThumb_wmtype() {
        return imgThumb_wmtype;
    }

    public void setImgThumb_wmtype(String imgThumb_wmtype) {
        this.imgThumb_wmtype = imgThumb_wmtype;
    }

    public String getImgThumb_wmtext() {
        return imgThumb_wmtext;
    }

    public void setImgThumb_wmtext(String imgThumb_wmtext) {
        this.imgThumb_wmtext = imgThumb_wmtext;
    }

    public String getImgThumb_wmimg() {
        return imgThumb_wmimg;
    }

    public void setImgThumb_wmimg(String imgThumb_wmimg) {
        this.imgThumb_wmimg = imgThumb_wmimg;
    }
}
