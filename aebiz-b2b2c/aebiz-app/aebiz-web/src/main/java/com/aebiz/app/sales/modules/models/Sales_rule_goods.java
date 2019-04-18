package com.aebiz.app.sales.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 商品促销表
 * Created by wizzer on 2017/6/7.
 */
@Table("sales_rule_goods")
public class Sales_rule_goods extends BaseModel implements Serializable {
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
    @Comment("规则名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String name;

    @Column
    @Comment("规则描述")
    @ColDefine(type = ColType.TEXT)
    private String note;

    @Column
    @Comment("开始时间")
    @ColDefine(type = ColType.INT)
    private Integer sartAt;

    @Column
    @Comment("结束时间")
    @ColDefine(type = ColType.INT)
    private Integer endAt;

    @Column
    @Comment("生效区域")
    @ColDefine(type = ColType.TEXT)
    private String limit_areas;//区域划分方式+区域值 JSON字符串

    @Column
    @Comment("会员限制")
    @ColDefine(type = ColType.TEXT)
    private String limit_members;//会员类型+会员等级 JSON字符串

    @Column
    @Comment("生效终端")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String limit_client;//数组以,号分隔:  pc,wap,tv

    @Column
    @Comment("是否排它")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean limit_other;

    @Column
    @Comment("优先级")
    @ColDefine(type = ColType.INT)
    private Integer limit_priority;//0-99 数字越大越优先

    @Column
    @Comment("是否禁用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    @Column
    @Comment("规则条件")
    @ColDefine(type = ColType.TEXT)
    private String conditions;

    @Column
    @Comment("动作执行条件")
    @ColDefine(type = ColType.TEXT)
    private String action_conditions;

    @Column
    @Comment("动作执行方案")
    @ColDefine(type = ColType.TEXT)
    private String action_solution;

    @Column
    @Comment("过滤条件模板")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String cnd_template;

    @Column
    @Comment("优惠方案模板")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String sale_template;

    @Column
    @Comment("审核状态")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer checkStatus;//0 未提交  1 待审核  2 审核通过  3 未通过

    private Integer salesPrice;//促销价

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getSartAt() {
        return sartAt;
    }

    public void setSartAt(Integer sartAt) {
        this.sartAt = sartAt;
    }

    public Integer getEndAt() {
        return endAt;
    }

    public void setEndAt(Integer endAt) {
        this.endAt = endAt;
    }

    public String getLimit_areas() {
        return limit_areas;
    }

    public void setLimit_areas(String limit_areas) {
        this.limit_areas = limit_areas;
    }

    public String getLimit_members() {
        return limit_members;
    }

    public void setLimit_members(String limit_members) {
        this.limit_members = limit_members;
    }

    public String getLimit_client() {
        return limit_client;
    }

    public void setLimit_client(String limit_client) {
        this.limit_client = limit_client;
    }

    public boolean isLimit_other() {
        return limit_other;
    }

    public void setLimit_other(boolean limit_other) {
        this.limit_other = limit_other;
    }

    public Integer getLimit_priority() {
        return limit_priority;
    }

    public void setLimit_priority(Integer limit_priority) {
        this.limit_priority = limit_priority;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getAction_conditions() {
        return action_conditions;
    }

    public void setAction_conditions(String action_conditions) {
        this.action_conditions = action_conditions;
    }

    public String getAction_solution() {
        return action_solution;
    }

    public void setAction_solution(String action_solution) {
        this.action_solution = action_solution;
    }

    public String getCnd_template() {
        return cnd_template;
    }

    public void setCnd_template(String cnd_template) {
        this.cnd_template = cnd_template;
    }

    public String getSale_template() {
        return sale_template;
    }

    public void setSale_template(String sale_template) {
        this.sale_template = sale_template;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Integer getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Integer salesPrice) {
        this.salesPrice = salesPrice;
    }
}
