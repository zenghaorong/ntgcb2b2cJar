package com.aebiz.app.order.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 订单退货表
 * Created by wizzer on 2017/4/5.
 */
@Table("order_return")
public class Order_return extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("发货单号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("配送方式")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer sendType;//OrderSendTypeEnum

    @Column
    @Comment("配送费用")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer sendMoney;

    @Column
    @Comment("物流公司ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String expressId;

    @Column
    @Comment("物流公司名称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String expressName;

    @Column
    @Comment("物流单号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String expressNo;

    @Column
    @Comment("送货省")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String deliveryProvince;

    @Column
    @Comment("送货市")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String deliveryCity;

    @Column
    @Comment("送货县区")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String deliveryCounty;

    @Column
    @Comment("送货街道")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String deliveryTown;

    @Column
    @Comment("送货地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String deliveryAddress;

    @Column
    @Comment("送货手机号")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String deliveryMobile;

    @Column
    @Comment("送货电话")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String deliveryPhone;

    @Column
    @Comment("邮政编码")
    @ColDefine(type = ColType.VARCHAR, width = 10)
    private String deliveryPostcode;

    @Column
    @Comment("单据生成时间")
    @ColDefine(type = ColType.INT)
    private Integer beginAt;

    @Column
    @Comment("单据结束时间")
    @ColDefine(type = ColType.INT)
    private Integer sendAt;

    @Column
    @Comment("单据确认时间")
    @ColDefine(type = ColType.INT)
    private Integer confirmAt;

    @Column
    @Comment("状态")
    @ColDefine(type = ColType.INT)
    private Integer status;//OrderSSendStatusEnum

    @Column
    @Comment("是否保价")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean protect;

    @Column
    @Comment("是否无效")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Integer getSendMoney() {
        return sendMoney;
    }

    public void setSendMoney(Integer sendMoney) {
        this.sendMoney = sendMoney;
    }

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getDeliveryProvince() {
        return deliveryProvince;
    }

    public void setDeliveryProvince(String deliveryProvince) {
        this.deliveryProvince = deliveryProvince;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getDeliveryCounty() {
        return deliveryCounty;
    }

    public void setDeliveryCounty(String deliveryCounty) {
        this.deliveryCounty = deliveryCounty;
    }

    public String getDeliveryTown() {
        return deliveryTown;
    }

    public void setDeliveryTown(String deliveryTown) {
        this.deliveryTown = deliveryTown;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryMobile() {
        return deliveryMobile;
    }

    public void setDeliveryMobile(String deliveryMobile) {
        this.deliveryMobile = deliveryMobile;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public String getDeliveryPostcode() {
        return deliveryPostcode;
    }

    public void setDeliveryPostcode(String deliveryPostcode) {
        this.deliveryPostcode = deliveryPostcode;
    }

    public Integer getBeginAt() {
        return beginAt;
    }

    public void setBeginAt(Integer beginAt) {
        this.beginAt = beginAt;
    }

    public Integer getSendAt() {
        return sendAt;
    }

    public void setSendAt(Integer sendAt) {
        this.sendAt = sendAt;
    }

    public Integer getConfirmAt() {
        return confirmAt;
    }

    public void setConfirmAt(Integer confirmAt) {
        this.confirmAt = confirmAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean isProtect() {
        return protect;
    }

    public void setProtect(boolean protect) {
        this.protect = protect;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
