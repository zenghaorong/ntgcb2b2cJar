package com.aebiz.app.order.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 订单优惠信息表
 * 开发注意事项：
 * 1、每个店铺订单可能存在多条优惠信息，比如满减、优惠券等；
 * 2、平台级的优惠活动，都是关联到具体店铺订单的；
 * Created by wizzer on 2017/3/29.
 */
@Table("order_main_discount")
@TableIndexes({@Index(name = "INDEX_ORDER_MAIN_DISCOUNT_ORDER", fields = {"orderId"}, unique = false),
        @Index(name = "INDEX_ORDER_MAIN_DISCOUNT_FREE", fields = {"ruleId"}, unique = false)})
public class Order_main_discount extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("订单ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String orderId;

    @Column
    @Comment("订单组ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String groupId;

    @Column
    @Comment("帐号ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String accountId;

    @Column
    @Comment("商城ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeId;

    @Column
    @Comment("优惠类型")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String ruleType;//order--订单促销  coupon--优惠券

    @Column
    @Comment("优惠ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String ruleId;

    @Column
    @Comment("优惠金额")
    @ColDefine(type = ColType.INT)
    private int freeMoney;

    @Column
    @Comment("赠品ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String giftId;

    @Column
    @Comment("优惠描述")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String note;

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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public int getFreeMoney() {
        return freeMoney;
    }

    public void setFreeMoney(int freeMoney) {
        this.freeMoney = freeMoney;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
