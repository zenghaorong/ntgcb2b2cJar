package com.aebiz.app.web.modules.controllers.open.H5;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_login;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountLoginService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.member.modules.models.Member_user;
import com.aebiz.app.sys.modules.models.Sys_log;
import com.aebiz.app.utils.modules.services.SmsService;
import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.app.web.commons.shiro.token.MemberCaptchaToken;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.redis.RedisService;
import com.aebiz.baseframework.shiro.exception.CaptchaEmptyException;
import com.aebiz.baseframework.shiro.exception.CaptchaIncorrectException;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.*;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.nutz.dao.Cnd;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;

/**
 * @Auther: zenghaorong
 * @Date: 2019/3/30  15:11
 * @Description: 登录 注册
 */
@Controller
@RequestMapping("open/H5/login")
public class LoginController {

    private static final Log log = Logs.get();

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private AccountLoginService accountLoginService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SmsService smsService;

    private static final String cookieName = "cheryfsmemberRemeberMe";

    /**
     * 进入登录页
     */
    @RequestMapping("login.html")
    public String login() {
        return "pages/front/h5/niantu/login";
    }

    /**
     * 进入注册页
     */
    @RequestMapping("reg.html")
    public String reg() {
        return "pages/front/h5/niantu/reg";
    }

    /**
     * 进入忘记密码页
     */
    @RequestMapping("forgetPassword.html")
    public String forgetPassword() {
        return "pages/front/h5/niantu/forgetPassword";
    }


