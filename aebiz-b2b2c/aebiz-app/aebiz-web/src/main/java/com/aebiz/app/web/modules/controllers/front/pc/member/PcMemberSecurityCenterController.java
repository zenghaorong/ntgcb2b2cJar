package com.aebiz.app.web.modules.controllers.front.pc.member;

import com.aebiz.app.acc.modules.commons.utils.PasswordStrengthUtil;
import com.aebiz.app.acc.modules.models.Account_login;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountLoginService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.member.modules.models.Member_account;

import com.aebiz.app.member.modules.models.Member_user;
import com.aebiz.app.member.modules.services.MemberAccountService;
import com.aebiz.app.member.modules.services.MemberUserService;

import com.aebiz.app.msg.modules.models.Msg_send;
import com.aebiz.app.msg.modules.services.CommMsgService;
import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.redis.RedisService;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.DateUtil;
import com.aebiz.commons.utils.SpringUtil;
import com.aebiz.commons.utils.StringUtil;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Chain;
import org.nutz.lang.Strings;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 会员安全中心相关接口的控制器。
 * 主要接口：
 * 1.安全中心首页的相关数据（包括：1.1登录密码，1.2绑定的手机号，1.3绑定的邮箱，1.4支付密码，1.5安全服务提示）。
 * 2.修改登录密码。
 * 3.1绑定手机号，3.2修改绑定的手机号。
 * 4.1绑定邮箱，4.2修改
 */
@Controller
@RequestMapping("/member/security")
public class PcMemberSecurityCenterController {

    private static final Log log = Logs.get();

    @Autowired
    private MemberAccountService memberAccountService;

    @Autowired
    private AccountLoginService accountLoginService;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private MemberUserService memberUserService;

    @Autowired
    private CommMsgService commMsgService;

    @Autowired
    private RedisService redisService;

    /**
     * 安全中心的首页
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request) {
        String lastLoginTime = "", mobile = "",email = ""; // 最后的登录时间 --> 格式:yyyy-MM-dd HH:mm:ss

        // 是否有支付密码、手机号、邮箱
        boolean hasPayPassword = false, hasMobile = false, hasEmail = false;

        String accountId = StringUtil.getMemberUid();
        Subject subject = SecurityUtils.getSubject();
        Member_user user = (Member_user) subject.getPrincipal();

        Account_login accountLogin = accountLoginService.fetch(Cnd.where("accountId", "=", user.getAccountId()).desc("loginAt"));
        accountLogin = accountLoginService.fetch(Cnd.where("accountId", "=", user.getAccountId()).and("loginAt","!=",accountLogin.getLoginAt()).desc("loginAt"));

        if (accountLogin != null) {
            Integer loginTime = accountLogin.getLoginAt();
            lastLoginTime = DateUtil.getDate(loginTime);
        }

        Member_account memberAccount = memberAccountService.fetch(user.getAccountId());
        if (memberAccount != null) {
            String password = memberAccount.getPayPassword();
            hasPayPassword = !Strings.isEmpty(password);
        }

        Account_user accountUser = accountUserService.getAccount(user.getAccountId());
        if (accountUser != null) {
            mobile = accountUser.getMobile();
            hasMobile = Strings.isNotBlank(mobile);

            // 手机号中间四位替换成*
            mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");

            email = accountUser.getEmail();
            hasEmail = !Strings.isEmpty(email);
        }

        NutMap obj = NutMap.NEW();
        obj.put("mobile", mobile);
        obj.put("email", email);
        obj.put("lastLoginTime", lastLoginTime);
        obj.put("hasEmail", hasEmail);
        obj.put("hasMobile", hasMobile);
        obj.put("hasPayPassword", hasPayPassword);
        obj.put("passwordStrength", accountUser.getPasswordStrength());

        int safeStrength = memberAccountService.getSecurityScore(accountId);
        obj.put("safeStrength", safeStrength);

        request.setAttribute("obj", obj);
        return "pages/front/pc/member/safe/bind";
    }

    /**
     * 跳转到会员安全中心-更换绑定手机页面
     * @return
     */
    @RequestMapping("/securityBindMobile")
    public String securityBindMobile(HttpServletRequest request) {
        String accountId = StringUtil.getMemberUid();
        Account_user accountUser = accountUserService.getAccount(accountId);
        String mobile = accountUser.getMobile();
        request.setAttribute("mobile",mobile);
        // 手机号中间四位替换成*
        mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        request.setAttribute("mobileHide",mobile);
        return "pages/front/pc/member/safe/changeBindMoile";
    }

