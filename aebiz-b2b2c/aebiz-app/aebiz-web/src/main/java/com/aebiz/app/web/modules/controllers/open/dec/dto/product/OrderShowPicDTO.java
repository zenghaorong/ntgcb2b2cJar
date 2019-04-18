package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

/**
 * share order DTO
 * Created by Aebiz_yjq on 2017/1/21.
 */
public class OrderShowPicDTO {

    /* order show picture uuid */
    private String orderShowPicUuid;

    /* 晒单的编号 =评论内容uuid*/
    private String orderShowUuid="";

    /* 图片名称 */
    private String picName="";

    /* 图片路径 */
    private String picUrl="";

    /* 是否封面 0否 1是 */
    private String frontCover="";

    /* 图片描述 */
    private String picDesc="";

    public OrderShowPicDTO() {
    }

    public OrderShowPicDTO(String orderShowPicUuid, String orderShowUuid, String picName, String picUrl, String frontCover, String picDesc) {
        this.orderShowPicUuid = orderShowPicUuid;
        this.orderShowUuid = orderShowUuid;
        this.picName = picName;
        this.picUrl = picUrl;
        this.frontCover = frontCover;
        this.picDesc = picDesc;
    }

    public String getOrderShowPicUuid() {
        return orderShowPicUuid;
    }

    public void setOrderShowPicUuid(String orderShowPicUuid) {
        this.orderShowPicUuid = orderShowPicUuid;
    }

    public String getOrderShowUuid() {
        return orderShowUuid;
    }

    public void setOrderShowUuid(String orderShowUuid) {
        this.orderShowUuid = orderShowUuid;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getFrontCover() {
        return frontCover;
    }

    public void setFrontCover(String frontCover) {
        this.frontCover = frontCover;
    }

    public String getPicDesc() {
        return picDesc;
    }

    public void setPicDesc(String picDesc) {
        this.picDesc = picDesc;
    }

    @Override
    public String toString() {
        return "OrderShowPicDTO{" +
                "orderShowPicUuid='" + orderShowPicUuid + '\'' +
                ", orderShowUuid='" + orderShowUuid + '\'' +
                ", picName='" + picName + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", frontCover='" + frontCover + '\'' +
                ", picDesc='" + picDesc + '\'' +
                '}';
    }
}
