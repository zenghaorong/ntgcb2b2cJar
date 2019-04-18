package com.aebiz.app.web.modules.controllers.platform.store;

import com.aebiz.app.store.modules.models.Store_type;
import com.aebiz.app.store.modules.services.StoreTypeService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/store/config/type")
public class StoreTypeController {

    @Autowired
    private StoreTypeService storeTypeService;

    @RequestMapping("")
    @RequiresPermissions("store.config.type")
    public String index() {
        return "pages/platform/store/config/type/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("store.config.type")
    public Object data(@RequestParam(value = "name", required = false) String name, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(name)) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(name) + "%"));
        }
        return storeTypeService.data(dataTable.getLength(), dataTable.getStart(),
                dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("store.config.type")
    public String add() {
        return "pages/platform/store/config/type/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Store_type")
    @RequiresPermissions("store.config.type.add")
    public Object addDo(Store_type storeType, HttpServletRequest req) {
        try {
            storeTypeService.add(storeType);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.config.type")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", storeTypeService.fetch(id));
        return "pages/platform/store/config/type/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Store_type")
    @RequiresPermissions("store.config.type.edit")
    public Object editDo(Store_type storeType, HttpServletRequest req) {
        try {
            storeTypeService.edit(storeType);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Store_type")
    @RequiresPermissions("store.config.type.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids", required = false) String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                storeTypeService.delete(ids);
            } else {
                storeTypeService.delete(id);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/enable/{id}")
    @SJson
    @SLog(description = "Store_type")
    @RequiresPermissions("store.config.type.edit")
    public Object enable(@PathVariable String id, HttpServletRequest req) {
        try {
            storeTypeService.update(Chain.make("disabled", false), Cnd.where("id", "=", id));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/disable/{id}")
    @SJson
    @SLog(description = "Store_type")
    @RequiresPermissions("store.config.type.edit")
    public Object disable(@PathVariable String id, HttpServletRequest req) {
        try {
            storeTypeService.update(Chain.make("disabled", true), Cnd.where("id", "=", id));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/setDefault/{id}")
    @SJson
    @SLog(description = "Store_role")
    @RequiresPermissions("store.config.role.edit")
    public Object setDefault(@PathVariable String id, HttpServletRequest req) {
        try {
            Store_type storeType = storeTypeService.fetch(id);
            storeType.setDisabled(false);//设为默认时需启用
            storeType.setDefaultValue(true);
            storeTypeService.edit(storeType);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
