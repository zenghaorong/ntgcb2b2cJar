package com.aebiz.app.goods.modules.services.impl;

import com.aebiz.app.goods.modules.models.*;
import com.aebiz.app.goods.modules.services.*;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@CacheConfig(cacheNames = "goodsCache")
public class GoodsTypeServiceImpl extends BaseServiceImpl<Goods_type> implements GoodsTypeService {

    @Autowired
    private GoodsTypeBrandService goodsTypeBrandService;
    @Autowired
    private GoodsTypeParamgService goodsTypeParamgService;
    @Autowired
    private GoodsTypeParamsService goodsTypeParamsService;
    @Autowired
    private GoodsTypePropsService goodsTypePropsService;
    @Autowired
    private GoodsTypePropsValuesService goodsTypePropsValuesService;
    @Autowired
    private GoodsTypeSpecService goodsTypeSpecService;
    @Autowired
    private GoodsTypeSpecValuesService goodsTypeSpecValuesService;
    @Autowired
    private GoodsTypeTabService goodsTypeTabService;

    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @CacheEvict(key = "#root.targetClass.getName()+'*'")
    @Async
    public void clearCache() {

    }

    @Override
    @Transactional
    public void add(Goods_type goodsType, String[] brand, String[] props_name, String[] props_type, String[] props_values, String[] specId, String[] specValIds, String[] specValUrls, String[] specValText, String[] group_name, String[] group_params, String[] tab_name, String[] tab_note) {
        this.insert(goodsType);
        if (brand != null && goodsType.isHasBrand()) {
            for (int i = 0; i < brand.length; i++) {
                Goods_type_brand brand1 = new Goods_type_brand();
                brand1.setTypeId(goodsType.getId());
                brand1.setBrandId(Strings.sNull(brand[i]));
                goodsTypeBrandService.insert(brand1);
            }
        }
        if (props_name != null && goodsType.isHasProp()) {
            for (int i = 0; i < props_name.length; i++) {
                Goods_type_props props = new Goods_type_props();
                props.setTypeId(goodsType.getId());
                props.setName(Strings.sNull(props_name[i]));
                props.setType(Strings.sNull(props_type[i]));
                props.setLocation(i);
                goodsTypePropsService.insert(props);
                String[] pv = StringUtils.split(Strings.sNull(props_values[i]), ",");
                for (int j = 0; j < pv.length; j++) {
                    Goods_type_props_values values = new Goods_type_props_values();
                    values.setPropsId(props.getId());
                    values.setName(Strings.sNull(pv[j]));
                    values.setLocation(j);
                    values.setTypeId(goodsType.getId());
                    goodsTypePropsValuesService.insert(values);
                }
            }
        }
        if (specId != null && goodsType.isHasSpec()) {
            for (int i = 0; i < specId.length; i++) {
                Goods_type_spec spec = new Goods_type_spec();
                spec.setTypeId(goodsType.getId());
                spec.setSpecId(Strings.sNull(specId[i]));
                spec.setLocation(i);
                goodsTypeSpecService.insert(spec);
                //保存规格值
                String[] specValIdsTemp =StringUtils.split(Strings.sNull( specValIds[i]), ",");
                String[] specValTextTemp =StringUtils.split(Strings.sNull( specValText[i]), ",");
                String[] specValUrlsTemp =StringUtils.split(Strings.sNull( specValUrls[i]), ",");
                for (int j = 0; j < specValIdsTemp.length; j++) {
                    if (!Strings.isBlank(specValIdsTemp[j])) {
                        Goods_type_spec_values spec_values = new Goods_type_spec_values();
                        spec_values.setSpecId(specId[i]);
                        if(specValUrlsTemp.length > 0 && !StringUtils.isBlank(specValUrlsTemp[i]) && !specValUrlsTemp[i].equals("undefined")){
                            spec_values.setSpecValUrl(specValUrls[i]);
                        }

                        spec_values.setSpecValueId(specValIdsTemp[j]);
                        spec_values.setTypeId(goodsType.getId());
                        spec_values.setTypeSpecId(spec.getId());
                        spec_values.setSpecValText(specValTextTemp[j]);
                        spec_values.setLocation(j);
                        goodsTypeSpecValuesService.insert(spec_values);
                    }

                }

            }
        }
        if (group_name != null && goodsType.isHasParam()) {
            for (int i = 0; i < group_name.length; i++) {
                Goods_type_paramg paramg = new Goods_type_paramg();
                paramg.setTypeId(goodsType.getId());
                paramg.setName(Strings.sNull(group_name[i]));
                paramg.setLocation(i);
                goodsTypeParamgService.insert(paramg);
                String[] params = StringUtils.split(Strings.sNull(group_params[i]), ",");
                for (int j = 0; j < params.length; j++) {
                    Goods_type_params params1 = new Goods_type_params();
                    params1.setGroupId(paramg.getId());
                    params1.setName(Strings.sNull(params[j]));
                    params1.setLocation(j);
                    params1.setTypeId(goodsType.getId());
                    goodsTypeParamsService.insert(params1);
                }
            }
        }
        if (tab_name != null && goodsType.isHasTab()) {
            for (int i = 0; i < tab_name.length; i++) {
                Goods_type_tab tab = new Goods_type_tab();
                tab.setName(Strings.sNull(tab_name[i]));
                tab.setNote(Strings.sNull(tab_note[i]));
                tab.setTypeId(goodsType.getId());
                tab.setLocation(i);
                goodsTypeTabService.insert(tab);
            }
        }
    }

