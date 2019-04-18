package com.aebiz.app.shop.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.shop.modules.models.Shop_adv_main;
import com.aebiz.app.shop.modules.services.ShopAdvMainService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ShopAdvMainServiceImpl extends BaseServiceImpl<Shop_adv_main> implements ShopAdvMainService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }


    //根据广告位的id获取下面的所有的广告图片
    public List<Shop_adv_main> getAdvMainByPositionId(String positionId){
        return this.query(Cnd.where("positionId","=",positionId));
    }
}
