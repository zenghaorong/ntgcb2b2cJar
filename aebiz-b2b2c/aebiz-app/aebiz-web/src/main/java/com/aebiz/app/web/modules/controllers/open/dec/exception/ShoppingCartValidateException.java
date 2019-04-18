package com.aebiz.app.web.modules.controllers.open.dec.exception;

/**
 * 购物车校验异常
 *
 * Created by Aebiz_yjq on 2017/1/21.
 */
public class ShoppingCartValidateException extends ShoppingCartException{

    public ShoppingCartValidateException(String message){
        super(message);
    }

    public ShoppingCartValidateException(String message, Throwable cause){
        super(message, cause);
    }

}
