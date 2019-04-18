package com.aebiz.app.dec.commons.comps.sideenterance;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_login;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.acc.modules.services.AccountLoginService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.dec.commons.comps.sideenterance.vo.SideEnteranceCompModel;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.goods.modules.models.Goods_favorite;
import com.aebiz.app.goods.modules.services.GoodsFavoriteService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.member.modules.models.Member_user;
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
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import java.security.interfaces.RSAPrivateKey;
import java.util.*;

/**
 * 侧边快速入口
 * @author hyl
 * 2016年12月19日
 */
@Controller
@RequestMapping("/sideEnteranceComp")
public class SideEnteranceCompController  extends BaseCompController {
	private static final Log log = Logs.get();
	// 会员登录失败次数
	private static final String CUSTOMERLOGINERRORTIME = "CUSTOMERLOGINERRORTIME";

	private String cookieName = "memberRemeberMe";

	@Autowired
	private AccountLoginService accountLoginService;
	@Autowired
	private AccountUserService accountUserService;
	@Autowired
	private AccountInfoService accountInfoService;

	@Autowired
	private GoodsFavoriteService goodsFavoriteService;

	@Autowired
	private GoodsProductService goodsProductService;
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm){
		Document doc =  Jsoup.parse(pageViewHtml);
		super.delCompLoading(doc, bcm);
		SideEnteranceCompModel scm = (SideEnteranceCompModel)bcm;
		String compId = scm.getCompId();
		Cookie[] cookies = null;
		//获取Cookie
		if(wpm.getMapPageParams().get("cookies") != null){
			cookies = (Cookie[])wpm.getMapPageParams().get("cookies");
		}
		Map<String,Object> map = getDataFromCookie(cookies);
		//初始化登录信息
		initLoginInfo(doc,map,compId);
		String contextPath = (String)wpm.getMapPageParams().get(DecorateCommonConstant.COMPONENT_REQUEST_CONTEXTPATH);
		//初始化页面数据
		initPageViewHtml(doc,scm,contextPath);
		
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
				doc.select("#loginName1").val(map.get("loginName")+"");
			}
			
			if(map.get("pwd") != null){
				doc.select("#pwd1").val(map.get("pwd")+"");
			}
			
			if(map.get("hasVilidateCode") != null){
				doc.select("#hasVilidateCode1").val(map.get("hasVilidateCode")+"");
			}
			
			if("on".equals(map.get("remember")+"")){
				doc.select("#remember").attr("checked", "checked");
			}else{
				doc.select("#remember").removeAttr("checked");
			}
		}
	}
	
	/**
	 * 异步初始化页面
	 * @return
	 */
	@RequestMapping(value="/ajaxLoadData", produces = "application/json; charset=utf-8")
    @ResponseBody
	public String ajaxLoadData(HttpServletRequest request){
		Map<String,Object> map = getDataFromCookie(request.getCookies());
		
		String jsonString = JSON.toJSONString(map);
		return jsonString;
	}
	/**
	 * 跳到属性编辑页面
	 * String toUrl,WebPageModel wpm,BaseCompModel bcm
	 * @return
	 */
	@RequestMapping(value="toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request){
		String toUrl = (String) request.getAttribute("toParamsJspURL");
		return toUrl;
	}
	
	/**
	 * 替换js
	 */
	public String genJs(String designJs,WebPageModel wpm,BaseCompModel bcm){
		SideEnteranceCompModel scm = (SideEnteranceCompModel)bcm;
		//调用接口返回设置的QQ
		List<Map<String,Object>> qqList =  getQQInfo();
		StringBuffer sb = new StringBuffer();
		for(Map<String,Object> map : qqList){
			sb.append("<p>").append(map.get("qqName")+":");
			sb.append("<a target='_blank' href='http://wpa.qq.com/msgrd?v=3&uin="+map.get("qqNumber")+"&site=qq&menu=yes' >").append(map.get("qqNumber")).append("</a>");
			sb.append("</p>");
		}
		scm.setQqListStr(sb.toString());
		designJs = designJs.replaceAll("\\$_compId",scm.getCompId());
		String js = designJs.replaceAll("\\$_compConfig_\\$", JSON.toJSONString(scm));
		
		return js;
	}
	
	/**
	 * 初始化页面数据
	 * @param doc
	 */
	private void initPageViewHtml(Document doc, SideEnteranceCompModel scm, String contextPath){
		String compId = scm.getCompId();
		if("0".equals(scm.getLoginIsShow())){
			doc.select("#"+compId+"_loginIsShow").val("0");
		}
		if("0".equals(scm.getCartIsShow())){
			doc.select("#"+compId+"_cartIsShow").val("0");
		}
		if("0".equals(scm.getCollectionIsShow())){
			doc.select("#"+compId+"_collectionIsShow").val("0");
		}
		if("0".equals(scm.getQuesNaireIsShow())){
			doc.select("#"+compId+"_quesNaireIsShow").val("0");
		}else{
			doc.select("#"+compId+"_pr_quesnaire").attr("href", contextPath + "/" +scm.getQuesNaireUrl());
		}
		if("0".equals(scm.getQqIsShow())){
			doc.select("#"+compId+"_qqIsShow").val("0");
		}
		if("0".equals(scm.getOnlineIsShow())){
			doc.select("#"+compId+"_onlineIsShow").val("0");
		}
		
		doc.select("#"+compId+"_a_reg").attr("href", contextPath + "/" + scm.getA_forgetPwdUrl()).html(scm.getA_forgetPwdName());
		doc.select("#"+compId+"_a_forgetpwd").attr("href", contextPath + "/" + scm.getA_regUrl()).html(scm.getA_regName());
	}
	
	/**
	 * 加载会员收藏的商品	
	 * @return
	 */
	@RequestMapping(value="/platRightFavoriteProduct")
	public String platRightFavoriteProduct(HttpServletRequest req){
		String accountId = StringUtil.getMemberUid();
		if(!Strings.isEmpty(accountId)){

			List<Goods_favorite> favorites=goodsFavoriteService.query(Cnd.where("accountId","=",accountId));
			if(favorites !=null && favorites.size()>0 ){
				for(Goods_favorite favorite:favorites){
					String img=goodsProductService.getProductImage(favorite.getSku());
					favorite.setImg(img);
				}
			}
			// 将标签列表发送到页面，以供搜索
			//model.addAttribute("tagList", tagList);
			req.setAttribute("favList", favorites);
		}
		return "pages/front/pc/goods/productFavorite";
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
		if(Strings.isEmpty(StringUtil.getMemberUid())){
			returnMap.put("flag", false);
		}else{
			Account_info infoModel=accountInfoService.fetch(StringUtil.getMemberUid());
			if(infoModel !=null){
				returnMap.put("flag", true);
				returnMap.put("profilePic", infoModel.getImageUrl());
			}
		}
		return JSON.toJSONString(returnMap);
	}
	
	/**
	 * 右侧快速登录
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/quickRightLogin", method = RequestMethod.GET)
	@ResponseBody
	public Object quickRightLogin(@RequestParam("loginName") String loginName,
                                  @RequestParam("pwd") String pwd,
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
			subject.login(createToken(loginName, pwd, false, validateCode, request));
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
	/**
	 * 调用接口返回设置的QQ
	 * @return
	 * List<Map<String,Object>>
	 */
	private List<Map<String,Object>>  getQQInfo(){
		//CustomerServiceDTO  customerServiceDto = platInfoForCompService.getQQCustomerService();
		String qqNameStr = "";
		String qqNumberStr = "";
		/*if(customerServiceDto != null){
		    qqNameStr = customerServiceDto.getCsRoleStr();
		    qqNumberStr = customerServiceDto.getCsNumberStr();
		}*/
		//将多个qq和名称拆分成数组
		List<Map<String,Object>>  qqList = new ArrayList<>();
		if(qqNameStr != null && !"".equals(qqNameStr) && qqNumberStr != null && !"".equals(qqNumberStr)){
			String [] qqNames = qqNameStr.split(",");
			String [] qqNumbers = qqNumberStr.split(",");
			for(int i=0;i < qqNames.length;i++){
				Map<String,Object> map = new HashMap<>();
				map.put("qqName", qqNames[i]);
				map.put("qqNumber", qqNumbers[i]);
			    qqList.add(map);
			}
		}
	
	    return qqList;
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
			String loginname= Base64.decodeToString(base64);
			HttpSession s=request.getSession();
			resultMap.put("loginName", "");
			resultMap.put("pwd", "");
			resultMap.put("remember", "");
			resultMap.put("hasVilidateCode", "false");
		}else{
			resultMap.put("loginName", "");
			resultMap.put("pwd", "");
			resultMap.put("remember", "");
			resultMap.put("hasVilidateCode", "false");
		}
		return resultMap;
	}
}
