package com.aebiz.app.goods.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 商品品牌表
 * Created by wizzer on 2016/9/27.
 */
@Table("goods_brand")
public class Goods_brand extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("品牌名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String name;

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

    @Many(target = Goods_type_brand.class, field = "brandId")
    private List<Goods_type_brand> typeList;

    public List<Goods_type_brand> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<Goods_type_brand> typeList) {
        this.typeList = typeList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