    /**
     * 会员登录接口
     * @param username
     * @param password
     * @param rememberMe
     * @param captcha
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value = {"/doLogin"}, method = RequestMethod.POST)
    @SJson
    public Object doLogin(@RequestParam("mobile") String username,
                          @RequestParam("password") String password,
                          @RequestParam(value = "rememberme", defaultValue = "0", required = false) boolean rememberMe,
                          @RequestParam(value = "captcha", defaultValue = "", required = false) String captcha,
                          HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        int errCount = 0;
        try {
            Subject subject = SecurityUtils.getSubject();
            ThreadContext.bind(subject);

//            String verifycode = Strings.sNull(session.getAttribute("memberCaptcha"));
//            if (!captcha.equalsIgnoreCase(Strings.sNull(verifycode))) {
//                return Result.error("sys.login.error.captcha");
//            }
            AuthenticationToken authenticationToken =createToken(username, password, false, "6666", request);
            subject.login(authenticationToken);



            Account_user accountUser = (Account_user) subject.getPrincipal();
//            Account_user accountUser = accountUserService.getAccount(user.getAccountId());
            if (accountUser.isDisabled()) {
                return Result.error("此用户被冻结").addCode(3);
            }
            if (rememberMe) {
                SimpleCookie cookie = new SimpleCookie(cookieName);
                cookie.setHttpOnly(true);
                cookie.setMaxAge(31536000);
                String base64 = Base64.encodeToString(accountUser.getLoginname().getBytes());
                cookie.setValue(base64);
                cookie.saveTo(request, response);
            } else {
                SimpleCookie cookie = new SimpleCookie(cookieName);
                cookie.removeFrom(request, response);
            }

            //设置登录
            CookieUtil.setCookie(response, "cheryfs_member_login", "true");

            Account_login accountLogin = new Account_login();
            accountLogin.setAccountId(accountUser.getAccountId());
            accountLogin.setIp(Lang.getIP(request));
            accountLogin.setLoginType("member");
            accountLogin.setLoginAt((int) (System.currentTimeMillis() / 1000));
            accountLogin.setClientType("wap");
            OperatingSystem operatingSystem = UserAgentUtils.getOperatingSystem(request);
            if (operatingSystem != null) {
                accountLogin.setClientName(operatingSystem.getName());
            }
            Browser browser = UserAgentUtils.getBrowser(request);
            if (browser != null) {
                accountLogin.setClientBrowser(browser.getName());
            }
            accountLoginService.insert(accountLogin);
            return Result.success("sys.login.success");
        } catch (CaptchaIncorrectException e) {
            //自定义的验证码错误异常
            return Result.error(1, "sys.login.error.captcha");
        } catch (CaptchaEmptyException e) {
            //验证码为空
            return Result.error(2, "sys.login.error.captcha");
        } catch (LockedAccountException e) {
            return Result.error(3, "sys.login.error.locked");
        } catch (UnknownAccountException e) {
            errCount++;
            SecurityUtils.getSubject().getSession(true).setAttribute("memberErrCount", errCount);
            return Result.error(4, "sys.login.error.username");
        } catch (AuthenticationException e) {
            errCount++;
            SecurityUtils.getSubject().getSession(true).setAttribute("memberErrCount", errCount);
            return Result.error(5, "sys.login.error.password");
        } catch (Exception e) {
            errCount++;
            SecurityUtils.getSubject().getSession(true).setAttribute("memberErrCount", errCount);
            return Result.error(6, "sys.login.error.system");
        }

    }

    protected AuthenticationToken createToken(String username, String password, boolean rememberMe, String captcha, HttpServletRequest request) {
        String host = request.getRemoteHost();
        try {
            RSAPrivateKey memberPrivateKey = (RSAPrivateKey) request.getSession().getAttribute("memberPrivateKey");
            if (memberPrivateKey != null) {
                password = RSAUtil.decryptByPrivateKey(password, memberPrivateKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MemberCaptchaToken(username, password, rememberMe, host, captcha);
    }


    /**
     * 用户注册
     */
    @RequestMapping("/doRegister")
    @SJson
    public Object doRegister(@RequestParam("mobile") String mobile,
                             String mobileCode,
                             @RequestParam("password") String password,
                             @RequestParam(value = "promotionCode",required = false) String promotionCode,
                             @RequestParam(value = "captcha",required = false) String captcha,HttpSession session,HttpServletRequest request) {
        try {
            //验证码校验
            String key = "memberRegisterMobileCaptcha_" + mobile;
            try (Jedis jedis = redisService.jedis()) {
                String code = jedis.get(key);
                if (Strings.isBlank(code)) {//验证码失效
                    return Result.error("验证码失效");
                } else if (!Strings.sNull(mobileCode).equalsIgnoreCase(code)) {//验证码不对
                    return Result.error("验证码错误");
                }
            }
            Cnd cnd = Cnd.NEW();
            cnd.and("mobile", "=", mobile );
            Account_user account_user = accountUserService.fetch(cnd);
            if(account_user !=null){
                return Result.error("当前手机号已注册");
            }
            Account_user accountUser =new Account_user();
            accountUser.setMobile(mobile);
            accountUser.setPassword(password);
            accountLoginService.memberRegister(accountUser);
            return Result.success("member.register.join.success");
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("member.register.join.fail");
        }
    }

    /**
     * 退出登录
     */
    @RequestMapping("logout.html")
    @SLog(description = "用户退出", methodReturn = true, type = Sys_log.TypeEnum.LOGIN)
    public void logout(HttpServletResponse response) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
//        return "redirect:/open/H5/login/login.html"; 这样写会报错
        response.sendRedirect("/open/H5/login/login.html");
    }

    /**
     * 获取登录验证码
     */
    @RequestMapping("/getLoginMsgCode")
    @SJson
    public Result getLoginMsgCode(@RequestParam("mobile") String mobile) {
        try {
            String key = "memberRegisterMobileCaptcha_" + mobile;
            String expireTime = "300";
            String code = StringUtil.getRndNumber(6);
            try (Jedis jedis = redisService.jedis()) {
                jedis.set(key, code);
                jedis.expire(key, Integer.valueOf(expireTime));
            }
            String content = "验证码"+code+"。次验正码用于校验身份";
            smsService.sendMessages(content,mobile);
            log.info("短信验证码:  " + code);
            return Result.success("member.register.join.success");
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("member.register.join.fail");
        }
    }


}
