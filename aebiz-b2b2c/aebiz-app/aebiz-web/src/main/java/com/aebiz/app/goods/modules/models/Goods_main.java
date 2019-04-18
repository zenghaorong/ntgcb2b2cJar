package com.aebiz.app.goods.modules.models;

import com.aebiz.app.store.modules.models.Store_goodsclass;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 商品主表
 * Created by wizzer on 2017/3/7.
 */
@Table("goods_main")
@TableIndexes({@Index(name = "INDEX_GOODS_MAIN_STOREID", fields = {"storeId"}, unique = false)})
public class Goods_main extends BaseModel implements Serializable {
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
    @Comment("商品名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String name;

    @Column
    @Comment("商品标题")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String title;

    @Column
    @Comment("商品分类")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String classId;

    @Column
    @Comment("商品类型")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String typeId;

    @Column
    @Comment("商品品牌")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String brandId;

    @Column
    @Comment("商品主图")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String imgList;

    @Column
    @Comment("商品详情")
    @ColDefine(type = ColType.TEXT)
    private String pcNote;

    @Column
    @Comment("手机详情")
    @ColDefine(type = ColType.TEXT)
    private String wapNote;

    @Column
    @Comment("电视详情")
    @ColDefine(type = ColType.TEXT)
    private String tvNote;

    @Column
    @Comment("属性详情")
    @ColDefine(type = ColType.TEXT)
    private String prop;

    @Column
    @Comment("规格详情")
    @ColDefine(type = ColType.TEXT)
    private String spec;

    @Column
    @Comment("参数详情")
    @ColDefine(type = ColType.TEXT)
    private String param;

    @Column
    @Comment("关键词")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String keywords;

    @Column
    @Comment("启用规格")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean hasSpec;

    @Column
    @Comment("计量单位")
    @ColDefine(type = ColType.VARCHAR, width = 25)
    private String unit;

    @Column
    @Comment("是否上架")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean sale;

    @Column
    @Comment("无库存可销售")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean saleZero;

    @Column
    @Comment("登录后显示价格")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean hiddenSalePrice;

    @Column
    @Comment("审核状态")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer checkStatus;//GoodsCheckStatusEnum

    @Column
    @Comment("推荐商品")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer recommend;//0 默认 1 推荐商品

    @Column
    @Comment("减库存方式")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer stockOffType;//GoodsStockOffTypeEnum

    @Column
    @Comment("定时上架")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean autoSale;

    @Column
    @Comment("定时上架时间")
    @ColDefine(type = ColType.INT)
    private Integer saleAt;

    @Column
    @Comment("定时下架")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean autoOff;

    @Column
    @Comment("定时下架时间")
    @ColDefine(type = ColType.INT)
    private Integer offAt;

    @Column
    @Comment("电脑端销售")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean saleClientPC;

    @Column
    @Comment("移动端销售")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean saleClientWAP;//含微信 WAP APP

    @Column
    @Comment("电视端销售")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean saleClientTV;

    @Column
    @Comment("版本号")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer goodsVersion;//每修改一次+1

    @Column
    @Comment("上架时间")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer saleTimeBy;//0 立即上架  1 暂不上架  2 定时上架

    @Column
    @Comment("状态")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer status;//GoodsStatusEnum

    @ManyMany(from = "goodsId", relation = "goods_main_tag", to = "tagId")
    private List<Goods_tag> tagList;

    @One(target = Goods_class.class, field = "classId")
    private Goods_class goodsClass;

    @One(target = Goods_type.class, field = "typeId")
    private Goods_type goodsType;

    @One(target = Goods_brand.class, field = "brandId")
    private Goods_brand goodsBrand;

    @One(target = Goods_product.class, field = "id")
    private Goods_product goods_product;

    @Many(field = "goodsId")
    private List<Goods_image> imageList;

    @Many(field = "goodsId")
    private List<Goods_product> productList;

    private String price;
    private String marketPrice;

    private String saleNumMonth;

    //用于下单时购买数量
    private String num;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getSaleNumMonth() {
        return saleNumMonth;
    }

    public void setSaleNumMonth(String saleNumMonth) {
        this.saleNumMonth = saleNumMonth;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @ManyMany(from = "goodsId", relation = "goods_main_store_goodsclass", to = "storeGoodsClassId")
    private List<Store_goodsclass> storeGoodsClassList;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getImgList() {
        return imgList;
    }

    public void setImgList(String imgList) {
        this.imgList = imgList;
    }

    public String getPcNote() {
        return pcNote;
    }

    public void setPcNote(String pcNote) {
        this.pcNote = pcNote;
    }

    public String getWapNote() {
        return wapNote;
    }

    public void setWapNote(String wapNote) {
        this.wapNote = wapNote;
    }

    public String getTvNote() {
        return tvNote;
    }

    public void setTvNote(String tvNote) {
        this.tvNote = tvNote;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public boolean isHasSpec() {
        return hasSpec;
    }

    public void setHasSpec(boolean hasSpec) {
        this.hasSpec = hasSpec;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isSale() {
        return sale;
    }

    public void setSale(boolean sale) {
        this.sale = sale;
    }

    public boolean isSaleZero() {
        return saleZero;
    }

    public void setSaleZero(boolean saleZero) {
        this.saleZero = saleZero;
    }

    public boolean isHiddenSalePrice() {
        return hiddenSalePrice;
    }

    public void setHiddenSalePrice(boolean hiddenSalePrice) {
        this.hiddenSalePrice = hiddenSalePrice;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Integer getRecommend() {
        return recommend;
    }

    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    public Integer getStockOffType() {
        return stockOffType;
    }

    public void setStockOffType(Integer stockOffType) {
        this.stockOffType = stockOffType;
    }

    public boolean isAutoSale() {
        return autoSale;
    }

    public void setAutoSale(boolean autoSale) {
        this.autoSale = autoSale;
    }

    public Integer getSaleAt() {
        return saleAt;
    }

    public void setSaleAt(Integer saleAt) {
        this.saleAt = saleAt;
    }

    public boolean isAutoOff() {
        return autoOff;
    }

    public void setAutoOff(boolean autoOff) {
        this.autoOff = autoOff;
    }

    public Integer getOffAt() {
        return offAt;
    }

    public void setOffAt(Integer offAt) {
        this.offAt = offAt;
    }

    public boolean isSaleClientPC() {
        return saleClientPC;
    }

    public void setSaleClientPC(boolean saleClientPC) {
        this.saleClientPC = saleClientPC;
    }

    public boolean isSaleClientWAP() {
        return saleClientWAP;
    }

    public void setSaleClientWAP(boolean saleClientWAP) {
        this.saleClientWAP = saleClientWAP;
    }

    public boolean isSaleClientTV() {
        return saleClientTV;
    }

    public void setSaleClientTV(boolean saleClientTV) {
        this.saleClientTV = saleClientTV;
    }

    public Integer getGoodsVersion() {
        return goodsVersion;
    }

    public void setGoodsVersion(Integer goodsVersion) {
        this.goodsVersion = goodsVersion;
    }

    public Integer getSaleTimeBy() {
        return saleTimeBy;
    }

    public void setSaleTimeBy(Integer saleTimeBy) {
        this.saleTimeBy = saleTimeBy;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Goods_tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Goods_tag> tagList) {
        this.tagList = tagList;
    }

    public Goods_class getGoodsClass() {
        return goodsClass;
    }

    public void setGoodsClass(Goods_class goodsClass) {
        this.goodsClass = goodsClass;
    }

    public Goods_type getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Goods_type goodsType) {
        this.goodsType = goodsType;
    }

    public Goods_brand getGoodsBrand() {
        return goodsBrand;
    }

    public void setGoodsBrand(Goods_brand goodsBrand) {
        this.goodsBrand = goodsBrand;
    }

    public List<Goods_image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Goods_image> imageList) {
        this.imageList = imageList;
    }

    public List<Goods_product> getProductList() {
        return productList;
    }

    public void setGoodsProducts(List<Goods_product> productList) {
        this.productList = productList;
    }

    public List<Store_goodsclass> getStoreGoodsClassList() {
        return storeGoodsClassList;
    }

    public void setStoreGoodsClassList(List<Store_goodsclass> storeGoodsClassList) {
        this.storeGoodsClassList = storeGoodsClassList;
    }

    public Goods_product getGoods_product() {
        return goods_product;
    }

    public void setGoods_product(Goods_product goods_product) {
        this.goods_product = goods_product;
    }
}
