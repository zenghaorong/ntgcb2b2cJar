package com.aebiz.app.web.modules.controllers.platform.sys;

import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.rabbit.RabbitMessage;
import com.aebiz.baseframework.rabbit.RabbitProducer;
import com.aebiz.commons.utils.SpringUtil;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.sys.modules.models.Sys_route;
import com.aebiz.app.sys.modules.services.SysRouteService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping("/platform/sys/route")
public class SysRouteController {
    @Autowired
    private SysRouteService sysRouteService;
    @Autowired
    private RabbitProducer rabbitProducer;

    @RequestMapping("")
    @RequiresPermissions("sys.manager.route")
    public String index() {
        return "pages/platform/sys/route/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("sys.manager.route")
    public Object data(DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        return sysRouteService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("sys.manager.route")
    public String add() {
        return "pages/platform/sys/route/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "新增自定义路由")
    @RequiresPermissions("sys.manager.route.add")
    public Object addDo(Sys_route sysRoute, HttpServletRequest req) {
        try {
            if (sysRouteService.insert(sysRoute) != null) {
                Globals.initRoute(sysRouteService.dao());
                //如果启用队列，则广播
                if (SpringUtil.isRabbitEnabled()) {
                    String exchange = "fanoutExchange";
                    String routeKey = "sysroute";
                    RabbitMessage msg = new RabbitMessage(exchange, routeKey, new HashMap<>());
                    rabbitProducer.sendMessage(msg);
                }
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.route")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", sysRouteService.fetch(id));
        return "pages/platform/sys/route/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "编辑自定义路由")
    @RequiresPermissions("sys.manager.route.edit")
    public Object editDo(Sys_route sysRoute, HttpServletRequest req) {
        try {
            sysRoute.setOpBy(StringUtil.getUid());
            sysRoute.setOpAt((int) (System.currentTimeMillis() / 1000));
            if (sysRouteService.updateIgnoreNull(sysRoute) > 0) {
                Globals.initRoute(sysRouteService.dao());
                //如果启用队列，则广播
                if (SpringUtil.isRabbitEnabled()) {
                    String exchange = "fanoutExchange";
                    String routeKey = "sysroute";
                    RabbitMessage msg = new RabbitMessage(exchange, routeKey, new HashMap<>());
                    rabbitProducer.sendMessage(msg);
                }
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "删除自定义路由")
    @RequiresPermissions("sys.manager.route.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids", required = false) String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                sysRouteService.delete(ids);
                req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
            } else {
                sysRouteService.delete(id);
                req.setAttribute("id", id);
            }
            Globals.initRoute(sysRouteService.dao());
            //如果启用队列，则广播
            if (SpringUtil.isRabbitEnabled()) {
                String exchange = "fanoutExchange";
                String routeKey = "sysroute";
                RabbitMessage msg = new RabbitMessage(exchange, routeKey, new HashMap<>());
                rabbitProducer.sendMessage(msg);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/enable/{id}")
    @RequiresPermissions("sys.manager.route.edit")
    @SJson
    @SLog(description = "启用自定义路由")
    public Object enable(@PathVariable(required = false) String id, HttpServletRequest req) {
        try {
            sysRouteService.update(org.nutz.dao.Chain.make("disabled", false), Cnd.where("id", "=", id));
            Globals.initRoute(sysRouteService.dao());
            //如果启用队列，则广播
            if (SpringUtil.isRabbitEnabled()) {
                String exchange = "fanoutExchange";
                String routeKey = "sysroute";
                RabbitMessage msg = new RabbitMessage(exchange, routeKey, new HashMap<>());
                rabbitProducer.sendMessage(msg);
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @RequestMapping("/disable/{id}")
    @RequiresPermissions("sys.manager.route.edit")
    @SJson
    @SLog(description = "禁用自定义路由")
    public Object disable(@PathVariable(required = false) String id, HttpServletRequest req) {
        try {
            sysRouteService.update(org.nutz.dao.Chain.make("disabled", true), Cnd.where("id", "=", id));
            Globals.initRoute(sysRouteService.dao());
            //如果启用队列，则广播
            if (SpringUtil.isRabbitEnabled()) {
                String exchange = "fanoutExchange";
                String routeKey = "sysroute";
                RabbitMessage msg = new RabbitMessage(exchange, routeKey, new HashMap<>());
                rabbitProducer.sendMessage(msg);
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

}
