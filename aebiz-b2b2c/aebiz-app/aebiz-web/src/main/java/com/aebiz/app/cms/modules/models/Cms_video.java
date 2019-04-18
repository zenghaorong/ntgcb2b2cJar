package com.aebiz.app.cms.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * @Auther: zenghaorong
 * @Date: 2019/2/28  21:20
 * @Description: 视频模块
 */
@Table("cms_video")
public class Cms_video extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("视频地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String videoUrl;


    @Column
    @Comment("视频封面图片地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String imageUrl;

    @Column
    @Comment("视频标题")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String videoTitle;

    @Column
    @Comment("视频详情介绍")
    @ColDefine(type = ColType.TEXT)
    private String videoDetails;

    @Column
    @Comment("商户编号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String storeId;

    @Column
    @Comment("浏览量")
    @ColDefine(type = ColType.INT, width = 6)
    private Integer pageViews;

    @Column
    @Comment("点赞量")
    @ColDefine(type = ColType.INT, width = 6)
    private Integer likeNum;

    @Column
    @Comment("评论数量")
    @ColDefine(type = ColType.INT, width = 6)
    private Integer evaluateNum;

    @Column
    @Comment("排序")
    @ColDefine(type = ColType.INT, width = 6)
    private Integer sort;

    @Column
    @Comment("购买价格")
    @ColDefine(type = ColType.INT, width = 6)
    private Double price;


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Integer getPageViews() {
        return pageViews;
    }

    public void setPageViews(Integer pageViews) {
        this.pageViews = pageViews;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getEvaluateNum() {
        return evaluateNum;
    }

    public void setEvaluateNum(Integer evaluateNum) {
        this.evaluateNum = evaluateNum;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDetails() {
        return videoDetails;
    }

    public void setVideoDetails(String videoDetails) {
        this.videoDetails = videoDetails;
    }
}
