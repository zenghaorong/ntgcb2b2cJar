package com.aebiz.app.shop.modules.models;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.*;
import com.aebiz.baseframework.base.model.BaseModel;

/**
 * 商城货币单位
 */
@Table("shop_currency")
@TableIndexes({@Index(name = "INDEX_SHOP_CURRENCY", fields = {"currency"}, unique = true)})
public class Shop_currency extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("货币")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String currency;

    @Column
    @Comment("货币名称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String name;

    @Column
    @Comment("货币符号")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String symbol;

    @Column
    @Comment("汇率")
    @ColDefine(type = ColType.FLOAT)
    private Double rate;

    @Column
    @Comment("是否默认")
    private boolean defaultValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }
}
