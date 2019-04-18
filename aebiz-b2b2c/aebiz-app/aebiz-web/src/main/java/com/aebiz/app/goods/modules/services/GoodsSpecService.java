package com.aebiz.app.goods.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.goods.modules.models.Goods_spec;

import java.util.List;

public interface GoodsSpecService extends BaseService<Goods_spec>{
    void clearCache();

    void add(Goods_spec goodsSpec, String[] spec_value, String[] spec_alias, String[] spec_picurl);

    void update(Goods_spec goodsSpec, String[] spec_value, String[] spec_alias, String[] spec_picurl, String[] spec_value_id, String uid);

    void deleteSpec(String[] ids);

    void deleteSpec(String id);

    Goods_spec getById(String id);

    List<Goods_spec> getList();
}
