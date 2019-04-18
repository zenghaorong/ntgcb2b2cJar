package com.aebiz.app.sales.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.sales.modules.models.Sales_rule_goods;

import java.util.List;

public interface SalesRuleGoodsService extends BaseService<Sales_rule_goods>{

    /**
     * 审核
     */
    void check(Sales_rule_goods salesRuleGoods);

    /**
     * 获取商品促销规则
     * @param storeId 商家ID
     * @param sku 货品SKU
     * @param countyCode 销售区县code
     * @param memberLevelId 会员等级ID
     * @return
     */
    List<Sales_rule_goods> sales(String storeId, String sku, String countyCode, String memberLevelId);

    /**
     * 获取商品促销规则
     * @param storeId 商家ID
     * @param sku 货品SKU
     * @param client 销售终端
     * @param countyCode 销售区县code
     * @param memberLevelId 会员等级ID
     * @return
     */
    List<Sales_rule_goods> sales(String storeId, String sku, int client, String countyCode, String memberLevelId);

    /**
     * 计算货品价格
     * @param sku 货品SKU
     * @param countyCode 销售区县code
     * @param memberLevelId 会员等级ID
     * @param salesId 促销ID
     * @return
     */
    int price(String sku, String countyCode, String memberLevelId, String salesId);

    /**
     * 计算货品价格
     * @param sku 货品SKU
     * @param client 销售终端
     * @param countyCode 销售区县code
     * @param memberLevelId 会员等级ID
     * @param salesId 促销ID
     * @return
     */
    int price(String sku, int client, String countyCode, String memberLevelId, String salesId);
}
