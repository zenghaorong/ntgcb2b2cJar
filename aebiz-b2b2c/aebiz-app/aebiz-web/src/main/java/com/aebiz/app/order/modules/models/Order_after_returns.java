package com.aebiz.app.order.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 买家的退/换货申请被卖家同意后，买家填写的退货信息表,简称退货单
 * Created by yangjian on 2017/6/5.
 */
@Table("order_after_returns")
public class Order_after_returns extends BaseModel implements Serializable {
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
    @Comment("备注说明")
    @ColDefine(type = ColType.VARCHAR, width = 140)
    private String note;

    @Column
    @Comment("凭证1")
    @ColDefine(type = ColType.VARCHAR)
    private String evidence1;

    @Column
    @Comment("凭证2")
    @ColDefine(type = ColType.VARCHAR)
    private String evidence2;

    @Column
    @Comment("凭证3")
    @ColDefine(type = ColType.VARCHAR)
    private String evidence3;

    @Column
    @Comment("物流公司")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String logisticsCompany;

    @Column
    @Comment("物流单号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String logisticsSheetId;

    @Column
    @Comment("退货人")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String returnsMan;

    @Column
    @Comment("退货人ID")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String returnsManId;

    @Column
    @Comment("退货时间")
    @ColDefine(type = ColType.INT)
    private Integer returnsTime;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getLogisticsSheetId() {
        return logisticsSheetId;
    }

    public void setLogisticsSheetId(String logisticsSheetId) {
        this.logisticsSheetId = logisticsSheetId;
    }

    public String getReturnsMan() {
        return returnsMan;
    }

    public void setReturnsMan(String returnsMan) {
        this.returnsMan = returnsMan;
    }

    public String getReturnsManId() {
        return returnsManId;
    }

    public void setReturnsManId(String returnsManId) {
        this.returnsManId = returnsManId;
    }

    public Integer getReturnsTime() {
        return returnsTime;
    }

    public void setReturnsTime(Integer returnsTime) {
        this.returnsTime = returnsTime;
    }

}
