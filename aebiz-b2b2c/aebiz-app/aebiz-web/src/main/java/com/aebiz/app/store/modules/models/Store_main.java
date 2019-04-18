package com.aebiz.app.store.modules.models;

import com.aebiz.app.goods.modules.models.Goods_class;
import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 店铺信息表
 * Created by wizzer on 2017/3/1.
 */
@Table("store_main")
public class Store_main extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("商场ID（预留）")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String mallId;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String userId;

    @Column
    @Comment("店铺名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String storeName;

    @Column
    @Comment("店铺分类")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String classId;

    @Column
    @Comment("店铺等级")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String levelId;

    @Column
    @Comment("店铺类型")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String typeId;

    @Column
    @Comment("开店时长")
    @ColDefine(type = ColType.INT)
    private Integer joininYear;

    @Column
    @Comment("到期日期")
    @ColDefine(type = ColType.INT)
    private Integer endAt;

    @Column
    @Comment("店铺地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String storeAddress;

    @Column
    @Comment("店铺logo")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String logo;

    @Column
    @Comment("联系电话")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String storeTel;

    @Column
    @Comment("邮政编码")
    @ColDefine(type = ColType.VARCHAR, width = 10)
    private String storePostcode;

    @Column
    @Comment("所在省")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeProvince;

    @Column
    @Comment("所在市")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeCity;

    @Column
    @Comment("所在县区")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeCounty;

    @Column
    @Comment("所在街道")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeTown;

    @Column
    @Comment("是否关闭")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    @Column
    @Comment("是否推荐")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean recommend;

    @Column
    @Comment("是否自营")
    @ColDefine(type = ColType.BOOLEAN)
    @Default("0")
    private boolean self;

//    @Column
//    @ColDefine(type = ColType.VARCHAR, width = 255)
//    @Comment("登陆页Logo")
//    private String logo;

    @One(field = "classId")
    private Store_class storeClass;

    @One(field = "levelId")
    private Store_level storeLevel;

    @One(field = "typeId")
    private Store_type storeType;

    //商品分类
    @ManyMany(from = "storeId", relation = "store_goods_class", to = "classId")
    private List<Goods_class> goodsClasses;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public Integer getJoininYear() {
        return joininYear;
    }

    public void setJoininYear(Integer joininYear) {
        this.joininYear = joininYear;
    }

    public Integer getEndAt() {
        return endAt;
    }

    public void setEndAt(Integer endAt) {
        this.endAt = endAt;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreTel() {
        return storeTel;
    }

    public void setStoreTel(String storeTel) {
        this.storeTel = storeTel;
    }

    public String getStorePostcode() {
        return storePostcode;
    }

    public void setStorePostcode(String storePostcode) {
        this.storePostcode = storePostcode;
    }

    public String getStoreProvince() {
        return storeProvince;
    }

    public void setStoreProvince(String storeProvince) {
        this.storeProvince = storeProvince;
    }

    public String getStoreCity() {
        return storeCity;
    }

    public void setStoreCity(String storeCity) {
        this.storeCity = storeCity;
    }

    public String getStoreCounty() {
        return storeCounty;
    }

    public void setStoreCounty(String storeCounty) {
        this.storeCounty = storeCounty;
    }

    public String getStoreTown() {
        return storeTown;
    }

    public void setStoreTown(String storeTown) {
        this.storeTown = storeTown;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public Store_class getStoreClass() {
        return storeClass;
    }

    public void setStoreClass(Store_class storeClass) {
        this.storeClass = storeClass;
    }

    public Store_level getStoreLevel() {
        return storeLevel;
    }

    public void setStoreLevel(Store_level storeLevel) {
        this.storeLevel = storeLevel;
    }

    public Store_type getStoreType() {
        return storeType;
    }

    public void setStoreType(Store_type storeType) {
        this.storeType = storeType;
    }

    public List<Goods_class> getGoodsClasses() {
        return goodsClasses;
    }

    public void setGoodsClasses(List<Goods_class> goodsClasses) {
        this.goodsClasses = goodsClasses;
    }

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
