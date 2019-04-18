package com.aebiz.app.web.modules.controllers.front.pc.member;

import cn.apiclub.captcha.Captcha;
import com.aebiz.app.acc.modules.models.Account_login;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountLoginService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.member.modules.models.Member_user;
import com.aebiz.app.member.modules.services.MemberCartService;
import com.aebiz.app.member.modules.services.MemberCouponService;
import com.aebiz.app.member.modules.services.MemberUserService;
import com.aebiz.app.order.commons.constant.OrderConsts;
import com.aebiz.app.sys.modules.models.Sys_log;
import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.app.web.commons.log.SLogService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.app.web.commons.shiro.token.MemberCaptchaToken;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.shiro.exception.CaptchaEmptyException;
import com.aebiz.baseframework.shiro.exception.CaptchaIncorrectException;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.CookieUtil;
import com.aebiz.commons.utils.RSAUtil;
import com.aebiz.commons.utils.UserAgentUtils;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.img.Images;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

/**
 * Created by wizzer on 2017/1/16.
 */
@Controller
@RequestMapping("/member/login")
public class PcMemberLoginController {
    private static final Log log = Logs.get();

    @Autowired
    private MemberUserService memberUserService;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private AccountLoginService accountLoginService;

    @Autowired
    private MemberCouponService memberCouponService;

    @Autowired
    private MemberCartService memberCartService;

    private static final String cookieName = "memberRemeberMe";

    @RequestMapping("")
    public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        SimpleCookie cookie = new SimpleCookie(cookieName);
        String base64 = cookie.readValue(request, response);
        if (Strings.isNotBlank(base64)) {
            request.setAttribute("loginname", Base64.decodeToString(base64));
        }
        try {
            HashMap<String, Object> map = RSAUtil.getKeys();
            //生成公钥和私钥
            RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
            RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
            //模
            String publicKeyModulus = publicKey.getModulus().toString(16);
            //公钥指数
            String publicKeyExponent = publicKey.getPublicExponent().toString(16);
            //私钥指数
            request.setAttribute("memberPublicKeyExponent", publicKeyExponent);
            request.setAttribute("memberPublicKeyModulus", publicKeyModulus);
            session.setAttribute("memberPrivateKey", privateKey);
        } catch (Exception e) {
            log.error("跳转到会员登录页："+e.getMessage());
        }
        return "pages/front/pc/member/login";
    }

    @RequestMapping("/noPermission")
    public String noPermission() {
        return "pages/platform/sys/login";
    }

    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam(value = "w", required = false, defaultValue = "0") int w, @RequestParam(value = "h", required = false, defaultValue = "0") int h) throws Exception {
        if (w * h < 1) { //长或宽为0?重置为默认长宽.
            w = 200;
            h = 60;
        }
        Captcha captcha = new Captcha.Builder(w, h)
                .addText()
                .build();
        String text = captcha.getAnswer();
        session.setAttribute("memberCaptcha", text);
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        Images.write(captcha.getImage(), "png", os);
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
        log.debug("password:::"+password);
        return new MemberCaptchaToken(username, password, rememberMe, host, captcha);
    }

    @RequestMapping("/doLogin")
    @SJson
    public Object doLogin(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam(value = "rememberme", defaultValue = "0", required = false) boolean rememberMe,
                          @RequestParam(value = "captcha", defaultValue = "", required = false) String captcha,
                          HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        int errCount = 0;
        try {
            //输错三次显示验证码窗口
            errCount = NumberUtils.toInt(Strings.sNull(SecurityUtils.getSubject().getSession(true).getAttribute("memberErrCount")));
            Subject subject = SecurityUtils.getSubject();
            ThreadContext.bind(subject);
            subject.login(createToken(username, password, false, captcha, request));
            Member_user user = (Member_user) subject.getPrincipal();
            Account_user accountUser = accountUserService.getAccount(user.getAccountId());
            //等级名称
            String levelName=user.getMemberLevel().getName();
            request.getSession().setAttribute("level",levelName);
            //头像
            String personal_photo=user.getAccountInfo().getImageUrl();
            request.getSession().setAttribute("personal_photo",personal_photo);
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
            Account_login accountLogin = new Account_login();
            accountLogin.setAccountId(user.getAccountId());
            accountLogin.setIp(Lang.getIP(request));
            accountLogin.setLoginType("member");
            accountLogin.setLoginAt((int) (System.currentTimeMillis() / 1000));
            accountLogin.setClientType("pc");
            OperatingSystem operatingSystem = UserAgentUtils.getOperatingSystem(request);
            if (operatingSystem != null) {
                accountLogin.setClientName(operatingSystem.getName());
            }
            Browser browser = UserAgentUtils.getBrowser(request);
            if (browser != null) {
                accountLogin.setClientBrowser(browser.getName());
            }

            //取优惠券数量
            int count = memberCouponService.count(Cnd.where("accountId","=",accountUser.getAccountId()).and("status","=",0).and("delFlag","=",0));
            request.getSession().setAttribute("couponCount",count);

            //从cookie里获取购物车信息,若存在,则同步会员信息[取出并删除掉]
            String cookieSkuStr = CookieUtil.getCookie(request,response, OrderConsts.CART_COOKIE_SKU);
            String cookieNumStr = CookieUtil.getCookie(request,response, OrderConsts.CART_COOKIE_NUM);

            accountLoginService.doLogin(accountUser.getAccountId(), cookieSkuStr, cookieNumStr, accountLogin);
            //把cookie里的购物车数据删除
            String[] cartSku1= new String[]{};
            String[] cartNum1= new String[]{};
            CookieUtil.setCookie(response, OrderConsts.CART_COOKIE_SKU, Json.toJson(cartSku1));
            CookieUtil.setCookie(response,OrderConsts.CART_COOKIE_NUM,Json.toJson(cartNum1));
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

    @RequestMapping("/logout")
    @SLog(description = "用户退出", methodReturn = true, type = Sys_log.TypeEnum.LOGIN)
    public void logout(HttpServletResponse response, HttpSession session) throws IOException {
        try {
            Subject subject = SecurityUtils.getSubject();
            if (!Lang.isEmpty(subject.getPrincipal())) {
                Member_user user = (Member_user) subject.getPrincipal();
                subject.logout();
                memberUserService.update(Chain.make("isOnline", false), Cnd.where("id", "=", user.getId()));
            }
        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        } catch (Exception e) {
            log.debug("Logout error", e);
        }
        response.sendRedirect(Globals.APP_BASE + "/member/login");
    }
}






