package com.aebiz.app.goods.modules.models;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.app.sys.modules.models.Sys_user;
import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 商品审核日志表
 * Created by hechao on 2017/05/10.
 */
@Table("goods_main_checklog")
public class Goods_main_checklog extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("商品ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String goodsId;

    @Column
    @Comment("申请商家ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeId;

    @Column
    @Comment("申请人ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String applicantId;

    @Column
    @Comment("审核人ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String checkerId;

    @Column
    @Comment("操作类型")
    @ColDefine(type = ColType.INT)
    private Integer opType;

    @Column
    @Comment("审核结果")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer checkStatus;//GoodsCheckStatusEnum

    @Column
    @Comment("意见")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String reason;

    @One(field = "storeId")
    private Store_main storeMain;

    @One(field = "applicantId")
    private Account_info applicant;

    @One(field = "checkerId")
    private Sys_user checker;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getCheckerId() {
        return checkerId;
    }

    public void setCheckerId(String checkerId) {
        this.checkerId = checkerId;
    }

    public Integer getOpType() {
        return opType;
    }

    public void setOpType(Integer opType) {
        this.opType = opType;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Store_main getStoreMain() {
        return storeMain;
    }

    public void setStoreMain(Store_main storeMain) {
        this.storeMain = storeMain;
    }

    public Account_info getApplicant() {
        return applicant;
    }

    public void setApplicant(Account_info applicant) {
        this.applicant = applicant;
    }

    public Sys_user getChecker() {
        return checker;
    }

    public void setChecker(Sys_user checker) {
        this.checker = checker;
    }
}