    @Override
    @Transactional
    public void update(Goods_type goodsType, String[] brand, String[] props_name, String[] props_type, String[] props_values, String[] specId, String[] specValIds, String[] specValUrls, String[] specValText, String[] group_name, String[] group_params, String[] tab_name, String[] tab_note, String uid) {
        goodsType.setOpAt((int) (System.currentTimeMillis() / 1000));
        goodsType.setOpBy(uid);
        this.updateIgnoreNull(goodsType);
        goodsTypeBrandService.clear(Cnd.where("typeId", "=", goodsType.getId()));
        if (brand != null && goodsType.isHasBrand()) {
            for (int i = 0; i < brand.length; i++) {
                Goods_type_brand brand1 = new Goods_type_brand();
                brand1.setTypeId(goodsType.getId());
                brand1.setBrandId(Strings.sNull(brand[i]));
                goodsTypeBrandService.insert(brand1);
            }
        }
        goodsTypePropsValuesService.clear(Cnd.where("typeId", "=", goodsType.getId()));
        goodsTypePropsService.clear(Cnd.where("typeId", "=", goodsType.getId()));
        if (props_name != null && goodsType.isHasProp()) {
            for (int i = 0; i < props_name.length; i++) {
                Goods_type_props props = new Goods_type_props();
                props.setTypeId(goodsType.getId());
                props.setName(Strings.sNull(props_name[i]));
                props.setType(Strings.sNull(props_type[i]));
                props.setLocation(i);
                goodsTypePropsService.insert(props);
                String[] pv = StringUtils.split(Strings.sNull(props_values[i]), ",");
                for (int j = 0; j < pv.length; j++) {
                    Goods_type_props_values values = new Goods_type_props_values();
                    values.setPropsId(props.getId());
                    values.setName(Strings.sNull(pv[j]));
                    values.setLocation(j);
                    values.setTypeId(goodsType.getId());
                    goodsTypePropsValuesService.insert(values);
                }
            }
        }
        goodsTypeSpecService.clear(Cnd.where("typeId", "=", goodsType.getId()));
        if (specId != null && goodsType.isHasSpec()) {
            goodsTypeSpecValuesService.clear(Cnd.where("typeId","=",goodsType.getId()));
            for (int i = 0; i < specId.length; i++) {
                Goods_type_spec spec = new Goods_type_spec();
                spec.setTypeId(goodsType.getId());
                spec.setSpecId(Strings.sNull(specId[i]));
                spec.setLocation(i);
                goodsTypeSpecService.insert(spec);

                //保存规格值
                String[] specValIdsTemp =StringUtils.split(Strings.sNull( specValIds[i]), ",");
                String[] specValTextTemp =StringUtils.split(Strings.sNull( specValText[i]), ",");
                String[] specValUrlsTemp =StringUtils.split(Strings.sNull( specValUrls[i]), ",");
                for (int j = 0; j < specValIdsTemp.length; j++) {
                    if (!Strings.isBlank(specValIdsTemp[j])) {
                        Goods_type_spec_values spec_values = new Goods_type_spec_values();
                        spec_values.setSpecId(specId[i]);
                        if(specValUrlsTemp.length > 0 && !StringUtils.isBlank(specValUrlsTemp[i]) && !specValUrlsTemp[i].equals("undefined")){
                            spec_values.setSpecValUrl(specValUrls[i]);
                        }

                        spec_values.setSpecValueId(specValIdsTemp[j]);
                        spec_values.setTypeId(goodsType.getId());
                        spec_values.setTypeSpecId(spec.getId());
                        spec_values.setSpecValText(specValTextTemp[j]);
                        spec_values.setLocation(j);
                        goodsTypeSpecValuesService.insert(spec_values);
                    }

                }
            }
        }
//        goodsTypeParamsService.clear(Cnd.where("typeId", "=", goodsType.getId()));
        goodsTypeParamgService.clear(Cnd.where("typeId", "=", goodsType.getId()));
        if (group_name != null && goodsType.isHasParam()) {
            for (int i = 0; i < group_name.length; i++) {
                Goods_type_paramg paramg = new Goods_type_paramg();
                paramg.setTypeId(goodsType.getId());
                paramg.setName(Strings.sNull(group_name[i]));
                paramg.setLocation(i);
                goodsTypeParamgService.insert(paramg);
                String[] params = StringUtils.split(Strings.sNull(group_params[i]), ",");
                for (int j = 0; j < params.length; j++) {
                    Goods_type_params params1 = new Goods_type_params();
                    params1.setGroupId(paramg.getId());
                    params1.setName(Strings.sNull(params[j]));
                    params1.setLocation(j);
                    params1.setTypeId(goodsType.getId());
                    goodsTypeParamsService.insert(params1);
                }
            }
        }
        goodsTypeTabService.clear(Cnd.where("typeId", "=", goodsType.getId()));
        if (tab_name != null && goodsType.isHasTab()) {
            for (int i = 0; i < tab_name.length; i++) {
                Goods_type_tab tab = new Goods_type_tab();
                tab.setName(Strings.sNull(tab_name[i]));
                tab.setNote(Strings.sNull(tab_note[i]));
                tab.setTypeId(goodsType.getId());
                tab.setLocation(i);
                goodsTypeTabService.insert(tab);
            }
        }
    }