    /**
     * 检查绑定的手机号
     * @param mobile    手机号
     * @param mobileCode   验证码
     */
    @RequestMapping("/checkbindMobile")
    public String checkbindMobile(@RequestParam("mobile") String mobile,
                             @RequestParam("mobileCode") String mobileCode,
                             HttpServletRequest request) {
        String accountId = StringUtil.getMemberUid();
        try {
            //验证码校验
            String key = mobile+"_"+ accountId;
            try (Jedis jedis = redisService.jedis()) {
                String code = jedis.get(key);
                if (Strings.isBlank(code)) {//验证码失效
                    request.setAttribute("error", "验证码失效！");
                    return "pages/front/pc/member/safe/changeBindMoile";
                }
                if (!Strings.sNull(mobileCode).equalsIgnoreCase(code)) {//验证码不对
                    request.setAttribute("mobile", mobile);
                    // 手机号中间四位替换成*
                    mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                    request.setAttribute("mobileHide", mobile);
                    request.setAttribute("error", "验证码不对！");
                    return "pages/front/pc/member/safe/changeBindMoile";
                }
            }
            Chain chain = Chain.make("mobile", mobile).add("opAt", DateUtil.getTime(new Date())).add("opBy", StringUtil.getUid());
            accountUserService.update(chain, Cnd.where("accountId", "=", accountId));
            return "pages/front/pc/member/safe/changeBindMoile2";
        } catch (Exception e) {
            request.setAttribute("mobile",mobile);
            // 手机号中间四位替换成*
            mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            request.setAttribute("mobileHide",mobile);
            request.setAttribute("error","验证码错误！");
            return "pages/front/pc/member/safe/changeBindMoile";
        }
    }

    /**
     * 异步校验手机号是否已存在
     * @return
     */
    @RequestMapping("/checkMobile/{mobile}")
    @SJson
    public Object checkMobile(@PathVariable("mobile") String mobile) {
        try {
            //判断是否手机号是否已被注册
            boolean exists = memberUserService.checkUnique("mobile",mobile);
            if(exists){//存在
                return Result.error();
            }
            //不存在
            return Result.success();
        }catch (Exception e){
            return Result.error();
        }
    }


    /**
     * 绑定或者修改绑定的手机号
     * @param mobile    手机号
     * @param captcha   密码
     */
    @RequestMapping("/bindMobile")
    public String bindMobile(@RequestParam("mobile") String mobile,
                             @RequestParam("mobileCode") String captcha,
                             HttpSession session,HttpServletRequest request) {
        String accountId = StringUtil.getMemberUid();
        try {
            //验证码校验
            String key = mobile+"_" + accountId;
            try (Jedis jedis = redisService.jedis()) {
                String code = jedis.get(key);
                if (Strings.isBlank(code)) {//验证码失效
                    request.setAttribute("error", "验证码失效！");
                    return "pages/front/pc/member/safe/changeBindMoileFail";
                }
                if (!Strings.sNull(captcha).equalsIgnoreCase(code)) {//验证码不对
                    request.setAttribute("error", "验证码不对！");
                    return "pages/front/pc/member/safe/changeBindMoileFail";
                }
            }
            Chain chain = Chain.make("mobile", mobile).add("opAt", DateUtil.getTime(new Date())).add("opBy", StringUtil.getUid());
            accountUserService.update(chain, Cnd.where("accountId", "=", accountId));
            return "pages/front/pc/member/safe/changeBindMoileSuccess";
        } catch (Exception e) {
            e.printStackTrace();
            return "pages/front/pc/member/safe/changeBindMoileFail";
        }
    }

    /**
     * 跳转到会员安全中心-绑定邮箱页面
     * @return
     */
    @RequestMapping("/BindEmail")
    public String BindEmail(HttpServletRequest request) {
        return "pages/front/pc/member/safe/bindEmail";
    }

    /**
     * 异步校验邮箱是否已存在
     * @return
     */
    @RequestMapping("/checkEmail")
    @SJson
    public Object checkEmail(@RequestParam("email") String email) {
        try {
            //判断email是否存在
            boolean exists = memberUserService.checkUnique("email",email);
            if(exists){//存在
                return Result.error();
            }
            //不存在
            return Result.success();
        }catch (Exception e){
            return Result.error();
        }
    }

    /**
     * 跳转到会员安全中心-修改绑定邮箱页面
     * @return
     */
    @RequestMapping("/securityBindEmail")
    public String securityBindEmail(HttpServletRequest request) {
        String accountId = StringUtil.getMemberUid();
        Account_user accountUser = accountUserService.getAccount(accountId);
        String email = accountUser.getEmail();
        request.setAttribute("email",email);
        // 手机号中间四位替换成*
        email = email.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        request.setAttribute("emailHide",email);
        return "pages/front/pc/member/safe/changebindEmail";
    }

