package com.aebiz.app.goods.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.goods.modules.models.Goods_type_tab;
import com.aebiz.app.goods.modules.services.GoodsTypeTabService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GoodsTypeTabServiceImpl extends BaseServiceImpl<Goods_type_tab> implements GoodsTypeTabService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
