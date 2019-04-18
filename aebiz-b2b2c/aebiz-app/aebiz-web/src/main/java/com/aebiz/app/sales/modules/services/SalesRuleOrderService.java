package com.aebiz.app.sales.modules.services;

import com.aebiz.app.sales.modules.commons.SalesRuleProduct;
import com.aebiz.app.sales.modules.commons.vo.StoreSalesVO;
import com.aebiz.app.sales.modules.models.Sales_rule_order;
import com.aebiz.baseframework.base.service.BaseService;

import java.util.List;

public interface SalesRuleOrderService extends BaseService<Sales_rule_order>{

    void check(Sales_rule_order salesRuleOrder);

    /**
     * 获取PC端订单促销规则列表
     * @param storeId 商家ID
     * @param products 货品列表
     * @param countyCode 销售区县code
     * @param memberLevelId 会员等级ID
     * @return
     */
    List<Sales_rule_order> sales(String storeId, List<SalesRuleProduct> products, String countyCode, String memberLevelId);

    /**
     * 获取订单促销规则列表
     * @param storeId 商家ID
     * @param products 货品列表
     * @param client 销售终端
     * @param countyCode 销售区县code
     * @param memberLevelId 会员等级ID
     * @return
     */
    List<Sales_rule_order> sales(String storeId, List<SalesRuleProduct> products, int client, String countyCode, String memberLevelId);

    /**
     * 取PC端订单促销价
     * @param products 货品列表
     * @param countyCode 区县code
     * @param memberLevelId 会员等级ID
     * @param salesId 促销ID
     * @return
     */
    int price(List<SalesRuleProduct> products, String countyCode, String memberLevelId, String salesId);

    /**
     * 取订单促销价
     * @param products 货品列表
     * @param client 销售终端
     * @param countyCode 区县code
     * @param memberLevelId 会员等级ID
     * @param salesId 促销ID
     * @return
     */
    int price(List<SalesRuleProduct> products, int client, String countyCode, String memberLevelId, String salesId);

    /**
     * 店铺优惠
     * @return
     */
    StoreSalesVO storeSales(String salesId, Integer score, Integer price, Integer freightMoney);
}
