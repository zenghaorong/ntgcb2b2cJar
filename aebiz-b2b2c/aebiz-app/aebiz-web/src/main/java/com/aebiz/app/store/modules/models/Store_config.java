package com.aebiz.app.store.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 店铺配置表
 * Created by wizzer on 2017/3/1.
 */
@Table("store_config")
public class Store_config extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String id;

    @Column
    @Comment("注册协议")
    @ColDefine(type = ColType.TEXT)
    private String regNote;

    @Column
    @Comment("填写公司信息")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean nendCompanyInfo;

    @Column
    @Comment("填写开户银行信息")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean nendBankInfo;

    @Column
    @Comment("填写支付宝信息")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean nendAlipayInfo;

    @Column
    @Comment("填写微信信息")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean nendWechatInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegNote() {
        return regNote;
    }

    public void setRegNote(String regNote) {
        this.regNote = regNote;
    }

    public boolean isNendCompanyInfo() {
        return nendCompanyInfo;
    }

    public void setNendCompanyInfo(boolean nendCompanyInfo) {
        this.nendCompanyInfo = nendCompanyInfo;
    }

    public boolean isNendBankInfo() {
        return nendBankInfo;
    }

    public void setNendBankInfo(boolean nendBankInfo) {
        this.nendBankInfo = nendBankInfo;
    }

    public boolean isNendAlipayInfo() {
        return nendAlipayInfo;
    }

    public void setNendAlipayInfo(boolean nendAlipayInfo) {
        this.nendAlipayInfo = nendAlipayInfo;
    }

    public boolean isNendWechatInfo() {
        return nendWechatInfo;
    }

    public void setNendWechatInfo(boolean nendWechatInfo) {
        this.nendWechatInfo = nendWechatInfo;
    }
}
