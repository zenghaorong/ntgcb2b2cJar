package com.aebiz.app.web.modules.controllers.platform.msg;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.msg.modules.models.Msg_type;
import com.aebiz.app.msg.modules.services.MsgTypeService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/msg/type")
public class MsgTypeController {

    @Autowired
    private MsgTypeService msgTypeService;

    @RequestMapping("")
    @RequiresPermissions("mall.msgconfig.msgType")
    public String index() {
        return "pages/platform/msg/type/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("mall.msgconfig.msgType")
    public Object data(@RequestParam(value = "code",required = false) String code,
                       @RequestParam(value = "name",required = false) String name,
                       DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(code)) {
            cnd.and("code", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(code) + "%"));
        }
        if (Strings.isNotBlank(name)) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(name) + "%"));
        }
        return msgTypeService.data(dataTable.getLength(), dataTable.getStart(),
                dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("mall.msgconfig.msgType")
    public String add() {
        return "pages/platform/msg/type/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Msg_type")
    @RequiresPermissions("mall.msgconfig.msgType.add")
    public Object addDo(Msg_type msgType, HttpServletRequest req) {
        try {
            msgTypeService.insert(msgType);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("mall.msgconfig.msgType")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", msgTypeService.fetch(id));
        return "pages/platform/msg/type/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Msg_type")
    @RequiresPermissions("mall.msgconfig.msgType.edit")
    public Object editDo(Msg_type msgType, HttpServletRequest req) {
        try {
            msgType.setOpBy(StringUtil.getUid());
            msgType.setOpAt((int) (System.currentTimeMillis() / 1000));
            msgTypeService.updateIgnoreNull(msgType);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Msg_type")
    @RequiresPermissions("mall.msgconfig.msgType.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids", required = false) String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                msgTypeService.delete(ids);
            } else {
                msgTypeService.delete(id);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("mall.msgconfig.msgType")
    public String detail(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            req.setAttribute("obj", msgTypeService.fetch(id));
        } else {
            req.setAttribute("obj", null);
        }
        return "pages/platform/msg/type/detail";
    }

}
