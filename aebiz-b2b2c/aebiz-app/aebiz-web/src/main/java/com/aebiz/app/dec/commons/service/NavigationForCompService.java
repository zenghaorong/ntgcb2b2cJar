package com.aebiz.app.dec.commons.service;

import com.aebiz.app.web.modules.controllers.open.dec.dto.navigation.NavigationDTO;

import java.util.List;

/**
 * Created by ThinkPad on 2017/6/1.
 */
public interface NavigationForCompService{

    /**
     * 获取已发布的前台频道列表（NoJson）
     *
     * @return 返回频道信息
     */
    List<NavigationDTO> getPublishedChannel();
}
