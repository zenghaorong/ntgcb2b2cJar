package com.aebiz.app.web.modules.controllers.platform.wx;

import com.aebiz.app.wx.modules.models.Wx_config;
import com.aebiz.app.wx.modules.services.WxConfigService;
import com.aebiz.app.wx.modules.services.WxTplLogService;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/platform/wx/tpl/log")
public class WxTplLogController {
    private static final Log log = Logs.get();
    @Autowired
    private WxTplLogService wxTplLogService;
    @Autowired
    private WxConfigService wxConfigService;

    @RequestMapping(value = {"", "/index/{wxid}"})
    @RequiresPermissions("wx.tpl")
    public String index(@PathVariable(required = false) String wxid, HttpServletRequest req) {
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxid = list.get(0).getId();
        }
        req.setAttribute("wxList", list);
        req.setAttribute("wxid", Strings.sBlank(wxid));

        return "pages/platform/wx/tpl/log/index";

    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("wx.tpl")
    public Object data(@Param("wxid") String wxid,
                       @Param("nickname") String nickname,
                       @Param("openid") String openid, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        if (!Strings.isBlank(nickname)) {
            cnd.and("nickname", "like", "%" + nickname + "%");
        }
        if (!Strings.isBlank(wxid)) {
            cnd.and("openid", "=", openid);
        }
        return wxTplLogService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }


}
