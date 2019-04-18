package com.aebiz.app.web.commons.base;

import com.aebiz.app.sys.modules.models.Sys_config;
import com.aebiz.app.sys.modules.models.Sys_route;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.weixin.impl.WxApi2Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2016/12/19.
 */
public class Globals {
    //项目路径
    public static String APP_ROOT = "";
    //项目目录
    public static String APP_BASE = "";
    //项目名称
    public static String APP_NAME = "全网数商开发框架";
    //版权信息
    public static String APP_COPYRIGHT = "<www.aebiz.net>";
    //框架版本
    public static String APP_VERSION = "1.0";
    //项目短名称
    public static String APP_SHORT_NAME = "Qwwk";
    //项目域名
    public static String APP_DOMAIN = "127.0.0.1";
    //项目IP
    public static String APP_IP = "127.0.0.1";
    //项目IP
    public static String APP_PROTOCOL = "http://";
    //文件上传路径
    public static String APP_UPLOAD_PATH = "/upload";
    //系统自定义参数
    public static Map<String, String> MyConfig=new HashMap<>();
    //自定义路由
    public static Map<String, Sys_route> RouteMap=new HashMap<>();
    //微信map
    public static Map<String, WxApi2Impl> WxMap=new HashMap<>();

    public static void initSysConfig(Dao dao) {
        Globals.MyConfig.clear();
        List<Sys_config> configList = dao.query(Sys_config.class, Cnd.NEW());
        for (Sys_config sysConfig : configList) {
            switch (sysConfig.getConfigKey()) {
                case "APP_NAME":
                    Globals.APP_NAME = sysConfig.getConfigValue();
                    break;
                case "APP_SHORT_NAME":
                    Globals.APP_SHORT_NAME = sysConfig.getConfigValue();
                    break;
                case "APP_DOMAIN":
                    Globals.APP_DOMAIN = sysConfig.getConfigValue();
                    break;
                case "APP_IP":
                    Globals.APP_IP = sysConfig.getConfigValue();
                    break;
                case "APP_PROTOCOL":
                    Globals.APP_PROTOCOL = sysConfig.getConfigValue();
                    break;
                case "APP_UPLOAD_PATH":
                    Globals.APP_UPLOAD_PATH = sysConfig.getConfigValue();
                    break;
                default:
                    Globals.MyConfig.put(sysConfig.getConfigKey(), sysConfig.getConfigValue());
                    break;
            }
        }
    }

    public static void initRoute(Dao dao) {
        Globals.RouteMap.clear();
        List<Sys_route> routeList = dao.query(Sys_route.class, Cnd.where("disabled", "=", 0));
        for (Sys_route route : routeList) {
            Globals.RouteMap.put(route.getUrl(), route);
        }
    }
}
