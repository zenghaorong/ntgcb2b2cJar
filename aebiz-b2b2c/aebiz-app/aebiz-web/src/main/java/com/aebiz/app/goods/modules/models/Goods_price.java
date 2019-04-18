package com.aebiz.app.goods.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 商品价格表(开发时注意：一组组合只可配一个价格)
 * Created by wizzer on 2017/3/7.
 */
@Table("goods_price")
@TableIndexes({@Index(name = "INDEX_GOODS_PRICE", fields = {"storeId", "goodsId", "productId", "sku", "saleClient"}, unique = false)})
public class Goods_price extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

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
    @Comment("销售终端")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer saleClient;//GoodsSaleClientEnum

    @Column
    @Comment("销售区域(全部区域)")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean saleToAllAera;

    @Column
    @Comment("销售对象(会员类型)")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer saleToMemberType;//0 全部会员 or 会员类型ID

    @Column
    @Comment("销售片区")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String saleArea;

    @Column
    @Comment("销售省份")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String saleProvince;

    @Column
    @Comment("销售城市")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String saleCity;

    @Column
    @Comment("销售价")
    @ColDefine(type = ColType.INT)
    private Integer salePrice;

    @Column
    @Comment("是否禁用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    @Many(field = "priceId")
    private List<Goods_price_level> priceLevelList;

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

    public Integer getSaleClient() {
        return saleClient;
    }

    public void setSaleClient(Integer saleClient) {
        this.saleClient = saleClient;
    }

    public boolean isSaleToAllAera() {
        return saleToAllAera;
    }

    public void setSaleToAllAera(boolean saleToAllAera) {
        this.saleToAllAera = saleToAllAera;
    }

    public Integer getSaleToMemberType() {
        return saleToMemberType;
    }

    public void setSaleToMemberType(Integer saleToMemberType) {
        this.saleToMemberType = saleToMemberType;
    }

    public String getSaleArea() {
        return saleArea;
    }

    public void setSaleArea(String saleArea) {
        this.saleArea = saleArea;
    }

    public String getSaleProvince() {
        return saleProvince;
    }

    public void setSaleProvince(String saleProvince) {
        this.saleProvince = saleProvince;
    }

    public String getSaleCity() {
        return saleCity;
    }

    public void setSaleCity(String saleCity) {
        this.saleCity = saleCity;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public List<Goods_price_level> getPriceLevelList() {
        return priceLevelList;
    }

    public void setPriceLevelList(List<Goods_price_level> priceLevelList) {
        this.priceLevelList = priceLevelList;
    }
}
