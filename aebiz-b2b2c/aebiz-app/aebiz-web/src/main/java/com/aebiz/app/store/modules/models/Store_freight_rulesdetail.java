package com.aebiz.app.store.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 物流运费规则明细表
 * Created by wizzer on 2017/3/1.
 */
@Table("Store_freight_rulesdetail")
public class Store_freight_rulesdetail extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("配送区域名称")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String areaName;

    @Column
    @Comment("配送片区Code")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    @Default("")
    private String areaCode;

    @Column
    @Comment("商品分类名称")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String goodsName;

    @Column
    @Comment("商品分类ID")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    @Default("")
    private String goodsId;

    @Column
    @Comment("计价方式")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String valuationType;//1--件 2--体重 3--体积

    @Column
    @Comment("物流公司名称")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String logisticsName;

    @Column
    @Comment("物流公司Code")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    @Default("")
    private String logisticsCode;

    @Column
    @Comment("首件")
    @ColDefine(type = ColType.INT)
    private Integer firstUnit;

    @Column
    @Comment("首费")
    @ColDefine(type = ColType.INT)
    private Integer firstCost;

    @Column
    @Comment("续件")
    @ColDefine(type = ColType.INT)
    private Integer continueUnit;

    @Column
    @Comment("续费")
    @ColDefine(type = ColType.INT)
    private Integer continueCost;

    @Column
    @Comment("模板ID")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String templateId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getValuationType() {
        return valuationType;
    }

    public void setValuationType(String valuationType) {
        this.valuationType = valuationType;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    public String getLogisticsCode() {
        return logisticsCode;
    }

    public void setLogisticsCode(String logisticsCode) {
        this.logisticsCode = logisticsCode;
    }

    public Integer getFirstUnit() {
        return firstUnit;
    }

    public void setFirstUnit(Integer firstUnit) {
        this.firstUnit = firstUnit;
    }

    public Integer getFirstCost() {
        return firstCost;
    }

    public void setFirstCost(Integer firstCost) {
        this.firstCost = firstCost;
    }

    public Integer getContinueUnit() {
        return continueUnit;
    }

    public void setContinueUnit(Integer continueUnit) {
        this.continueUnit = continueUnit;
    }

    public Integer getContinueCost() {
        return continueCost;
    }

    public void setContinueCost(Integer continueCost) {
        this.continueCost = continueCost;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
