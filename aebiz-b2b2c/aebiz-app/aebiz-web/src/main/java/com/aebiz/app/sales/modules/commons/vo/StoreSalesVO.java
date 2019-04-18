package com.aebiz.app.sales.modules.commons.vo;

import java.io.Serializable;

/**
 * Created by ThinkPad on 2017/7/24.
 */
public class StoreSalesVO implements Serializable{

    private Integer type;//类型 0-订单促销 1-优惠券

    private String salesId;//促销规则的ID

    private Integer freeMoney; //优惠金额

    private Integer score; //赠送积分

    private Integer freightMoney;//邮费

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    public Integer getFreeMoney() {
        return freeMoney;
    }

    public void setFreeMoney(Integer freeMoney) {
        this.freeMoney = freeMoney;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getFreightMoney() {
        return freightMoney;
    }

    public void setFreightMoney(Integer freightMoney) {
        this.freightMoney = freightMoney;
    }
}
