package com.aebiz.app.goods.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 货品会员价格表
 * Created by wizzer on 2017/3/7.
 */
@Table("goods_price_level")
@TableIndexes({@Index(name = "INDEX_GOODS_PRICE_LEVEL", fields = {"priceId", "memberLevelId"}, unique = false)})
public class Goods_price_level extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("价格ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String priceId;

    @Column
    @Comment("等级ID")
    @ColDefine(type = ColType.VARCHAR)
    private String memberLevelId;

    @Column
    @Comment("折扣率")
    @ColDefine(type = ColType.INT)
    private Integer discount;

    @Column
    @Comment("等级价格")
    @ColDefine(type = ColType.INT)
    private Integer memberLevelPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public String getMemberLevelId() {
        return memberLevelId;
    }

    public void setMemberLevelId(String memberLevelId) {
        this.memberLevelId = memberLevelId;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getMemberLevelPrice() {
        return memberLevelPrice;
    }

    public void setMemberLevelPrice(Integer memberLevelPrice) {
        this.memberLevelPrice = memberLevelPrice;
    }
}
