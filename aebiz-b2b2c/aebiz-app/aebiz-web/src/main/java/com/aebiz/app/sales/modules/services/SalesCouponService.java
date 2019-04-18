package com.aebiz.app.sales.modules.services;

import com.aebiz.app.sales.modules.commons.SalesRuleProduct;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.sales.modules.models.Sales_coupon;

import java.util.List;

public interface SalesCouponService extends BaseService<Sales_coupon>{

    void save(Sales_coupon coupon);

    /**
     * 审核
     * @param coupon
     */
    void check(Sales_coupon coupon);

    /**
     *  级联删除
     * @param id
     */
    void cascadeDelete(String id);

    /**
     * 批量级联删除
     * @param ids
     */
    void cascadeDelete(String[] ids);

    /**
     * PC端获取优惠券
     * @param storeId 商家ID
     * @param products 货品列表
     * @param countyCode 销售区县code
     * @param memberLevelId 会员等级ID
     * @return
     */
    List<Sales_coupon> sales(String storeId, List<SalesRuleProduct> products, String countyCode, String memberLevelId);

    /**
     * 获取优惠券
     * @param storeId 商家ID
     * @param products 货品列表
     * @param client 销售终端
     * @param countyCode 销售区县code
     * @param memberLevelId 会员等级ID
     * @return
     */
    List<Sales_coupon> sales(String storeId, List<SalesRuleProduct> products, int client, String countyCode, String memberLevelId);


    /**
     *获取店铺的优惠券（根据账号绑定是否领取）
     * @param storeId
     * @param accountId
     * @return
     */
    List<Sales_coupon> getCouponList(String storeId,String accountId);

    /**
     * 优惠券领取数量上限校验
     * @param couponId 优惠券ID
     * @param num 要领取的数量
     * @return
     */
    boolean isReceieveable(String couponId, int num);
}
