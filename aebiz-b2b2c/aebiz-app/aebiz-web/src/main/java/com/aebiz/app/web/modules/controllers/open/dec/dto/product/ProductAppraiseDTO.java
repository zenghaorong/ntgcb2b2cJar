package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

import java.util.ArrayList;
import java.util.List;

/**
 * product Appraise DTO
 *
 * Created by Aebiz_yjq on 2017/1/21.
 */
public class ProductAppraiseDTO {

    /* appraise uuid */
    private String appraiseUuid;

    /* order uuid */
    private String orderUuid = "";

    /* order detail uuid */
    private String orderDetailUuid = "";

    /* product sku No */
    private String productSkuNo = "";

    /* product uuid */
    private String productUuid = "";

    /* customer uuid */
    private String customerUuid = "";

    /* customer name */
    private String customerName = "";

    /* appraise score */
    private int appScore = 0;

    /* appraise tags  */
    private String appTag = "";

    /* appraise content */
    private String appContent = "";

    /* appraise time */
    private String appTime = "";

    /* product No */
    private String productNo = "";

    /* product name */
    private String productName = "";

    /* order uuid  */
    private String OrderId = "";

    /* tags Set */
    private List<String> tags = new ArrayList<String>();

    /* has picture to share */
    private String hasShowPic="0";

    public ProductAppraiseDTO() {
    }

    public String getAppraiseUuid() {
        return appraiseUuid;
    }

    public void setAppraiseUuid(String appraiseUuid) {
        this.appraiseUuid = appraiseUuid;
    }

    public String getOrderUuid() {
        return orderUuid;
    }

    public void setOrderUuid(String orderUuid) {
        this.orderUuid = orderUuid;
    }

    public String getOrderDetailUuid() {
        return orderDetailUuid;
    }

    public void setOrderDetailUuid(String orderDetailUuid) {
        this.orderDetailUuid = orderDetailUuid;
    }

    public String getProductSkuNo() {
        return productSkuNo;
    }

    public void setProductSkuNo(String productSkuNo) {
        this.productSkuNo = productSkuNo;
    }

    public String getProductUuid() {
        return productUuid;
    }

    public void setProductUuid(String productUuid) {
        this.productUuid = productUuid;
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getAppScore() {
        return appScore;
    }

    public void setAppScore(int appScore) {
        this.appScore = appScore;
    }

    public String getAppTag() {
        return appTag;
    }

    public void setAppTag(String appTag) {
        this.appTag = appTag;
    }

    public String getAppContent() {
        return appContent;
    }

    public void setAppContent(String appContent) {
        this.appContent = appContent;
    }

    public String getAppTime() {
        return appTime;
    }

    public void setAppTime(String appTime) {
        this.appTime = appTime;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getHasShowPic() {
        return hasShowPic;
    }

    public void setHasShowPic(String hasShowPic) {
        this.hasShowPic = hasShowPic;
    }

    @Override
    public String toString() {
        return "ProductAppraiseDTO{" +
                "appraiseUuid='" + appraiseUuid + '\'' +
                ", orderUuid='" + orderUuid + '\'' +
                ", orderDetailUuid='" + orderDetailUuid + '\'' +
                ", productSkuNo='" + productSkuNo + '\'' +
                ", productUuid='" + productUuid + '\'' +
                ", customerUuid='" + customerUuid + '\'' +
                ", customerName='" + customerName + '\'' +
                ", appScore=" + appScore +
                ", appTag='" + appTag + '\'' +
                ", appContent='" + appContent + '\'' +
                ", appTime='" + appTime + '\'' +
                ", productNo='" + productNo + '\'' +
                ", productName='" + productName + '\'' +
                ", OrderId='" + OrderId + '\'' +
                ", tags=" + tags +
                ", hasShowPic='" + hasShowPic + '\'' +
                '}';
    }
}
