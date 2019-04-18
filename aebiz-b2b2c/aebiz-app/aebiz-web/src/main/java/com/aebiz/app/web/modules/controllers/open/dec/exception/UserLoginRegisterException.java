package com.aebiz.app.web.modules.controllers.open.dec.exception;

/**
 * 用户登录注册异常类
 *
 * Created by Aebiz_yjq on 2017/1/21.
 */
public class UserLoginRegisterException extends RuntimeException {
    public UserLoginRegisterException(String message){
        super(message);
    }

    public UserLoginRegisterException (String message, Throwable cause){
        super(message, cause);
    }
}
