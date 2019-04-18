package com.aebiz.app.store.modules.services.impl;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.goods.modules.models.Goods_class;
import com.aebiz.app.goods.modules.services.GoodsClassService;
import com.aebiz.app.member.modules.models.Member_user;
import com.aebiz.app.store.modules.models.*;
import com.aebiz.app.store.modules.services.*;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.commons.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StoreMainServiceImpl extends BaseServiceImpl<Store_main> implements StoreMainService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    @Autowired
    private StoreMainService storeMainService;

    @Autowired
    private StoreUserService storeUserService;

    @Autowired
    private StoreCompanyService storeCompanyService;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private StoreRoleService storeRoleService;

    @Autowired
    private GoodsClassService goodsClassService;
    @Autowired
    private StoreApplyMainService storeApplyMainService;


    @Override
    public List<Goods_class> getSubGoodsClasses(String storeId, String goodsClassId) {
        Store_main store = this.fetchLinks(this.fetch(storeId), "goodsClasses", Cnd.where("disabled", "=", false).and("delFlag", "=", false).and("parentId", "=", Strings.sNull(goodsClassId)));
        if (store != null) {
            return store.getGoodsClasses();
        }
        return null;
    }

    @Override
    @Transactional
    public void join(Store_main storeMain, Store_company storeCompany, String ids) {

        //获取当前会员的登陆信息
        String accountId = StringUtil.getMemberUid();
        Store_user storeUser = new Store_user();
        storeUser.setAccountId(accountId);
        storeUser.setOpBy(accountId);
        storeUser.setOpAt((int)(System.currentTimeMillis()/1000));
        storeUser.setDelFlag(false);
        storeUser = storeUserService.insert(storeUser);

        storeMain.setUserId(storeUser.getId());
        storeMain.setDisabled(true);
        Store_main store = storeMainService.insert(storeMain);
        storeCompany.setStoreId(store.getId());
        storeCompanyService.insert(storeCompany);
        storeUser.setStoreId(store.getId());
        String[] id = StringUtils.split(ids, ",");
        for (String s : id) {
            if (!Strings.isEmpty(s)) {
                storeUserService.insert("store_goods_class", org.nutz.dao.Chain.make("storeId", store.getId()).add("classId", s));
            }
        }
        Store_role storeRole = storeRoleService.fetch(Cnd.where("defaultValue","=",1));
        storeUserService.insert("store_user_role", org.nutz.dao.Chain.make("userId", storeUser.getId()).add("roleId", storeRole.getId()));

        storeUserService.update(storeUser);

        Store_apply_main applyMain=new Store_apply_main();
        Member_user memberUser = (Member_user) SecurityUtils.getSubject().getPrincipal();
        String storeId = storeUserService.fetch(Cnd.where("accountId","=", memberUser.getAccountId())).getStoreId();
        applyMain.setStoreId(storeId);
        applyMain.setStatus(0);
        applyMain.setApplyNote(String.format("会员%s(%s)发起商户入驻申请！", memberUser.getAccountUser().getLoginname(), memberUser.getAccountUser().getMobile()));
        applyMain.setCheckNote("发起申请！");
        applyMain.setApplyAt((int) (System.currentTimeMillis() / 1000));
        applyMain.setOpBy(StringUtil.getUid());
        storeApplyMainService.insert(applyMain);
    }

    @Override
    @Transactional
    public void addDo(Store_main storeMain, Account_info accountInfo, Account_user accountUser, Store_company storeCompany, String ids) {
        Account_info info = accountInfoService.insert(accountInfo);
        accountUser.setAccountId(info.getId());
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        String salt = rng.nextBytes().toBase64();
        String hashedPasswordBase64 = new Sha256Hash(accountUser.getPassword(), salt, 1024).toBase64();
        accountUser.setSalt(salt);
        accountUser.setPassword(hashedPasswordBase64);
        accountUserService.insert(accountUser);

        Store_user storeUser = new Store_user();
        storeUser.setAccountId(info.getId());
        Store_user user = storeUserService.insert(storeUser);

        storeMain.setUserId(user.getId());
        Store_main store = storeMainService.insert(storeMain);

        storeCompany.setStoreId(store.getId());
        storeCompanyService.insert(storeCompany);

        user.setStoreId(store.getId());

        String[] id = StringUtils.split(ids, ",");
        for (String s : id) {
            if (!Strings.isEmpty(s)) {
                goodsClassService.insert("store_goods_class", Chain.make("storeId", store.getId()).add("classId", s));
            }
        }

        Store_role storeRole = storeRoleService.fetch(Cnd.where("defaultValue","=",1));
        storeUserService.insert("store_user_role", Chain.make("userId", user.getId()).add("roleId", storeRole.getId()));

        storeUserService.update(user);
    }

    @Override
    @Transactional
    public void editDo(Store_main storeMain, Store_company storeCompany, String ids) {
        storeMain.setOpBy(StringUtil.getUid());
        storeMain.setOpAt((int) (System.currentTimeMillis() / 1000));
        storeMainService.updateIgnoreNull(storeMain);

        storeCompany.setOpBy(StringUtil.getUid());
        storeCompany.setOpAt((int) (System.currentTimeMillis() / 1000));
        storeCompanyService.update(storeCompany);

        String[] id = StringUtils.split(ids, ",");
        if (ids.length() > 0) {
            goodsClassService.clear("store_goods_class",Cnd.where("storeId","=",storeMain.getId()));
        }
        for (String s : id) {
            if (!Strings.isEmpty(s)) {
                goodsClassService.insert("store_goods_class", Chain.make("storeId", storeMain.getId()).add("classId", s));
            }
        }
    }
}
