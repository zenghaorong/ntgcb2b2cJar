package com.aebiz.app.store.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 商品库存提示规则表
 * Created by wizzer on 2017/3/7.
 */
@Table("store_stockprompt_values")
public class Store_stockprompt_values extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("最小值")
    @ColDefine(type = ColType.INT)
    private Integer minNum;

    @Column
    @Comment("最大值")
    @ColDefine(type = ColType.INT)
    private Integer maxNum;

    @Column
    @Comment("提示文字")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String note;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String stockpromptId;

    @One(field = "stockpromptId")
    private Store_stockprompt stockprompt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMinNum() {
        return minNum;
    }

    public void setMinNum(Integer minNum) {
        this.minNum = minNum;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStockpromptId() {
        return stockpromptId;
    }

    public void setStockpromptId(String stockpromptId) {
        this.stockpromptId = stockpromptId;
    }

    public Store_stockprompt getStockprompt() {
        return stockprompt;
    }

    public void setStockprompt(Store_stockprompt stockprompt) {
        this.stockprompt = stockprompt;
    }
}
