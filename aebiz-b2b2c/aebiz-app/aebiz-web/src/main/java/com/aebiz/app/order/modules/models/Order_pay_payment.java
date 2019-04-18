package com.aebiz.app.order.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 订单支付表
 * Created by wizzer on 2017/3/31.
 */
@Table("order_pay_payment")
public class Order_pay_payment extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("订单组ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String groupId;

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
    @Comment("支付金额")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer payMoney;

    @Column
    @Comment("网上支付code")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String payCode;//shop_payment 表的code值

    @Column
    @Comment("网上支付名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String payName;//shop_payment 表的name值

    @Column
    @Comment("支付帐号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String payAccount;

    @Column
    @Comment("IP地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String payIp;

    @Column
    @Comment("回调地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String return_url;

    @Column
    @Comment("交易流水号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String trade_no;

    @Column
    @Comment("支付备注")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String note;

    @Column
    @Comment("支付时间")
    @ColDefine(type = ColType.INT)
    private Integer payAt;

    @Column
    @Comment("完成时间")
    @ColDefine(type = ColType.INT)
    private Integer finishAt;

    @Column
    @Comment("支付成功")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean paySucess;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public Integer getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Integer payMoney) {
        this.payMoney = payMoney;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getPayIp() {
        return payIp;
    }

    public void setPayIp(String payIp) {
        this.payIp = payIp;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getPayAt() {
        return payAt;
    }

    public void setPayAt(Integer payAt) {
        this.payAt = payAt;
    }

    public Integer getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(Integer finishAt) {
        this.finishAt = finishAt;
    }

    public boolean isPaySucess() {
        return paySucess;
    }

    public void setPaySucess(boolean paySucess) {
        this.paySucess = paySucess;
    }
}
