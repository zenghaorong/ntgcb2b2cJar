package com.aebiz.app.web.modules.controllers.platform.msg;

import com.aebiz.app.msg.modules.models.Msg_conf_sms;
import com.aebiz.app.msg.modules.services.MsgConfSmsService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.msg.modules.models.Msg_info;
import com.aebiz.app.msg.modules.services.MsgInfoService;
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
@RequestMapping("/platform/msg/info")
public class MsgInfoController {

    @Autowired
	private MsgInfoService msgInfoService;

    @Autowired
    private MsgConfSmsService msgConfSmsService;

    @RequestMapping("")
    @RequiresPermissions("mall.msgconfig.sysmsgsetting")
	public String index() {
		return "pages/platform/msg/info/index";
	}

    @RequestMapping("/setting")
    @RequiresPermissions("mall.msgconfig.sysmsgsetting")
    public String setting(HttpServletRequest req) {
        req.setAttribute("smsConf", msgConfSmsService.fetchLinks(msgConfSmsService.fetch(Cnd.where("disabled", "=", false)), "smsTpls"));
        return "pages/platform/msg/info/setting";
    }

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("mall.msgconfig.sysmsgsetting")
    public Object data(@RequestParam(value = "name",required = false) String name,
                       @RequestParam(value = "nameCode",required = false) String nameCode,
                       DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(name)) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(name) + "%"));
        }
        if (Strings.isNotBlank(nameCode)) {
            cnd.and("nameCode", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(nameCode) + "%"));
        }
    	return msgInfoService.data(dataTable.getLength(), dataTable.getStart(),
                dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("mall.msgconfig.sysmsgsetting")
    public String add() {
    	return "pages/platform/msg/info/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Msg_info")
    @RequiresPermissions("mall.msgconfig.sysmsgsetting.add")
    public Object addDo(Msg_info msgInfo, HttpServletRequest req) {
		try {
			msgInfoService.insert(msgInfo);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("mall.msgconfig.sysmsgsetting")
    public String edit(@PathVariable String id,HttpServletRequest req) {
        Msg_info obj = msgInfoService.fetch(id);
		req.setAttribute("obj",obj);
		return "pages/platform/msg/info/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Msg_info")
    @RequiresPermissions("mall.msgconfig.sysmsgsetting.edit")
    public Object editDo(Msg_info msgInfo, HttpServletRequest req) {
		try {
            msgInfo.setOpBy(StringUtil.getUid());
			msgInfo.setOpAt((int) (System.currentTimeMillis() / 1000));
			msgInfoService.updateIgnoreNull(msgInfo);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Msg_info")
    @RequiresPermissions("mall.msgconfig.sysmsgsetting.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids != null && ids.length > 0){
				msgInfoService.delete(ids);
			}else{
				msgInfoService.delete(id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("mall.msgconfig.sysmsgsetting")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (Strings.isNotBlank(id)) {
            req.setAttribute("obj", msgInfoService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/msg/info/detail";
    }

    /**
     * 修改是否支持 站内信 ，短信 , 电子邮件
     * @param id
     * @param type
     * @param flag
     * @param req
     * @return
     */
    @RequestMapping("/chooseMsgInfo")
    @SJson
    @SLog(description = "Msg_info")
    @RequiresPermissions("mall.msgconfig.sysmsgsetting")
    public Object chooseMsgInfo(@RequestParam String id,@RequestParam String type,@RequestParam String flag,HttpServletRequest req) {
        Msg_info obj = msgInfoService.fetch(id);
        if("msg".equals(type)){
            obj.setSupportMSG(Boolean.valueOf(flag));
        }else if("sms".equals(type)){
            obj.setSupportSMS(Boolean.valueOf(flag));
        }else{
            obj.setSupportEmail(Boolean.valueOf(flag));
        }
        msgInfoService.updateIgnoreNull(obj);//忽略控制更新
        return Result.success("globals.result.success");
    }
}
