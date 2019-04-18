package com.aebiz.app.web.commons.interceptor;

import com.aebiz.app.sys.modules.services.SysApiService;
import com.aebiz.app.sys.modules.services.impl.SysApiServiceImpl;
import com.aebiz.baseframework.base.Result;
import com.aebiz.commons.utils.SpringUtil;
import org.nutz.lang.Encoding;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wizzer on 2017/4/4.
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {
    private static final Log log = Logs.get();

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler)
            throws Exception {
        SysApiService apiService = SpringUtil.getBean(SysApiServiceImpl.class);
        String appId = Strings.sNull(request.getParameter("appid"));
        String token = Strings.sNull(request.getParameter("token"));
        if (!apiService.verifyToken(appId, token)) {
            response.reset();
            response.setCharacterEncoding(Encoding.UTF8);
            response.setContentType("application/json");
            response.getWriter().write(Result.error("token invalid").toString());
            response.getWriter().flush();
            response.getWriter().close();
            return false;
        }
        return true;
    }
}
