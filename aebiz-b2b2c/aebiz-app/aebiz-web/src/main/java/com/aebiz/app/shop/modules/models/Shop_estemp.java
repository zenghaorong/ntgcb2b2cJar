package com.aebiz.app.shop.modules.models;

import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 搜索引擎临时数据表
 * Created by wizzer on 2016/9/27.
 */
@Table("shop_estemp")
public class Shop_estemp extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("商品ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String goodsId;

    @Column
    @Comment("操作类型")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String action;//create  update  delete

    @One(target = Goods_main.class, field = "goodsId")
    private Goods_main goods;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Goods_main getGoods() {
        return goods;
    }

    public void setGoods(Goods_main goods) {
        this.goods = goods;
    }
}
