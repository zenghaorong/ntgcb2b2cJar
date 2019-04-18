package com.aebiz.app.web.modules.controllers.platform.dec;

import com.aebiz.app.dec.modules.services.DecTemplatesManagerService;
import com.aebiz.app.dec.modules.services.DecTemplatesPagesService;
import org.elasticsearch.common.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class FrontPageController {
	private static final Log log = Logs.get();
	@Autowired
	private DecTemplatesManagerService decTemplatesManagerService;

	@Autowired
	private DecTemplatesPagesService decTemplatesPagesService;

	/**
	 * 跳转至前台页面
	 * 
	 * @param pageName
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/front/{pageName}", method = RequestMethod.GET)
	public String toPage(@PathVariable("pageName") String pageName, Model model,
                         HttpServletResponse response, HttpServletRequest request) {
		// 获取当前正在使用的模板的uuid
		String templateUuid = decTemplatesManagerService.getIsUsingTemplate();
		pageName=pageName+".html";
		String pageUuid=decTemplatesPagesService.getPageId(templateUuid,pageName);
		try {
			if(!Strings.isEmpty(pageUuid)){
				request.getServletContext()
						.getRequestDispatcher("/platform/dec/templates/manager/run?templateUuid="+ templateUuid + "&pageUuid=" + pageUuid)
						.forward(request, response);
			}else{
				request.getServletContext()
						.getRequestDispatcher("/404")
						.forward(request, response);
			}

		} catch (Exception ex) {
			log.error(ex);
		}

		return null;
	}

	/**
	 * 跳转至前台页面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String toFronIndex( Model model,
						 HttpServletResponse response, HttpServletRequest request) {
		// 获取当前正在使用的模板的uuid
		String templateUuid = decTemplatesManagerService.getIsUsingTemplate();
		String pageUuid=decTemplatesPagesService.getPageId(templateUuid,"index.html");
		try {
			request.getServletContext()
					.getRequestDispatcher("/platform/dec/templates/manager/run?templateUuid="+ templateUuid + "&pageUuid=" + pageUuid)
					.forward(request, response);
		} catch (Exception ex) {
			log.error(ex);
		}

		return null;
	}

}