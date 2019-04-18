package com.aebiz.app.store.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 店铺评分表
 * Created by wizzer on 2016/6/21.
 */
@Table("store_score")
public class Store_feedback extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("描述相符")
    @ColDefine(type = ColType.FLOAT, precision = 1)
    private Double descriptionScore = 5.0;

    @Column
    @Comment("服务态度")
    @ColDefine(type = ColType.FLOAT, precision = 1)
    private Double serviceScore = 5.0;

    @Column
    @Comment("物流速度")
    @ColDefine(type = ColType.FLOAT, precision = 1)
    private Double speedScore = 5.0;

    @Column
    @Comment("评价总数量")
    @ColDefine(type = ColType.INT, width = 11)
    private Integer feedAccountNum = 0;//每次评价记得更新这张表。feedAccountNum+1

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

    public Double getDescriptionScore() {
        return descriptionScore;
    }

    public void setDescriptionScore(Double descriptionScore) {
        this.descriptionScore = descriptionScore;
    }

    public Double getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Double serviceScore) {
        this.serviceScore = serviceScore;
    }

    public Integer getFeedAccountNum() {
        return feedAccountNum;
    }

    public void setFeedAccountNum(Integer feedAccountNum) {
        this.feedAccountNum = feedAccountNum;
    }

    public Double getSpeedScore() {
        return speedScore;
    }

    public void setSpeedScore(Double speedScore) {
        this.speedScore = speedScore;
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