    @RequestMapping("/checkbindEmail")
    public String checkbindEmail(@RequestParam("email") String email,
                                  @RequestParam("emailCode") String emailCode,
                                  HttpServletRequest request) {
        String accountId = StringUtil.getMemberUid();
        try {
            //验证码校验
            String key = email+"_" +accountId;
            try (Jedis jedis = redisService.jedis()) {
                String code = jedis.get(key);
                if (Strings.isBlank(code)) {//验证码失效
                    request.setAttribute("email", email);
                    // 手机号中间四位替换成*
                    email = email.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                    request.setAttribute("emailHide", email);
                    request.setAttribute("error", "验证码失效！");
                    return "pages/front/pc/member/safe/changeBindEmail";
                }
                if (!Strings.sNull(emailCode).equalsIgnoreCase(code)) {//验证码不对
                    request.setAttribute("email", email);
                    // 手机号中间四位替换成*
                    email = email.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                    request.setAttribute("emailHide", email);
                    request.setAttribute("error", "验证码不对！");
                    return "pages/front/pc/member/safe/changeBindEmail";
                }

            }
            return "pages/front/pc/member/safe/changeBindEmail2";
        } catch (Exception e) {
            request.setAttribute("email",email);
            // 手机号中间四位替换成*
            email = email.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            request.setAttribute("emailHide",email);
            request.setAttribute("error","验证码错误！");
            return "pages/front/pc/member/safe/changeBindEmail";
        }
    }

    /**
     * 绑定或者修改绑定的邮箱
     * @param email     手机号
     * @param captcha   密码
     * @return 成功或者失败的提示
     */
    @RequestMapping("/userBindEmail")
    public String bindEmail(@RequestParam("email") String email,
                            @RequestParam("emailCode") String captcha,
                            HttpServletRequest request) {
        String accountId = StringUtil.getMemberUid();
        try {
            //验证码校验
            String key = email+"_" + StringUtil.getMemberUid();
            try (Jedis jedis = redisService.jedis()) {
                String code = jedis.get(key);
                if (Strings.isBlank(code)) {//验证码失效
                    request.setAttribute("error", "验证码失效！");
                    return "pages/front/pc/member/safe/BindEmailSuccessFail";
                }
                if (!Strings.sNull(captcha).equalsIgnoreCase(code)) {//验证码不对
                    request.setAttribute("error", "验证码不对！");
                    return "pages/front/pc/member/safe/BindEmailSuccessFail";
                }
            }
            Chain chain = Chain.make("email", email).add("opAt", DateUtil.getTime(new Date())).add("opBy", StringUtil.getUid());
            accountUserService.update(chain, Cnd.where("accountId", "=", accountId));
            return "pages/front/pc/member/safe/BindEmailSuccess";
        } catch (Exception e) {
            e.printStackTrace();
            return "pages/front/pc/member/safe/BindEmailSuccessFail";
        }
    }

    /**
     * 绑定或者修改绑定的邮箱
     * @param email     手机号
     * @param captcha   密码
     * @return 成功或者失败的提示
     */
    @RequestMapping("/userChangeBindEmail")
    public String userChangeBindEmail(@RequestParam("email") String email,
                            @RequestParam("emailCode") String captcha,
                            HttpSession session,HttpServletRequest request) {
        String accountId = StringUtil.getMemberUid();
        try {
            //验证码校验
            String key = email+"_" + StringUtil.getMemberUid();
            try (Jedis jedis = redisService.jedis()) {
                String code = jedis.get(key);
                if (Strings.isBlank(code)) {//验证码失效
                    request.setAttribute("error", "验证码失效！");
                    return "pages/front/pc/member/safe/BindEmailSuccessFail";
                }
                if (!Strings.sNull(captcha).equalsIgnoreCase(code)) {//验证码不对
                    request.setAttribute("error", "验证码不对！");
                    return "pages/front/pc/member/safe/BindEmailSuccessFail";
                }
            }
            Chain chain = Chain.make("email", email).add("opAt", DateUtil.getTime(new Date())).add("opBy", StringUtil.getUid());
            accountUserService.update(chain, Cnd.where("accountId", "=", accountId));
            return "pages/front/pc/member/safe/BindEmailSuccess";

        } catch (Exception e) {
            e.printStackTrace();
            return "pages/front/pc/member/safe/BindEmailSuccessFail";
        }
    }

