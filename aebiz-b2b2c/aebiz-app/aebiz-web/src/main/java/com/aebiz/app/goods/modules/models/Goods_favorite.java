package com.aebiz.app.goods.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 商品收藏表
 * Created by Thinkpad on 2017/6/14.
 */
@Table("goods_favorite")
public class Goods_favorite extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("帐号ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String accountId;   //account_info 表中的id

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
    @Comment("货品名")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String productName;

    @Column
    @Comment("SKU")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String sku;

    @Column
    @Comment("货品版本号")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer productVersion;

    @Column
    @Comment("收藏时价格")
    @ColDefine(type = ColType.INT)
    private Integer favoritePrice;

    @Column
    @Comment("收藏时间")
    @ColDefine(type = ColType.INT)
    private Integer favoriteTime;

    @Column
    @Comment("取消收藏时间")
    @ColDefine(type = ColType.INT)
    private Integer unFavoriteTime;

    @Comment("商品图片")
    @ColDefine(type = ColType.VARCHAR)
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getProductVersion() {
        return productVersion;
    }
    public void setProductVersion(Integer productVersion) {
        this.productVersion = productVersion;
    }

    public Integer getFavoritePrice() {
        return favoritePrice;
    }
    public void setFavoritePrice(Integer favoritePrice) {
        this.favoritePrice = favoritePrice;
    }

    public Integer getFavoriteTime() {
        return favoriteTime;
    }
    public void setFavoriteTime(Integer favoriteTime) {
        this.favoriteTime = favoriteTime;
    }

    public Integer getUnFavoriteTime() {
        return unFavoriteTime;
    }
    public void setUnFavoriteTime(Integer unFavoriteTime) {
        this.unFavoriteTime = unFavoriteTime;
    }
}
