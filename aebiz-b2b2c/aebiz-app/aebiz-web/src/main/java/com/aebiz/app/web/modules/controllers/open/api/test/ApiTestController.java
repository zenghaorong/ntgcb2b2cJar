package com.aebiz.app.web.modules.controllers.open.api.test;

import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wizzer on 2017/4/4.
 */
@Controller
@RequestMapping("/open/api/test")
public class ApiTestController {
    private static final Log log = Logs.get();

    /**
     * @api {post} /open/api/test/hi?appid=appid&token=token 测试API
     * @apiGroup Test
     * @apiVersion 1.0.0
     * @apiPermission token
     * @apiParam {Object}	data 				    数据对象
     * @apiParamExample {json} 示例
     * POST /open/api/test/hi?appid=appid&token=token
     * {
     * "txt": "你好，大鲨鱼"
     * }
     * @apiSuccess {number} code 			         code
     * @apiSuccess {String} msg 			         msg
     * @apiSuccessExample {json} 示例
     * HTTP/1.1 200 OK
     * {
     * "code": 0,
     * "msg": "ok"
     * }
     * @apiError (失败) {number} code 不等于0
     * @apiError (失败) {string} msg 错误文字描述
     * @apiErrorExample {json} 示例
     * HTTP/1.1 200 OK
     * {
     * "code": 1,
     * "msg": "fail"
     * }
     */
    @RequestMapping(value = "/hi", method = RequestMethod.POST)
    @SJson
    public Object hi(@RequestBody NutMap map, HttpServletRequest req) {
        try {
            log.debug("map::" + Json.toJson(map));
            return Result.success("ok");
        } catch (Exception e) {
            return Result.error("fail");
        }
    }

}
