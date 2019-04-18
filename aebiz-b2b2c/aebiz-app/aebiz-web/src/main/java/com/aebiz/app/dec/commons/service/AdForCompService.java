package com.aebiz.app.dec.commons.service;


import com.aebiz.app.web.modules.controllers.open.dec.dto.advertisement.AdDTO;

import java.util.List;

/**
 * 平台广告先关业务
 *
 * Created by yewei on 2017/5/26.
 */
public interface AdForCompService {

    /**
     * 获取平台广告
     *
     * @param pageShow 显示条数
     * @param pageNo 当前页数
     * @return 返回广告列表
     */
    List<AdDTO> getAdList(int pageShow, int pageNo);

    /**
     * 获取平台广告条数
     *
     * @return 返回获取到的平台的广告条数
     */
    int getAdCount();

    /**
     * 根据uuid获取广告
     *
     * @param adUuid 广告uuid
     * @return 广告dto
     */
    AdDTO getAdByUuid(String adUuid);
}
