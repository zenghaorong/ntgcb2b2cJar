package com.aebiz.app.store.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 物流模板管理
 * Created by wanghuaixian on 2017/5/8.
 */
@Table("store_logistics_mode")
public class Store_logistics_mode extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("商户编号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String storeId;

    @Column
    @ColDefine(type = ColType.INT)
    @Comment("运费模板应用模式")
    @Default("0")
    private Integer mode;//0--单品运费  1--店铺运费

    @Column
    @ColDefine(type = ColType.INT)
    @Comment("计算方式")
    @Default("0")
    private Integer calculation ;//0--按件数  1--按重量 2--按体积

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getCalculation() {
        return calculation;
    }

    public void setCalculation(Integer calculation) {
        this.calculation = calculation;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
