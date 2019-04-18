package com.aebiz.app.web.commons.utils;

import org.nutz.http.Request;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * 对接快递100接口，根据订单号，返回物流信息
 * 1、返回物流信息json数据
 * 2、返回查看物流的页面地址url
 */
@Component
public class KuaiDi100Util {
    private final static Log log = Logs.get();

    /**
     * 查询快递信息
     *
     * @param com 快递公司代码
     * @param nu  快递单号
     * @param key
     * @return 返回json字符串
     */
    public static String getExpressInfo(String com, String nu, String key) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("http://api.kuaidi100.com/api?id=").append(key);
            sb.append("&com=").append(com);
            sb.append("&nu=").append(nu);
            sb.append("&show=0&muti=1&order=desc");
            Request req = Request.create(sb.toString(), Request.METHOD.GET);
            Response resp = Sender.create(req).send();
            if (resp.isOK()) {
                return resp.getContent();
            }
        } catch (Exception e) {
            log.error("快递查询错误:" + e.getMessage());
        }
        return "";
    }

    /**
     * 该接口使用与收费的快递公司接口查询
     *
     * @param com 快递公司代码
     * @param nu  快递单号
     * @param key
     * @return 返回查询快递的url地址，可以使用iframe进行显示内容
     */
    public static String getInfoUrl(String com, String nu, String key) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("http://www.kuaidi100.com/applyurl?key=").append(key);
            sb.append("&com=").append(com);
            sb.append("&nu=").append(nu);
            Request req = Request.create(sb.toString(), Request.METHOD.GET);
            Response resp = Sender.create(req).send();
            if (resp.isOK()) {
                return resp.getContent();
            }
        } catch (Exception e) {
            log.error("快递查询错误:" + e.getMessage());
        }
        return "";
    }
}
