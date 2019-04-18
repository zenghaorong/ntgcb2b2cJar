package com.aebiz.app.web.modules.controllers.platform.sys;

import com.aebiz.app.sys.modules.models.Sys_api;
import com.aebiz.app.sys.modules.services.SysApiService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Created by wizzer on 2017/4/4.
 */
@Controller
@RequestMapping("/platform/sys/api")
public class SysApiController {
    @Autowired
    private SysApiService sysApiService;

    @RequestMapping("")
    @RequiresPermissions("sys.manager.api")
    public String index() {
        return "pages/platform/sys/api/index";
    }

    @RequestMapping("/add")
    @RequiresPermissions("sys.manager.api")
    public String add() {
        return "pages/platform/sys/api/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @RequiresPermissions("sys.manager.api.add")
    @SLog(description = "新增应用")
    public Object addDo(Sys_api api) {
        try {
            if (sysApiService.fetch(Cnd.where("appId", "=", api.getAppId())) != null)
                return Result.error("globals.result.error");
            api.setAppId(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10));
            api.setAppKey(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16));
            api.setDisabled(false);
            sysApiService.insert(api);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @RequiresPermissions("sys.manager.api.delete")
    @SLog(description = "删除应用")
    public Object delete(@PathVariable String id) {
        try {
            sysApiService.delete(id);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/reset/{id}", "/reset"})
    @SJson
    @RequiresPermissions("sys.manager.api.edit")
    @SLog(description = "重置应用")
    public Object reset(@PathVariable  String id) {
        try {
            sysApiService.update(org.nutz.dao.Chain.make("appKey", UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16)), Cnd.where("id", "=", id));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @RequestMapping("/data")
    @RequiresPermissions("sys.manager.api")
    @SJson("full")
    public Object data(DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        return sysApiService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/enable/{id}")
    @SJson
    @SLog(description = "启用应用")
    @RequiresPermissions("sys.manager.api.edit")
    public Object enable(@PathVariable String id, HttpServletRequest req) {
        try {
            sysApiService.update(org.nutz.dao.Chain.make("disabled", false), Cnd.where("id", "=", id));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/disable/{id}")
    @SJson
    @SLog(description = "禁用应用")
    @RequiresPermissions("sys.manager.api.edit")
    public Object disable(@PathVariable String id, HttpServletRequest req) {
        try {
            sysApiService.update(org.nutz.dao.Chain.make("disabled", true), Cnd.where("id", "=", id));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
