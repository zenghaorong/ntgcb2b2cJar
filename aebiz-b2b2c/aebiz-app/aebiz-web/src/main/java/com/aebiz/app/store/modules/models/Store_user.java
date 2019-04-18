package com.aebiz.app.store.modules.models;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.sys.modules.models.Sys_menu;
import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 商家用户表
 * Created by wizzer on 2017/3/1.
 */
@Table("store_user")
@TableIndexes({@Index(name = "INDEX_STORE_USER_ACC", fields = {"accountId"}, unique = true)})
public class Store_user extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("帐号ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String accountId;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeId;

    @Column
    @Comment("常用菜单")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String customMenu;

    @Column
    @Comment("皮肤样式")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String loginTheme;

    @Column
    private boolean loginSidebar;

    @Column
    private boolean loginBoxed;

    @Column
    private boolean loginScroll;

    @Column
    private boolean loginPjax;

    @One(target = Account_info.class, field = "accountId")
    private Account_info accountInfo;

    @One(field = "storeId")
    private Store_main storeMain;

    @ManyMany(from = "userId", relation = "store_user_role", to = "roleId")
    private List<Store_role> roles;

    protected List<Store_menu> menus;

    protected List<Store_menu> firstMenus;

    protected Map<String, List<Store_menu>> secondMenus;

    private List<Store_menu> customMenus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomMenu() {
        return customMenu;
    }

    public void setCustomMenu(String customMenu) {
        this.customMenu = customMenu;
    }

    public String getLoginTheme() {
        return loginTheme;
    }

    public void setLoginTheme(String loginTheme) {
        this.loginTheme = loginTheme;
    }

    public boolean isLoginSidebar() {
        return loginSidebar;
    }

    public void setLoginSidebar(boolean loginSidebar) {
        this.loginSidebar = loginSidebar;
    }

    public boolean isLoginBoxed() {
        return loginBoxed;
    }

    public void setLoginBoxed(boolean loginBoxed) {
        this.loginBoxed = loginBoxed;
    }

    public boolean isLoginScroll() {
        return loginScroll;
    }

    public void setLoginScroll(boolean loginScroll) {
        this.loginScroll = loginScroll;
    }

    public boolean isLoginPjax() {
        return loginPjax;
    }

    public void setLoginPjax(boolean loginPjax) {
        this.loginPjax = loginPjax;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Account_info getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(Account_info accountInfo) {
        this.accountInfo = accountInfo;
    }

    public Store_main getStoreMain() {
        return storeMain;
    }

    public void setStoreMain(Store_main storeMain) {
        this.storeMain = storeMain;
    }

    public List<Store_role> getRoles() {
        return roles;
    }

    public void setRoles(List<Store_role> roles) {
        this.roles = roles;
    }

    public List<Store_menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Store_menu> menus) {
        this.menus = menus;
    }

    public List<Store_menu> getFirstMenus() {
        return firstMenus;
    }

    public void setFirstMenus(List<Store_menu> firstMenus) {
        this.firstMenus = firstMenus;
    }

    public Map<String, List<Store_menu>> getSecondMenus() {
        return secondMenus;
    }

    public void setSecondMenus(Map<String, List<Store_menu>> secondMenus) {
        this.secondMenus = secondMenus;
    }

    public List<Store_menu> getCustomMenus() {
        return customMenus;
    }

    public void setCustomMenus(List<Store_menu> customMenus) {
        this.customMenus = customMenus;
    }
}
