package com.aebiz.app.web.modules.controllers.platform.wx;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.app.wx.modules.models.Wx_config;
import com.aebiz.app.wx.modules.models.Wx_tpl_list;
import com.aebiz.app.wx.modules.services.WxConfigService;
import com.aebiz.app.wx.modules.services.WxTplListService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/platform/wx/tpl/list")
public class WxTplListController {
    private static final Log log = Logs.get();
    @Autowired
    private WxTplListService wxTplListService;
    @Autowired
    private WxConfigService wxConfigService;

    @RequestMapping(value = {"", "/index/{wxid}"})
    @RequiresPermissions("wx.tpl.list")
    public String index(String wxid, HttpServletRequest req) {
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxid = list.get(0).getId();
        }

        req.setAttribute("wxList", list);
        req.setAttribute("wxid", Strings.sBlank(wxid));
        return "pages/platform/wx/tpl/list/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("wx.tpl.list")
    public Object data(@RequestParam("wxid") String wxid, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }

        return wxTplListService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }


    @RequestMapping("/getDo")
    @SJson
    @SLog(description = "获取模板列表")
    @RequiresPermissions("wx.tpl.list.get")
    public Object getDo(@RequestParam("wxid") String wxid, HttpServletRequest req) {
        try {
            WxApi2 wxApi2 = wxConfigService.getWxApi2(wxid);
            WxResp wxResp = wxApi2.get_all_private_template();
            List<Wx_tpl_list> lists = wxResp.getList("template_list", Wx_tpl_list.class);
            for (Wx_tpl_list o : lists) {
                o.setWxid(wxid);
                wxTplListService.insert(o);
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

}
