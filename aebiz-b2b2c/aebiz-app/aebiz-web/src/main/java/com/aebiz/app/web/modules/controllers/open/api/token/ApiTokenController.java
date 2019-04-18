package com.aebiz.app.web.modules.controllers.open.api.token;

import com.aebiz.app.sys.modules.models.Sys_api;
import com.aebiz.app.sys.modules.services.SysApiService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.DateUtil;
import org.nutz.dao.Cnd;
import org.nutz.http.Request;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by wizzer on 2016/8/11.
 */
@Controller
@RequestMapping("/open/api/token")
public class ApiTokenController {
    private static final Log log = Logs.get();
    @Autowired
    private SysApiService apiService;

    /**
     * @api {post} /open/api/token/get 获取Token
     * @apiGroup Token
     * @apiVersion 1.0.0
     * @apiPermission anyone
     * @apiParam {String}	appId 					appId
     * @apiParam {String}	sign 				appId+appKey+yyyyMMddHH 计算出的MD5值
     * @apiParamExample {json} 示例
     * POST /open/api/token
     * {
     * "appId": "appId",
     * "sign": "sign"
     * }
     * @apiSuccess {number} code 			         code
     * @apiSuccess {String} msg 			         msg
     * @apiSuccess {Object[]} data 				数据对象
     * @apiSuccess {number} data.expires 			有效期
     * @apiSuccess {String} data.token 			Token
     * @apiSuccessExample {json} 示例
     * HTTP/1.1 200 OK
     * {
     * "code": 0,
     * "msg": "ok",
     * "data": {
     * "token": ""eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0IiwiZXhwIjoxNDcwOTA5OTc4fQ._T7egDYhCL27jCvEv4J0cyjRj8s_YLj2gZjjTA8mzk81mTdeM-JXnH7VmtfaenW33BpJJzs2Hs2sXiiNHdzU6Q",
     * "expires": 7200,
     * }
     * }
     * @apiError (失败) {number} code 不等于0
     * @apiError (失败) {string} msg 错误文字描述
     * @apiErrorExample {json} 示例
     * HTTP/1.1 200 OK
     * {
     * "code": 1
     * "msg": "token invalid"
     * }
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    @SJson
    public Object get(@RequestBody NutMap map, HttpServletRequest req) {
        try {
            Sys_api api = apiService.fetch(Cnd.where("appId", "=", map.getString("appid", "")));
            if (api == null)
                return Result.error("appId error");
            if (!Lang.md5(map.getString("appid", "") + api.getAppKey() + DateUtil.format(new Date(), "yyyyMMddHH")).equalsIgnoreCase(map.getString("sign", "")))
                return Result.error("sign error");
            NutMap res = new NutMap();
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.HOUR, +2);
            date = c.getTime();
            res.addv("expires", 7200);
            res.addv("token", apiService.generateToken(date, map.getString("appid", "")));
            return Result.success("ok", res);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return Result.error("fail");
        }
    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @SJson
    public Object test() {
        try {
            Request req = Request.create("http://127.0.0.1:8089/open/api/token/get", Request.METHOD.POST);
            NutMap nutMap = NutMap.NEW();
            nutMap.addv("appid", "1af6410b68");
            nutMap.addv("sign", Lang.md5("1af6410b68" + "6e307c640eb84d6c" + DateUtil.format(new Date(), "yyyyMMddHH")));
            req.setData(Json.toJson(nutMap));
            req.getHeader().set("Content-Type", "application/json");
            Response resp = Sender.create(req).send();
            if (resp.isOK()) {
                return resp.getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
