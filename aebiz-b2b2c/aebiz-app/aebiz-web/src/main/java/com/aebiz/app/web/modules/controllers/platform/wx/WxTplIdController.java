package com.aebiz.app.web.modules.controllers.platform.wx;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.app.wx.modules.models.Wx_config;
import com.aebiz.app.wx.modules.models.Wx_tpl_id;
import com.aebiz.app.wx.modules.services.WxConfigService;
import com.aebiz.app.wx.modules.services.WxTplIdService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/platform/wx/tpl/id")
public class WxTplIdController {
    private static final Log log = Logs.get();
    @Autowired
    private WxTplIdService wxTplIdService;
    @Autowired
    private WxConfigService wxConfigService;

    @RequestMapping(value = {"", "/index/{wxid}"})
    @RequiresPermissions("wx.tpl.id")
    public String index(@PathVariable(required = false) String wxid, HttpServletRequest req) {
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxid = list.get(0).getId();
        }

        req.setAttribute("wxList", list);
        req.setAttribute("wxid", Strings.sBlank(wxid));
        return "pages/platform/wx/tpl/id/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("wx.tpl.id")
    public Object data(@RequestParam("wxid") String wxid, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        return wxTplIdService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping(value = {"/add", "/add/{wxid}"})
    @RequiresPermissions("wx.tpl.id")
    public String add(@PathVariable(required = false) String wxid, HttpServletRequest req) {
        req.setAttribute("wxid", wxid);
        return "pages/platform/wx/tpl/id/add";


    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "添加模板")
    @RequiresPermissions("wx.tpl.id.add")
    public Object addDo(Wx_tpl_id wxTplId, HttpServletRequest req) {
        try {
            WxApi2 wxApi2 = wxConfigService.getWxApi2(wxTplId.getWxid());
            WxResp wxResp = wxApi2.template_api_add_template(wxTplId.getId());
            if (wxResp.errcode() == 0) {
                wxTplId.setTemplate_id(wxResp.template_id());
                wxTplIdService.insert(wxTplId);
                return Result.success("globals.result.success");
            }
            return Result.error("globals.result.error");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping({"/delete", "/delete/{id}"})
    @SJson
    @SLog(description = "删除模板")
    @RequiresPermissions("wx.tpl.id.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam("wxid") String wxid, @RequestParam("ids") String[] ids, HttpServletRequest req) {
        try {
            WxApi2 wxApi2 = wxConfigService.getWxApi2(wxid);
            if (ids != null && ids.length > 0) {
                for (String i : ids) {
                    Wx_tpl_id wxTplId = wxTplIdService.fetch(i);
                    WxResp wxResp = wxApi2.template_api_del_template(wxTplId.getTemplate_id());
                    if (wxResp.errcode() == 0) {
                        wxTplIdService.delete(i);
                        return Result.success("system.success");
                    }
                }
                wxTplIdService.delete(ids);
            } else {
                Wx_tpl_id wxTplId = wxTplIdService.fetch(id);
                WxResp wxResp = wxApi2.template_api_del_template(wxTplId.getTemplate_id());
                if (wxResp.errcode() == 0) {
                    wxTplIdService.delete(id);
                    return Result.success("system.success");
                }
            }
            return Result.success("system.error");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }


    @RequestMapping("/detail/{id}")
    @RequiresPermissions("wx.tpl.id")
    public Object detail(@PathVariable(required = false) String id) {
        if (!Strings.isBlank(id)) {
            return wxTplIdService.fetch(id);
        }

        return "pages/platform/wx/tpl/id/detail";
    }

}
