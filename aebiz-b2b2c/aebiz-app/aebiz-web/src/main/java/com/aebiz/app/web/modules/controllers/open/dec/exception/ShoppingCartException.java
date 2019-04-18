package com.aebiz.app.web.modules.controllers.open.dec.exception;

/**
 * 购物车相关业务异常
 *
 * Created by Aebiz_yjq on 2017/1/21.
 */
public class ShoppingCartException extends RuntimeException{
    public ShoppingCartException(String message){
        super(message);
    }

    public ShoppingCartException(String message, Throwable cause){
        super(message, cause);
    }
}
