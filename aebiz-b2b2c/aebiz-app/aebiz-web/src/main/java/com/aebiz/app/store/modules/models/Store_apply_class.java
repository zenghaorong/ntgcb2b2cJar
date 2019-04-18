package com.aebiz.app.store.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 店铺类型申请表
 * Created by wizzer on 2017/3/1.
 */
@Table("store_apply_class")
public class Store_apply_class extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("申请说明")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String applyNote;//多行文本框

    @Column
    @Comment("申请时间")
    @ColDefine(type = ColType.INT)
    private Integer applyAt;

    @Column
    @Comment("审核状态")
    @ColDefine(type = ColType.INT, width = 1)
    private int status;//0 待审核 1 审核通过 2 审核不通过

    @Column
    @Comment("审核说明")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String checkNote;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeId;

    @One(field = "storeId")
    private Store_main storeMain;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplyNote() {
        return applyNote;
    }

    public void setApplyNote(String applyNote) {
        this.applyNote = applyNote;
    }

    public void setApplyAt(Integer applyAt) {
        this.applyAt = applyAt;
    }

    public Integer getApplyAt() {
        return applyAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCheckNote() {
        return checkNote;
    }

    public void setCheckNote(String checkNote) {
        this.checkNote = checkNote;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Store_main getStoreMain() {
        return storeMain;
    }

    public void setStoreMain(Store_main storeMain) {
        this.storeMain = storeMain;
    }
}
