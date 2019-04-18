package com.aebiz.app.acc.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountUserService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@CacheConfig(cacheNames = "accCache")
public class AccountUserServiceImpl extends BaseServiceImpl<Account_user> implements AccountUserService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    /**
     * 通过accountId从缓存取用户数据
     *
     * @param accountId
     * @return
     */
    @Cacheable(key = "'ACC'+#accountId")
    public Account_user getAccount(String accountId) {
        return this.fetch(Cnd.where("accountId", "=", accountId));
    }

    /**
     * 通过loginname从缓存取用户数据
     *
     * @param loginname
     * @return
     */
    @Cacheable(key = "'LOGIN'+#loginname")
    public Account_user getAccountByLoginname(String loginname) {
        return this.fetch(Cnd.where("loginname", "=", loginname).or("email", "=", loginname).or("mobile", "=", loginname));
    }

    /**
     * 清除帐号缓存,可能存在用户修改登录名,导致旧登录缓存无法清除掉,所以accCache要设置失效时间
     * @param accountId
     */
    @Async
    public void clearAccount(String accountId) {
        this.clearGetAccount(accountId);
        Account_user accountUser = this.getAccount(accountId);
        this.clearGetAccountByLoginname(accountUser.getLoginname());
        this.clearGetAccountByLoginname(accountUser.getEmail());
        this.clearGetAccountByLoginname(accountUser.getMobile());
    }

    @CacheEvict(key = "'ACC'+#accountId")
    public void clearGetAccount(String accountId) {

    }

    @CacheEvict(key = "'LOGIN'+#loginname")
    public void clearGetAccountByLoginname(String loginname) {

    }


}
