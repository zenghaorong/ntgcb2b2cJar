package com.aebiz.app.web.modules.controllers.store.sys;

import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.cms.modules.models.Cms_review;
import com.aebiz.app.order.modules.services.OrderMainService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: zenghaorong
 * @Date: 2019/4/15  10:44
 * @Description: 数据统计相关控制层
 */
@Controller
@RequestMapping("/store/count")
public class StoreCountController {

    private static final Log log = Logs.get();

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private OrderMainService orderMainService;

    /**
     * 获取商城总用户量
     */
    @RequestMapping("getUserCount.html")
    @SJson
    public Result getUserCount(){
        try {
            Integer count=accountUserService.count();
            return Result.success("ok",count);
        } catch (Exception e) {
            log.error("发布评论异常",e);
            return Result.error("fail");
        }
    }

    /**
     * 获取商城总订单量
     */
    @RequestMapping("getOrderCount.html")
    @SJson
    public Result getOrderCount(){
        try {
            Integer count=orderMainService.count();
            return Result.success("ok",count);
        } catch (Exception e) {
            log.error("发布评论异常",e);
            return Result.error("fail");
        }
    }




}
