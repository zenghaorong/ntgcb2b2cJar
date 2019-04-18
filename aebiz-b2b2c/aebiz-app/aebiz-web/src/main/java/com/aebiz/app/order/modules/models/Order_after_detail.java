package com.aebiz.app.order.modules.models;

import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.lang.annotation.Target;
import java.util.List;

/**
 * 售后详情表
 * Created by 杨剑 on 2017/5/22.
 */
@Table("order_after_detail")
public class Order_after_detail extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("售后单号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String afterSaleId;

    @Column
    @Comment("申请售后的数量")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer afterSaleNum;

    @Column
    @Comment("订单明细id")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String orderGoodsId;

    @Column
    @Comment("商品ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String goodsId;

    @Column
    @Comment("SKU")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String sku;

    @One(target = Order_after_main.class, field = "afterSaleId")
    private Order_after_main orderAfterMain;

    @One(target = Goods_main.class, field = "goodsId")
    private Goods_main goodsMain;

    @One(target = Order_goods.class, field = "orderGoodsId")
    private Order_goods orderGoods;

    @One(target = Goods_product.class,field = "sku")
    private Goods_product goods_product;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAfterSaleId() {
        return afterSaleId;
    }

    public void setAfterSaleId(String afterSaleId) {
        this.afterSaleId = afterSaleId;
    }

    public Integer getAfterSaleNum() {
        return afterSaleNum;
    }

    public void setAfterSaleNum(Integer afterSaleNum) {
        this.afterSaleNum = afterSaleNum;
    }

    public String getOrderGoodsId() {
        return orderGoodsId;
    }

    public void setOrderGoodsId(String orderGoodsId) {
        this.orderGoodsId = orderGoodsId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Goods_product getGoods_product() {
        return goods_product;
    }

    public void setGoods_product(Goods_product goods_product) {
        this.goods_product = goods_product;
    }

    public Order_goods getOrderGoods() {
        return orderGoods;
    }

    public void setOrderGoods(Order_goods orderGoods) {
        this.orderGoods = orderGoods;
    }

    public Goods_main getGoodsMain() {
        return goodsMain;
    }

    public void setGoodsMain(Goods_main goodsMain) {
        this.goodsMain = goodsMain;
    }

    public Order_after_main getOrderAfterMain() {
        return orderAfterMain;
    }

    public void setOrderAfterMain(Order_after_main orderAfterMain) {
        this.orderAfterMain = orderAfterMain;
    }
}
