package com.aebiz.app.order.modules.models;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 订单商品评论表
 * Created by wizzer on 2017/3/29.
 */
@Table("order_goods_feedback")
@TableIndexes({@Index(name = "INDEX_ORDER_GOODS_FEEDBACK", fields = {"orderId", "productId"}, unique = false)})
public class Order_goods_feedback extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("订单ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String orderId;

    @Column
    @Comment("帐号ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String accountId;

    @Column
    @Comment("商城ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeId;

    @Column
    @Comment("商品评分")
    @ColDefine(type = ColType.INT, width = 11)
    private Integer appScore;

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
    @Comment("购买规格")
    @ColDefine(type = ColType.VARCHAR, width = 1000)
    private String spec;//json字符串

    @Column
    @Comment("评价时间")
    @ColDefine(type = ColType.INT)
    private Integer feedAt;

    @Column
    @Comment("评价内容")
    @ColDefine(type = ColType.VARCHAR, width = 1000)
    private String feedNote;//500字以内

    @Column
    @Comment("评价图片")
    @ColDefine(type = ColType.VARCHAR, width = 1000)
    private String feedImage;//以,分割,不要带域名的相对路径

    @Column
    @Comment("评价是否公开")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean feedOpen;

    @Column
    @Comment("回复内容")
    @ColDefine(type = ColType.VARCHAR, width = 1000)
    private String storeReplyNote;//500字以内

    @Column
    @Comment("回复时间")
    @ColDefine(type = ColType.INT)
    private Integer storeReplyAt;

    @Column
    @Comment("店铺用户ID")
    @ColDefine(type = ColType.INT)
    private Integer storeReplyUserId;

    @One(field = "accountId")
    private Account_info accountInfo;

    @One(field = "goodsId")
    private Goods_main goodsMain;

    @One(field = "productId")
    private Goods_product goodsProduct;

    @One(field = "orderId")
    private Order_main orderMain;

    @One(field = "accountId",key = "accountId")
    private Account_user accountUser;

    public Integer getAppScore() {
        return appScore;
    }

    public void setAppScore(Integer appScore) {
        this.appScore = appScore;
    }
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

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Integer getFeedAt() {
        return feedAt;
    }

    public void setFeedAt(Integer feedAt) {
        this.feedAt = feedAt;
    }

    public String getFeedNote() {
        return feedNote;
    }

    public void setFeedNote(String feedNote) {
        this.feedNote = feedNote;
    }

    public String getFeedImage() {
        return feedImage;
    }

    public void setFeedImage(String feedImage) {
        this.feedImage = feedImage;
    }

    public boolean isFeedOpen() {
        return feedOpen;
    }

    public void setFeedOpen(boolean feedOpen) {
        this.feedOpen = feedOpen;
    }

    public String getStoreReplyNote() {
        return storeReplyNote;
    }

    public void setStoreReplyNote(String storeReplyNote) {
        this.storeReplyNote = storeReplyNote;
    }

    public Integer getStoreReplyAt() {
        return storeReplyAt;
    }

    public void setStoreReplyAt(Integer storeReplyAt) {
        this.storeReplyAt = storeReplyAt;
    }

    public Integer getStoreReplyUserId() {
        return storeReplyUserId;
    }

    public void setStoreReplyUserId(Integer storeReplyUserId) {
        this.storeReplyUserId = storeReplyUserId;
    }

    public Account_info getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(Account_info accountInfo) {
        this.accountInfo = accountInfo;
    }

    public Account_user getAccountUser() {
        return accountUser;
    }

    public void setAccountUser(Account_user accountUser) {
        this.accountUser = accountUser;
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

    public Order_main getOrderMain() {
        return orderMain;
    }

    public void setOrderMain(Order_main orderMain) {
        this.orderMain = orderMain;
    }
}
