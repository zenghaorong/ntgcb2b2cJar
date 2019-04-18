package com.aebiz.app.web.commons.shiro.realm;

import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountLoginService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.member.modules.models.Member_user;
import com.aebiz.app.member.modules.services.MemberUserService;
import com.aebiz.app.store.modules.models.Store_role;
import com.aebiz.app.store.modules.models.Store_user;
import com.aebiz.app.store.modules.services.StoreRoleService;
import com.aebiz.app.store.modules.services.StoreUserService;
import com.aebiz.app.web.commons.shiro.token.MemberCaptchaToken;
import com.aebiz.app.web.commons.shiro.token.StoreCaptchaToken;
import com.aebiz.baseframework.shiro.exception.CaptchaEmptyException;
import com.aebiz.baseframework.shiro.exception.CaptchaIncorrectException;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wizzer on 2017/1/11.
 */
public class MemberAuthorizingRealm extends AuthorizingRealm {

    private static final Log log = Logs.get();

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private MemberUserService memberUserService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (token.getClass().isAssignableFrom(MemberCaptchaToken.class)) {
            MemberCaptchaToken authcToken = Castors.me().castTo(token, MemberCaptchaToken.class);
            String loginname = authcToken.getUsername();
            String captcha = authcToken.getCaptcha();
            if (Strings.isBlank(loginname)) {
                throw Lang.makeThrow(AuthenticationException.class, "Member name is empty");
            }
            int errCount = NumberUtils.toInt(Strings.sNull(SecurityUtils.getSubject().getSession(true).getAttribute("memberErrCount")));
            if (errCount > 2) {
                //输错三次显示验证码窗口
                if (Strings.isBlank(captcha)) {
                    throw Lang.makeThrow(CaptchaEmptyException.class, "Captcha is empty");
                }
                String _captcha = Strings.sBlank(SecurityUtils.getSubject().getSession(true).getAttribute("memberCaptcha"));
                if (!authcToken.getCaptcha().equalsIgnoreCase(_captcha)) {
                    throw Lang.makeThrow(CaptchaIncorrectException.class, "Captcha is error");
                }
            }
            Account_user accountUser = accountUserService.getAccountByLoginname(loginname);
            if (Lang.isEmpty(accountUser)) {
                throw Lang.makeThrow(UnknownAccountException.class, "Member [ %s ] not found", loginname);
            }
            Member_user user = new Member_user();
// emberUserService.fetch(Cnd.where("accountId", "=", accountUser.getAccountId()));
//            if (Lang.isEmpty(user)) {
//                throw Lang.makeThrow(UnknownAccountException.class, "Member [ %s ] not found", loginname);
//            }
            if (accountUser.isDisabled()) {
                throw Lang.makeThrow(LockedAccountException.class, "Member [ %s ] is locked.", loginname);
            }
//            memberUserService.fetchLinks(user, null);
            SecurityUtils.getSubject().getSession(true).setAttribute("memberErrCount", 0);
//            SecurityUtils.getSubject().getSession(true).setAttribute("memberUid", user.getAccountId());
            SecurityUtils.getSubject().getSession(true).setAttribute("memberUsername", accountUser.getLoginname());

            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(accountUser, accountUser.getPassword(), getName());
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
        if (object.getClass().isAssignableFrom(Member_user.class)) {
            Member_user user = Castors.me().castTo(object, Member_user.class);
            if (!Lang.isEmpty(user)) {
                SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
                List<String> roleNameList = new ArrayList<String>();
                roleNameList.add("member");
                info.addRoles(roleNameList);
                return info;
            } else {
                return null;
            }
        }
        return null;
    }

    public MemberAuthorizingRealm() {
        this(null, null);
    }

    public MemberAuthorizingRealm(CacheManager cacheManager, CredentialsMatcher matcher) {
        super(cacheManager, matcher);
        setAuthenticationTokenClass(MemberCaptchaToken.class);
    }

    public MemberAuthorizingRealm(CacheManager cacheManager) {
        this(cacheManager, null);
    }

    public MemberAuthorizingRealm(CredentialsMatcher matcher) {
        this(null, matcher);
    }
}