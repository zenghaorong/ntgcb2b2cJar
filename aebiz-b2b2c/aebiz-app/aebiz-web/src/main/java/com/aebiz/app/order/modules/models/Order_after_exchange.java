package com.aebiz.app.order.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 换货单（换货单id,售后单号,旧订单id,新订单id,物流公司,物流单号,换货状态,[备注说明,发货人,发货人id，发货时间,凭证1,凭证2,凭证3]）
 * Created by yangjian on 2017/6/5.
 */
@Table("order_after_exchange")
public class Order_after_exchange extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("换货单号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("售后单号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String afterSaleId;

    @Column
    @Comment("旧订单号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String oldOrder;

    @Column
    @Comment("新订单号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String newOrder;

    @Column
    @Comment("物流公司")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String logisticsCompany;

    @Column
    @Comment("物流单号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String logisticsId;

    @Column
    @Comment("换货状态")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String exchangeState;

    private String exchangeStateName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOldOrder() {
        return oldOrder;
    }

    public void setOldOrder(String oldOrder) {
        this.oldOrder = oldOrder;
    }

    public String getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(String newOrder) {
        this.newOrder = newOrder;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(String logisticsId) {
        this.logisticsId = logisticsId;
    }

    public String getExchangeState() {
        return exchangeState;
    }

    public void setExchangeState(String exchangeState) {
        this.exchangeState = exchangeState;
    }

    public String getExchangeStateName() {
        return exchangeStateName;
    }

    public void setExchangeStateName(String exchangeStateName) {
        this.exchangeStateName = exchangeStateName;
    }

    public String getAfterSaleId() {
        return afterSaleId;
    }

    public void setAfterSaleId(String afterSaleId) {
        this.afterSaleId = afterSaleId;
    }
}
