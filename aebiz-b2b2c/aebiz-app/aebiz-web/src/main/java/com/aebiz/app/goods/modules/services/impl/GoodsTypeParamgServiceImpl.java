package com.aebiz.app.goods.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.goods.modules.models.Goods_type_paramg;
import com.aebiz.app.goods.modules.services.GoodsTypeParamgService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GoodsTypeParamgServiceImpl extends BaseServiceImpl<Goods_type_paramg> implements GoodsTypeParamgService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
