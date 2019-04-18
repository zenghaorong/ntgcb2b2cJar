package com.aebiz.app.order.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 退款表：当退款或者退货申请到退款这一步的时候会忘这张表添加数据，退款相关操作都针对这张表
 * Created by wy on 2017/5/26.
 * updated by yi on 2017/5/26.
 */

@Table("order_after_refundment")
public class Order_after_refundment extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Comment("ID")
    @Name
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("售后单号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String afterSaleId;

    @Column
    @Comment("订单编号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String orderId;

    @Column
    @Comment("商户编号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeId;

    @Column
    @Comment("申请人")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String applyMan;

    @Column
    @Comment("申请时间")
    @ColDefine(type = ColType.INT)
    private Integer applyTime;

    @Column
    @Comment("退款时间")
    @ColDefine(type = ColType.INT)
    private Integer refundTime;

    @Column
    @Comment("退款流水号")
    @ColDefine(type = ColType.VARCHAR, width = 64)
    private String refundSerialNum;

    @Column
    @Comment("退款原因")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String refundReason;

    @Column
    @Comment("退款备注")
    @ColDefine(type = ColType.TEXT)
    private String refundNote;

    @Column
    @Comment("退款方式")
    @ColDefine(type = ColType.INT)
    private Integer refundStyle;//OrderAfterBackMoneyStyleEnum

    private String refundStyleName = "";

    @Column
    @Comment("退款途径")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String refundMoneyWay;

    @Column
    @Comment("退款账号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String refundMoneyAccount;

    @Column
    @Comment("退款账号姓名")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String name;

    // 退款方式描述，仅做展示，不存表
    private String returnStyleDisc;

    @Column
    @Comment("退款状态")
    @ColDefine(type = ColType.INT)
    private Integer refundState;//OrderAfterRefundStateEnum

    private String refundStateName = "";

    @Column
    @Comment("退款金额")
    @ColDefine(type = ColType.INT)
    private Integer refundMoney;// 单位是分

    @Column
    @Comment("虚拟金额")
    @ColDefine(type = ColType.INT)
    private Integer virtualMoney; // 退给用户账户的虚拟金额,单位是分

    @One(field = "afterSaleId")
    private Order_after_main orderAfterMain;

    public Order_after_main getOrderAfterMain() {
        return orderAfterMain;
    }

    public void setOrderAfterMain(Order_after_main orderAfterMain) {
        this.orderAfterMain = orderAfterMain;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAfterSaleId() {
        return afterSaleId;
    }

    public void setAfterSaleId(String afterSaleId) {
        this.afterSaleId = afterSaleId;
    }

    public String getApplyMan() {
        return applyMan;
    }

    public void setApplyMan(String applyMan) {
        this.applyMan = applyMan;
    }

    public Integer getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Integer applyTime) {
        this.applyTime = applyTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRefundSerialNum() {
        return refundSerialNum;
    }

    public void setRefundSerialNum(String refundSerialNum) {
        this.refundSerialNum = refundSerialNum;
    }

    public Integer getVirtualMoney() {
        return virtualMoney;
    }

    public void setVirtualMoney(Integer virtualMoney) {
        this.virtualMoney = virtualMoney;
    }

    public String getReturnStyleDisc() {
        return returnStyleDisc;
    }

    public void setReturnStyleDisc(String returnStyleDisc) {
        this.returnStyleDisc = returnStyleDisc;
    }

    public Integer getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Integer refundTime) {
        this.refundTime = refundTime;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getRefundNote() {
        return refundNote;
    }

    public void setRefundNote(String refundNote) {
        this.refundNote = refundNote;
    }

    public Integer getRefundStyle() {
        return refundStyle;
    }

    public void setRefundStyle(Integer refundStyle) {
        this.refundStyle = refundStyle;
    }

    public Integer getRefundState() {
        return refundState;
    }

    public void setRefundState(Integer refundState) {
        this.refundState = refundState;
    }

    public Integer getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(Integer refundMoney) {
        this.refundMoney = refundMoney;
    }

    public String getRefundMoneyWay() {
        return refundMoneyWay;
    }

    public void setRefundMoneyWay(String refundMoneyWay) {
        this.refundMoneyWay = refundMoneyWay;
    }

    public String getRefundMoneyAccount() {
        return refundMoneyAccount;
    }

    public void setRefundMoneyAccount(String refundMoneyAccount) {
        this.refundMoneyAccount = refundMoneyAccount;
    }

    public String getRefundStyleName() {
        return refundStyleName;
    }

    public void setRefundStyleName(String refundStyleName) {
        this.refundStyleName = refundStyleName;
    }

    public String getRefundStateName() {
        return refundStateName;
    }

    public void setRefundStateName(String refundStateName) {
        this.refundStateName = refundStateName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
