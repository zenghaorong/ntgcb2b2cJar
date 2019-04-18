package com.aebiz.app.dec.commons.service.impl;

import com.aebiz.app.dec.commons.service.StoreInfoForCompService;
import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.goods.modules.services.GoodsService;
import com.aebiz.app.store.modules.models.Store_feedback;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.app.store.modules.services.StoreFeedbackService;
import com.aebiz.app.store.modules.services.StoreMainService;
import com.aebiz.app.web.modules.controllers.open.dec.dto.store.StoreInfoDTO;
import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 店铺相关业务实现
 *
 * Created by Aebiz_yjq on 2016/12/14.
 */
@Service
public class StoreInfoForCompServiceImpl implements StoreInfoForCompService {
    @Autowired
    private StoreMainService storeMainService;

    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StoreFeedbackService storeFeedbackService;
	@Override
	public StoreInfoDTO getStoreInfoForReputation(String storeUuid) {
		
        Store_main scm = storeMainService.fetch(storeUuid);

        Store_feedback storeScore = storeFeedbackService.getAverageScore(storeUuid);

		
		StoreInfoDTO storeInfoDTO = new StoreInfoDTO();
        storeInfoDTO.setStoreUuid(scm.getId());
        storeInfoDTO.setStoreName(scm.getStoreName());
        //店铺log
        storeInfoDTO.setLogo(scm.getLogo());
        //店铺简介
        storeInfoDTO.setCompanyIntroduction("");
        if(storeScore !=null){
            //店铺服务得分
            storeInfoDTO.setServiceAttitudeScore(storeScore.getServiceScore());
            //店铺物流得分
            storeInfoDTO.setLogisticSpeedScore(storeScore.getSpeedScore());
            //店铺实物描述得分
            storeInfoDTO.setProdDescScore(storeScore.getDescriptionScore());
        }else{
            //店铺服务得分
            storeInfoDTO.setServiceAttitudeScore(5.0);
            //店铺物流得分
            storeInfoDTO.setLogisticSpeedScore(5.0);
            //店铺实物描述得分
            storeInfoDTO.setProdDescScore(5.0);
        }
		return storeInfoDTO;
	}

    /**
     * 根据商品sku获取 店铺id
     * @param sku
     * @return
     */
    @Override
    public String getStoreUuidByProductUuid(String sku) {
        Goods_product product=goodsProductService.fetch(Cnd.where("sku","=",sku));
        if(product !=null){
            String goodsId=product.getGoodsId();
            Goods_main main=goodsService.fetch(goodsId);
            String storeId=main.getStoreId();
            return  storeId;
        }
        return "";
    }
}
