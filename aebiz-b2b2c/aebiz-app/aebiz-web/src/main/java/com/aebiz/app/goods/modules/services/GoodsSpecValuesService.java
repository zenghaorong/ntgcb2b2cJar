package com.aebiz.app.goods.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.goods.modules.models.Goods_spec_values;

import java.util.List;

public interface GoodsSpecValuesService extends BaseService<Goods_spec_values>{
    void clearCache();

    List<Goods_spec_values> getValueBySpecId(String specId);
}
