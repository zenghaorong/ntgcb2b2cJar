package com.aebiz.app.shop.modules.models;

import java.io.Serializable;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;
import org.nutz.dao.entity.annotation.Table;

import com.aebiz.baseframework.base.model.BaseModel;

/**
 * 商城物流公司
 * Created by wanghx on 2017/2/28.
 */
@Table("shop_express")
public class Shop_express extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;


    @Column
    @Comment("名称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String name;

    @Column
    @Comment("编码")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String code;

    @Column
    @Comment("网址")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String url;

    @Column
    @Comment("物流公司类型编码")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String wayCode;

    @Column
    @Comment("物流公司类型名称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String wayName;

    @Column
    @Comment("物流公司询件网址")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String queryurl;

    @Column
    @Comment("排序")
    @Prev({
            @SQL(db = DB.MYSQL, value = "SELECT IFNULL(MAX(location),0)+1 FROM shop_express"),
            @SQL(db = DB.ORACLE, value = "SELECT COALESCE(MAX(location),0)+1 FROM shop_express")
    })
    private Integer location;

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


    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getQueryurl() {
        return queryurl;
    }


    public void setQueryurl(String queryurl) {
        this.queryurl = queryurl;
    }


    public Integer getLocation() {
        return location;
    }


    public void setLocation(Integer location) {
        this.location = location;
    }

    public String getWayCode() {
        return wayCode;
    }

    public void setWayCode(String wayCode) {
        this.wayCode = wayCode;
    }

    public String getWayName() {
        return wayName;
    }

    public void setWayName(String wayName) {
        this.wayName = wayName;
    }
}
