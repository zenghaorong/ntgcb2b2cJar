package com.aebiz.app.dec.commons.service.impl;
import com.aebiz.app.dec.commons.service.AdForCompService;
import com.aebiz.app.shop.modules.models.Shop_adv_main;
import com.aebiz.app.shop.modules.models.Shop_adv_position;
import com.aebiz.app.shop.modules.services.ShopAdvMainService;
import com.aebiz.app.shop.modules.services.ShopAdvPositionService;
import com.aebiz.app.web.modules.controllers.open.dec.dto.advertisement.AdDTO;
import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 广告相关业务实现
 *
 * Created by yewei on 2017/5/26.
 */
@Service
public class AdForCompServiceImpl implements AdForCompService {

    //广告管理的接口 为了获取广告
    @Autowired
    private ShopAdvMainService shopAdvMainService;

    //广告位的接口,为了获取广告关联的图片
    @Autowired
    private ShopAdvPositionService shopAdvPositionService;


    /**
     * 获取平台广告
     *
     * @param pageShow 显示条数
     * @param pageNo 当前页数
     * @return 返回广告列表
     */
    @Override
    public List<AdDTO> getAdList(int pageShow, int pageNo) {
        Shop_adv_position qm = new Shop_adv_position();
        List<Shop_adv_position> positionlist = shopAdvPositionService.getAll( pageShow, pageNo);
        List<AdDTO> dtoList = new ArrayList<>();
        AdDTO adDTO ;
        if(positionlist !=null && positionlist.size()>0){
            for (Shop_adv_position platAdModel : positionlist) {
                List<String> urlList = new ArrayList<>();
                List<Shop_adv_main> mainList = shopAdvMainService.getAdvMainByPositionId(platAdModel.getId());
                if(mainList != null && mainList.size()> 0){
                    for (Shop_adv_main MainModel : mainList) {
                        //图片的地址和图片配置的链接
                        urlList.add(MainModel.getImgurl()+";"+MainModel.getUrl());
                    }
                }
                adDTO = new AdDTO(platAdModel.getId(), platAdModel.getName(), urlList);
                dtoList.add(adDTO);
            }
        }

        return dtoList;
    }

    /**
     * 获取广告位的条数
     *
     * @return 返回获取广告位的条数
     */
    @Override
    public int getAdCount() {
       return shopAdvPositionService.count();
    }

    /**
     * 根据uuid获取广告
     *
     * @param positonId 广告位id
     * @return 广告dto
     */
    @Override
    public AdDTO getAdByUuid(String positonId) {

        /*1、根据uuid获取广告位Model*/
        Shop_adv_position position=shopAdvPositionService.fetch(positonId);
        /*2、获取广告位关联的广告*/
        List<Shop_adv_main> advMainList =shopAdvMainService.query(Cnd.where("positionId","=",positonId).desc("location"));
        /*3、将广告关联的图片的地址和对应广告的地址进行拼接,并保存到一个List中。*/
        List<String> urlList = new ArrayList<>();
        if(advMainList != null && advMainList.size()> 0){
            for (Shop_adv_main advMainModle : advMainList) {
                //图片的地址和图片配置的链接
                urlList.add(advMainModle.getImgurl()+";"+advMainModle.getUrl());
            }
        }

        /*4、生成一个新的DTO并返回*/
        return new AdDTO(position.getId(), position.getName(), urlList);
    }
}
