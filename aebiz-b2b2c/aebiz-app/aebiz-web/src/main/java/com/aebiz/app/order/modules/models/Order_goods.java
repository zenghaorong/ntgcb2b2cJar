package com.aebiz.app.order.modules.models;

import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 订单商品详情表
 * Created by wizzer on 2017/3/29.
 */
@Table("order_goods")
@TableIndexes({@Index(name = "INDEX_ORDER_GOODS_ORDER", fields = {"orderId"}, unique = false)
,@Index(name = "INDEX_ORDER_GOODS_ACC", fields = {"accountId"}, unique = false)})
public class Order_goods extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("订单ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String orderId;

    @Column
    @Comment("订单类型")
    @ColDefine(type = ColType.VARCHAR, width = 10)
    private String orderType; //1.商品订单 2.视频订单

    @Column
    @Comment("帐号ID")
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
    @Comment("商品名")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String goodsName;

    @Column
    @Comment("商品版本号")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer goodsVersion;

    @Column
    @Comment("货品ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String productId;

    @Column
    @Comment("SKU")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String sku;

    @Column
    @Comment("货品名")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String name;

    @Column
    @Comment("购买规格")
    @ColDefine(type = ColType.TEXT)
    private String spec;//json字符串

    @Column
    @Comment("货品版本号")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer productVersion;

    @Column
    @Comment("购买数量")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer buyNum;

    @Column
    @Comment("销售价")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer salePrice;

    @Column
    @Comment("购买价")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer buyPrice;//通过区域、销售对象、销售终端、会员等级等算出的购买价格

    @Column
    @Comment("总重量")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer totalWeight;

    @Column
    @Comment("总金额")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer totalMoney;

    @Column
    @Comment("商品优惠")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer freeMoney;//商品优惠，比如组合商品优惠

    @Column
    @Comment("应付金额")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer payMoney;

    @Column
    @Comment("获得积分")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer score;

    @Column
    @Comment("发货数量")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer sendNum;

    @Column
    @Comment("评价分数")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer feedScore;

    @Column
    @Comment("评价时间")
    @ColDefine(type = ColType.INT)
    private Integer feedAt;

    private Account_user accountUser;

    @One(field = "orderId")
    private Order_main orderMain;

    @One(field = "storeId")
    private Store_main storeMain;

    @One(field = "goodsId")
    private Goods_main goodsMain;

    private String imgUrl;//货品图片文件服务器地址

    private Integer deliveryTime;//货品的发货时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsVersion() {
        return goodsVersion;
    }

    public void setGoodsVersion(Integer goodsVersion) {
        this.goodsVersion = goodsVersion;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Integer getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(Integer productVersion) {
        this.productVersion = productVersion;
    }

    public Integer getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Integer buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Integer getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Integer totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Integer getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Integer totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getFreeMoney() {
        return freeMoney;
    }

    public void setFreeMoney(Integer freeMoney) {
        this.freeMoney = freeMoney;
    }

    public Integer getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Integer payMoney) {
        this.payMoney = payMoney;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getSendNum() {
        return sendNum;
    }

    public void setSendNum(Integer sendNum) {
        this.sendNum = sendNum;
    }

    public Integer getFeedScore() {
        return feedScore;
    }

    public void setFeedScore(Integer feedScore) {
        this.feedScore = feedScore;
    }

    public Integer getFeedAt() {
        return feedAt;
    }

    public void setFeedAt(Integer feedAt) {
        this.feedAt = feedAt;
    }

    public Account_user getAccountUser() {
        return accountUser;
    }

    public void setAccountUser(Account_user accountUser) {
        this.accountUser = accountUser;
    }

    public Order_main getOrderMain() {
        return orderMain;
    }

    public void setOrderMain(Order_main orderMain) {
        this.orderMain = orderMain;
    }

    public Store_main getStoreMain() {
        return storeMain;
    }

    public void setStoreMain(Store_main storeMain) {
        this.storeMain = storeMain;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Goods_main getGoodsMain() {
        return goodsMain;
    }

    public void setGoodsMain(Goods_main goodsMain) {
        this.goodsMain = goodsMain;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
