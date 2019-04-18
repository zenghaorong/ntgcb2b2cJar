package com.aebiz.app.store.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 店铺等级表
 * Created by wizzer on 2017/3/1.
 */
@Table("store_level")
public class Store_level extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("等级名称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String name;

    @Column
    @Comment("可发布商品数量")
    @ColDefine(type = ColType.INT)
    private Integer goodsLimit;//0 表示没有限制

    @Column
    @Comment("级别")
    @ColDefine(type = ColType.INT)
    private Integer level;//数字越大级别越高

    @Column
    @Comment("收费标准")
    @ColDefine(type = ColType.INT)
    private Integer price;//收费标准，在会员开通或升级店铺时将显示在前台

    @Column
    @Comment("佣金比例")
    @ColDefine(type = ColType.FLOAT)
    private Double rate;//店铺等级越高佣金比例越小

    @Column
    @Comment("申请说明")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String note;//在会员开通或升级店铺时将显示在前台

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGoodsLimit() {
        return goodsLimit;
    }

    public void setGoodsLimit(Integer goodsLimit) {
        this.goodsLimit = goodsLimit;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
