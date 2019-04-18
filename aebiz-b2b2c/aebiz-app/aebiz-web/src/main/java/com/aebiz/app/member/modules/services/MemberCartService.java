package com.aebiz.app.member.modules.services;

import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.member.modules.models.Member_cart;

/**
 * 会员购物车service
 */
public interface MemberCartService extends BaseService<Member_cart>{

    /**
     * (调用方法前要先验证accountId、goods_product对应的记录是否存在)
     * @param accountId 会员id
     * @param goods_product 货品
     * @param price 加入购物车的价格
     * @param num 购物车数量
     * @param imageUrl 商品图片地址
     * @return
     */
    Member_cart saveCart(String accountId, Goods_product goods_product, Integer price, Integer num, String imageUrl);

    /**
     * 同步cookie数据到购物车
     * @param cookieSkuStr
     * @param cookieNumStr
     * @param accountId
     */
    void synchronizeCart(String cookieSkuStr, String cookieNumStr,String accountId);
}
