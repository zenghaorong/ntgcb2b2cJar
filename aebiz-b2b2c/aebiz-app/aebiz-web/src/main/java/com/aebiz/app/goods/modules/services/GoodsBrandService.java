package com.aebiz.app.goods.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.goods.modules.models.Goods_brand;
import com.aebiz.baseframework.page.Pagination;
import org.nutz.dao.entity.Record;

import java.util.List;

public interface GoodsBrandService extends BaseService<Goods_brand>{
    void clearCache();

    void add(Goods_brand goodsBrand, String[] type);

    void edit(Goods_brand goodsBrand, String[] type);

    Pagination listPage(int pageNumber, int pageSize);

    List<Record> listByGoodsTypeId(String TypeId);
}
