package com.aebiz.app.web.modules.controllers.open.api.cms;

import com.aebiz.app.cms.modules.models.Cms_site;
import com.aebiz.app.cms.modules.services.CmsSiteService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by heliu on 2017/4/7.
 */
@Controller
@RequestMapping("/open/api/cms/site")
public class ApiCmsSiteController {
    private static final Log log = Logs.get();

    @Autowired
    private CmsSiteService cmsSiteService;


    /**
     * @api {post} /open/api/cms/site/getCmsSite?appid=appid&token=token 测试API
     * @apiGroup cms
     * @apiVersion 1.0.0
     * @apiPermission token
     * @apiParam {Object}	data 				    数据对象
     * @apiParamExample {json} 示例
     * POST /open/api/cms/site/getCmsSite?appid=appid&token=token
     * {
     *  "id":""
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


    @RequestMapping(value = "/getCmsSite", method = RequestMethod.POST)
    @SJson
    public Object getCmsSite(@RequestBody NutMap map, HttpServletRequest req) {

        try {
            log.debug("map::" + Json.toJson(map));
            String id= map.getString("id","");
            Cms_site  site= cmsSiteService.getCmsSite(id);
            if(null==site){
                site=new Cms_site();
            }
            return Result.success("ok",site);
        } catch (Exception e) {
            return Result.error("fail");
        }
    }

}
