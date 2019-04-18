package com.aebiz.app.web.modules.controllers.open.api.cms;

import com.aebiz.app.cms.modules.models.Cms_link_class;
import com.aebiz.app.cms.modules.services.CmsLinkClassService;
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
import java.util.List;

/**
 * Created by heliu on 2017/4/10.
 */
@Controller
@RequestMapping("/open/api/cms/linkClass")
public class ApiCmsLinkClassController {
    private static final Log log = Logs.get();
    @Autowired
    private CmsLinkClassService cmsLinkClassService;

    /**
     * @api {post} /open/api/cms/linkClass/list?appid=appid&token=token 测试API
     * @apiGroup cms
     * @apiVersion 1.0.0
     * @apiPermission token
     * @apiParam {Object}	data 				    数据对象
     * @apiParamExample {json} 示例
     * POST /open/api/cms/linkClass/list?appid=appid&token=token
     * {
     *
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


    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @SJson
    public Object list(@RequestBody NutMap map, HttpServletRequest req) {

        try {
            log.debug("map::" + Json.toJson(map));
            List<Cms_link_class> cmsLinkList= cmsLinkClassService.list();
            return Result.success("ok",cmsLinkList);
        } catch (Exception e) {
            return Result.error("fail");
        }
    }

}
