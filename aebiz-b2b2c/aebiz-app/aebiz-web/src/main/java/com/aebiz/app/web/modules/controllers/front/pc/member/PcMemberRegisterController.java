package com.aebiz.app.web.modules.controllers.front.pc.member;

import cn.apiclub.captcha.Captcha;
import com.aebiz.app.member.modules.services.MemberRegisterService;
import com.aebiz.app.member.modules.services.MemberUserService;
import com.aebiz.app.msg.modules.models.Msg_send;
import com.aebiz.app.msg.modules.services.CommMsgService;
import com.aebiz.app.shop.modules.models.Shop_config;
import com.aebiz.app.shop.modules.services.ShopConfigService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.redis.RedisService;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Cnd;
import org.nutz.img.Images;
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
 * 会员注册
 */
@Controller
@RequestMapping("/member/register")
public class PcMemberRegisterController {

    private static final Log log = Logs.get();

    @Autowired
    private MemberUserService memberUserService;

    @Autowired
    private MemberRegisterService memberRegisterService;

    @Autowired
    private ShopConfigService shopConfigService;

    @Autowired
    private CommMsgService commMsgService;

    @Autowired
    private RedisService redisService;

    /**
     * 跳转到注册页面
     * @return
     */
    @RequestMapping("")
    public String register(HttpServletRequest request) {
        Shop_config shopConfig =  shopConfigService.fetch(Cnd.where("id","=","system"));
        request.setAttribute("shopConfig",shopConfig);
        return "pages/front/pc/member/register";
    }

    /**
     *  图片验证码
     */
    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                        @RequestParam(value = "w", required = false, defaultValue = "0") int w,
                        @RequestParam(value = "h", required = false, defaultValue = "0") int h) throws Exception {
        if (w * h < 1) {  w = 200;h = 54;}
        Captcha captcha = new Captcha.Builder(w, h).addText().build();
        String text = captcha.getAnswer();
        session.setAttribute("memberRegisterCaptcha", text);
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        Images.write(captcha.getImage(), "png", os);
    }

    /**
     *  手机验证码
     */
    @SJson
    @RequestMapping("/getMobileCaptcha/{mobile}")
    public Object mobileCaptcha(@PathVariable("mobile") String mobile,
                        HttpServletRequest request, HttpSession session) throws Exception {
        try {
            String code = StringUtil.getRndNumber(6);

            NutMap param = new NutMap();
            param.put("code", code);
            param.put("product","茗流荟");

            String key = "memberRegisterMobileCaptcha_" + StringUtil.getMemberUid();
            try (Jedis jedis = redisService.jedis()) {
                jedis.set(key, code);
                jedis.expire(key, 600);
            }

            commMsgService.sendMsg("pcsjyzdx",null,null,null,null, Msg_send.SendTypeEnum.SMS.getName(),null,mobile,param,false);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("globals.result.error");
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
                return Result.error("member.register.checkmobile.fail");
            }
            //不存在
            return Result.success("member.register.checkmobile.success");
        }catch (Exception e){
            return Result.error("member.register.checkmobile.fail");
        }
    }

    /**
     * 异步校验用户名是否已存在
     * @return
     */
    @RequestMapping("/checkLoginname/{loginname}")
    @SJson
    public Object checkLoginname(@PathVariable("loginname") String loginname) {
        try {
            //判断是否用户名是否已被注册
            boolean exists = memberUserService.checkUnique("loginname",loginname);
            if(exists){//存在
                return Result.error("member.register.checkmobile.fail");
            }
            //不存在
            return Result.success("member.register.checkmobile.success");
        }catch (Exception e){
            return Result.error("member.register.checkmobile.fail");
        }
    }

    /**
     * 用户注册
     */
    @RequestMapping("/doRegister")
    @SJson
    public Object doRegister(@RequestParam("mobile") String mobile,
                             @RequestParam("verifyCode") String verifyCode,
                             @RequestParam("mobileVerifyCode") String mobileVerifyCode,
                             @RequestParam("loginname") String loginname,
                             @RequestParam("password") String password,
                             @RequestParam("mobileArea") String mobileArea,
                             @RequestParam("passwordStrength") String passwordStrength,
                             HttpServletRequest request,HttpSession session) {
        try {
            String verifycode = Strings.sNull(session.getAttribute("memberRegisterCaptcha"));
            if (!verifycode.equalsIgnoreCase(Strings.sNull(verifyCode))) {
                return Result.error("member.register.join.verifycode");
            }
//            String mobilecode = Strings.sNull(session.getAttribute("memberRegisterMobileCaptcha"));
//            if (!mobilecode.equalsIgnoreCase(Strings.sNull(mobileVerifyCode))) {
//                return Result.error("member.register.join.mobilecode");
//            }
            //验证码校验
            String key = "memberRegisterMobileCaptcha_" + StringUtil.getMemberUid();
            try (Jedis jedis = redisService.jedis()) {
                String code = jedis.get(key);
                if (Strings.isBlank(code)) {//验证码失效
                    return Result.error("验证码失效");
                } else if (!Strings.sNull(mobileVerifyCode).equalsIgnoreCase(code)) {//验证码不对
                    return Result.error("验证码错误");
                }
            }
            //注册
            memberRegisterService.memberRegister(mobile,password,loginname,passwordStrength);
            return Result.success("member.register.join.success");
        }catch (Exception e){
            return Result.error("member.register.join.fail");
        }
    }
}