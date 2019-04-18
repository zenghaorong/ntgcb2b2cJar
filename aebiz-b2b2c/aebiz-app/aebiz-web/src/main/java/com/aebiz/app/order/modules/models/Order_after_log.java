package com.aebiz.app.order.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 售后日志记录表
 * Created by 杨剑 on 2017/5/22.
 */
@Table("order_after_log")
public class Order_after_log extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("售后单号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String afterSaleId;

    @Column
    @Comment("描述")
    @ColDefine(type = ColType.TEXT)
    private String description;

    @Column
    @Comment("凭证1")
    @ColDefine(type = ColType.TEXT)
    private String evidence1;

    @Column
    @Comment("凭证2")
    @ColDefine(type = ColType.TEXT)
    private String evidence2;

    @Column
    @Comment("凭证3")
    @ColDefine(type = ColType.TEXT)
    private String evidence3;

    @Column
    @Comment("处理时间")
    @ColDefine(type = ColType.INT,width = 10)
    private Integer handleTime;

    @Column
    @Comment("处理人")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String handleMan;

    @Column
    @Comment("处理人类型")
    @ColDefine(type = ColType.INT)
    private Integer handleType; //0平台，1自营，2商户，3会员

    // 非表字段，通过枚举获取到的处理人类型的名称。一般地：平台、商户、会员
    private String handleTypeName;

    @Column
    @Comment("售后状态")
    @ColDefine(type = ColType.INT)
    private Integer afterSaleState;

    //非表字段，售后状态名
    private String afterSaleStateName;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Integer handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleMan() {
        return handleMan;
    }

    public void setHandleMan(String handleMan) {
        this.handleMan = handleMan;
    }

    public Integer getHandleType() {
        return handleType;
    }

    public void setHandleType(Integer handleType) {
        this.handleType = handleType;
    }

    public Integer getAfterSaleState() {
        return afterSaleState;
    }

    public void setAfterSaleState(Integer afterSaleState) {
        this.afterSaleState = afterSaleState;
    }

    public String getHandleTypeName() {
        return handleTypeName;
    }

    public void setHandleTypeName(String handleTypeName) {
        this.handleTypeName = handleTypeName;
    }

    public String getAfterSaleStateName() {
        return afterSaleStateName;
    }

    public void setAfterSaleStateName(String afterSaleStateName) {
        this.afterSaleStateName = afterSaleStateName;
    }
}
