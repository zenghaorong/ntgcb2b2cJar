package com.aebiz.app.dec.commons.service;

import com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo.PartnerDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo.PlatImageCategoryDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo.PlatInfoDTO;
import com.aebiz.baseframework.page.Pagination;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 获取平台基础数据相关业务
 *
 * Created by Aebiz_yjq on 2016/12/21.
 */
public interface PlatInfoForCompService {


    /**
     * 获取平台信息
     *
     * @return  返回平台信息
     */
    /*PlatInfoDTO getPlatInfo() throws IllegalAccessException, InvocationTargetException ;
*/
    /**
     * 获取平台底部信息
     *
     * @return  返回平台底部信息
     */
    /*String getPlatButtonInfo() throws IllegalAccessException, InvocationTargetException ;*/

    /**
     * 根据分类ID获取图片库子分类
     *
     * @param  图片库分类uuid,如果为根分类则传空字符串。
     * @return 子分类集合
     */
    List<PlatImageCategoryDTO> getImageLibCategoryById();

    /**
     * 根据分类ID获取分类下的图片
     *
     * @param categoryUuid 图片库分类uuid、
     * @param nowPage 当前页面
     * @param pageShow 查询条数
     * @return 图片集合
     */
    Pagination getImageLigByCateId(String categoryUuid, int nowPage, int pageShow) ;


  /*  *//**
     * 获取qq客服信息
     *
     * @return 返回客服信息DTO
     *//*
    CustomerServiceDTO getQQCustomerService();*/

    /**
     * 获取友情链接
     *
     * @return 返回友情链接DTO列表
     */
    List<PartnerDTO> getPartnerSites();

    /**
     * 获取平台用户注册协议
     *
     * @return  返回平台用户注册协议
     */
  /*  String getPlatRegistrationAgreement() throws IllegalAccessException, InvocationTargetException;*/
    /**
     * 获取平台信息
     *
     * @return  返回平台信息
     */
    PlatInfoDTO getPlatInfo() throws IllegalAccessException, InvocationTargetException;
}
