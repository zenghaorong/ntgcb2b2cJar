package com.aebiz.app.goods.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.goods.modules.models.Goods_class;

import java.util.List;

public interface GoodsClassService extends BaseService<Goods_class>{

    void deleteAndChild(Goods_class obj);

    void save(Goods_class goodsClass, String parentId);
    //获取所有的商品分类列表
    public List<Goods_class> getAllClassGoodsName();

   //获取一级分类
    public List<Goods_class> getSubProductCategoryByParentUuid();
}
