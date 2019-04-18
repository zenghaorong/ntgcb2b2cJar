package com.aebiz.app.goods.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.goods.modules.models.Goods_price_level;

import java.util.List;

public interface GoodsPriceLevelService extends BaseService<Goods_price_level>{

    void batchUpdate(List<Goods_price_level> priceLevelList);

}
