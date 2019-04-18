package com.aebiz.app.web.commons.shiro.realm;

import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountLoginService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.store.modules.models.Store_role;
import com.aebiz.app.store.modules.models.Store_user;
import com.aebiz.app.store.modules.services.StoreRoleService;
import com.aebiz.app.store.modules.services.StoreUserService;
import com.aebiz.baseframework.shiro.exception.CaptchaEmptyException;
import com.aebiz.baseframework.shiro.exception.CaptchaIncorrectException;
import com.aebiz.app.web.commons.shiro.token.StoreCaptchaToken;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.nutz.castor.Castors;
import org.nutz.dao.Cnd;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by wizzer on 2017/1/11.
 */
public class StoreAuthorizingRealm extends AuthorizingRealm {
    private static final Log log = Logs.get();
    @Autowired
    private AccountUserService accountUserService;
    @Autowired
    private AccountLoginService accountLoginService;
    @Autowired
    private StoreUserService storeUserService;
    @Autowired
    private StoreRoleService storeRoleService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (token.getClass().isAssignableFrom(StoreCaptchaToken.class)) {
            StoreCaptchaToken authcToken = Castors.me().castTo(token, StoreCaptchaToken.class);
            String loginname = authcToken.getUsername();
            String captcha = authcToken.getCaptcha();
            if (Strings.isBlank(loginname)) {
                throw Lang.makeThrow(AuthenticationException.class, "Account name is empty");
            }
            int errCount = NumberUtils.toInt(Strings.sNull(SecurityUtils.getSubject().getSession(true).getAttribute("storeErrCount")));
            if (errCount > 2) {
                //输错三次显示验证码窗口
                if (Strings.isBlank(captcha)) {
                    throw Lang.makeThrow(CaptchaEmptyException.class, "Captcha is empty");
                }
                String _captcha = Strings.sBlank(SecurityUtils.getSubject().getSession(true).getAttribute("storeCaptcha"));
                if (!authcToken.getCaptcha().equalsIgnoreCase(_captcha)) {
                    throw Lang.makeThrow(CaptchaIncorrectException.class, "Captcha is error");
                }
            }
            Account_user accountUser = accountUserService.getAccountByLoginname(loginname);
            if (Lang.isEmpty(accountUser)) {
                throw Lang.makeThrow(UnknownAccountException.class, "Account [ %s ] not found", loginname);
            }
            Store_user user = storeUserService.fetch(Cnd.where("accountId", "=", accountUser.getAccountId()));
            if (Lang.isEmpty(user)) {
                throw Lang.makeThrow(UnknownAccountException.class, "Account [ %s ] not found", loginname);
            }
            if (accountUser.isDisabled()) {
                throw Lang.makeThrow(LockedAccountException.class, "Account [ %s ] is locked.", loginname);
            }
            storeUserService.fetchLinks(user, null);
            storeUserService.fillMenu(user);
            SecurityUtils.getSubject().getSession(true).setAttribute("storeErrCount", 0);
            SecurityUtils.getSubject().getSession(true).setAttribute("storeUid", user.getAccountId());
            SecurityUtils.getSubject().getSession(true).setAttribute("storeId", user.getStoreId());
            SecurityUtils.getSubject().getSession(true).setAttribute("storeUsername", accountUser.getLoginname());
            String storeName = "";
            if (user.getStoreMain() != null) {
                storeName = user.getStoreMain().getStoreName();
            }
            SecurityUtils.getSubject().getSession(true).setAttribute("storeName", storeName);
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accountUser.getPassword(), getName());
            info.setCredentialsSalt(ByteSource.Util.bytes(accountUser.getSalt()));
            return info;
        }
        return null;
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Object object = principals.getPrimaryPrincipal();
        if (object.getClass().isAssignableFrom(Store_user.class)) {
            Store_user user = Castors.me().castTo(object, Store_user.class);
            if (!Lang.isEmpty(user)) {
                SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
                info.addRoles(storeUserService.getRoleCodeList(user));
                for (Store_role role : user.getRoles()) {
                    if (!role.isDisabled())
                        info.addStringPermissions(storeRoleService.getPermissionNameList(role));
                }
                return info;
            } else {
                return null;
            }
        }
        return null;
    }

    public StoreAuthorizingRealm() {
        this(null, null);
    }

    public StoreAuthorizingRealm(CacheManager cacheManager, CredentialsMatcher matcher) {
        super(cacheManager, matcher);
        setAuthenticationTokenClass(StoreCaptchaToken.class);
    }

    public StoreAuthorizingRealm(CacheManager cacheManager) {
        this(cacheManager, null);
    }

    public StoreAuthorizingRealm(CredentialsMatcher matcher) {
        this(null, matcher);
    }
}