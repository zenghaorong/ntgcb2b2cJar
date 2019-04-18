package com.aebiz.app.web.modules.controllers.open.dec.exception;

/**
 * 商品相关业务异常
 *
 * Created by Aebiz_yjq on 2017/1/21.
 */
public class ProductException extends RuntimeException{
    public ProductException(String message){
        super(message);
    }

    public ProductException(String message, Throwable cause){
        super(message, cause);
    }
}
