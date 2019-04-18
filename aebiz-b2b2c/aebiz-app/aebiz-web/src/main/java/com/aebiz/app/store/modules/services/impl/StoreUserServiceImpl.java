package com.aebiz.app.store.modules.services.impl;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.store.modules.models.Store_menu;
import com.aebiz.app.store.modules.models.Store_role;
import com.aebiz.app.store.modules.services.StoreMenuService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.store.modules.models.Store_user;
import com.aebiz.app.store.modules.services.StoreUserService;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoreUserServiceImpl extends BaseServiceImpl<Store_user> implements StoreUserService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    @Autowired
    private StoreUserService storeUserService;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private StoreMenuService storeMenuService;

    /**
     * 获取用户菜单
     *
     * @param user
     */
    public void fillMenu(Store_user user) {
        user.setMenus(getMenus(user.getId()));
        //计算左侧菜单
        List<Store_menu> firstMenus = new ArrayList<>();
        Map<String, List<Store_menu>> secondMenus = new HashMap<>();
        for (Store_menu menu : user.getMenus()) {
            if (menu.getPath().length() > 4) {
                List<Store_menu> s = secondMenus.get(StringUtil.getParentId(menu.getPath()));
                if (s == null) s = new ArrayList<>();
                s.add(menu);
                secondMenus.put(StringUtil.getParentId(menu.getPath()), s);
            } else if (menu.getPath().length() == 4) {
                firstMenus.add(menu);
            }
        }
        user.setFirstMenus(firstMenus);
        user.setSecondMenus(secondMenus);
        if (!Strings.isBlank(user.getCustomMenu())) {
            user.setCustomMenus(storeMenuService.query(Cnd.where("id", "in", user.getCustomMenu().split(","))));
        }
    }

    /**
     * 查询用户菜单权限
     *
     * @param userId
     * @return
     */
    public List<Store_menu> getMenus(String userId) {
        Sql sql = Sqls.create("select distinct a.* from store_menu a,store_role_menu b where a.id=b.menuId and a.system=@system and" +
                " b.roleId in(select c.roleId from store_user_role c,store_role d where c.roleId=d.id and c.userId=@userId and d.disabled=0) and a.disabled=0 and a.isShow=1 and a.type='menu' order by a.location ASC,a.path asc");
        sql.params().set("userId", userId);
        sql.params().set("system", "store");
        Entity<Store_menu> entity = dao().getEntity(Store_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Store_menu.class);
    }

    /**
     * 查询用户角色code列表
     *
     * @param user
     * @return
     */
    public List<String> getRoleCodeList(Store_user user) {
        dao().fetchLinks(user, "roles");
        List<String> roleNameList = new ArrayList<String>();
        for (Store_role role : user.getRoles()) {
            if (!role.isDisabled())
                roleNameList.add(role.getCode());
        }
        return roleNameList;
    }

    public Store_user getUser(String accountId) {
        return this.fetch(Cnd.where("accountId", "=", accountId));
    }

    @Override
    @Transactional
    public void addDo(Account_info accountInfo, Account_user accountUser) {
        Account_info info = accountInfoService.insert(accountInfo);
        accountUser.setAccountId(info.getId());
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        String salt = rng.nextBytes().toBase64();
        String hashedPasswordBase64 = new Sha256Hash(accountUser.getPassword(), salt, 1024).toBase64();
        accountUser.setSalt(salt);
        accountUser.setPassword(hashedPasswordBase64);
        accountUserService.insert(accountUser);
        Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
        Store_user storeUser = new Store_user();
        storeUser.setAccountId(info.getId());
        storeUser.setStoreId(user.getStoreId());
        storeUserService.insert(storeUser);
    }
}
