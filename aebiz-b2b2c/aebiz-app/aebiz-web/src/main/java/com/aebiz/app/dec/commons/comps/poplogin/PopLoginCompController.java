package com.aebiz.app.dec.commons.comps.poplogin;


import com.aebiz.app.acc.modules.models.Account_login;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountLoginService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.dec.commons.comps.poplogin.vo.PopLoginCompModel;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.member.modules.models.Member_account;
import com.aebiz.app.member.modules.models.Member_cart;
import com.aebiz.app.member.modules.models.Member_user;
import com.aebiz.app.member.modules.services.MemberUserService;
import com.aebiz.app.order.commons.constant.OrderConsts;
import com.aebiz.app.web.commons.shiro.token.MemberCaptchaToken;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.shiro.exception.CaptchaEmptyException;
import com.aebiz.baseframework.shiro.exception.CaptchaIncorrectException;
import com.aebiz.commons.utils.CookieUtil;
import com.aebiz.commons.utils.RSAUtil;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.commons.utils.UserAgentUtils;
import com.alibaba.fastjson.JSON;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.http.Http;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.beetl.core.statement.GrammarToken.createToken;

/**
 * 弹出登录框组件
 * @author hyl
 * 2017年1月5日
 */
@Controller
@RequestMapping("/popLoginComp")
public class PopLoginCompController extends BaseCompController {

	// 会员登录失败次数
	private static final Log log = Logs.get();
	private static final String cookieName = "memberRemeberMe";

	@Autowired
	AccountUserService accountUserService;

