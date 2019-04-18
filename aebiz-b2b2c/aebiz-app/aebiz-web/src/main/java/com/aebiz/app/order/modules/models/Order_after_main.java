package com.aebiz.app.order.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 售后主表：保存售后申请就存这张表
 * Created by yj on 2017/5/17.
 */
@Table("order_after_main")
public class Order_after_main extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("售后单号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("订单编号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String orderId;

    @Column
    @Comment("申请时间")
    @ColDefine(type = ColType.INT)
    private Integer applyTime;

    @Column
    @Comment("申请类型")
    @ColDefine(type = ColType.INT)
    private Integer applyType;

    //申请类型名称
    private String applyTypeName;

    @Column
    @Comment("申请人")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Default("")
    private String applyMan;

    @Column
    @Comment("申请人ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String applyManId;

    @Column
    @Comment("申请人地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String applyManAddress;

    @Column
    @Comment("申请人手机号")
    @ColDefine(type = ColType.VARCHAR, width = 11)
    private String applyManMobile;
    
    @Column
    @Comment("商户编号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeId;

    @Column
    @Comment("商户名")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeName;

    @Column
    @Comment("申请的退款金额")
    @ColDefine(type = ColType.INT)
    private Integer returnMoney;

    @Column
    @Comment("申请售后的原因")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String returnReason; // 后台返回

    @Column
    @Comment("问题描述")
    @ColDefine(type = ColType.VARCHAR,width = 150)
    private String description; // 用户填写

    @Column
    @Comment("凭证1")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String evidence1;

    @Column
    @Comment("凭证2")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String evidence2;

    @Column
    @Comment("凭证3")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String evidence3;

    @Column
    @Comment("售后状态")
    @ColDefine(type = ColType.INT)
    private Integer afterSaleState;//OrderAfterStateEnum

    //售后状态名称，用来塞售后状态的名称
    private String afterSaleStateName;

    @Column
    @Comment("退款方式")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String returnMoneyStyle;

    @Column
    @Comment("取消的原因")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String cancelReason = "";

    @Column
    @Comment("包装是否良好")
    @ColDefine(type = ColType.BOOLEAN)
    @Default("true")
    private Boolean packWell;

    private Order_after_detail orderAfterDetail;

    @One(target = Order_main.class, field = "orderId")
    private Order_main orderMain;

    public String getAfterSaleStateName() {
        return afterSaleStateName;
    }

    public void setAfterSaleStateName(String afterSaleStateName) {
        this.afterSaleStateName = afterSaleStateName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Integer applyTime) {
        this.applyTime = applyTime;
    }

    public Integer getApplyType() {
        return applyType;
    }

    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    public String getApplyMan() {
        return applyMan;
    }

    public void setApplyMan(String applyMan) {
        this.applyMan = applyMan;
    }

    public String getApplyManId() {
        return applyManId;
    }

    public void setApplyManId(String applyManId) {
        this.applyManId = applyManId;
    }

    public String getStoreUuid() {
        return storeId;
    }

    public void setStoreUuid(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(Integer returnMoney) {
        this.returnMoney = returnMoney;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getEvidence1() {
        return evidence1;
    }

    public void setEvidence1(String evidence1) {
        this.evidence1 = evidence1;
    }

    public String getEvidence2() {
        return evidence2;
    }

    public void setEvidence2(String evidence2) {
        this.evidence2 = evidence2;
    }

    public String getEvidence3() {
        return evidence3;
    }

    public void setEvidence3(String evidence3) {
        this.evidence3 = evidence3;
    }

    public Integer getAfterSaleState() {
        return afterSaleState;
    }

    public void setAfterSaleState(Integer afterSaleState) {
        this.afterSaleState = afterSaleState;
    }

    public String getReturnMoneyStyle() {
        return returnMoneyStyle;
    }

    public void setReturnMoneyStyle(String returnMoneyStyle) {
        this.returnMoneyStyle = returnMoneyStyle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStoreId() {

        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Boolean getPackWell() {
        return packWell;
    }

    public void setPackWell(Boolean packWell) {
        this.packWell = packWell;
    }

    public String getApplyManAddress() {
        return applyManAddress;
    }

    public void setApplyManAddress(String applyManAddress) {
        this.applyManAddress = applyManAddress;
    }

    public String getApplyManMobile() {
        return applyManMobile;
    }

    public void setApplyManMobile(String applyManMobile) {
        this.applyManMobile = applyManMobile;
    }

    public String getApplyTypeName() {
        return applyTypeName;
    }

    public void setApplyTypeName(String applyTypeName) {
        this.applyTypeName = applyTypeName;
    }

    public Order_after_detail getOrderAfterDetail() {
        return orderAfterDetail;
    }

    public void setOrderAfterDetail(Order_after_detail orderAfterDetail) {
        this.orderAfterDetail = orderAfterDetail;
    }

    public Order_main getOrderMain() {
        return orderMain;
    }

    public void setOrderMain(Order_main orderMain) {
        this.orderMain = orderMain;
    }
}
