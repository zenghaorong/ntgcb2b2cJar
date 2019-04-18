package com.aebiz.app.goods.modules.services.impl;

import com.aebiz.app.goods.modules.services.GoodsProductAreaService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.goods.modules.models.Goods_product_area;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.lang.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodsProductAreaServiceImpl extends BaseServiceImpl<Goods_product_area> implements GoodsProductAreaService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
