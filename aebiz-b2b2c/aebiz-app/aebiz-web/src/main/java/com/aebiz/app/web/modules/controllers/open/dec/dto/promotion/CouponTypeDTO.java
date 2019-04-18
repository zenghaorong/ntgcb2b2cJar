package com.aebiz.app.web.modules.controllers.open.dec.dto.promotion;

/**
 * 优惠券类型DTO
 *
 * Created by Aebiz_yjq on 2017/1/20.
 */
public class CouponTypeDTO {

    /* 优惠券类型uuid */
    private String couponTypeUuid ;

    /* 优惠券类型名称 */
    private String couponTypeName ;

    /* 店铺编号 */
    private String storeUuid ;

    /* 面额 */
    private double denomination ;

    /* 生效时间 */
    private String startTime ;

    /* 失效时间 */
    private String endTime ;

    /* 使用范围 1通用券 2手机券 */
    private String scope ;

    /* 总发行量 */
    private int issuedNum ;

    /* 可发行量 */
    private int canIssuedNum ;

    /* 每人限领量 不限为0 */
    private int limitNum ;

    /* 使用条件 订单满多少 不限为0 */
    private double couCondition ;

    /* 是否公开领取 0否 后台赠送 1是 前台领取 */
    private String collectType ;

    /* 是否允许转赠 */
    private String enableSend ;

    /* 兑换积分数 */
    private int convertIntegral ;

    /* 商户名称 */
    private String storeName ;

    /* 链接地址 */
    private String couponUrl ;

    /* 是否已生成优惠券(只针对后台发放优惠券的类型) */
    private boolean makeFlag = false;

    /*优惠券类型，1：普通优惠券。2：定向优惠券*/
    private String couponType ;

    /*创建时间*/
    private String createTime ;

    /* 每人限领量类型 限制或是不限制 */
    private String limitNumType ;

    /* 使用条件类型 限制或是不限制 */
    private String couConditionType ;

    private boolean canEdit = true;

    //剩余多少优惠券=总发行量-已领取的
    private int surplusCount ;

    public CouponTypeDTO() {
    }

    public String getCouponTypeUuid() {
        return couponTypeUuid;
    }

    public void setCouponTypeUuid(String couponTypeUuid) {
        this.couponTypeUuid = couponTypeUuid;
    }

    public String getCouponTypeName() {
        return couponTypeName;
    }

    public void setCouponTypeName(String couponTypeName) {
        this.couponTypeName = couponTypeName;
    }

    public String getStoreUuid() {
        return storeUuid;
    }

    public void setStoreUuid(String storeUuid) {
        this.storeUuid = storeUuid;
    }

    public double getDenomination() {
        return denomination;
    }

    public void setDenomination(double denomination) {
        this.denomination = denomination;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getIssuedNum() {
        return issuedNum;
    }

    public void setIssuedNum(int issuedNum) {
        this.issuedNum = issuedNum;
    }

    public int getCanIssuedNum() {
        return canIssuedNum;
    }

    public void setCanIssuedNum(int canIssuedNum) {
        this.canIssuedNum = canIssuedNum;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public double getCouCondition() {
        return couCondition;
    }

    public void setCouCondition(double couCondition) {
        this.couCondition = couCondition;
    }

    public String getCollectType() {
        return collectType;
    }

    public void setCollectType(String collectType) {
        this.collectType = collectType;
    }

    public String getEnableSend() {
        return enableSend;
    }

    public void setEnableSend(String enableSend) {
        this.enableSend = enableSend;
    }

    public int getConvertIntegral() {
        return convertIntegral;
    }

    public void setConvertIntegral(int convertIntegral) {
        this.convertIntegral = convertIntegral;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(String couponUrl) {
        this.couponUrl = couponUrl;
    }

    public boolean isMakeFlag() {
        return makeFlag;
    }

    public void setMakeFlag(boolean makeFlag) {
        this.makeFlag = makeFlag;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLimitNumType() {
        return limitNumType;
    }

    public void setLimitNumType(String limitNumType) {
        this.limitNumType = limitNumType;
    }

    public String getCouConditionType() {
        return couConditionType;
    }

    public void setCouConditionType(String couConditionType) {
        this.couConditionType = couConditionType;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public int getSurplusCount() {
        return surplusCount;
    }

    public void setSurplusCount(int surplusCount) {
        this.surplusCount = surplusCount;
    }

    @Override
    public String toString() {
        return "CouponTypeDTO{" +
                "couponTypeUuid='" + couponTypeUuid + '\'' +
                ", couponTypeName='" + couponTypeName + '\'' +
                ", storeUuid='" + storeUuid + '\'' +
                ", denomination=" + denomination +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", scope='" + scope + '\'' +
                ", issuedNum=" + issuedNum +
                ", canIssuedNum=" + canIssuedNum +
                ", limitNum=" + limitNum +
                ", couCondition=" + couCondition +
                ", collectType='" + collectType + '\'' +
                ", enableSend='" + enableSend + '\'' +
                ", convertIntegral=" + convertIntegral +
                ", storeName='" + storeName + '\'' +
                ", couponUrl='" + couponUrl + '\'' +
                ", makeFlag=" + makeFlag +
                ", couponType='" + couponType + '\'' +
                ", createTime='" + createTime + '\'' +
                ", limitNumType='" + limitNumType + '\'' +
                ", couConditionType='" + couConditionType + '\'' +
                ", canEdit=" + canEdit +
                ", surplusCount=" + surplusCount +
                '}';
    }
}
