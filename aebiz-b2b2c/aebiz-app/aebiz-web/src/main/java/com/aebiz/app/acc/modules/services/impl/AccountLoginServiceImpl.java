package com.aebiz.app.acc.modules.services.impl;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.member.modules.services.MemberCartService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.acc.modules.models.Account_login;
import com.aebiz.app.acc.modules.services.AccountLoginService;
import com.aebiz.commons.utils.DateUtil;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class AccountLoginServiceImpl extends BaseServiceImpl<Account_login> implements AccountLoginService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    public MemberCartService memberCartService;

    @Autowired
    public AccountUserService accountUserService;

    @Autowired
    public AccountInfoService accountInfoService;

    @Override
    @Transactional
    public void doLogin(String accountId, String cookieSkuStr, String cookieNumStr, Account_login accountLogin) {
        this.insert(accountLogin);
        //同步购物车数据
        memberCartService.synchronizeCart(cookieSkuStr,cookieNumStr, accountId);
    }


    @Override
    @Transactional
    public void memberRegister(Account_user accountUser) {
        //注册
        /*账户信息表添加一条记录*/
        Account_info accountInfo = new Account_info();
        String name=accountUser.getMobile();
        accountInfo.setName(name);//前台上传的名称字段用mobile字段来接收
        accountInfo.setNickname(name);

        accountInfo = accountInfoService.insert(accountInfo);

        // 获取账户id
        String accountId = accountInfo.getId();

        /*密码加密*/
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        String salt = rng.nextBytes().toBase64();
        String hashedPasswordBase64 = new Sha256Hash(accountUser.getPassword(), salt, 1024).toBase64();
        accountUser.setPassword(hashedPasswordBase64);
        accountUser.setSalt(salt);// 设置密码盐
        accountUser.setPasswordStrength(1);
        accountUser.setMobile(accountUser.getMobile());
        accountUser.setAccountId(accountId);
        accountUserService.insert(accountUser);
    }
}
