package com.aebiz.app.store.modules.services.impl;

import com.aebiz.app.goods.modules.models.Goods_brand;
import com.aebiz.app.goods.modules.models.Goods_type_brand;
import com.aebiz.app.goods.modules.services.GoodsBrandService;
import com.aebiz.app.goods.modules.services.GoodsTypeBrandService;
import com.aebiz.app.store.modules.models.Store_apply_brand;
import com.aebiz.app.store.modules.models.Store_apply_type_brand;
import com.aebiz.app.store.modules.models.Store_user;
import com.aebiz.app.store.modules.services.StoreApplyBrandService;
import com.aebiz.app.store.modules.services.StoreApplyTypeBrandService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StoreApplyBrandServiceImpl extends BaseServiceImpl<Store_apply_brand> implements StoreApplyBrandService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private StoreApplyTypeBrandService storeApplyTypeBrandService;

    @Autowired
    private GoodsBrandService goodsBrandService;

    @Autowired
    private GoodsTypeBrandService goodsTypeBrandService;

    @Override
    @Transactional
    public void add(Store_apply_brand storeApplyBrand, String[] type) {
        Store_user storeUser = (Store_user)SecurityUtils.getSubject().getPrincipal();
        storeApplyBrand.setApplyAt((int)(System.currentTimeMillis()/1000));
        storeApplyBrand.setApplyNote("商户 发起"+storeApplyBrand.getBrandName()+"品牌申请");
        storeApplyBrand.setStatus(0);
        storeApplyBrand.setOpBy(StringUtil.getStoreUid());
        storeApplyBrand.setOpAt((int)(System.currentTimeMillis()/1000));
        storeApplyBrand.setStoreId(storeUser.getStoreId());
        storeApplyBrand.setDelFlag(false);
        storeApplyBrand = this.insert(storeApplyBrand);
        if (type != null ) {
            for (int i = 0; i < type.length; i++) {
                Store_apply_type_brand brandType = new Store_apply_type_brand();
                brandType.setTypeId(Strings.sNull(type[i]));
                brandType.setApplyBrandId(storeApplyBrand.getId());
                brandType.setOpBy(StringUtil.getStoreUid());
                brandType.setOpAt((int)(System.currentTimeMillis()/1000));
                brandType.setDelFlag(false);
                storeApplyTypeBrandService.insert(brandType);
            }
        }
    }

    @Override
    @Transactional
    public void edit(Store_apply_brand storeApplyBrand, String[] type) {
        this.updateIgnoreNull(storeApplyBrand);
        storeApplyTypeBrandService.clear(Cnd.where("applyBrandId", "=", storeApplyBrand.getId()));
        if (type != null ) {
            for (int i = 0; i < type.length; i++) {
                Store_apply_type_brand brandType = new Store_apply_type_brand();
                brandType.setTypeId(Strings.sNull(type[i]));
                brandType.setApplyBrandId(storeApplyBrand.getId());
                brandType.setOpBy(StringUtil.getStoreUid());
                brandType.setOpAt((int)(System.currentTimeMillis()/1000));
                brandType.setDelFlag(false);
                storeApplyTypeBrandService.insert(brandType);
            }
        }
    }

    @Override
    @Transactional
    public void verify(Store_apply_brand storeApplyBrand) {
        storeApplyBrand.setOpBy(StringUtil.getUid());
        storeApplyBrand.setOpAt((int) (System.currentTimeMillis() / 1000));
        this.updateIgnoreNull(storeApplyBrand);
        //审核通过,把品牌录入
        if(storeApplyBrand.getStatus() == 1){
            storeApplyBrand = this.fetch(storeApplyBrand.getId());
            this.fetchLinks(storeApplyBrand,"typeList");
            Goods_brand goodsBrand = new Goods_brand();
            goodsBrand.setName(storeApplyBrand.getBrandName());
            goodsBrand.setAliasName(storeApplyBrand.getAliasName());
            goodsBrand.setUrl(storeApplyBrand.getUrl());
            goodsBrand.setImgurl(storeApplyBrand.getImgurl());
            goodsBrand.setNote(storeApplyBrand.getNote());
            goodsBrand = goodsBrandService.insert(goodsBrand);
            List<Store_apply_type_brand> typeBrandList = storeApplyBrand.getTypeList();
            if(typeBrandList != null){
                for(Store_apply_type_brand storeApplyTypeBrand : typeBrandList){
                    Goods_type_brand brand = new Goods_type_brand();
                    brand.setTypeId(storeApplyTypeBrand.getTypeId());
                    brand.setBrandId(goodsBrand.getId());
                    goodsTypeBrandService.insert(brand);
                }
            }
        }
    }


}
