package com.aebiz.app.goods.modules.services.impl;

import com.aebiz.app.goods.modules.models.Goods_spec_values;
import com.aebiz.app.goods.modules.services.GoodsSpecValuesService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.goods.modules.models.Goods_spec;
import com.aebiz.app.goods.modules.services.GoodsSpecService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
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
public class GoodsSpecServiceImpl extends BaseServiceImpl<Goods_spec> implements GoodsSpecService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    @Autowired
    private GoodsSpecValuesService goodsSpecValuesService;

    @CacheEvict(key = "#root.targetClass.getName()+'*'")
    @Async
    public void clearCache() {

    }

    public void add(Goods_spec goodsSpec, String[] spec_value, String[] spec_alias, String[] spec_picurl) {
        this.insert(goodsSpec);
        for (int i = 0; i < spec_value.length; i++) {
            Goods_spec_values values = new Goods_spec_values();
            values.setSpecId(goodsSpec.getId());
            values.setSpec_alias(Strings.sNull(spec_alias[i]));
            values.setSpec_value(Strings.sNull(spec_value[i]));
            if (goodsSpec.getType().equals("image")) {
                values.setSpec_picurl(Strings.sNull(spec_picurl[i]));
            }
            goodsSpecValuesService.insert(values);
        }
    }

    public void update(Goods_spec goodsSpec, String[] spec_value, String[] spec_alias, String[] spec_picurl, String[] spec_value_id, String uid) {
        goodsSpec.setOpAt((int) (System.currentTimeMillis() / 1000));
        goodsSpec.setOpBy(uid);
        this.updateIgnoreNull(goodsSpec);
        goodsSpecValuesService.clear(Cnd.where("specId", "=", goodsSpec.getId()).and("id","not in",spec_value_id));
        for (int i = 0; i < spec_value.length; i++) {
            Goods_spec_values values = new Goods_spec_values();
            values.setSpecId(goodsSpec.getId());
            values.setId(Strings.sNull(spec_value_id[i]));
            values.setSpec_alias(Strings.sNull(spec_alias[i]));
            values.setSpec_value(Strings.sNull(spec_value[i]));
            if (goodsSpec.getType().equals("image")) {
                values.setSpec_picurl(Strings.sNull(spec_picurl[i]));
            }

            if(!Strings.isBlank(values.getId())){
                goodsSpecValuesService.update(values);
            }else {
                goodsSpecValuesService.insert(values);
            }
        }
    }

    public void deleteSpec(String[] ids) {
        goodsSpecValuesService.clear(Cnd.where("specId", "in", ids));
        this.delete(ids);
    }

    public void deleteSpec(String id) {
        goodsSpecValuesService.clear(Cnd.where("specId", "=", id));
        this.delete(id);
    }

    @Cacheable
    public Goods_spec getById(String id) {
        return this.fetch(id);
    }

    @Cacheable
    public List<Goods_spec> getList() {
        Cnd cnd=Cnd.NEW();
        cnd.asc("opAt");
        cnd.asc("location");
        return this.query(cnd);
    }
}

