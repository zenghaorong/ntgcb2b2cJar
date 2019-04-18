package com.aebiz.app.web.modules.controllers.platform.msg;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.msg.modules.models.Msg_conf_sms_tpl;
import com.aebiz.app.msg.modules.services.MsgConfSmsTplService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/msg/conf/sms/tpl")
public class MsgConfSmsTplController {
    private static final Log log = Logs.get();
    @Autowired
	private MsgConfSmsTplService msgConfSmsTplService;

    @RequestMapping("")
    @RequiresPermissions("msg.conf.sms.tpl")
	public String index() {
		return "pages/platform/msg/conf/sms/tpl/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("msg.conf.sms.tpl")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return msgConfSmsTplService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("msg.conf.sms.tpl")
    public String add() {
    	return "pages/platform/msg/conf/sms/tpl/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Msg_conf_sms_tpl")
    @RequiresPermissions("msg.conf.sms.tpl.add")
    public Object addDo(Msg_conf_sms_tpl msgConfSmsTpl, HttpServletRequest req) {
		try {
			msgConfSmsTplService.insert(msgConfSmsTpl);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("msg.conf.sms.tpl")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", msgConfSmsTplService.fetch(id));
		return "pages/platform/msg/conf/sms/tpl/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Msg_conf_sms_tpl")
    @RequiresPermissions("msg.conf.sms.tpl.edit")
    public Object editDo(Msg_conf_sms_tpl msgConfSmsTpl, HttpServletRequest req) {
		try {
            msgConfSmsTpl.setOpBy(StringUtil.getUid());
			msgConfSmsTpl.setOpAt((int) (System.currentTimeMillis() / 1000));
			msgConfSmsTplService.updateIgnoreNull(msgConfSmsTpl);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Msg_conf_sms_tpl")
    @RequiresPermissions("msg.conf.sms.tpl.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				msgConfSmsTplService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				msgConfSmsTplService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("msg.conf.sms.tpl")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", msgConfSmsTplService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/msg/conf/sms/tpl/detail";
    }

}