    /**
     * 手机验证码
     */
    @SJson
    @RequestMapping("/getMobileCaptcha/{mobile}")
    public Object mobileCaptcha(@PathVariable("mobile") String mobile, HttpSession session) {

        try {
            // 随机生成6位数作为验证码。
            String captcha = StringUtil.getRndString(6);
            String key = mobile+"_" + StringUtil.getMemberUid();
            try (Jedis jedis = redisService.jedis()) {
                jedis.set(key, captcha);
                jedis.expire(key, 60);
            }
            boolean isSuccess = commMsgService.sendSMSCaptcha("getCaptcha", mobile, captcha);//发送验证码到用户手机。
            if (isSuccess) {
                return Result.success();
            } else {
                return Result.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(4, e.getMessage());
        }
    }

    /**
     * 邮箱验证码
     */
    @SJson
    @RequestMapping("/getEmailCaptcha")
    public Object emailCaptcha(@RequestParam("email") String email) {
        try {
            // 随机生成6位数作为验证码。
            String captcha = StringUtil.getRndString(6);
            if(!Strings.isEmpty(email)){
                NutMap param = new NutMap();
                param.put("url",captcha);
                commMsgService.sendMsg("hyzhmm",null,null,null,"会员绑定邮箱服务", Msg_send.SendTypeEnum.EMAIL.getName(),null,email,param,false);

                String key = email+"_" + StringUtil.getMemberUid();
                try (Jedis jedis = redisService.jedis()) {
                    jedis.set(key, captcha);
                    jedis.expire(key, 600);
                }
                return Result.success();
            }
            return Result.error();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    /**
     * 跳转到会员安全中心-修改登录密码页面
     * @return
     */
    @RequestMapping("/midifyPassword")
    public String midifyPassword(HttpServletRequest request) {
        String accountId = StringUtil.getMemberUid();
        Account_user accountUser = accountUserService.getAccount(accountId);
        String mobile = accountUser.getMobile();
        request.setAttribute("mobile",mobile);
        // 手机号中间四位替换成*
        mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        request.setAttribute("mobileHide",mobile);
        return "pages/front/pc/member/safe/modifyPassword";
    }

    /**
     * 修改登录密码 - 手机验证码
     * @return
     */
    @RequestMapping("/mobileCaptcha/{mobile}")
    @SJson
    public Object mobileCaptcha1(@PathVariable("mobile") String mobile,HttpSession session) throws Exception {
        try{
            NutMap param = new NutMap();
            String code = StringUtil.getRndNumber(6);
//            session.setAttribute("memberMobileCaptcha", code);

            //短信验证码
            String key = "memberPasswordMobileCaptcha_" + StringUtil.getMemberUid();
            try (Jedis jedis = redisService.jedis()) {
                jedis.set(key, code);
                jedis.expire(key, 600);
            }
            param.put("code", code);
            param.put("product","茗流荟");
            commMsgService.sendMsg("hyzhmm",null,null,null,null, Msg_send.SendTypeEnum.SMS.getName(),null,mobile,param,false);
            return Result.success();
        }catch (Exception e){
            e.printStackTrace();
            return Result.error();
        }
    }

    @RequestMapping("/getMobileCaptcha")
    @SJson
    public Object getMobileCaptcha(@RequestParam("verifyCode")String verifyCode) {
        try{
            //验证码校验
            String key = "memberPasswordMobileCaptcha_" + StringUtil.getMemberUid();
            try (Jedis jedis = redisService.jedis()) {
                String code = jedis.get(key);
                if (Strings.isBlank(code)) {//验证码失效
                    return Result.error("验证码失效");
                } else if (!Strings.sNull(verifyCode).equalsIgnoreCase(code)) {//验证码不对
                    return Result.error("验证码不对");
                }
            }
            return Result.success();
        }catch (Exception e){
            return Result.error();
        }
    }

    /**
     * 修改登录密码
     */
    @RequestMapping("/updatePassword/{mobile}")
    public String updatePassword(@PathVariable("mobile") String mobile,
                                 @RequestParam("passwordStrength") String passwordStrength,
                                 @RequestParam("password") String password,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("mobileCode") String mobileCode,
                                 HttpServletRequest request) {
        try{
            String accountId = StringUtil.getMemberUid();
            Account_user accountUser = accountUserService.getAccount(accountId);
            String oldhashedPasswordBase64 = new Sha256Hash(oldPassword, accountUser.getSalt(), 1024).toBase64();
            if(!oldhashedPasswordBase64.equals(accountUser.getPassword())){
                request.setAttribute("error","原密码错误!");
                mobile = accountUser.getMobile();
                request.setAttribute("mobile",mobile);
                // 手机号中间四位替换成*
                mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                request.setAttribute("mobileHide",mobile);
                return "pages/front/pc/member/safe/modifyPassword";
            }
            String hashedPasswordBase64 = new Sha256Hash(password, accountUser.getSalt(), 1024).toBase64();
            accountUser.setPassword(hashedPasswordBase64);
            accountUser.setPasswordStrength(Integer.parseInt(passwordStrength));
            accountUserService.updateIgnoreNull(accountUser);
            return "pages/front/pc/member/safe/modifyPasswordSuccess";
        }catch (Exception e){
            e.printStackTrace();
            return "pages/front/pc/member/safe/modifyPasswordfail";
        }
    }

    /**
     * 跳转到会员安全中心-启用禁用页
     * @return
     */
    @RequestMapping("/midifyPayPasswordEnable")
    @RequiresAuthentication
    public String midifyPayPasswordEnable(HttpServletRequest req) {
        Member_account account = memberAccountService.getField("payPassword", Cnd.where("accountId", "=", StringUtil.getMemberUid()));
        if (Strings.isNotBlank(account.getPayPassword())) {
            return "pages/front/pc/member/safe/modifyPayPasswordEnable";
        }
        return midifyPayPassword(req);
    }

    /**
     * 跳转到会员安全中心-修改支付密码页面
     * @return
     */
    @RequestMapping("/midifyPayPassword")
    @RequiresAuthentication
    public String midifyPayPassword(HttpServletRequest req) {
        Account_user accountUser = accountUserService.getAccount(StringUtil.getMemberUid());
        String mobile = accountUser.getMobile();
        req.setAttribute("mobile",mobile);
        // 手机号中间四位替换成*
        mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        req.setAttribute("mobileHide",mobile);
        return "pages/front/pc/member/safe/modifyPayPassword";
    }

    /**
     * 修改支付密码 - 手机验证码
     * @return
     */
    @RequestMapping("/payCaptcha/{mobile}")
    @SJson
    public Object payCaptcha(@PathVariable("mobile") String mobile) throws Exception {
        try{
            NutMap param = new NutMap();
            String code = StringUtil.getRndNumber(6);
            param.put("code", code);
            param.put("product","茗流荟");
            commMsgService.sendMsg("hyzhmm",null,null,null,null, Msg_send.SendTypeEnum.SMS.getName(),null,mobile,param,false);

            //短信验证码
            String key = "captcha_paypassword_" + StringUtil.getMemberUid();
            try (Jedis jedis = redisService.jedis()) {
                jedis.set(key, code);
                jedis.expire(key, 600);
            }
            return Result.success("发送成功");
        }catch (Exception e){
            log.error(e.getMessage());
            return Result.error("发送失败！");
        }
    }

    /**
     * 修改支付密码
     */
    @RequestMapping("/updatePayPassword")
    @SJson
    @RequiresAuthentication
    public Result updatePayPassword(@RequestParam("mobileCode") String payCaptcha, @RequestParam("password") String payPassword, HttpServletRequest request) {
        try{
            //验证码校验
            String key = "captcha_paypassword_" + StringUtil.getMemberUid();
            try (Jedis jedis = redisService.jedis()) {
                String code = jedis.get(key);
                if (Strings.isBlank(code)) {//验证码失效
                    return Result.error("验证码失效");
                } else if (!payCaptcha.equalsIgnoreCase(code)){//验证码不对
                    return Result.error("验证码不对");
                }
            }

            Member_account account = memberAccountService.getField("id", Cnd.where("accountId", "=", StringUtil.getMemberUid()));
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash(payPassword, salt, 1024).toBase64();
            memberAccountService.update(Chain.make("payPassword", hashedPasswordBase64).add("payPasswordSalt", salt).add("payPasswordEnabled", true), Cnd.where("id", "=", account.getId()));
            return Result.success("修改成功");
        }catch (Exception e){
            log.error(e.getMessage());
            return Result.success("修改失败");
        }
    }

    /**
     * 修改支付密码-成功页
     */
    @RequestMapping("/modifyPayPasswordSuccess")
    @RequiresAuthentication
    public String modifyPayPasswordSuccess(HttpServletRequest request) {
        return "pages/front/pc/member/safe/modifyPayPasswordSuccess";
    }

    /**
     * 修改支付密码-失败页
     */
    @RequestMapping("/modifyPayPasswordfail")
    @RequiresAuthentication
    public String modifyPayPasswordfail(HttpServletRequest request) {
        return "pages/front/pc/member/safe/modifyPayPasswordfail";
    }

}