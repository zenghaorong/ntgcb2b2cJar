package com.aebiz.app.order.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 订单组表
 * Created by wizzer on 2017/3/29.
 */
@Table("order_group")
@TableIndexes({@Index(name = "INDEX_ORDER_GROUP_ACC", fields = {"accountId"}, unique = false)})
public class Order_group extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("帐号ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String accountId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
