package com.aebiz.app.dec.commons.comps.slideads.vo;


import com.aebiz.app.dec.commons.comps.slideads.SlideAdsCompController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

/**
 * 首页轮播广告组件
 *
 * Created by Aebiz_yjq on 2016/12/28.
 */
public class SlideAdsCompModel extends BaseCompModel {

    private static final long serialVersionUID = -208145999097851568L;

    /*使用的广告的uuid*/
    private String  adUuid = "";

    /* 轮播 缓动效果速度 */
    private int slideSpeed = 300;

    /* 自动轮播间隔时间 */
    private int interTime = 5000;


    public SlideAdsCompModel() {
        super(SlideAdsCompController.class,"/slideAds/toParamsDesign");
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAdUuid() {
        return adUuid;
    }

    public void setAdUuid(String adUuid) {
        this.adUuid = adUuid;
    }

    public int getSlideSpeed() {
        return slideSpeed;
    }

    public void setSlideSpeed(int slideSpeed) {
        this.slideSpeed = slideSpeed;
    }

    public int getInterTime() {
        return interTime;
    }

    public void setInterTime(int interTime) {
        this.interTime = interTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SlideAdsCompModel that = (SlideAdsCompModel) o;

        if (slideSpeed != that.slideSpeed) return false;
        if (interTime != that.interTime) return false;
        return adUuid != null ? adUuid.equals(that.adUuid) : that.adUuid == null;

    }

    @Override
    public int hashCode() {
        int result = adUuid != null ? adUuid.hashCode() : 0;
        result = 31 * result + slideSpeed;
        result = 31 * result + interTime;
        return result;
    }

    @Override
    public String toString() {
        return "SlideAdsCompModel{" +
                "adUuid='" + adUuid + '\'' +
                ", slideSpeed=" + slideSpeed +
                ", interTime=" + interTime +
                '}';
    }
}
