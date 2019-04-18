package com.aebiz.app.dec.commons.comps.notice.vo;

/**
 * Created by 金辉 on 2016/12/19.
 */
public class NoticeContentModel {
    /*公告ID*/
    private String contentId;
    /*内容简介*/
    private String note;
    /*公告标题*/
    private String title;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
