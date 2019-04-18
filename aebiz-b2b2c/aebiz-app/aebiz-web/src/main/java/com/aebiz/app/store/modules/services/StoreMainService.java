package com.aebiz.app.store.modules.services;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.goods.modules.models.Goods_class;
import com.aebiz.app.store.modules.models.Store_company;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.store.modules.models.Store_main;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface StoreMainService extends BaseService<Store_main> {

    /**
     * 获取商户的商品分类的下级分类
     *
     * @param storeId      商户ID
     * @param goodsClassId 商品分类ID，
     * @return
     */
    List<Goods_class> getSubGoodsClasses(String storeId, String goodsClassId);

    void join(Store_main storeMain, Store_company storeCompany, String ids);

    void addDo(Store_main storeMain, Account_info accountInfo, Account_user accountUser, Store_company storeCompany,String ids);

    void editDo(Store_main storeMain,Store_company storeCompany,String ids);
}
