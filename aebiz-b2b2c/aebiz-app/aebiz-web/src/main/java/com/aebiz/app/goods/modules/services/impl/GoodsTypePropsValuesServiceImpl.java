package com.aebiz.app.goods.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.goods.modules.models.Goods_type_props_values;
import com.aebiz.app.goods.modules.services.GoodsTypePropsValuesService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GoodsTypePropsValuesServiceImpl extends BaseServiceImpl<Goods_type_props_values> implements GoodsTypePropsValuesService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
