package com.aebiz.app.web.modules.controllers.open.dec.dto.advertisement;

import java.util.List;

/**
 * 广告相关DTO
 *
 * Created by Aebiz_yjq on 2017/5/26.
 */
public class AdDTO {

    //广告位id
    private String adUuid;
    //广告名称
    private String adName;
    //广告图片地址
    private List adUrlAndPicList;

    public AdDTO() {
    }

    public AdDTO(String adUuid, String adName, List adUrlAndPicList) {
        this.adUuid = adUuid;
        this.adName = adName;
        this.adUrlAndPicList = adUrlAndPicList;
    }

    public String getAdUuid() {
        return adUuid;
    }

    public void setAdUuid(String adUuid) {
        this.adUuid = adUuid;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public List getAdUrlAndPicList() {
        return adUrlAndPicList;
    }

    public void setAdUrlAndPicList(List adUrlAndPicList) {
        this.adUrlAndPicList = adUrlAndPicList;
    }

    @Override
    public String toString() {
        return "AdDTO{" +
                "adUuid='" + adUuid + '\'' +
                ", adName='" + adName + '\'' +
                ", adUrlAndPicList=" + adUrlAndPicList +
                '}';
    }
}
