package com.aebiz.app.dec.commons.service;

import com.aebiz.app.web.modules.controllers.open.dec.exception.ShoppingCartValidateException;
import org.nutz.lang.util.NutMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by ThinkPad on 2017/6/17.
 */
public interface ShoppingCartService {
    //获取购物车数据
    public Map getCart();
    //删除购物车数据  cookies是用户未登陆的时候从cookies里面删除数据
    public Map removeCart(HttpServletResponse res, HttpServletRequest req, String sku, Cookie[] cookies) throws ShoppingCartValidateException;
}
