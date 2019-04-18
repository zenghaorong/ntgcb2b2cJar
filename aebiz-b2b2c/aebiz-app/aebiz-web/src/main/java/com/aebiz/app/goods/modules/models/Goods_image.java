package com.aebiz.app.goods.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2017/3/7.
 */
@Table("goods_image")
@TableIndexes({@Index(name = "INDEX_GOODS_IMAGE", fields = {"goodsId"}, unique = false)})
public class Goods_image extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("商品ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String goodsId;

    @Column
    @Comment("销售终端")
    @ColDefine(type = ColType.INT)
    private int saleClient;//0 全部  1 PC端  2 移动端 3 TV端

    @Column
    @Comment("列表图")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String imgList;

    @Column
    @Comment("相册图")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String imgAlbum;

    @Column
    @Comment("详情图")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String imgDetail;

    @Column
    @Comment("原始图")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String imgOriginal;

    @Column
    @Comment("是否主图")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean defaultValue;

    @Column
    @Comment("排序字段")
    @Prev({
            @SQL(db = DB.MYSQL, value = "SELECT IFNULL(MAX(location),0)+1 FROM goods_image"),
            @SQL(db = DB.ORACLE, value = "SELECT COALESCE(MAX(location),0)+1 FROM goods_image")
    })
    private Integer location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getSaleClient() {
        return saleClient;
    }

    public void setSaleClient(int saleClient) {
        this.saleClient = saleClient;
    }

    public String getImgList() {
        return imgList;
    }

    public void setImgList(String imgList) {
        this.imgList = imgList;
    }

    public String getImgAlbum() {
        return imgAlbum;
    }

    public void setImgAlbum(String imgAlbum) {
        this.imgAlbum = imgAlbum;
    }

    public String getImgDetail() {
        return imgDetail;
    }

    public void setImgDetail(String imgDetail) {
        this.imgDetail = imgDetail;
    }

    public String getImgOriginal() {
        return imgOriginal;
    }

    public void setImgOriginal(String imgOriginal) {
        this.imgOriginal = imgOriginal;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }
}
