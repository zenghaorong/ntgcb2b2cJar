package com.aebiz.app.web.modules.controllers.platform.msg;

import com.aebiz.app.msg.modules.models.Msg_info;
import com.aebiz.app.msg.modules.services.MsgInfoService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.msg.modules.models.Msg_info_param;
import com.aebiz.app.msg.modules.services.MsgInfoParamService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/platform/msg/info/param")
public class MsgInfoParamController {

    @Autowired
	private MsgInfoParamService msgInfoParamService;

    @Autowired
    private MsgInfoService msgInfoService;

    @RequestMapping("")
	@RequiresPermissions(value = {"mall.msgconfig.param", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
	public String index() {
		return "pages/platform/msg/info/param/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions(value = {"mall.msgconfig.param", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public Object data(@RequestParam(value = "infoName",required = false) String infoName,
                       @RequestParam(value = "code",required = false) String code,
                       DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(infoName)) {
            cnd.and("infoName", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(infoName) + "%"));
        }
        if (Strings.isNotBlank(code)) {
            cnd.and("code", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(code) + "%"));
        }
    	return msgInfoParamService.data(dataTable.getLength(), dataTable.getStart(),
                dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions(value = {"mall.msgconfig.param", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public String add(HttpServletRequest req) {
        Cnd cnd = Cnd.NEW();
        List<Msg_info> msglist = msgInfoService.query(cnd);
        req.setAttribute("msglist",msglist);
        return "pages/platform/msg/info/param/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Msg_info_param")
    @RequiresPermissions(value = {"mall.msgconfig.param.add", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public Object addDo(Msg_info_param msgInfoParam, HttpServletRequest req) {
		try {
			msgInfoParamService.insert(msgInfoParam);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions(value = {"mall.msgconfig.param", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public String edit(@PathVariable String id, HttpServletRequest req) {
		req.setAttribute("obj", msgInfoParamService.fetch(id));
		return "pages/platform/msg/info/param/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Msg_info_param")
    @RequiresPermissions(value = {"mall.msgconfig.param.edit", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public Object editDo(Msg_info_param msgInfoParam, HttpServletRequest req) {
		try {
            msgInfoParam.setOpBy(StringUtil.getUid());
			msgInfoParam.setOpAt((int) (System.currentTimeMillis() / 1000));
			msgInfoParamService.updateIgnoreNull(msgInfoParam);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Msg_info_param")
    @RequiresPermissions(value = {"mall.msgconfig.param.delete", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				msgInfoParamService.delete(ids);
			}else{
				msgInfoParamService.delete(id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions(value = {"mall.msgconfig.param", "mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", msgInfoParamService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/msg/info/param/detail";
    }


    @RequestMapping("/getMsgParam")
    @SJson("full")
    @RequiresPermissions(value = {"mall.msgconfig.param","mall.msgconfig.sysmsgsetting"}, logical = Logical.OR)
    public Object getMsgParam(@RequestParam String infoId, HttpServletRequest req) {
        try {
            Cnd cnd = Cnd.NEW();
            Msg_info msgInfo = msgInfoService.fetch(infoId);
            msgInfoService.fetchLinks(msgInfo,"paramList",cnd);
            return Result.success("globals.result.success",msgInfo);
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }



}
