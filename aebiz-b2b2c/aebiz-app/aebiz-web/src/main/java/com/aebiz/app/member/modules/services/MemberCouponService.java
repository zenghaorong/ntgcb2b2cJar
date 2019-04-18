package com.aebiz.app.member.modules.services;

import com.aebiz.app.member.modules.models.Member_coupon;
import com.aebiz.baseframework.base.service.BaseService;

import java.util.Map;

public interface MemberCouponService extends BaseService<Member_coupon>{

    Map<String,Object> selectData(String accountId, Integer page, Integer rows, Integer status);

    void save(String storeId, String couponId, String accountId, int num);

    /**
     * 优惠券重复领取校验
     * @param couponId 优惠券ID
     * @param accountId 要领取的数量
     * @return
     */
    boolean isReceieved(String couponId, String accountId);
}
