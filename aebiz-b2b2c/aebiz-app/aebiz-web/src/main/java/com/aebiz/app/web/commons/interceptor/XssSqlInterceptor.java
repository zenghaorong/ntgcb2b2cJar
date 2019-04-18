package com.aebiz.app.web.commons.interceptor;

import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.baseframework.base.Result;
import com.aebiz.commons.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.nutz.lang.Encoding;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

/**
 * Created by wizzer on 2016/12/21.
 */
public class XssSqlInterceptor extends HandlerInterceptorAdapter {
    private Log log = Logs.get();
    private static String[] keys = new String[]{"script", "iframe"};

    //Action之前执行:
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        try {
            Iterator<String[]> values = request.getParameterMap().values().iterator();
            if (isXss(request, values)) {
                if (StringUtil.isAjax(request)) {
                    response.reset();
                    response.setCharacterEncoding(Encoding.UTF8);
                    response.setContentType("application/json");
                    response.getWriter().write(Result.error("XSS").toString());
                    response.getWriter().flush();
                    response.getWriter().close();
                } else {
                    response.sendRedirect(Globals.APP_BASE);
                }
                return false;
            }
        } catch (Exception e) {
            log.error(e);
        }
        return true;
    }

    boolean isXss(HttpServletRequest request, Iterator<String[]> values) {
        while (values.hasNext()) {
            String[] valueArray = (String[]) values.next();
            for (int i = 0; i < valueArray.length; i++) {
                String value = valueArray[i].toLowerCase();
                for (int j = 0; j < keys.length; j++) {
                    // 判断如果路径参数值中含有关键字则返回true,并且结束循环
                    if (value.contains("<" + keys[j] + ">")
                            || value.contains("<" + keys[j])
                            || value.contains(keys[j] + ">")) {
                        log.debugf("[%-4s]URI=%s %s", request.getMethod(), request.getRequestURI(), "XSS关键字过滤:" + value);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
