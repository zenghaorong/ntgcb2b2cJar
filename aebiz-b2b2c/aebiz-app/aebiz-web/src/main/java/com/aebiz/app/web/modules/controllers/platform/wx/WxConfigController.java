package com.aebiz.app.web.modules.controllers.platform.wx;

import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.rabbit.RabbitMessage;
import com.aebiz.baseframework.rabbit.RabbitProducer;
import com.aebiz.commons.utils.SpringUtil;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.wx.modules.models.Wx_config;
import com.aebiz.app.wx.modules.services.WxConfigService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping("/platform/wx/conf/account")
public class WxConfigController {
    @Autowired
	private WxConfigService wxConfigService;
    @Autowired
    private RabbitProducer rabbitProducer;

    @RequestMapping("")
	@RequiresPermissions("wx.conf.account")
	public String index() {
		return "pages/platform/wx/account/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("wx.conf.account")
    public Object data(DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
    	return wxConfigService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("wx.conf.account")
    public String add() {
    	return "pages/platform/wx/account/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Wx_config")
    @RequiresPermissions("wx.conf.account.add")
    public Object addDo(Wx_config wxConfig, HttpServletRequest req) {
		try {
			wxConfigService.insert(wxConfig);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("wx.conf.account")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", wxConfigService.fetch(id));
		return "pages/platform/wx/account/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Wx_config")
    @RequiresPermissions("wx.conf.account.edit")
    public Object editDo(Wx_config wxConfig, HttpServletRequest req) {
		try {
            wxConfig.setOpBy(StringUtil.getUid());
			wxConfig.setOpAt((int) (System.currentTimeMillis() / 1000));
			wxConfigService.updateIgnoreNull(wxConfig);
            Globals.WxMap.clear();
            //如果启用队列，则广播
            if (SpringUtil.isRabbitEnabled()) {
                String exchange = "fanoutExchange";
                String routeKey = "wxtoken";
                RabbitMessage msg = new RabbitMessage(exchange, routeKey, new HashMap<>());
                rabbitProducer.sendMessage(msg);
            }
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Wx_config")
    @RequiresPermissions("wx.conf.account.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				wxConfigService.delete(ids);
			}else{
				wxConfigService.delete(id);
			}
            Globals.WxMap.clear();
            //如果启用队列，则广播
            if (SpringUtil.isRabbitEnabled()) {
                String exchange = "fanoutExchange";
                String routeKey = "wxtoken";
                RabbitMessage msg = new RabbitMessage(exchange, routeKey, new HashMap<>());
                rabbitProducer.sendMessage(msg);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("wx.conf.account")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", wxConfigService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/wx/account/detail";
    }

}
