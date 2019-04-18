package com.aebiz.app.web.modules.controllers.open.api.goods;

import com.aebiz.app.cms.modules.models.Cms_article;
import com.aebiz.app.cms.modules.services.CmsArticleService;
import com.aebiz.app.goods.modules.models.Goods_spec;
import com.aebiz.app.goods.modules.models.Goods_spec_values;
import com.aebiz.app.goods.modules.services.GoodsSpecService;
import com.aebiz.app.goods.modules.services.GoodsSpecValuesService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.Pagination;
import com.aebiz.baseframework.view.annotation.SJson;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by gaoen on 2017/4/10.
 */
@Controller
@RequestMapping("/open/api/goods/spec")
public class ApiGoodsSpecController {
    private static final Log log = Logs.get();
    @Autowired
    private GoodsSpecService goodsSpecService;
    @Autowired
    private GoodsSpecValuesService GoodsSpecValuesService;

    /**
     * @api {post} /open/api/goods/spec/getValueBySpecId/specId
     * @apiGroup goods
     * @apiVersion 1.0.0
     * @apiPermission token
     * @apiParam {Object}	data 				    数据对象
     * @apiParamExample {json} 示例
     * POST /open/api/goods/spec/getValueBySpecId/specId
     * {
     * "id":""
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
    @RequestMapping(value = "/getValueBySpecId/{specId}", method = RequestMethod.POST)
    @SJson
    public Object getValueBySpecId(@PathVariable String specId, HttpServletRequest req) {

        try {
            List<Goods_spec_values> goodsSpecValues = GoodsSpecValuesService.getValueBySpecId(specId);
            return Result.success("ok", goodsSpecValues);
        } catch (Exception e) {
            return Result.error("fail");
        }
    }

    /**
     * @api {post} /open/api/goods/spec/getList 测试API
     * @apiGroup cms
     * @apiVersion 1.0.0
     * @apiPermission token
     * @apiParam {Object}	data 				    数据对象
     * @apiParamExample {json} 示例
     * POST /open/api/goods/spec/getList
     * {
     * "pageNumber":"",
     * "pageSize":"",
     * "channelId":"",
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
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @SJson
    public Object getList(@RequestBody NutMap map, HttpServletRequest req) {

        try {
            List<Goods_spec> goodsSpecs = goodsSpecService.getList();
            return Result.success("ok", goodsSpecs);
        } catch (Exception e) {
            return Result.error("fail");
        }
    }


}
