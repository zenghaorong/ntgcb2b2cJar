package com.aebiz.app.web.commons.interceptor;

import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * debug模式下打印URL请求响应时间
 * Created by wizzer on 2016/12/27.
 */
public class LogTimeInterceptor extends HandlerInterceptorAdapter {
    private Log log = Logs.get();

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler)
            throws Exception {
        if (log.isDebugEnabled()) {
            request.setAttribute("__MVC_EXE_TIME", System.currentTimeMillis());
        }
        return true;
    }

    public void postHandle(
            HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView)
            throws Exception {

    }

    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        if (log.isDebugEnabled()) {
            long startTime = (Long) request.getAttribute("__MVC_EXE_TIME");
            long executeTime = System.currentTimeMillis() - startTime;
            log.debugf("[%-4s]URI=%s %sms", request.getMethod(), request.getRequestURI(), executeTime);
        }
    }
}
