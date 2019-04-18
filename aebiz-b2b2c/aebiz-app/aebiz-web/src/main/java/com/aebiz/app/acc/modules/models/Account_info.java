package com.aebiz.app.acc.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;
import org.nutz.lang.Strings;

import java.io.Serializable;

/**
 * 帐号信息表
 * Created by wizzer on 2017/4/8.
 */
@Table("account_info")
public class Account_info extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("昵称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String nickname;

    @Column
    @Comment("姓名")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String name;

    @Column
    @Comment("性别")
    @ColDefine(type = ColType.VARCHAR, width = 5)
    private String sex;

    @Column
    @Comment("头像")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String imageUrl;

    @Column
    @Comment("生日年")
    @ColDefine(type = ColType.INT)
    private Integer b_year;

    @Column
    @Comment("生日月")
    @ColDefine(type = ColType.INT)
    private Integer b_month;

    @Column
    @Comment("生日日")
    @ColDefine(type = ColType.INT)
    private Integer b_day;

    @Column
    @Comment("星座")
    @ColDefine(type = ColType.VARCHAR, width = 5)
    private String astro;

    @Column
    @Comment("所在省")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String provinceId;

    @Column
    @Comment("所在市")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String cityId;

    @Comment("所在地")
    private String locus;

    @Comment("生日")
    private String birthday;

    @Column
    @Comment("所在县区")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String areaId;

    @Column
    @Comment("家乡所在省")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String jx_province;

    @Column
    @Comment("家乡所在市")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String jx_city;

    @Column
    @Comment("家乡所在县区")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String jx_county;

    @Column
    @Comment("家乡所在街道")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String jx_town;

    @Comment("籍贯")
    private String nativePlace;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getB_year() {
        return b_year;
    }

    public void setB_year(Integer b_year) {
        this.b_year = b_year;
    }

    public Integer getB_month() {
        return b_month;
    }

    public void setB_month(Integer b_month) {
        this.b_month = b_month;
    }

    public Integer getB_day() {
        return b_day;
    }

    public void setB_day(Integer b_day) {
        this.b_day = b_day;
    }

    public String getAstro() {
        return astro;
    }

    public void setAstro(String astro) {
        this.astro = astro;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getJx_province() {
        return jx_province;
    }

    public void setJx_province(String jx_province) {
        this.jx_province = jx_province;
    }

    public String getJx_city() {
        return jx_city;
    }

    public void setJx_city(String jx_city) {
        this.jx_city = jx_city;
    }

    public String getJx_county() {
        return jx_county;
    }

    public void setJx_county(String jx_county) {
        this.jx_county = jx_county;
    }

    public String getJx_town() {
        return jx_town;
    }

    public void setJx_town(String jx_town) {
        this.jx_town = jx_town;
    }

    public String getLocus() {
        return locus;
    }

    public void setLocus(String province, String city, String area) {
        this.locus = province + " " + city + " " + area;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public String getNativePlace() {
        StringBuffer sb = new StringBuffer();
        if (!Strings.isEmpty(this.jx_province)) {
            sb.append(this.jx_province).append(" ");
        }
        if (!Strings.isEmpty(this.jx_city)) {
            sb.append(this.jx_city).append(" ");
        }
        if (!Strings.isEmpty(this.jx_county)) {
            sb.append(this.jx_county).append(" ");
        }
        if (!Strings.isEmpty(this.jx_town)) {
            sb.append(this.jx_town);
        }
        return sb.toString();
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
