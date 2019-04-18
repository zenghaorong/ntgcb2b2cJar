package com.aebiz.app.member.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.member.modules.models.Member_address;

public interface MemberAddressService extends BaseService<Member_address>{
    /**
     * 设置默认地址只有一种
     */
     void updateDefault(String id,String accountId);

    String add(Member_address member_address);

    /**
     * 保存编辑收货地址信息
     * @param member_address
     * @return
     */
    void editAddress(Member_address member_address);

}
