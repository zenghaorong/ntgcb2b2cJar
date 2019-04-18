package com.aebiz.app.web.commons.interceptor;

import com.aebiz.baseframework.base.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.UsesJava7;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Created by wizzer on 2017/1/16.
 */
public class LocaleInterceptor extends HandlerInterceptorAdapter {

    /**
     * Default name of the locale specification parameter: "locale".
     */
    public static final String DEFAULT_PARAM_NAME = "locale";


    protected final Log logger = LogFactory.getLog(getClass());

    private String paramName = DEFAULT_PARAM_NAME;

    private String[] httpMethods;

    private boolean ignoreInvalidLocale = false;

    private boolean languageTagCompliant = false;


    /**
     * Set the name of the parameter that contains a locale specification
     * in a locale change request. Default is "locale".
     */
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    /**
     * Return the name of the parameter that contains a locale specification
     * in a locale change request.
     */
    public String getParamName() {
        return this.paramName;
    }

    /**
     * Configure the HTTP method(s) over which the locale can be changed.
     *
     * @param httpMethods the methods
     * @since 4.2
     */
    public void setHttpMethods(String... httpMethods) {
        this.httpMethods = httpMethods;
    }

    /**
     * Return the configured HTTP methods.
     *
     * @since 4.2
     */
    public String[] getHttpMethods() {
        return this.httpMethods;
    }

    /**
     * Set whether to ignore an invalid value for the locale parameter.
     *
     * @since 4.2.2
     */
    public void setIgnoreInvalidLocale(boolean ignoreInvalidLocale) {
        this.ignoreInvalidLocale = ignoreInvalidLocale;
    }

    /**
     * Return whether to ignore an invalid value for the locale parameter.
     *
     * @since 4.2.2
     */
    public boolean isIgnoreInvalidLocale() {
        return this.ignoreInvalidLocale;
    }

    /**
     * Specify whether to parse request parameter values as BCP 47 language tags
     * instead of Java's legacy locale specification format.
     * The default is {@code false}.
     * <p>Note: This mode requires JDK 7 or higher. Set this flag to {@code true}
     * for BCP 47 compliance on JDK 7+ only.
     *
     * @see Locale#forLanguageTag(String)
     * @see Locale#toLanguageTag()
     * @since 4.3
     */
    public void setLanguageTagCompliant(boolean languageTagCompliant) {
        this.languageTagCompliant = languageTagCompliant;
    }

    /**
     * Return whether to use BCP 47 language tags instead of Java's legacy
     * locale specification format.
     *
     * @since 4.3
     */
    public boolean isLanguageTagCompliant() {
        return this.languageTagCompliant;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ServletException {

        String newLocale = request.getParameter(getParamName());
        if (newLocale != null) {
            if (checkHttpMethod(request.getMethod())) {
                LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
                if (localeResolver == null) {
                    throw new IllegalStateException(
                            "No LocaleResolver found: not in a DispatcherServlet request?");
                }
                try {
                    Locale locale = parseLocaleValue(newLocale, request);
                    if (Message.isExist(locale)) {
                        localeResolver.setLocale(request, response, locale);
                    }
                } catch (IllegalArgumentException ex) {
                    //传参错误也没关系,不抛烦人的异常
                }
            }
        }
        Locale locale = RequestContextUtils.getLocale(request);
        request.setAttribute("lang", Message.getLanguage(locale));
        request.setAttribute("msg", Message.getMessage(locale));
        return true;
    }

    private boolean checkHttpMethod(String currentMethod) {
        String[] configuredMethods = getHttpMethods();
        if (ObjectUtils.isEmpty(configuredMethods)) {
            return true;
        }
        for (String configuredMethod : configuredMethods) {
            if (configuredMethod.equalsIgnoreCase(currentMethod)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Parse the given locale value as coming from a request parameter.
     * <p>The default implementation calls {@link StringUtils#parseLocaleString(String)}
     * or JDK 7's {@link Locale#forLanguageTag(String)}, depending on the
     * {@link #setLanguageTagCompliant "languageTagCompliant"} configuration property.
     *
     * @param locale the locale value to parse
     * @return the corresponding {@code Locale} instance
     * @since 4.3
     */
    @UsesJava7
    protected Locale parseLocaleValue(String locale, HttpServletRequest request) {
        return (isLanguageTagCompliant() ? Locale.forLanguageTag(locale) : StringUtils.parseLocaleString(locale));
    }

}
