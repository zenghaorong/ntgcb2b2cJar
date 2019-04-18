package com.aebiz.app.web.modules.controllers.front.pc.member;

import cn.apiclub.captcha.Captcha;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.member.modules.services.MemberUserService;
import com.aebiz.app.msg.modules.models.Msg_send;
import com.aebiz.app.msg.modules.services.CommMsgService;
import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.redis.RedisService;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.dao.Cnd;
import org.nutz.img.Images;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
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
import java.io.OutputStream;



/**
 * 会员找回密码
 */
@Controller
@RequestMapping("/member/fogetPassword")
public class PcMemberFogetPasswordController {

    private static final Log log = Logs.get();

    @Autowired
    private MemberUserService memberUserService;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private CommMsgService commMsgService;

    @Autowired
    private RedisService redisService;

    /**
     * 跳转到找回密码页面
     * @return
     */
    @RequestMapping("")
    public String fogetPassword() {
        return "pages/front/pc/member/resetPassword1";
    }

    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                        @RequestParam(value = "w", required = false, defaultValue = "0") int w,
                        @RequestParam(value = "h", required = false, defaultValue = "0") int h) throws Exception {
        if (w * h < 1) { //长或宽为0?重置为默认长宽.
            w = 120;
            h = 40;
        }
        Captcha captcha = new Captcha.Builder(w, h).addText().build();
        String text = captcha.getAnswer();
        session.setAttribute("memberFogetPasswordCaptcha", text);
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        Images.write(captcha.getImage(), "png", os);
    }

    @RequestMapping("/getCaptcha")
    @SJson
    public Object getCaptcha(@RequestParam("verifyCode")String verifyCode,
                             HttpSession session,HttpServletRequest request) throws Exception {
        try{
            String verifycode = Strings.sNull(session.getAttribute("memberFogetPasswordCaptcha"));
            if (verifycode.equalsIgnoreCase(Strings.sNull(verifyCode))) {
                return Result.success();
            }
            return Result.error();
        }catch (Exception e){
            return Result.error();
        }
    }


    @RequestMapping("/mobileCaptcha/{mobile}")
    @SJson
    public Object mobileCaptcha(@PathVariable("mobile") String mobile) throws Exception {
        try{
            NutMap param = new NutMap();
            String code = StringUtil.getRndNumber(6);
            param.put("code", code);
            param.put("product","茗流荟");
            commMsgService.sendMsg("hyzhmm",null,null,null,null, Msg_send.SendTypeEnum.SMS.getName(),null,mobile,param,false);

            String key = "forget:captcha_mobile_" + mobile;
            try (Jedis jedis = redisService.jedis()) {
                jedis.set(key, code);
                jedis.expire(key, 600);
            }
            return Result.success();
        }catch (Exception e){
            log.error("发送手机验证码失败："+e.getMessage());
            return Result.error("发送失败！");
        }
    }

    /**
     * 异步校验手机验证码
     * @return
     */
    @RequestMapping("/checkMobileCaptcha/{mobile}")
    public String checkMobileCaptcha(@PathVariable("mobile") String mobile,
                                     @RequestParam("mobileCaptcha") String mobileCaptcha,
                                     HttpServletRequest request) {
        try {
            String key = "forget:captcha_mobile_" + mobile;
            try (Jedis jedis = redisService.jedis()) {
                String code = jedis.get(key);
                if (Strings.isBlank(code)) {//验证码失效
                    request.setAttribute("error", "短信验证码失效！");
                    return "pages/front/pc/member/resetPassword2";
                } else if (!Strings.sNull(mobileCaptcha).equalsIgnoreCase(code)) {//验证码不对
                    request.setAttribute("error", "短信验证码不对！");
                    return "pages/front/pc/member/resetPassword2";
                }
            }
            Account_user account_user= accountUserService.fetch(Cnd.where("mobile","=",mobile).and("disabled","=",0).and("delFlag","=",0));
            request.setAttribute("account_user",account_user);
            return "pages/front/pc/member/resetPassword3";
        }catch (Exception e){
            request.setAttribute("error","短信验证码错误！");
            return "pages/front/pc/member/resetPassword2";
        }

    }

    /**
     * 校验用户名是否已存在
     * @return
     */
    @RequestMapping("/checkLoginName")
    @SJson
    public Object checkLoginName(@RequestParam("loginname") String loginname,
                                 HttpServletRequest request,HttpSession session) {
        try{
            //判断用户名是否存在
            boolean loginnameExist = memberUserService.checkUnique("loginname",loginname);
            //判断手机号是否存在
            boolean mobilebExist = memberUserService.checkUnique("mobile",loginname);
            //判断邮箱是否存在
            boolean emailExist = memberUserService.checkUnique("email",loginname);
            Account_user account_user = null;
            if(loginnameExist||mobilebExist||emailExist){//用户名存在
                return Result.success();
            }
            //不存在
            return Result.error();
        }catch (Exception e){
            return Result.error();
        }

    }

    /**
     * 校验用户名是否已存在
     * @return
     */
    @RequestMapping("/checkLoginname")
    public String checkLoginname(@RequestParam("verifyCode") String verifyCode,
                                 @RequestParam("loginname") String loginname,
                                 HttpServletRequest request) {
        try{
            String code = (String) request.getSession().getAttribute("memberFogetPasswordCaptcha");
            if (!Strings.sNull(code).equalsIgnoreCase(Strings.sNull(verifyCode))) {
                request.setAttribute("error","验证码错误！");
                return "pages/front/pc/member/resetPassword1";
            }
            //判断用户名是否存在
            boolean loginnameExist = memberUserService.checkUnique("loginname",loginname);
            //判断手机号是否存在
            boolean mobilebExist = memberUserService.checkUnique("mobile",loginname);
            //判断邮箱是否存在
            boolean emailExist = memberUserService.checkUnique("email",loginname);
            Account_user account_user = null;
            if(loginnameExist){//用户名存在
                account_user= accountUserService.fetch(Cnd.where("loginname","=",loginname).and("disabled","=",0).and("delFlag","=",0));
                request.setAttribute("mobile",account_user.getMobile());
                request.setAttribute("email",account_user.getEmail());
                return "pages/front/pc/member/resetPassword2";
            }
            if(mobilebExist){//手机号存在
                account_user= accountUserService.fetch(Cnd.where("mobile","=",loginname).and("disabled","=",0).and("delFlag","=",0));
                request.setAttribute("mobile",account_user.getMobile());
                request.setAttribute("email",account_user.getEmail());
                return "pages/front/pc/member/resetPassword2";
            }
            if(emailExist){//邮箱存在
                account_user= accountUserService.fetch(Cnd.where("email","=",loginname).and("disabled","=",0).and("delFlag","=",0));
                request.setAttribute("mobile",account_user.getMobile());
                request.setAttribute("email",account_user.getEmail());
                return "pages/front/pc/member/resetPassword2";
            }
            //不存在
            request.setAttribute("error","用户名不存在！");
            return "pages/front/pc/member/resetPassword1";
        }catch (Exception e){
            request.setAttribute("error","用户名不存在！");
            return "pages/front/pc/member/resetPassword1";
        }

    }


    /**
     * 用户修改密码
     */
    @RequestMapping("/updatePassword/{loginname}")
    public String updatePassword(@PathVariable("loginname") String loginname,
                                 @RequestParam("passwordStrength") String passwordStrength,
                                 @RequestParam("password") String password,
                                 HttpServletRequest request) {
        try{
            Account_user account_user= accountUserService.fetch(Cnd.where("loginname","=",loginname).and("disabled","=",0).and("delFlag","=",0));
            String hashedPasswordBase64 = new Sha256Hash(password, account_user.getSalt(), 1024).toBase64();
            account_user.setPassword(hashedPasswordBase64);
            account_user.setPasswordStrength(Integer.parseInt(passwordStrength));
            accountUserService.updateIgnoreNull(account_user);
            return "pages/front/pc/member/resetPassword4";
        }catch (Exception e){
            e.printStackTrace();
            request.setAttribute("error","修改失败");
            return "pages/front/pc/member/resetPassword3";
        }
    }

    /**
     * 发送邮件
     */
    @RequestMapping("/sendEmail")
    @SJson
    public Object sendEmail(@RequestParam("email") String email) {
        try{
            Account_user account_user = accountUserService.fetch(Cnd.where("email","=",email).and("disabled","=",0).and("delFlag","=",0));
            if(!Lang.isEmpty(account_user)){

                //生成校验码
                String checkCode = StringUtil.getRndString(20);

                //生成链接
                StringBuilder url = new StringBuilder(Globals.APP_PROTOCOL).append(Globals.APP_DOMAIN);
                url.append("/member/fogetPassword/email");
                url.append("/").append(account_user.getLoginname());
                url.append("?code=").append(checkCode);
                NutMap param = new NutMap();
                param.put("url",String.format("<a href='%s'>点击跳转,修改密码</a>", url));

                //发邮件
                commMsgService.sendMsg("hyzhmm",null,null,null,"会员找回密码服务", Msg_send.SendTypeEnum.EMAIL.getName(),null,account_user.getEmail(),param,false);

                //设置失效时间，控制时效
                String key = "forget:captcha_email_" + account_user.getLoginname();
                try (Jedis jedis = redisService.jedis()) {
                    jedis.set(key, checkCode);
                    jedis.expire(key, 300);
                }

                return Result.success();
            }
            return Result.error();
        }catch (Exception e){
            log.error("发送重置密码邮件失败："+e.getMessage());
            return Result.error();
        }
    }

    /**
     * 跳转到找回密码页面
     * @return
     */
    @RequestMapping("/email/{loginName}")
    public String fogetPasswordEmail(@PathVariable("loginName")String loginName, @RequestParam("code") String checkCode, HttpServletRequest request) {

        String key = "forget:captcha_email_" + loginName;
        try (Jedis jedis = redisService.jedis()) {
            String code = jedis.get(key);
            if (Strings.isBlank(code)) {//验证码失效
                return "pages/front/pc/member/resetPassword1";
            } else if (code.equals(checkCode)) {//校验成功
                Account_user account_user= accountUserService.fetch(Cnd.where("loginname","=",loginName).and("disabled","=",0).and("delFlag","=",0));
                request.setAttribute("account_user",account_user);
            } else {//校验码不对
                return "pages/front/pc/member/resetPassword1";
            }
        }
        return "pages/front/pc/member/resetPassword3";
    }
}