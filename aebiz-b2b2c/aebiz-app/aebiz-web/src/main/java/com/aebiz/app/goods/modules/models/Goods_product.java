package com.aebiz.app.goods.modules.models;

import com.aebiz.app.member.modules.models.Member_type;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 商品规格货品表
 * 开发注意事项：
 * 1、商品每次修改都需要生成对应的快照表，通过mongoDB存储；
 * Created by wizzer on 2017/3/7.
 */
@Table("goods_product")
@TableIndexes({@Index(name = "INDEX_GOODS_PRODUCT_SG", fields = {"storeId", "goodsId"}, unique = false), @Index(name = "INDEX_GOODS_PRODUCT_SKU", fields = {"sku"}, unique = true)})
public class Goods_product extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("商场ID（预留）")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String mallId;

    @Column
    @Comment("商城ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeId;

    @Column
    @Comment("商品ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String goodsId;

    @Column
    @Comment("SKU")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String sku;

    @Column
    @Comment("货品名")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String name;

    @Column
    @Comment("货品规格")
    @ColDefine(type = ColType.TEXT)
    private String spec;

    @Column
    @Comment("成本价")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer costPrice;

    @Column
    @Comment("销售价")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer salePrice;

    @Column
    @Comment("市场价")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer marketPrice;

    @Column
    @Comment("重量")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer weight;

    @Column
    @Comment("长")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer length;

    @Column
    @Comment("宽")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer width;

    @Column
    @Comment("高")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer height;

    @Column
    @Comment("库存")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer stock;

    @Column
    @Comment("冻结库存")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer stockFreeze;

    @Column
    @Comment("最小购买量")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer buyMin;

    @Column
    @Comment("最大购买量")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer buyMax;

    @Column
    @Comment("是否上架")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean sale;

    @Column
    @Comment("默认货品")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean defaultValue;

    @Column
    @Comment("总销量")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer saleNumAll;

    @Column
    @Comment("月销量")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer saleNumMonth;

    @Column
    @Comment("版本号")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer productVersion;//每修改一次+1

    @Column
    @Comment("订单审核")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean orderVerify;

    @Column
    @Comment("线上商品")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean online;

    @Column
    @Comment("是否销售全部区域")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean saleToAllAera;

    @Column
    @Comment("区域划分方式")
    @ColDefine(type = ColType.INT)
    private Integer partitionBy;//1 按片区， 2 按省市

    @Column
    @Comment("版本号")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer saleToMemberType;

    @Column
    @Comment("是否销售全部对象")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean saleToAllMemberType;

    @Column
    @Comment("发货时间")
    @ColDefine(type = ColType.INT)
    private Integer deliveryTime;//0 当日发货 1 隔日发货

    @Column
    @Comment("启用序列号")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean serializable;

    @Column
    @Comment("排序字段")
    @Prev({
            @SQL(db = DB.MYSQL, value = "SELECT IFNULL(MAX(location),0)+1 FROM goods_product"),
            @SQL(db = DB.ORACLE, value = "SELECT COALESCE(MAX(location),0)+1 FROM goods_product")
    })
    private Integer location;

    @Column
    @Comment("是否启用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean enabled;

    @Many(target = Goods_product_area.class, field = "productId")
    private List<Goods_product_area> areaList;

    @One(target = Goods_main.class, field = "goodsId")
    private Goods_main goodsMain;

    @One(target = Store_main.class, field = "storeId")
    private Store_main storeMain;

    @ManyMany(from = "productId", relation = "goods_product_member_type", to = "memberTypeId")
    private List<Member_type> memberTypeList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMallId() {
        return mallId;
    }

    public void setMallId(String mallId) {
        this.mallId = mallId;
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

    public Integer getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Integer costPrice) {
        this.costPrice = costPrice;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Integer marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockFreeze() {
        return stockFreeze;
    }

    public void setStockFreeze(Integer stockFreeze) {
        this.stockFreeze = stockFreeze;
    }

    public Integer getBuyMin() {
        return buyMin;
    }

    public void setBuyMin(Integer buyMin) {
        this.buyMin = buyMin;
    }

    public Integer getBuyMax() {
        return buyMax;
    }

    public void setBuyMax(Integer buyMax) {
        this.buyMax = buyMax;
    }

    public boolean isSale() {
        return sale;
    }

    public void setSale(boolean sale) {
        this.sale = sale;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getSaleNumAll() {
        return saleNumAll;
    }

    public void setSaleNumAll(Integer saleNumAll) {
        this.saleNumAll = saleNumAll;
    }

    public Integer getSaleNumMonth() {
        return saleNumMonth;
    }

    public void setSaleNumMonth(Integer saleNumMonth) {
        this.saleNumMonth = saleNumMonth;
    }

    public Integer getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(Integer productVersion) {
        this.productVersion = productVersion;
    }

    public boolean isOrderVerify() {
        return orderVerify;
    }

    public void setOrderVerify(boolean orderVerify) {
        this.orderVerify = orderVerify;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public boolean isSaleToAllAera() {
        return saleToAllAera;
    }

    public void setSaleToAllAera(boolean saleToAllAera) {
        this.saleToAllAera = saleToAllAera;
    }

    public Integer getPartitionBy() {
        return partitionBy;
    }

    public void setPartitionBy(Integer partitionBy) {
        this.partitionBy = partitionBy;
    }
    public Integer getSaleToMemberType() {
        return saleToMemberType;
    }

    public void setSaleToMemberType(Integer saleToMemberType) {
        this.saleToMemberType = saleToMemberType;
    }
    public boolean isSaleToAllMemberType() {
        return saleToAllMemberType;
    }

    public void setSaleToAllMemberType(boolean saleToAllMemberType) {
        this.saleToAllMemberType = saleToAllMemberType;
    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public boolean isSerializable() {
        return serializable;
    }

    public void setSerializable(boolean serializable) {
        this.serializable = serializable;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Goods_product_area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Goods_product_area> areaList) {
        this.areaList = areaList;
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

    public List<Member_type> getMemberTypeList() {
        return memberTypeList;
    }

    public void setMemberTypeList(List<Member_type> memberTypeList) {
        this.memberTypeList = memberTypeList;
    }
}
