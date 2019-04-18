package com.aebiz.app.shop.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.shop.modules.models.Shop_adv_position;
import com.aebiz.app.shop.modules.services.ShopAdvPositionService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShopAdvPositionServiceImpl extends BaseServiceImpl<Shop_adv_position> implements ShopAdvPositionService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    @CacheEvict(key = "#root.targetClass.getName()+'*'")
    @Async
    public void clearCache() {

    }

    //获取所有的广告位管理（前台用）
    public List<Shop_adv_position> getAll(int pageShow, int pageNo){
        //获取分页广告位数据
        List<Shop_adv_position> positionsList=(List<Shop_adv_position>)this.listPage(pageNo,pageShow,Cnd.NEW()).getList();
        return positionsList;
    }
}
