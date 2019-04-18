package com.aebiz.app.web.modules.controllers.platform.msg;


import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;

import com.aebiz.app.msg.modules.models.Msg_conf_sms;
import com.aebiz.app.msg.modules.services.MsgConfSmsService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.Logical;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/msg/conf/sms")
public class MsgConfSmsController {

    private static final Log log = Logs.get();

    @Autowired
	private MsgConfSmsService msgConfSmsService;

    @RequestMapping("")
    @RequiresPermissions(value = {"msg.conf.sms", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
	public String index() {
		return "pages/platform/msg/conf/sms/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions(value = {"msg.conf.sms", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public Object data(@RequestParam(value = "name",required = false) String name,
                       @RequestParam(value = "signname",required = false) String signname,
                       DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(name)) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(name) + "%"));
        }
        if (Strings.isNotBlank(signname)) {
            cnd.and("signname", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(signname) + "%"));
        }
    	return msgConfSmsService.data(dataTable.getLength(), dataTable.getStart(),
                dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions(value = {"msg.conf.sms", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public String add() {
    	return "pages/platform/msg/conf/sms/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "新增短信接口配置")
    @RequiresPermissions(value = {"msg.conf.sms.add", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public Object addDo(Msg_conf_sms msgConfSms, HttpServletRequest req) {
		try {
            if (!msgConfSms.isDisabled() && msgConfSmsService.fetch(Cnd.where("disabled", "=", false)) != null) {
                return Result.error("msg.conf.sms.error.exists");
            }
			msgConfSmsService.insertWith(msgConfSms, "smsTpls");
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions(value = {"msg.conf.sms", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", msgConfSmsService.dao().fetchLinks(msgConfSmsService.fetch(id), "smsTpls"));
		return "pages/platform/msg/conf/sms/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "保存短信接口配置")
    @RequiresPermissions(value = {"msg.conf.sms.edit", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public Object editDo(Msg_conf_sms msgConfSms, HttpServletRequest req) {
		try {
		    if (!msgConfSms.isDisabled() && msgConfSmsService.fetch(Cnd.where("disabled", "=", false).and("id", "!=", msgConfSms.getId())) != null) {
                return Result.error("msg.conf.sms.error.exists");
            }
            msgConfSmsService.save(msgConfSms);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "删除短信接口配置")
    @RequiresPermissions(value = {"msg.conf.sms.delete", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids != null && ids.length > 0){
                return Result.error("system.not.allow");
			}else{
                if (msgConfSmsService.fetch(Cnd.where("disabled", "=", false)) != null) {
                    return Result.error("msg.conf.sms.error.exists");
                }
				msgConfSmsService.deleteWith(id, "smsTpls");
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions(value = {"msg.conf.sms", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", msgConfSmsService.fetchLinks(msgConfSmsService.fetch(id), "smsTpls"));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/msg/conf/sms/detail";
    }

    @RequestMapping("/enable/{configId}")
    @SJson
    @RequiresPermissions(value = {"msg.conf.sms.edit", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    @SLog(description = "启用短信接口配置")
    public Object enable(@PathVariable(required = false) String configId, HttpServletRequest req) {
        try {
            if (msgConfSmsService.fetch(Cnd.where("disabled", "=", false).and("id", "!=", configId)) != null) {
                return Result.error("msg.conf.sms.error.exists");
            }
            msgConfSmsService.update(Chain.make("disabled", false), Cnd.where("id", "=", configId));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/disable/{configId}")
    @SJson
    @RequiresPermissions(value = {"msg.conf.sms.edit", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    @SLog(description = "禁用短信接口配置")
    public Object disable(@PathVariable(required = false) String configId, HttpServletRequest req) {
        try {
            msgConfSmsService.update(Chain.make("disabled", true), Cnd.where("id", "=", configId));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

}
