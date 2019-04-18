package com.aebiz.app.order.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 订单日志表
 * 开发注意事项：
 * 1、type=0时opBy记录会员ID，1时记录管理员ID，2时记录商城用户ID；
 * 2、取消订单等操作时，note报错操作内容；
 * Created by wizzer on 2017/3/31.
 */
@Table("order_log")
public class Order_log  extends BaseModel implements Serializable {
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
    @Comment("帐号ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String accountId;

    @Column
    @Comment("商城ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeId;

    @Column
    @Comment("日志内容")
    @ColDefine(type = ColType.TEXT)
    private String note;

    @Column
    @Comment("日志行为")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer behavior;//OrderLogBehaviorEnum

    @Column
    @Comment("日志操作人")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String opByName; //由于有多个账户体系,根据opBy不知道关联那张表的主键

    @Column
    @Comment("操作是否成功")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean success;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getBehavior() {
        return behavior;
    }

    public void setBehavior(Integer behavior) {
        this.behavior = behavior;
    }

    public String getOpByName() {
        return opByName;
    }

    public void setOpByName(String opByName) {
        this.opByName = opByName;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
