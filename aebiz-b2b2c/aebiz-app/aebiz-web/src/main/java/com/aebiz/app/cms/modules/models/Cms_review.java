package com.aebiz.app.cms.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: zenghaorong
 * @Date: 2019/3/3  20:32
 * @Description: 评论回复实体类
 */
@Table("cms_review")
public class Cms_review extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("商城ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeId;

    @Column
    @Comment("内容类型 1黏土教程 2视频专区")
    @ColDefine(type = ColType.CHAR, width = 32)  private String cmsType;

    @Column
    @Comment("内容ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String cmsId;

    @Column
    @Comment("内容标题")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String cmsTitle;

    @Column
    @Comment("回复父级评论编号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String reviewId;

    @Column
    @Comment("回复父级回复编号（区分回复的是评论底下哪条回复）")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String reviewReplyId;

    @Column
    @Comment("类型 1评论 2回复")
    @ColDefine(type = ColType.CHAR, width = 2)
    private String type;

    @Column
    @Comment("评论回复内容")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String content;

    @Column
    @Comment("评论点赞数")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String loveNum;

    @Column
    @Comment("评论人名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String reviewOpName;

    @Column
    @Comment("评论人编号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String reviewOpId;

    @Column
    @Comment("回复父级发布人名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String reviewFatherName;

    @Column
    @Comment("回复父级发布人编号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String reviewFatherId;

    @Column
    @Comment("是否为官方 1是 2不是")
    @ColDefine(type = ColType.CHAR, width = 2)
    private String isStore;

    @Comment("回复实体")
    private List<ReplyVO> replyVOList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCmsType() {
        return cmsType;
    }

    public void setCmsType(String cmsType) {
        this.cmsType = cmsType;
    }

    public String getCmsId() {
        return cmsId;
    }

    public void setCmsId(String cmsId) {
        this.cmsId = cmsId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLoveNum() {
        return loveNum;
    }

    public void setLoveNum(String loveNum) {
        this.loveNum = loveNum;
    }

    public String getReviewOpName() {
        return reviewOpName;
    }

    public void setReviewOpName(String reviewOpName) {
        this.reviewOpName = reviewOpName;
    }

    public String getReviewOpId() {
        return reviewOpId;
    }

    public void setReviewOpId(String reviewOpId) {
        this.reviewOpId = reviewOpId;
    }

    public String getReviewFatherName() {
        return reviewFatherName;
    }

    public void setReviewFatherName(String reviewFatherName) {
        this.reviewFatherName = reviewFatherName;
    }

    public String getReviewFatherId() {
        return reviewFatherId;
    }

    public void setReviewFatherId(String reviewFatherId) {
        this.reviewFatherId = reviewFatherId;
    }

    public String getIsStore() {
        return isStore;
    }

    public void setIsStore(String isStore) {
        this.isStore = isStore;
    }

    public String getCmsTitle() {
        return cmsTitle;
    }

    public void setCmsTitle(String cmsTitle) {
        this.cmsTitle = cmsTitle;
    }

    public List<ReplyVO> getReplyVOList() {
        return replyVOList;
    }

    public void setReplyVOList(List<ReplyVO> replyVOList) {
        this.replyVOList = replyVOList;
    }

    public String getReviewReplyId() {
        return reviewReplyId;
    }

    public void setReviewReplyId(String reviewReplyId) {
        this.reviewReplyId = reviewReplyId;
    }
}