	@Autowired
	AccountLoginService accountLoginService;
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm){
		Document doc = Jsoup.parse(pageViewHtml);
		super.delCompLoading(doc, bcm);
		PopLoginCompModel pcm = (PopLoginCompModel)bcm;
		String compId = pcm.getCompId();
		
		Cookie[] cookies = null;
		//获取Cookie
		if(wpm.getMapPageParams().get("cookies") != null){
			cookies = (Cookie[])wpm.getMapPageParams().get("cookies");
		}
		Map<String,Object> map = getDataFromCookie(cookies);
		String contextPath = (String)wpm.getMapPageParams().get(DecorateCommonConstant.COMPONENT_REQUEST_CONTEXTPATH);
		//初始化登录信息
		initLoginInfo(doc,map,compId);
		
		doc.select("#"+compId+"_a_reg").attr("href", contextPath + "/" +pcm.getA_forgetPwdUrl()).html(pcm.getA_forgetPwdName());
		doc.select("#"+compId+"_a_forgetpwd").attr("href", contextPath + "/"+pcm.getA_regUrl()).html(pcm.getA_regName());
		return doc.html();
	}
	
	/**
	 * 初始化登录信息
	 * @param doc
	 * @param map
	 * @param compId
	 * void
	 */
	private void initLoginInfo(Document doc, Map<String,Object> map, String compId){
		if(map != null){
			if(map.get("loginName") != null){
				doc.select("#"+compId+"_loginName").val(map.get("loginName")+"");
			}
			if(map.get("pwd") != null){
				doc.select("#"+compId+"_pwd").val(map.get("pwd")+"");
			}
			if(map.get("hasVilidateCode") != null){
				doc.select("#"+compId+"_hasVilidateCode").val(map.get("hasVilidateCode")+"");
			}
			
			if("on".equals(map.get("remember")+"")){
				doc.select("#"+compId+"_remember").attr("checked", "checked");
			}else{
				doc.select("#"+compId+"_remember").removeAttr("checked");
			}
		}
	}
	
	/**
	 * 跳转到属性编辑页面
	 */
	@RequestMapping(value="toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request){
		String toUrl = (String) request.getAttribute("toParamsJspURL");
		return toUrl;
	}
	
	/**
	 * 替换js
	 */
	public String genJs(String designJs, WebPageModel wpm, BaseCompModel bcm) {
		designJs = designJs.replaceAll("\\$_compId",bcm.getCompId());
		String js = designJs.replaceAll("\\$_compConfig_\\$", JSON.toJSONString(bcm));
		return js;
	}
	
	/**
	 * 异步初始化页面数据
	 * @param request
	 * @return
	 * String
	 */
	@RequestMapping(value="/ajaxLoadData", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String ajaxLoadData(HttpServletRequest request){
		Map<String,Object> map = getDataFromCookie(request.getCookies());
		return JSON.toJSONString(map);
	}
	
	/**
	 * 从cookie中取数据
	 * @return
	 * Map<String,Object>
	 */
	private Map<String,Object> getDataFromCookie(Cookie[] cookies){
		Map<String,Object> resultMap = new HashMap<>();
		//获取request对象
		HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		//获取response对象
		HttpServletResponse response =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		SimpleCookie cookie = new SimpleCookie(cookieName);
		String base64 = cookie.readValue(request, response);
		if (!Strings.isEmpty(base64)) {
			String loginname=Base64.decodeToString(base64);
			HttpSession s=request.getSession();
			resultMap.put("loginName", "loginname");
			resultMap.put("pwd", "");
			resultMap.put("remember", "");
			resultMap.put("hasVilidateCode", "false");
		}else{
			resultMap.put("loginName", "");
			resultMap.put("pwd", "");
			resultMap.put("remember", "");
			resultMap.put("hasVilidateCode", "false");
		}








		/*if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("customerLoginNameAndPassword")) {
					String customerLoginNameAndPassword = "";
					try {
						customerLoginNameAndPassword = URLDecoder.decode(cookie.getValue(), "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					// 获取用户名和密码
					if (!Strings.isEmpty(customerLoginNameAndPassword)) {
						String[] v = customerLoginNameAndPassword.split(",");
						resultMap.put("loginName", v[0]);
						resultMap.put("pwd", v[1]);
						resultMap.put("remember", "on");
					}
				}
				if (CUSTOMERLOGINERRORTIME.equals(cookie.getName())) {
					int cookiesErrorTime = 0;
					// 如果已经存在当前登录的用户的cookies
					String value = cookie.getValue();
					String[] v = value.split(",");
					// 第四个为错误登录次数
					if(v.length>=4){
						cookiesErrorTime = Integer.parseInt(v[3]);
					}

					// 如果失败次数大于3次,则再次登陆时需要输入验证码,如果小于3则还需要再去数据库里查看错误登陆次数
					if (cookiesErrorTime >= 3) {
						// 则再次登陆时需要输入验证码,
						resultMap.put("hasVilidateCode", "true");
					} else {
						// 如果数据的错误次数小于3,等有可能是恶意更改了cookies,这个时候要以数据库的为准
						if (!Strings.isEmpty(v[0])) {
							// 根据会员输入的用户名来查找会员
							Member_account customer = new Member_account(); //customerService
							Account_login login=new Account_login();
									//.getCustomerModelByLoginNameOrMobileOrEmail(v[0]);
							if (customer != null) {
								Member_user user=memberUserService.fetch(Cnd.where("accountId","=",customer.getAccountId()));
								if (dbWrongTime < 3) {
									resultMap.put("hasVilidateCode", "false");
								}
							}
						}
					}
				}
			}
		}else{
			resultMap.put("loginName", "");
			resultMap.put("pwd", "");
			resultMap.put("remember", "");
			resultMap.put("hasVilidateCode", "false");
		}*/
		return resultMap;
	}
	
	/**
	 * 判断是否登录
	 * @return
	 * String
	 */
	@RequestMapping("isLogin")
	@ResponseBody
	public String isLogin(){
		Map<String,Object> returnMap = new HashMap<>();
		String customerUuid = StringUtil.getMemberUid();
		if(!Strings.isEmpty(customerUuid)){
			returnMap.put("flag", true);
			returnMap.put("customerUuid", customerUuid);
		}else{
			returnMap.put("flag", false);
		}
		return JSON.toJSONString(returnMap);
	}
	
	@RequestMapping(value = "/quickLogin", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object login(@RequestParam("loginName") String username,
                        @RequestParam("pwd") String password,
                        @RequestParam("hasVilidateCode") String hasVilidateCode,
                        HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String validateCode = request.getParameter("validateCode");
		int errCount = 0;
		try {
			//输错三次显示验证码窗口
			String remember = request.getParameter("remember");
			boolean rememberMe=true;
			if("on".equals(remember)){
				rememberMe=true;
			}else{
				rememberMe=false;
			}
			//输错三次显示验证码窗口
			//errCount = NumberUtils.toInt(Strings.sNull(SecurityUtils.getSubject().getSession(true).getAttribute("memberErrCount")));
			Subject subject = SecurityUtils.getSubject();
			ThreadContext.bind(subject);
			subject.login(createToken(username, password, false, validateCode, request));
			Member_user user = (Member_user) subject.getPrincipal();
			Account_user accountUser = accountUserService.getAccount(user.getAccountId());
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
			accountLoginService.insert(accountLogin);
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
}
