package com.aebiz.app.goods.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 商品相关商品表
 * 开发注意事项：
 * 1、前台查询相关商品时，不光要通过goodsId查单向关联的商品，还需通过linkGoodsId查询双向关联商品；
 * Created by wizzer on 2017/3/3.
 */
@Table("goods_link")
public class Goods_link extends BaseModel implements Serializable {
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
    @Comment("关联方向(0:单向 1:双向)")
    @ColDefine(type = ColType.INT)
    private int linkType;

    @Column
    @Comment("关联商品ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String linkGoodsId;

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

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    public String getLinkGoodsId() {
        return linkGoodsId;
    }

    public void setLinkGoodsId(String linkGoodsId) {
        this.linkGoodsId = linkGoodsId;
    }
}
