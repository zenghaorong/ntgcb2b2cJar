package com.aebiz.app.store.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 店铺收藏表
 * Created by Thinkpad on 2017/6/14.
 */
@Table("store_favorite")
public class Store_favorite  extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("帐号ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String accountId;   //account_info 表中的id

    @Column
    @Comment("店铺名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String storeName;

    @Column
    @Comment("店铺id")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeId;    //store_main 表中的id

    @Column
    @Comment("收藏时间")
    @ColDefine(type = ColType.INT)
    private Integer favoriteTime;

    @Column
    @Comment("取消收藏时间")
    @ColDefine(type = ColType.INT)
    private Integer unFavoriteTime;

    @Comment("店铺图片")
    @ColDefine(type = ColType.VARCHAR)
    private String log;

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

    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreId() {
        return storeId;
    }
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Integer getFavoriteTime() {
        return favoriteTime;
    }
    public void setFavoriteTime(Integer favoriteTime) {
        this.favoriteTime = favoriteTime;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Integer getUnFavoriteTime() {
        return unFavoriteTime;
    }
    public void setUnFavoriteTime(Integer unFavoriteTime) {
        this.unFavoriteTime = unFavoriteTime;
    }
}
