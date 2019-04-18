package com.aebiz.app.web.modules.controllers.open.dec.dto.content;

/**
 * 内容DTO
 *
 * Created by Aebiz_yjq on 2016/12/28.
 */
public class ContentDTO {

    /*内容uuid*/
    private String articlUuid;

    /*内容名称*/
    private String articlTitle;

    /*内容*/
    private String articlValue;

    /*内容类型*/
    private String contentType;

    public ContentDTO() {
    }

    public ContentDTO(String articlUuid, String articlTitle, String articlValue, String contentType) {
        this.articlUuid = articlUuid;
        this.articlTitle = articlTitle;
        this.articlValue = articlValue;
        this.contentType = contentType;
    }

    public String getArticlUuid() {
        return articlUuid;
    }

    public void setArticlUuid(String articlUuid) {
        this.articlUuid = articlUuid;
    }

    public String getArticlTitle() {
        return articlTitle;
    }

    public void setArticlTitle(String articlTitle) {
        this.articlTitle = articlTitle;
    }

    public String getArticlValue() {
        return articlValue;
    }

    public void setArticlValue(String articlValue) {
        this.articlValue = articlValue;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "ContentDTO{" +
                "articlUuid='" + articlUuid + '\'' +
                ", articlTitle='" + articlTitle + '\'' +
                ", articlValue='" + articlValue + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
