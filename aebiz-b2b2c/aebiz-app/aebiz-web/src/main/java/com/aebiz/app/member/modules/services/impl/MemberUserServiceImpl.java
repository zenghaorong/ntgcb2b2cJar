package com.aebiz.app.member.modules.services.impl;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.member.modules.models.Member_account;
import com.aebiz.app.member.modules.models.Member_level;
import com.aebiz.app.member.modules.models.Member_type;
import com.aebiz.app.member.modules.models.Member_user;
import com.aebiz.app.member.modules.services.MemberAccountService;
import com.aebiz.app.member.modules.services.MemberLevelService;
import com.aebiz.app.member.modules.services.MemberTypeService;
import com.aebiz.app.member.modules.services.MemberUserService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.commons.utils.DateUtil;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.beans.Transient;
import java.util.Calendar;

@Service
public class MemberUserServiceImpl extends BaseServiceImpl<Member_user> implements MemberUserService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private AccountUserService accountUserService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private MemberAccountService memberAccountService;
    @Autowired
    private ShopAreaService shopAreaService;
    @Autowired
    private MemberTypeService memberTypeService;
    @Autowired
    private MemberLevelService memberLevelService;

    /**
     * 添加会员
     *
     * @param memberUser
     * @param accountUser
     * @param accountInfo
     */
    @Transactional
    public void addMemberUser(Member_user memberUser, Account_user accountUser, Account_info accountInfo) {
        String accountId = accountInfoService.insert(accountInfo).getId();

        String password = accountUser.getPassword();
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        String salt = rng.nextBytes().toBase64();
        String hashedPasswordBase64 = new Sha256Hash(password, salt, 1024).toBase64();

        accountUser.setPassword(hashedPasswordBase64);
        accountUser.setSalt(salt);
        accountUser.setAccountId(accountId);
        accountUser.setDisabled(!accountUser.isDisabled());// 会员状态绿色表示启动，传过来的是true，是否禁用就该设置成false，反之亦然，所以要取反操作
        accountUserService.insert(accountUser);

        memberUser.setAccountId(accountId);
        this.insert(memberUser);

        Member_account memberAccount = new Member_account();
        memberAccount.setAccountId(accountId);
        memberAccountService.insert(memberAccount);
    }

    /**
     * 更新会员
     *
     * @param memberUser
     * @param accountUser
     * @param accountInfo
     */
    @Transactional
    public void updateMemberUser(Member_user memberUser, Account_user accountUser, Account_info accountInfo) {
        int opAt = (int) (System.currentTimeMillis() / 1000);
        String opBy = StringUtil.getUid();

        memberUser.setOpAt(opAt);
        memberUser.setOpBy(opBy);
        this.updateIgnoreNull(memberUser);

        accountUser.setOpAt(opAt);
        accountUser.setOpBy(opBy);
        String password = accountUser.getPassword();
        // 如果编辑的时候输入了新密码就更新密码和密码盐。
        if (!Strings.isEmpty(password)) {
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash(password, salt, 1024).toBase64();
            accountUser.setPassword(hashedPasswordBase64);
            accountUser.setSalt(salt);
        } else {
            // 如果是空的，就设为null，否则updateIgnoreNull方法在更新时会清空密码
            accountUser.setPassword(null);
        }
        //会员状态绿色表示启动，传过来的是true，是否禁用就该设置成false，反之亦然，所以要取反操作
        accountUser.setDisabled(!accountUser.isDisabled());
        accountUserService.updateIgnoreNull(accountUser);

        accountInfo.setOpAt(opAt);
        accountInfo.setOpBy(opBy);
        accountInfoService.updateIgnoreNull(accountInfo);
    }

    /**
     * 检测字段的唯一性
     *
     * @param fieldName  字段名
     * @param fieldValue 字段值
     *
     * @return 存在返回true，不存在返回false
     */
    public boolean checkUnique(String fieldName, String fieldValue) {
        if (!Strings.isEmpty(fieldName) && !Strings.isEmpty(fieldValue)) {
            Account_user accountUser = accountUserService.fetch(Cnd.where(fieldName, "=", fieldValue));
            if (accountUser != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据手机号注册成会员
     *
     * @param mobile   手机号
     * @param password 密码
     * @param username 用户名
     *
     * @return true 注册成功，false 注册失败
     */
    @Override
    @Transactional
    @Transient
    public boolean memberRegister(String mobile, String password,String username) {
        // 如果不是手机号直接返回
        if (!Strings.isMobile(mobile)) return false;
        if (Strings.isEmpty(username)) return false;

        /*账户信息表添加一条记录*/
        Account_info accountInfo = new Account_info();
        accountInfo = accountInfoService.insert(accountInfo);

        // 获取账户id
        String accountId = accountInfo.getId();

        /*账户用户表添加一条记录*/
        Account_user accountUser = new Account_user();
        accountUser.setAccountId(accountId);
        accountUser.setMobile(mobile);
        /*密码加密*/
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        String salt = rng.nextBytes().toBase64();
        String hashedPasswordBase64 = new Sha256Hash(password, salt, 1024).toBase64();
        accountUser.setPassword(hashedPasswordBase64);
        accountUser.setSalt(salt);// 设置密码盐
        accountUser.setLoginname(username);
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

        this.insert(memberUser);
        return true;
    }
}