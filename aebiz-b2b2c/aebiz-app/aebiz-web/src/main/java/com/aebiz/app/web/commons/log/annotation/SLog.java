package com.aebiz.app.web.commons.log.annotation;

import com.aebiz.app.sys.modules.models.Sys_log;

import java.lang.annotation.*;

/**
 * 自定义的日志注解
 * Created by wizzer on 2016/12/22.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SLog {

    String description() default "";

    Sys_log.TypeEnum type() default Sys_log.TypeEnum.SYSTEM;

    boolean methodReturn() default false;
}
