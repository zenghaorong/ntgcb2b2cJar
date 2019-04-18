package com.aebiz.app.web.commons.interceptor;

import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.commons.utils.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wizzer on 2017/1/16.
 */
public class GlobalsInterceptor  extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler)
            throws Exception {
        request.setAttribute("base", Globals.APP_BASE);
        request.setAttribute("app_name", Globals.APP_NAME);
        request.setAttribute("app_short_name", Globals.APP_SHORT_NAME);
        request.setAttribute("app_domain", Globals.APP_DOMAIN);
        request.setAttribute("shiro", SpringUtil.getBean(ShiroUtil.class));
        request.setAttribute("date", SpringUtil.getBean(DateUtil.class));
        request.setAttribute("string", SpringUtil.getBean(StringUtil.class));
        request.setAttribute("money", SpringUtil.getBean(MoneyUtil.class));
        response.setHeader("X-Powered-By","aebiz/"+ Globals.APP_VERSION+" "+ Globals.APP_COPYRIGHT+" ");
        return true;
    }
}