package com.aebiz.app.web.modules.controllers.platform.sys;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.app.sys.modules.models.Sys_config;
import com.aebiz.app.sys.modules.services.SysConfigService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.rabbit.RabbitMessage;
import com.aebiz.baseframework.rabbit.RabbitProducer;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.SpringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Created by wizzer on 2017/1/19.
 */
@Controller
@RequestMapping("/platform/sys/conf")
public class SysConfController {
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private RabbitProducer rabbitProducer;

    @RequestMapping("")
    @RequiresPermissions("sys.manager.conf")
    public String index() {
        return "pages/platform/sys/conf/index";
    }

    @RequestMapping("/data")
    @RequiresPermissions("sys.manager.conf")
    @SJson("full")
    public Object data(DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        return sysConfigService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("sys.manager.conf")
    public String add() {
        return "pages/platform/sys/conf/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @RequiresPermissions("sys.manager.conf.add")
    @SLog(description = "新增系统参数")
    public Object addDo(Sys_config conf) {
        try {
            if (sysConfigService.insert(conf) != null) {
                Globals.initSysConfig(sysConfigService.dao());
                //如果启用队列，则广播
                if (SpringUtil.isRabbitEnabled()) {
                    String exchange = "fanoutExchange";
                    String routeKey = "sysconfig";
                    RabbitMessage msg = new RabbitMessage(exchange, routeKey, new HashMap<>());
                    rabbitProducer.sendMessage(msg);
                }
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/edit/{id}", "/edit"})
    @RequiresPermissions("sys.manager.conf")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", sysConfigService.fetch(id));
        return "pages/platform/sys/conf/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @RequiresPermissions("sys.manager.conf.edit")
    @SLog(description = "修改系统参数")
    public Object editDo(Sys_config conf) {
        try {
            conf.setOpAt((int) (System.currentTimeMillis() / 1000));
            if (sysConfigService.updateIgnoreNull(conf) > 0) {
                Globals.initSysConfig(sysConfigService.dao());
                //如果启用队列，则广播
                if (SpringUtil.isRabbitEnabled()) {
                    String exchange = "fanoutExchange";
                    String routeKey = "sysconfig";
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
    @RequiresPermissions("sys.manager.conf.delete")
    @SLog(description = "删除系统参数")
    public Object delete(@PathVariable String id) {
        try {
            if (Strings.sBlank(id).startsWith("APP")) {
                return Result.error("sys.conf.no.delete");
            }
            if (sysConfigService.delete(id) > 0) {
                Globals.initSysConfig(sysConfigService.dao());
                //如果启用队列，则广播
                if (SpringUtil.isRabbitEnabled()) {
                    String exchange = "fanoutExchange";
                    String routeKey = "sysconfig";
                    RabbitMessage msg = new RabbitMessage(exchange, routeKey, new HashMap<>());
                    rabbitProducer.sendMessage(msg);
                }
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
