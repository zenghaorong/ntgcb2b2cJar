package com.aebiz.app.goods.modules.services.impl;


import com.aebiz.app.goods.modules.models.Goods_type_brand;
import com.aebiz.app.goods.modules.services.GoodsTypeBrandService;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.goods.modules.models.Goods_brand;
import com.aebiz.app.goods.modules.services.GoodsBrandService;
import com.aebiz.baseframework.page.Pagination;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;

import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@CacheConfig(cacheNames = "goodsCache")
public class GoodsBrandServiceImpl extends BaseServiceImpl<Goods_brand> implements GoodsBrandService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    @Autowired
    private GoodsTypeBrandService goodsTypeBrandService;

    @CacheEvict(key = "#root.targetClass.getName()+'*'")
    @Async
    public void clearCache() {

    }

    public void add(Goods_brand goodsBrand, String[] type) {
        this.insert(goodsBrand);
        if (type != null ) {
            for (int i = 0; i < type.length; i++) {
                Goods_type_brand brand1 = new Goods_type_brand();
                brand1.setTypeId(Strings.sNull(type[i]));
                brand1.setBrandId(goodsBrand.getId());
                goodsTypeBrandService.insert(brand1);
            }
        }
    }

    public void edit(Goods_brand goodsBrand, String[] type) {
        this.updateIgnoreNull(goodsBrand);
        goodsTypeBrandService.clear(Cnd.where("brandId", "=", goodsBrand.getId()));
        if (type != null ) {
            for (int i = 0; i < type.length; i++) {
                Goods_type_brand brand1 = new Goods_type_brand();
                brand1.setTypeId(Strings.sNull(type[i]));
                brand1.setBrandId(goodsBrand.getId());
                goodsTypeBrandService.insert(brand1);
            }
        }
    }

    @Cacheable
    public Pagination listPage(int pageNumber, int pageSize) {
        Cnd cnd=Cnd.NEW();
        cnd.and("disabled","=",false);
        cnd.asc("location");
        cnd.asc("path");
        return  this.listPage(pageNumber,pageSize,cnd);

    }

    public List<Record> listByGoodsTypeId(String typeId) {
        return this.list(Sqls.create("SELECT a.id,a.name FROM goods_brand a,goods_type_brand b WHERE a.id=b.brandId AND b.typeId=@typeId").setParam("typeId", Strings.sNull(typeId)));
    }
}
