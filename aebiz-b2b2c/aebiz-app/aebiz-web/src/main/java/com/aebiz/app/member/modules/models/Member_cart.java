package com.aebiz.app.member.modules.models;

import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.sales.modules.models.Sales_rule_goods;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 会员购物车表
 * Created by wizzer on 2017/3/29.
 */
@Table("member_cart")
@TableIndexes({@Index(name = "INDEX_MEMBER_CART_ACCOUNT", fields = {"accountId"}, unique = false)})
public class Member_cart extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("会员ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String accountId;

    @Column
    @Comment("商城ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeId;

    @Column
    @Comment("商品ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String goodsId;

    @Column
    @Comment("货品ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String productId;

    @Column
    @Comment("SKU")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String sku;

    @Column
    @Comment("数量")
    @ColDefine(type = ColType.INT)
    private Integer num;

    @Column
    @Comment("购买价")
    @ColDefine(type = ColType.INT)
    private Integer price;

    @Column
    @Comment("商品图片")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String imgurl;

    @One(target = Store_main.class, field = "storeId")
    private Store_main storeMain;

    @One(target = Goods_main.class, field = "goodsId")
    private Goods_main goodsMain;

    @One(target = Goods_product.class, field = "productId")
    private Goods_product goodsProduct;

    private List<Sales_rule_goods> salesRuleGoodsList;

    private Integer  salesPrice;

    private String salesId;//当前选择的促销Id

    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Store_main getStoreMain() {
        return storeMain;
    }

    public void setStoreMain(Store_main storeMain) {
        this.storeMain = storeMain;
    }

    public Goods_main getGoodsMain() {
        return goodsMain;
    }

    public void setGoodsMain(Goods_main goodsMain) {
        this.goodsMain = goodsMain;
    }

    public Goods_product getGoodsProduct() {
        return goodsProduct;
    }

    public void setGoodsProduct(Goods_product goodsProduct) {
        this.goodsProduct = goodsProduct;
    }

    public List<Sales_rule_goods> getSalesRuleGoodsList() {
        return salesRuleGoodsList;
    }

    public void setSalesRuleGoodsList(List<Sales_rule_goods> salesRuleGoodsList) {
        this.salesRuleGoodsList = salesRuleGoodsList;
    }

    public Integer getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Integer salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }
}
