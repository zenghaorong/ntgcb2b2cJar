package com.aebiz.app.dec.commons.comps.top;

import com.aebiz.app.dec.commons.comps.top.vo.TopCompModel;
import com.aebiz.app.dec.commons.service.PlatInfoForCompService;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo.PlatInfoDTO;
import com.aebiz.app.web.modules.controllers.platform.dec.BaseCompController;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * TopBanner组件
 * @author hyl
 * 2016年12月14日
 */
@Controller
@RequestMapping("/topComp")
public class TopCompController extends BaseCompController {

	@Autowired
	public PlatInfoForCompService platInfoForCompService;
	
	public String executeCompViewHtml(String pageViewHtml, WebPageModel wpm, BaseCompModel bcm){
		Document doc = Jsoup.parse(pageViewHtml);
        super.delCompLoading(doc, bcm);		
		TopCompModel tcm = (TopCompModel)bcm;
		String compId = tcm.getCompId();
		//获取接口数据
		Map<String,Object> resultMap = getPlatInfo();
		String platName = (String)resultMap.get("platName");
	    //初始化页面平台名称
	    initPlatName(doc,compId,platName);
	    //初始化页面登录信息
	    String nickName = (String)resultMap.get("nickName");
	    initPageView(doc,compId,nickName);
	    //获取参数配置信息
	    tcm.setNickName(nickName);
	    String contextPath = (String)wpm.getMapPageParams().get(DecorateCommonConstant.COMPONENT_REQUEST_CONTEXTPATH);
	    initConfigInfo(doc,compId,tcm,contextPath);
		return doc.html();
	}
	
	/**
	 * 替换js中的变量
	 */
	public String genJs(String designJs,WebPageModel wpm,BaseCompModel bcm){
		designJs = designJs.replaceAll("\\$_compId",bcm.getCompId());
		String js = designJs.replaceAll("\\$_compConfig_\\$", JSON.toJSONString(bcm));
		return js;
	}
	
	/**
	 * 初始化参数配置页面
	 */
	@RequestMapping(value="toParamsDesign")
	public String toParamsDesign(HttpServletResponse response, HttpServletRequest request){
		String toUrl = (String) request.getAttribute("toParamsJspURL");
		return toUrl;
	}
	
	/**
	 * ajax调用初始化数据
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="ajaxLoadData",method={RequestMethod.GET})
	@ResponseBody
	public void ajaxLoadData(HttpServletRequest request, HttpServletResponse response){
		//获取接口数据
		Map<String,Object> resultMap = getPlatInfo();
	
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		try {
			response.getWriter().print(JSON.toJSONString(resultMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化平台名称
	 * @param doc
	 * @param compId
	 * @param platName
	 * void
	 */
	private void initPlatName(Document doc, String compId, String platName){
		doc.select("#"+compId+"_em_platName").html(platName);
	}
	/**
	 * 初始化参数设置html
	 * @param doc
	 * @param compId
	 * void
	 */
	private void initConfigInfo(Document doc, String compId, TopCompModel tcm, String contextPath){
		String nickName = tcm.getNickName();
		if(nickName != null && !"".equals(nickName)){
			doc.select("#"+compId+"_a_nickname").attr("href",contextPath + tcm.getTxt_customerIndexUrl());
			doc.select("#"+compId+"_a_myorder").attr("href", contextPath + tcm.getTxt_myorderUrl()).html(tcm.getLbl_myorderName());
		    doc.select("#"+compId+"_a_logout").html(tcm.getLbl_logoutName());
		}else{
			doc.select("#"+compId+"_a_login").attr("href", contextPath + tcm.getTxt_loginUrl()).html(tcm.getLbl_loginName());
			doc.select("#"+compId+"_a_register").attr("href", contextPath + tcm.getTxt_regUrl()).html(tcm.getLbl_regName());
		}
		doc.select("#"+compId+"_a_index").attr("href", contextPath + tcm.getTxt_indexUrl()).html(tcm.getLbl_indexName());
		doc.select("#"+compId+"_a_favproduct").attr("href", contextPath + tcm.getTxt_favProductUrl()).html(tcm.getLbl_favProductName());
		doc.select("#"+compId+"_a_favstore").attr("href", contextPath + tcm.getTxt_favStoreUrl()).html(tcm.getLbl_favStoreName());
		doc.select("#"+compId+"_a_store").attr("href", contextPath + tcm.getTxt_storeUrl()).html(tcm.getLbl_storeName());
		doc.select("#"+compId+"_a_toapply").attr("href", contextPath + tcm.getTxt_toApplyUrl()).html(tcm.getLbl_toApplyName());
		doc.select("#"+compId+"_a_helpcenter").attr("href", contextPath + tcm.getTxt_helpCenterUrl()).html(tcm.getLbl_helpCenterName());
	}
	/**
	 * 初始化登录信息
	 * @param doc
	 * @param compId
	 * @param nickName
	 * void
	 */
	private void initPageView(Document doc, String compId, String nickName){
		if(nickName != null && !"".equals(nickName)){
	    	doc.select("#"+compId+"_a_nickname").html(nickName);
	    	doc.select("#"+compId+"_a_login").attr("style", "display:none");
	    	doc.select("#"+compId+"_a_register").attr("style", "display:none");
	    }else{
	    	doc.select("#"+compId+"_a_nickname").attr("style", "display:none");
	    	doc.select("#"+compId+"_a_logout").attr("style", "display:none");
	    	doc.select("#"+compId+"_a_myorder").attr("style", "display:none");
	    }
	}
	
	/**
	 * 调用接口返回平台名称、用户昵称
	 * @return
	 */
	private Map<String,Object> getPlatInfo(){
		PlatInfoDTO platInfo = null;
		try {
			platInfo = platInfoForCompService.getPlatInfo();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		String platName = "";
	    String nickName = "";
		if(platInfo != null){
			//获取平台名称
		    platName = platInfo.getPlatName();
		    //获取登录信息
		    nickName = platInfo.getLoginName();
		}
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("platName", platName);
		resultMap.put("nickName", nickName);
		return resultMap;
	}
	
	/**
	 * 退出
	 * @return
	 * String
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ResponseBody
	public String logout() {
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		
		return JSON.toJSONString("true");
	}
	
}
