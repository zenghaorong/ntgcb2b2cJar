package com.aebiz.app.web.commons.shiro.filter;


import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wizzer on 2017/1/10.
 */
public class StoreAuthenticationFilter extends FormAuthenticationFilter {

    protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
        return false;
    }

    protected boolean isLoginSubmission(ServletRequest request, ServletResponse response) {
        return false;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        ((HttpServletResponse) response).sendError(403);
        return false;
    }

    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (pathsMatch(getLoginUrl(), request))
            return true;
        return super.isAccessAllowed(request, response, mappedValue);
    }
}