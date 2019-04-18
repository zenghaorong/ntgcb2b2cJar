package com.aebiz.app.goods.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.goods.modules.models.Goods_price_level;
import com.aebiz.app.goods.modules.services.GoodsPriceLevelService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodsPriceLevelServiceImpl extends BaseServiceImpl<Goods_price_level> implements GoodsPriceLevelService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Transactional
    public void batchUpdate(List<Goods_price_level> priceLevelList) {
        for (Goods_price_level pl : priceLevelList) {
            dao().updateIgnoreNull(pl);
        }
    }
}
