package com.aebiz.app.store.modules.services;

import com.aebiz.app.store.modules.commons.vo.StoreFreightProduct;
import com.aebiz.app.store.modules.models.Store_freight;
import com.aebiz.baseframework.base.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface StoreFreightService extends BaseService<Store_freight> {
    /**
     * 添加模板
     *
     * @param storeFreight 主模板
     * @param temps        json串
     */
    void addStoreFreight(Store_freight storeFreight, String temps);
    /**
     * 添加模板规则
     *商户
     * @param storeFreight
     * @param temps
     */
    void addStoreTemplateFreight(Store_freight storeFreight, String temps);
    /**
     * 编辑模板
     *
     * @param storeFreight
     * @param temps
     */
    void editStoreFreight(Store_freight storeFreight, String temps);

    /**
     * 删除模板
     *
     * @param id
     */
    void del(String id);

    /**
     * 删除模板对应的规则
     *
     * @param id
     */
    void delRules(String id);

    /**
     * 启用
     *
     * @param id
     */
    void enabled(String id);

    /**
     * 手动录单运费计算
     * @param productsList  货品sku和num集合
     * @param provinceCode      收货人所属片区Code
     * @param logisticsCode 物流公司Code
     * @param memberId 会员Id(促销的时候用)
     * @param storeId 商户Id(促销的时候用)
     * @return
     */
    public int countFreight(List<StoreFreightProduct> productsList, String provinceCode, String logisticsCode, String memberId,String storeId);

    /**
     * 运费计算
     *商户
     * @param productsList  货品sku集合
     * @param provinceCode  省的Code
     * @param logisticsCode 物流公司Code
     * @param memberId      会员Id(促销的时候用)
     * @param storeId       storeId
     * @return
     */
    public int storeCountFreight(List<StoreFreightProduct> productsList, String provinceCode, String logisticsCode, String memberId, String storeId);
}
