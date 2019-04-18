package com.aebiz.app.dec.commons.service.impl;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.cms.modules.models.Cms_link;
import com.aebiz.app.cms.modules.models.Cms_site;
import com.aebiz.app.cms.modules.services.CmsLinkService;
import com.aebiz.app.cms.modules.services.CmsSiteService;
import com.aebiz.app.dec.commons.service.PlatInfoForCompService;
import com.aebiz.app.member.modules.models.Member_account;
import com.aebiz.app.member.modules.models.Member_user;
import com.aebiz.app.member.modules.services.MemberAccountService;
import com.aebiz.app.member.modules.services.MemberUserService;
import com.aebiz.app.shop.modules.models.Shop_adv_main;
import com.aebiz.app.shop.modules.models.Shop_adv_position;
import com.aebiz.app.shop.modules.services.ShopAdvMainService;
import com.aebiz.app.shop.modules.services.ShopAdvPositionService;
import com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo.PartnerDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo.PlatImageCategoryDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo.PlatImageLibDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo.PlatInfoDTO;
import com.aebiz.baseframework.page.Pagination;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取平台基础数据相关业务实现
 *
 * Created by yewei on 2017/5/21.
 */
@Service
public class PlatInfoForCompServiceImpl implements PlatInfoForCompService {

    @Autowired
    private CmsLinkService cmsLinkService;
    @Autowired
    private CmsSiteService cmsSiteService;
    @Autowired
    private ShopAdvPositionService shopAdvPositionService;

    @Autowired
    private ShopAdvMainService shopAdvMainService;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private AccountInfoService accountInfoService;
    /**
     * 根据分类ID获取图片库子分类
     *
     *
     * @return 子分类
     */
   @Override
    public List<PlatImageCategoryDTO> getImageLibCategoryById() {
        /*根据分类uuid获取子分类列表*/
        List<Shop_adv_position> imageLibCategoryList = shopAdvPositionService.query(Cnd.NEW());

        List<PlatImageCategoryDTO> platImageCategoryDTOs = new ArrayList<>();
        PlatImageCategoryDTO platImageCategoryDTO;

        if(!imageLibCategoryList.isEmpty()){
            for(Shop_adv_position model : imageLibCategoryList){
                platImageCategoryDTO = new PlatImageCategoryDTO(model.getId(), model.getName());
                platImageCategoryDTOs.add(platImageCategoryDTO);
            }
        }

        return platImageCategoryDTOs;
    }
    /**
     * 根据分类ID获取分类下的图片
     *
     * @param categoryUuid 图片库分类uuid、
     * @param nowPage 当前页面
     * @param pageShow 查询条数
     * @return 图片集合
     */
    public Pagination getImageLigByCateId(String categoryUuid, int nowPage, int pageShow) {
        /* 1、根据分类uuid获取分类下的图片列表 */
        Pagination p= shopAdvMainService.listPage(nowPage, pageShow,Cnd.where("positionId","=",categoryUuid));
        List<Shop_adv_main> imageLibModelList = (List<Shop_adv_main>)p.getList();
       /* 2、获取分类下图片总数 */
        int totalCount = p.getTotalCount();
        /* 4、创建返回的DTO列表 */
        List<PlatImageLibDTO> platImageLibDTOs = new ArrayList<>();
        PlatImageLibDTO platImageLibDTO;

        /* 5、循环model列表，创建新的DTO，并保存到DTO列表中*/
        if(!imageLibModelList.isEmpty()){
            for(Shop_adv_main model : imageLibModelList){
                platImageLibDTO = new PlatImageLibDTO(model.getId(), model.getPositionId(), model.getTitle(), model.getImgurl());
                platImageLibDTOs.add(platImageLibDTO);
            }
        }
        p.setList(platImageLibDTOs);
        return p;
    }
    /**
     * 获取友情链接
     *
     * @return 返回友情链接
     */
    @Override
    public List<PartnerDTO> getPartnerSites() {
        List<PartnerDTO> partnerDTOs = new ArrayList<>();
        PartnerDTO partnerDTO;
        List<Cms_link> list = cmsLinkService.query(Cnd.NEW());
        for(Cms_link model : list){
            partnerDTO = new PartnerDTO(model.getName(), model.getUrl(), model.getTarget());
            partnerDTOs.add(partnerDTO);
        }

       return partnerDTOs;
    }


    /**
     * 获取平台信息：包括平台名称以及用户名（如果已登录）
     *
     * @return 返回平台信息
     */
    @Override
    public PlatInfoDTO getPlatInfo() throws IllegalAccessException, InvocationTargetException {
        PlatInfoDTO platInfoDTO;
        String nickName = "";
        String memberUid=StringUtil.getMemberUid();
        if(!Strings.isEmpty(memberUid)){
            Account_info infoModel=accountInfoService.fetch(memberUid);
            Account_user user=accountUserService.fetch(Cnd.where("accountId","=",memberUid));
            if(infoModel !=null){
                nickName=infoModel.getNickname();
                if(Strings.isEmpty(nickName)){
                    //如果昵称是空，则返回用户名（此字段是必填）
                    nickName=user.getLoginname();
                }
            }
        }
        /*2、获取平台名称*/
        Cms_site siteModel=cmsSiteService.fetch("site");
        String platName="";
        if(siteModel !=null){
           platName= siteModel.getSite_name();
        }

        return new PlatInfoDTO(platName, nickName);

    }

}
