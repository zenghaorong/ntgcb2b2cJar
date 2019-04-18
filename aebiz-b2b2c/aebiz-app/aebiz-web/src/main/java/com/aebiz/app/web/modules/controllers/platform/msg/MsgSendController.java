package com.aebiz.app.web.modules.controllers.platform.msg;

import com.aebiz.app.msg.modules.services.MsgTypeService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.msg.modules.models.Msg_send;
import com.aebiz.app.msg.modules.services.MsgSendService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/msg/send")
public class MsgSendController {
    @Autowired
	private MsgSendService msgSendService;
    @Autowired
    private MsgTypeService msgTypeService;

    @RequestMapping("")
	@RequiresPermissions("platform.msg.send")
	public String index() {
		return "pages/platform/msg/send/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("platform.msg.send")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return msgSendService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("platform.msg.send")
    public String add() {
        return "pages/platform/msg/send/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Msg_send")
    @RequiresPermissions("platform.msg.send.add")
    public Object addDo(Msg_send msgSend, HttpServletRequest req) {
		try {
			msgSendService.insert(msgSend);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("platform.msg.send")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", msgSendService.fetch(id));
		return "pages/platform/msg/send/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Msg_send")
    @RequiresPermissions("platform.msg.send.edit")
    public Object editDo(Msg_send msgSend, HttpServletRequest req) {
		try {
            msgSend.setOpBy(StringUtil.getUid());
			msgSend.setOpAt((int) (System.currentTimeMillis() / 1000));
			msgSendService.updateIgnoreNull(msgSend);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Msg_send")
    @RequiresPermissions("platform.msg.send.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				msgSendService.delete(ids);
			}else{
				msgSendService.delete(id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("platform.msg.send")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", msgSendService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/msg/send/detail";
    }

}
