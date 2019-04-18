package com.aebiz.app.member.modules.services.impl;

import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.member.modules.models.Member_account;
import com.aebiz.app.member.modules.services.MemberAccountService;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MemberAccountServiceImpl extends BaseServiceImpl<Member_account> implements MemberAccountService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private AccountUserService accountUserService;
    @Autowired
    private MemberAccountService memberAccountService;
    /**
     * 获取会员账号评分[0,100]
     *
     * [0, 50) 弱
     * [50, 80) 中
     * [80, 100] 强
     *
     * @param accountId 会员ID
     * @return
     */
    public int getSecurityScore(String accountId) {
        int score = 0;
        Account_user accountUser = accountUserService.getAccount(accountId);
        if (accountUser != null) {
            //TODO score + 登录密码得分×权重30%
            if(accountUser.getPasswordStrength() !=null ){
                if(accountUser.getPasswordStrength()==2){
                    score+=30;
                }else if(accountUser.getPasswordStrength()==1){
                    score+=20;
                }else{
                    score+=10;
                }
            }
            //TODO score + 邮箱是否绑定得分×权重20%
            boolean hasEmail = !Strings.isEmpty(accountUser.getEmail());
            if (hasEmail) {
                score += 100 * 0.2;
            }
            //TODO score + 手机是否绑定得分×权重30%
            boolean hasMobile = !Strings.isEmpty(accountUser.getMobile());
            if (hasMobile) {
                score += 100 * 0.3;
            }
        }
        //TODO score + 支付密码得分×权重20%
        Member_account memberAccount = memberAccountService.fetch(accountId);
        if (memberAccount != null) {
            String password = memberAccount.getPayPassword();
            boolean hasPayPassword = !Strings.isEmpty(password);
            if(hasPayPassword){
                score += 100 * 0.2;
            }
        }
        return score;
    }
}
