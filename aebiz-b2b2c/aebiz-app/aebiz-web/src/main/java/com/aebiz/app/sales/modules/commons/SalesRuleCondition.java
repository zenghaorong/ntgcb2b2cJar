package com.aebiz.app.sales.modules.commons;

import java.util.List;

/**
 * 条件项
 * Created by hechao on 2017/7/17.
 */
public class SalesRuleCondition {

    private String type;
    private String aggregator;
    private String attr;
    private String op;
    private String value;
    private List<SalesRuleCondition> conditions;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAggregator() {
        return aggregator;
    }

    public void setAggregator(String aggregator) {
        this.aggregator = aggregator;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<SalesRuleCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<SalesRuleCondition> conditions) {
        this.conditions = conditions;
    }
}