package com.aebiz.app.member.modules.services.impl;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.member.modules.models.Member_account;
import com.aebiz.app.member.modules.models.Member_level;
import com.aebiz.app.member.modules.models.Member_type;
import com.aebiz.app.member.modules.models.Member_user;
import com.aebiz.app.member.modules.services.*;
import com.aebiz.app.msg.modules.models.Msg_conf_sms;
import com.aebiz.app.msg.modules.models.Msg_conf_sms_tpl;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MemberRegisterServiceImpl extends BaseServiceImpl<Member_user> implements MemberRegisterService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    @Autowired
    private MemberUserService memberUserService;
    @Autowired
    private AccountUserService accountUserService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private MemberAccountService memberAccountService;
    @Autowired
    private MemberTypeService memberTypeService;
    @Autowired
    private MemberLevelService memberLevelService;



    @Override
    @Transactional
    public void memberRegister(String mobile, String password,String username,String passwordStrength) {
        /*账户信息表添加一条记录*/
        Account_info accountInfo = new Account_info();
        accountInfo = accountInfoService.insert(accountInfo);

        // 获取账户id
        String accountId = accountInfo.getId();

        /*账户用户表添加一条记录*/
        Account_user accountUser = new Account_user();
        accountUser.setAccountId(accountId);
        accountUser.setMobile(mobile);
        accountUser.setLoginname(username);
        /*密码加密*/
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        String salt = rng.nextBytes().toBase64();
        String hashedPasswordBase64 = new Sha256Hash(password, salt, 1024).toBase64();
        accountUser.setPassword(hashedPasswordBase64);
        accountUser.setSalt(salt);// 设置密码盐
        accountUser.setPasswordStrength(Integer.parseInt(passwordStrength));
        accountUserService.insert(accountUser);

        /*会员账户表添加一条记录*/
        Member_account memberAccount = new Member_account();
        memberAccount.setAccountId(accountId);
        memberAccountService.insert(memberAccount);

        /*会员用户表添加一条记录*/
        Member_user memberUser = new Member_user();
        memberUser.setAccountId(accountId);

        /*初始化会员类型和等级*/
        Member_type memberType = memberTypeService.fetch(Cnd.NEW().asc("id"));
        if (memberType != null) {
            Integer typeId = memberType.getId();
            if (typeId != null) {
                memberUser.setTypeId(typeId);
                // 获取该类型的默认等级
                Member_level memberLevel = memberLevelService.fetch(Cnd.where("typeId", "=", typeId).and("defaultValue", "=", "1"));
                if (memberLevel != null) {
                    String levelId = memberLevel.getId();
                    if (!Strings.isEmpty(levelId)) {
                        memberUser.setLevelId(levelId);
                    }
                }
            }
        }

        memberUserService.insert(memberUser);

    }
}
