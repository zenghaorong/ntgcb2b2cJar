package com.aebiz.app.dec.commons.service.impl;

import com.aebiz.app.cms.modules.models.Cms_navigation;
import com.aebiz.app.cms.modules.services.CmsNavigationService;
import com.aebiz.app.dec.commons.service.NavigationForCompService;
import com.aebiz.app.web.modules.controllers.open.dec.dto.navigation.NavigationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThinkPad on 2017/6/1.
 */
@Service
public class NavigationForCompServiceImpl implements NavigationForCompService {
    @Autowired
    private CmsNavigationService navigationService;
    /**
     * 获取已发布的前台频道列表（NoJson）
     *
     * @return 频道列表
     */
    @Override
    public List<NavigationDTO> getPublishedChannel() {

        /*1、获取已经发布的频道*/
        List<Cms_navigation> list = navigationService.getPublishedChannel();

        /*2、将获取到的频道Mode转化为DTO*/
        List<NavigationDTO> channelDTOs = new ArrayList<>();
        NavigationDTO navigationDTO;
        for(Cms_navigation model : list){
            navigationDTO = new NavigationDTO(model.getNavigationName(), model.getNavigationUrl(), model.getLocation(),"");
            channelDTOs.add(navigationDTO);
        }

        /*3、返回频道列表*/
        return channelDTOs;
    }
}
