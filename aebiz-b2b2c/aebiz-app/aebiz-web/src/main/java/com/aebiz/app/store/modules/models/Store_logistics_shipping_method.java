package com.aebiz.app.store.modules.models;

import com.aebiz.app.member.modules.models.Member_level;
import com.aebiz.app.shop.modules.models.Shop_express;
import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 物流运送方式
 * Created by wanghuaixian on 2017/5/9.
 */
@Table("Store_logistics_shipping_method")
public class Store_logistics_shipping_method extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("片区名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String areaName;

    @Column
    @Comment("片区Id")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String areaId;

    @Column
    @Comment("物流公司名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String logisticsName;

    @Column
    @Comment("物流公司Id")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String logisticsId;

    @Column
    @Comment("首件(件)")
    @ColDefine(type = ColType.INT)
    private Integer firstThing;

    @Column
    @Comment("首费(分)")
    @ColDefine(type = ColType.INT)
    private Integer firstCost;

    @Column
    @Comment("续件(件)")
    @ColDefine(type = ColType.INT)
    private Integer continueThing;

    @Column
    @Comment("续费(分)")
    @ColDefine(type = ColType.INT)
    private Integer continueCost;

    @Column
    @Comment("物流模板Id")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String modeId;

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

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    public String getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(String logisticsId) {
        this.logisticsId = logisticsId;
    }

    public Integer getFirstThing() {
        return firstThing;
    }

    public void setFirstThing(Integer firstThing) {
        this.firstThing = firstThing;
    }

    public Integer getFirstCost() {
        return firstCost;
    }

    public void setFirstCost(Integer firstCost) {
        this.firstCost = firstCost;
    }

    public Integer getContinueThing() {
        return continueThing;
    }

    public void setContinueThing(Integer continueThing) {
        this.continueThing = continueThing;
    }

    public Integer getContinueCost() {
        return continueCost;
    }

    public void setContinueCost(Integer continueCost) {
        this.continueCost = continueCost;
    }

    public String getModeId() {
        return modeId;
    }

    public void setModeId(String modeId) {
        this.modeId = modeId;
    }
}
