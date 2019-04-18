package com.aebiz.app.sales.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 优惠券使用表
 * Created by wizzer on 2017/6/7.
 */
@Table("sales_coupon_log")
public class Sales_coupon_log extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("商城ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeId;

    @Column
    @Comment("订单号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String orderId;

    @Column
    @Comment("帐号ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String accountId;

    @Column
    @Comment("优惠券ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String couponId;

    @Column
    @Comment("优惠券Code")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String couponCode;

    @Column
    @Comment("订单促销规则ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String ruleId;

    @Column
    @Comment("优惠券名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String couponName;

    @Column
    @Comment("使用时间")
    @ColDefine(type = ColType.INT)
    private Integer orderAt;

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Integer getOrderAt() {
        return orderAt;
    }

    public void setOrderAt(Integer orderAt) {
        this.orderAt = orderAt;
    }
}
