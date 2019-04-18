package com.aebiz.app.store.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 品牌申请表
 * Created by wizzer on 2017/3/1.
 */
@Table("store_apply_brand")
public class Store_apply_brand extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("申请说明")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String applyNote;//多行文本框

    @Column
    @Comment("申请时间")
    @ColDefine(type = ColType.INT)
    private Integer applyAt;

    @Column
    @Comment("审核状态")
    @ColDefine(type = ColType.INT, width = 1)
    private int status;//0 待审核 1 审核通过 2 审核不通过

    @Column
    @Comment("审核说明")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String checkNote;

    @Column
    @Comment("品牌名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String brandName;

    @Column
    @Comment("品牌别名")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String aliasName;//页面提示: 用 , 分割

    @Column
    @Comment("品牌网址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String url;

    @Column
    @Comment("Logo")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String imgurl;

    @Column
    @Comment("品牌介绍")
    @ColDefine(type = ColType.TEXT)
    private String note;

    @Column
    @Comment("排序字段")
    @Prev({
            @SQL(db = DB.MYSQL, value = "SELECT IFNULL(MAX(location),0)+1 FROM goods_brand"),
            @SQL(db = DB.ORACLE, value = "SELECT COALESCE(MAX(location),0)+1 FROM goods_brand")
    })
    private Integer location;

    @Many(target = Store_apply_type_brand.class, field = "applyBrandId")
    private List<Store_apply_type_brand> typeList;


    @Column
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeId;

    @One(field = "storeId")
    private Store_main storeMain;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplyNote() {
        return applyNote;
    }

    public void setApplyNote(String applyNote) {
        this.applyNote = applyNote;
    }

    public void setApplyAt(Integer applyAt) {
        this.applyAt = applyAt;
    }

    public Integer getApplyAt() {
        return applyAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCheckNote() {
        return checkNote;
    }

    public void setCheckNote(String checkNote) {
        this.checkNote = checkNote;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public List<Store_apply_type_brand> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<Store_apply_type_brand> typeList) {
        this.typeList = typeList;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Store_main getStoreMain() {
        return storeMain;
    }

    public void setStoreMain(Store_main storeMain) {
        this.storeMain = storeMain;
    }
}
