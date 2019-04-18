package com.aebiz.app.goods.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.goods.modules.models.Goods_type;

public interface GoodsTypeService extends BaseService<Goods_type> {

    void clearCache();

    void add(Goods_type shopGoodsType, String[] brand, String[] props_name, String[] props_type, String[] props_values, String[] specId, String[] specValIds, String[] specValUrls, String[] specValText, String[] group_name, String[] group_params, String[] tab_name, String[] tab_note);

    void update(Goods_type goodsType, String[] brand, String[] props_name, String[] props_type, String[] props_values, String[] specId, String[] specValIds, String[] specValUrls, String[] specValText, String[] group_name, String[] group_params, String[] tab_name, String[] tab_note, String uid);

    void deleteType(String[] ids);

    void deleteType(String id);
}
