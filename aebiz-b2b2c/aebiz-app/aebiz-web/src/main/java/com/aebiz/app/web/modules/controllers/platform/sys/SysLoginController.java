package com.aebiz.app.web.modules.controllers.platform.sys;

import cn.apiclub.captcha.Captcha;
import com.aebiz.app.sys.modules.models.Sys_log;
import com.aebiz.app.sys.modules.models.Sys_user;
import com.aebiz.app.sys.modules.services.SysUserService;
import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.app.web.commons.log.SLogService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.shiro.exception.CaptchaEmptyException;
import com.aebiz.baseframework.shiro.exception.CaptchaIncorrectException;
import com.aebiz.app.web.commons.shiro.token.PlatformCaptchaToken;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.RSAUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.img.Images;
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
@RequestMapping("/platform/login")
public class SysLoginController {
    private static final Log log = Logs.get();
    @Autowired
    private SLogService sLogService;
    @Autowired
    private SysUserService sysUserService;
    private String cookieName = "platformRemeberMe";

    @RequestMapping("")
    public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        SimpleCookie cookie = new SimpleCookie(cookieName);
        String base64 = cookie.readValue(request, response);
        if (!Strings.isEmpty(base64)) {
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
            request.setAttribute("platformPublicKeyExponent", publicKeyExponent);
            request.setAttribute("platformPublicKeyModulus", publicKeyModulus);
            session.setAttribute("platformPrivateKey", privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "pages/platform/sys/login";
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
        session.setAttribute("platformCaptcha", text);
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        Images.write(captcha.getImage(), "png", os);
    }

    protected AuthenticationToken createToken(String username, String password, boolean rememberMe, String captcha, HttpServletRequest request) {
        String host = request.getRemoteHost();
        try {
            RSAPrivateKey platformPrivateKey = (RSAPrivateKey) request.getSession().getAttribute("platformPrivateKey");
            if (platformPrivateKey != null) {
                password = RSAUtil.decryptByPrivateKey(password, platformPrivateKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PlatformCaptchaToken(username, password, rememberMe, host, captcha);
    }

    @RequestMapping("/doLogin")
    @SJson
    public Object doLogin(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam(value = "rememberme", defaultValue = "0", required = false) boolean rememberMe, @RequestParam(value = "captcha", defaultValue = "", required = false) String captcha, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        int errCount = 0;
        try {
            //输错三次显示验证码窗口
            errCount = NumberUtils.toInt(Strings.sNull(SecurityUtils.getSubject().getSession(true).getAttribute("platformErrCount")));
            Subject subject = SecurityUtils.getSubject();
            ThreadContext.bind(subject);
            subject.login(createToken(username, password, false, captcha, request));
            Sys_user user = (Sys_user) subject.getPrincipal();
            if (rememberMe) {
                SimpleCookie cookie = new SimpleCookie(cookieName);
                cookie.setHttpOnly(true);
                cookie.setMaxAge(31536000);
                String base64 = Base64.encodeToString(user.getLoginname().getBytes());
                cookie.setValue(base64);
                cookie.saveTo(request, response);
            } else {
                SimpleCookie cookie = new SimpleCookie(cookieName);
                cookie.removeFrom(request, response);
            }
            int count = user.getLoginCount() == null ? 0 : user.getLoginCount();
            sysUserService.update(Chain.make("loginIp", user.getLoginIp()).add("loginAt", (int) (System.currentTimeMillis() / 1000))
                            .add("loginCount", count + 1).add("isOnline", true)
                    , Cnd.where("id", "=", user.getId()));
            Sys_log sysLog = new Sys_log();
            sysLog.setType(Sys_log.TypeEnum.LOGIN);
            sysLog.setModule(this.getClass().getName());
            sysLog.setAction("doLogin");
            sysLog.setIp(Lang.getIP(request));
            sysLog.setDescription("登录系统");
            sysLog.setOpBy(user.getId());
            sysLog.setOpAt((int) (System.currentTimeMillis() / 1000));
            sysLog.setUsername(user.getUsername());
            sLogService.async(sysLog);
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
            SecurityUtils.getSubject().getSession(true).setAttribute("platformErrCount", errCount);
            return Result.error(4, "sys.login.error.username");
        } catch (AuthenticationException e) {
            errCount++;
            SecurityUtils.getSubject().getSession(true).setAttribute("platformErrCount", errCount);
            return Result.error(5, "sys.login.error.password");
        } catch (Exception e) {
            errCount++;
            SecurityUtils.getSubject().getSession(true).setAttribute("platformErrCount", errCount);
            return Result.error(6, "sys.login.error.system");
        }
    }

    @RequestMapping("/logout")
    @SLog(description = "用户退出", methodReturn = true, type = Sys_log.TypeEnum.LOGIN)
    public void logout(HttpServletResponse response, HttpSession session) throws IOException {
        try {
            Subject subject = SecurityUtils.getSubject();
            Sys_user user = (Sys_user) subject.getPrincipal();
            if (user != null) {
                subject.logout();
                sysUserService.update(Chain.make("isOnline", false), Cnd.where("id", "=", user.getId()));
            }
        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        } catch (Exception e) {
            log.debug("Logout error", e);
        }
        //return "redirect:/platform/login"; 这样会报错,因为注销了session,所以使用下一行写法
        response.sendRedirect(Globals.APP_BASE + "/platform/login");
    }

    /**
     * 切换样式，对登陆用户有效
     *
     * @param theme
     * @param req
     * @RequiresUser 记住我有效
     * @RequiresAuthentication 就算记住我也需要重新验证身份
     */
    @RequestMapping("/theme")
    @RequiresAuthentication
    @SJson
    public void theme(@RequestParam("loginTheme") String theme, HttpServletRequest req) {
        if (!Strings.isEmpty(theme)) {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null) {
                Sys_user user = (Sys_user) subject.getPrincipal();
                user.setLoginTheme(theme);
                sysUserService.update(Chain.make("loginTheme", theme), Cnd.where("id", "=", user.getId()));
            }
        }
    }

    /**
     * 切换布局，对登陆用户有效
     *
     * @param p
     * @param v
     * @param req
     * @RequiresUser 记住我有效
     * @RequiresAuthentication 就算记住我也需要重新验证身份
     */
    @RequestMapping("/layout")
    @RequiresAuthentication
    @SJson
    public void layout(@RequestParam("p") String p, @RequestParam("v") boolean v, HttpServletRequest req) {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Sys_user user = (Sys_user) subject.getPrincipal();
            if ("sidebar".equals(p)) {
                sysUserService.update(Chain.make("loginSidebar", v), Cnd.where("id", "=", user.getId()));
                user.setLoginSidebar(v);
            } else if ("boxed".equals(p)) {
                sysUserService.update(Chain.make("loginBoxed", v), Cnd.where("id", "=", user.getId()));
                user.setLoginBoxed(v);
            } else if ("scroll".equals(p)) {
                sysUserService.update(Chain.make("loginScroll", v), Cnd.where("id", "=", user.getId()));
                user.setLoginScroll(v);
            }
        }

    }

}
