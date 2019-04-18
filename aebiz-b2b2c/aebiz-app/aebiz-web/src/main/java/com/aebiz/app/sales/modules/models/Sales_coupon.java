package com.aebiz.app.sales.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2017/6/7.
 */
@Table("sales_coupon")
@TableIndexes({@Index(name = "INDEX_SALES_COUPON", fields = {"codeprefix"}, unique = true)})
public class Sales_coupon extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("商城ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeId;

    @Column
    @Comment("优惠券名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String name;

    @Column
    @Comment("编码前缀")//唯一,手动输入或生成随机
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String codeprefix;

    @Column
    @Comment("优惠券类型") //优惠券类型 1.订单满减 2.订单免运费 3.订单打折
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String type;
    //A.此类优惠券，顾客只需获得一张，即可在规定的时间内重复使用。
    //B.此类优惠券，顾客可一次获得多张，但在规定时间内每张只能使用一次，无法重复使用。只限会员使用

    @Column
    @Comment("可领取张数")
    @ColDefine(type = ColType.INT)
    @Default("1")
    private Integer limit_num;

    @Column
    @Comment("已发放数量")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer send_num;

    @Column
    @Comment("获取方式")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String send_type;//score--积分兑换  receive--领取(店铺领取或通过code领取)

    @Column
    @Comment("所需积分")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer score;

    @Column
    @Comment("审核状态")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer checkStatus;//0 未提交  1 待审核  2 审核通过  3 未通过

    @Column
    @Comment("是否禁用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    @Column
    @Comment("订单规则ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String ruleId;

    @One(target = Sales_rule_order.class, field = "ruleId")
    private Sales_rule_order salesRuleOrder;

    @Column
    @Comment("优惠劵可用开始时间")
    @ColDefine(type = ColType.INT)
    private Integer startTime;

    @Column
    @Comment("优惠劵可用结束时间")
    @ColDefine(type = ColType.INT)
    private Integer endTime;

    @Column
    @Comment("满减抵扣的金额")
    @ColDefine(type = ColType.FLOAT)
    private Double deductibleAmount;

    @Column
    @Comment("订单满多少 条件金额")
    @ColDefine(type = ColType.FLOAT)
    private Double conditionAmount;

    @Column
    @Comment("根据商品数量优惠")
    @ColDefine(type = ColType.INT)
    private Integer productQuantityRule;

    @Column
    @Comment("优惠劵折扣额")
    @ColDefine(type = ColType.FLOAT)
    private Double discount;

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Double getDeductibleAmount() {
        return deductibleAmount;
    }

    public void setDeductibleAmount(Double deductibleAmount) {
        this.deductibleAmount = deductibleAmount;
    }

    public Double getConditionAmount() {
        return conditionAmount;
    }

    public void setConditionAmount(Double conditionAmount) {
        this.conditionAmount = conditionAmount;
    }

    public Integer getProductQuantityRule() {
        return productQuantityRule;
    }

    public void setProductQuantityRule(Integer productQuantityRule) {
        this.productQuantityRule = productQuantityRule;
    }

    private boolean hasReceive;//是否领取

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeprefix() {
        return codeprefix;
    }

    public void setCodeprefix(String codeprefix) {
        this.codeprefix = codeprefix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLimit_num() {
        return limit_num;
    }

    public void setLimit_num(Integer limit_num) {
        this.limit_num = limit_num;
    }

    public Integer getSend_num() {
        return send_num;
    }

    public void setSend_num(Integer send_num) {
        this.send_num = send_num;
    }

    public String getSend_type() {
        return send_type;
    }

    public void setSend_type(String send_type) {
        this.send_type = send_type;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public Sales_rule_order getSalesRuleOrder() {
        return salesRuleOrder;
    }

    public void setSalesRuleOrder(Sales_rule_order salesRuleOrder) {
        this.salesRuleOrder = salesRuleOrder;
    }

    public boolean isHasReceive() {
        return hasReceive;
    }

    public void setHasReceive(boolean hasReceive) {
        this.hasReceive = hasReceive;
    }
}
