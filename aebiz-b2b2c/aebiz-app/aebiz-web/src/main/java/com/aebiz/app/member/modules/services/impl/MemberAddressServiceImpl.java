package com.aebiz.app.member.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.member.modules.models.Member_address;
import com.aebiz.app.member.modules.services.MemberAddressService;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class MemberAddressServiceImpl extends BaseServiceImpl<Member_address> implements MemberAddressService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Transactional
    public void updateDefault(String id,String accountId){
        dao().execute(Sqls.create("update Member_address set defaultValue=0 where id <> @id and accountId = @accountId").setParam("id", id).setParam("accountId",accountId));
        dao().execute(Sqls.create("update Member_address set defaultValue=1 where id  = @id and accountId = @accountId").setParam("id", id).setParam("accountId",accountId));
    }

    @Transactional
    public String add(Member_address member_address){
        //判断有没有超过20个地址
        int addressCount = dao().count(Member_address.class,Cnd.where("accountId","=", StringUtil.getMemberUid()));
        if(addressCount >= 20){
            return "1";
        }
        if(member_address.isDefaultValue()){
            dao().execute(Sqls.create("update Member_address set defaultValue=0 where accountId = @accountId").setParam("accountId",StringUtil.getMemberUid()));
        }
        //添加新地址
        member_address.setAccountId(StringUtil.getMemberUid());
        dao().insert(member_address);
        return "success";
    }

    /**
     * 保存编辑收货地址信息
     * @param member_address
     * @return
     */
    @Override
    @Transactional
    public void editAddress(Member_address member_address){
        //如果编辑时，选择了为默认地址，则先将该会员全部地址置为非默认地址
        if(member_address.isDefaultValue()){
            this.update(Chain.make("defaultValue", false), Cnd.where("accountId", "=", member_address.getAccountId()));
        }
        //根据表单更新收货地址信息
        this.updateIgnoreNull(member_address);
    }
}