    @Override
    @Transactional
    public void deleteType(String[] ids) {
        goodsTypeBrandService.clear(Cnd.where("typeId", "in", ids));
        goodsTypePropsValuesService.clear(Cnd.where("typeId", "in", ids));
        goodsTypeSpecService.clear(Cnd.where("typeId", "in", ids));
        goodsTypeSpecValuesService.clear(Cnd.where("typeId", "in", ids));
        goodsTypeParamsService.clear(Cnd.where("typeId", "in", ids));
        goodsTypeParamgService.clear(Cnd.where("typeId", "in", ids));
        goodsTypeTabService.clear(Cnd.where("typeId", "in", ids));
        this.delete(ids);
    }

    @Override
    @Transactional
    public void deleteType(String id) {
        goodsTypeBrandService.clear(Cnd.where("typeId", "=", id));
        goodsTypePropsValuesService.clear(Cnd.where("typeId", "=", id));
        goodsTypeSpecService.clear(Cnd.where("typeId", "=", id));
        goodsTypeSpecValuesService.clear(Cnd.where("typeId", "=", id));
        goodsTypeParamsService.clear(Cnd.where("typeId", "=", id));
        goodsTypeParamgService.clear(Cnd.where("typeId", "=", id));
        goodsTypeTabService.clear(Cnd.where("typeId", "=", id));
        this.delete(id);
    }
}
