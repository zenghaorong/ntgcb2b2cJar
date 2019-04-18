package com.aebiz.app.dec.commons.comps.notice.vo;


import com.aebiz.app.dec.commons.comps.notice.NoticeCompController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

import java.io.Serializable;

/**
 * Created by yewei on 2017/5/27.
 */
public class NoticeCompModel extends BaseCompModel implements Serializable{
    private static final long serialVersionUID = -1;
    /*组件类型*/
    private String type="notice";
    private String compName = "公告栏组件";
    /*公告对应的分类ID*/
    private String categoryId ="";
    /*公告对应的分类名称*/
    private String categoryName="";
    /*文章内容URL*/
    private String contentUrl="";
    /*文章分类列表Url*/
    private String categoryUrl="";
    /*显示条数*/
    private int showNum = 5;
    /*滚动方式*/
    private int rollType = 0;

    /*无滚动*/
    public static int ROLL_NONE=0;
    /*向上滚动*/
    public static int ROLL_UP=1;
    /*向左滚动*/
    public static int ROLL_LEFT=2;

    public NoticeCompModel() {
        super(NoticeCompController.class,"/noticeComp/toParamsDesign");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getShowNum() {
        return showNum;
    }

    public void setShowNum(int showNum) {
        this.showNum = showNum;
    }

    public int getRollType() {
        return rollType;
    }

    public void setRollType(int rollType) {
        this.rollType = rollType;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }
}
