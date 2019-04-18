package com.aebiz.app.web.commons.log.aop;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.app.web.commons.log.SLogService;
import com.aebiz.commons.utils.DateUtil;
import com.aebiz.commons.utils.SpringUtil;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.sys.modules.models.Sys_log;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.nutz.lang.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
@Aspect
@Component
public class SLogAop {
    @Autowired
    private SLogService sLogService;
    private Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd hh:mm:ss")
            .create();

    @Pointcut("@annotation(com.aebiz.app.web.commons.log.annotation.SLog)")
    public void cutSLog() {

    }

    public SLog getSLogCut(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        SLog target = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    target = method.getAnnotation(SLog.class);
                    break;
                }
            }
        }
        return target;
    }

    @Around("cutSLog()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {
        SLog log = getSLogCut(point);
        Sys_log sysLog = new Sys_log();
        sysLog.setUsername(StringUtil.getUsername());
        sysLog.setActionTime(DateUtil.getDateTime());
        sysLog.setType(log.type());
        sysLog.setDescription(log.description());
        sysLog.setIp(Lang.getIP(SpringUtil.getRequest()));
        sysLog.setAction(point.getSignature().getName());
        sysLog.setModule(point.getTarget().getClass().getName());
        sysLog.setMethodMeta(point.getSignature().toLongString());
        sysLog.setParameters(getParameter(point));
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        Object obj = null;
        try {
            obj = point.proceed();
        } catch (Exception e) {
            stopwatch.stop();
            sysLog.setOperationTime(stopwatch.getTotalTimeMillis());
            sysLog.setMethodReturn(e.getMessage());
            sysLog.setSuccess(false);
            sLogService.async(sysLog);
            throw e;
        }
        stopwatch.stop();
        sysLog.setOperationTime(stopwatch.getTotalTimeMillis());
        sysLog.setOpAt((int) (System.currentTimeMillis() / 1000));
        sysLog.setOpBy(StringUtil.getUid());
        sysLog.setDelFlag(false);
        if (log.methodReturn()) {
            Object rObj = getMethodReturnObject(point, obj);
            sysLog.setMethodReturn(gson.toJson(rObj));
        }
        sLogService.async(sysLog);
        return obj;
    }

    /**
     * @param point
     * @return
     */
    private Object getMethodReturnObject(ProceedingJoinPoint point, Object obj) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 如果是 ajax 请求,返回方法的返回值
        if (signature.getMethod().getAnnotation(ResponseBody.class) != null) {
            return obj;
        }
        // 如果不是ajax 获取 Model 中的属性
        for (Object o : point.getArgs()) {
            if (o instanceof Model) {
                Model m = (Model) o;
                return m.asMap();
            }
        }
        return obj;// 其他情况
    }

    /**
     * @param point
     * @return
     */
    private String getParameter(ProceedingJoinPoint point) {
        List<Object> target = Lists.newArrayList();

        for (Object obj : point.getArgs()) {
            if (obj instanceof ServletRequest) {
                target.add(((ServletRequest) obj).getParameterMap());
            } else if (obj instanceof ServletResponse || obj instanceof HttpSession || obj instanceof Model) { // response/session/model

            } else {
                target.add(obj);
            }
        }
        return gson.toJson(target);
    }
}
