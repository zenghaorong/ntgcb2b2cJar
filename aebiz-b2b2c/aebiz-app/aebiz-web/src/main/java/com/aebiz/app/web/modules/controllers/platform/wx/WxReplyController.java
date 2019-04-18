package com.aebiz.app.web.modules.controllers.platform.wx;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.app.wx.modules.models.Wx_config;
import com.aebiz.app.wx.modules.models.Wx_reply;
import com.aebiz.app.wx.modules.models.Wx_reply_news;
import com.aebiz.app.wx.modules.services.WxConfigService;
import com.aebiz.app.wx.modules.services.WxReplyNewsService;
import com.aebiz.app.wx.modules.services.WxReplyService;
import com.aebiz.app.wx.modules.services.WxReplyTxtService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by gaoen on 2017-2-20 15:24:35
 */
@Controller
@RequestMapping("/platform/wx/reply/conf")
public class WxReplyController {
    private static final Log log = Logs.get();
    @Autowired
    WxReplyTxtService wxReplyTxtService;
    @Autowired
    WxReplyNewsService wxReplyNewsService;
    @Autowired
    WxReplyService wxReplyService;
    @Autowired
    WxConfigService wxConfigService;

    @RequestMapping(value = {"", "/{type}"})
    @RequiresPermissions("wx.reply")
    public String index(@PathVariable(required = false) String type, HttpServletRequest req) {
        String wxid = req.getParameter("wxid");
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxid = list.get(0).getId();
        }

        req.setAttribute("wxList", list);
        req.setAttribute("wxid", wxid);
        req.setAttribute("type", type);

        return "pages/platform/wx/reply/conf/index";
    }

    @RequestMapping("/{type}/add/{wxid}")
    @RequiresPermissions("wx.reply")
    public String add(@PathVariable(required = false) String type, @PathVariable(required = false) String wxid, HttpServletRequest req) {
        req.setAttribute("config", wxConfigService.fetch(wxid));
        req.setAttribute("type", type);
        req.setAttribute("wxid", wxid);

        return "pages/platform/wx/reply/conf/add";
    }

    @RequestMapping("/{type}/addDo")
    @SJson
    @RequiresPermissions(value = {"wx.reply.follow.add", "wx.reply.keyword.add"}, logical = Logical.OR)
    @SLog(description = "添加绑定")
    public Object addDo(@PathVariable(required = false)  String type,Wx_reply reply, HttpServletRequest req) {
        try {

            if ("follow".equals(reply.getType())) {
                int c = wxReplyService.count(Cnd.where("type", "=", "follow").and("wxid", "=", reply.getWxid()));
                if (c > 0) {
                    return Result.error("关注事件只可设置一条");
                }
            }
            if ("keyword".equals(reply.getType())) {
                int c = wxReplyService.count(Cnd.where("keyword", "=", reply.getKeyword()).and("wxid", "=", reply.getWxid()));
                if (c > 0) {
                    return Result.error("关键词已存在");
                }
            }
            wxReplyService.insert(reply);
            if ("news".equals(reply.getMsgType())) {
                String[] newsIds = Strings.sBlank(reply.getContent()).split(",");
                int i = 0;
                for (String id : newsIds) {
                    wxReplyNewsService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", id));
                    i++;
                }
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/{type}/edit/{id}")
    @RequiresPermissions("wx.reply")
    public Object edit(@PathVariable(required = false) String type, @PathVariable(required = false) String id, HttpServletRequest req) {
        Wx_reply reply = wxReplyService.fetch(id);
        if ("txt".equals(reply.getMsgType())) {
            req.setAttribute("txt", wxReplyTxtService.fetch(reply.getContent()));
        } else if ("news".equals(reply.getMsgType())) {
            String[] newsIds = Strings.sBlank(reply.getContent()).split(",");
            List<Wx_reply_news> newsList = wxReplyNewsService.query(Cnd.where("id", "in", newsIds).asc("location"));
            req.setAttribute("news", newsList);
        }

        req.setAttribute("type", reply.getType());
        req.setAttribute("wxid", reply.getWxid());
        req.setAttribute("config", wxConfigService.fetch(reply.getWxid()));
        req.setAttribute("obj", reply);

        return "pages/platform/wx/reply/conf/edit";

    }

    @RequestMapping("/{type}/editDo")
    @SJson
    @RequiresPermissions(value = {"wx.reply.follow.edit", "wx.reply.keyword.edit"}, logical = Logical.OR)
    @SLog(description = "修改绑定")
    public Object editDo(@PathVariable(required = false) String type, Wx_reply reply, HttpServletRequest req) {
        try {
            wxReplyService.updateIgnoreNull(reply);
            if ("news".equals(reply.getMsgType())) {
                String[] newsIds = Strings.sBlank(reply.getContent()).split(",");
                int i = 0;
                for (String id : newsIds) {
                    wxReplyNewsService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", id));
                    i++;
                }
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/{type}/delete/{id}")
    @SJson
    @RequiresPermissions(value = {"wx.reply.follow.delete", "wx.reply.keyword.delete"}, logical = Logical.OR)
    @SLog(description = "删除绑定")
    public Object delete(@PathVariable(required = false) String type, @PathVariable(required = false) String id, HttpServletRequest req) {
        try {
            wxReplyService.delete(id);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/{ids}/delete")
    @SJson
    @RequiresPermissions(value = {"wx.reply.follow.delete", "wx.reply.keyword.delete"}, logical = Logical.OR)
    @SLog(description = "删除绑定")
    public Object deletes(String type, @PathVariable("ids") String ids, HttpServletRequest req) {
        try {
            wxReplyService.delete(StringUtils.split(ids, ","));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/{type}/data")
    @SJson("full")
    @RequiresPermissions("wx.reply")
    public Object data(@PathVariable (required = false) String type, @RequestParam("wxid") String wxid, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(type)) {
            cnd.and("type", "=", type);
        }
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        return wxReplyService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/{type}/select")
    @RequiresPermissions("wx.reply")
    public String select(@PathVariable(required = false) String type, @RequestParam("wxid") String wxid, @RequestParam("msgType") String msgType, HttpServletRequest req) {
        req.setAttribute("type", type);
        req.setAttribute("wxid", wxid);
        req.setAttribute("msgType", msgType);
        return "pages/platform/wx/reply/conf/select";
    }

    @RequestMapping("/{type}/selectData")
    @SJson("full")
    @RequiresPermissions("wx.reply")
    public Object selectData(@PathVariable(required = false) String type,
                             @RequestParam("msgType") String msgType,
                             @RequestParam("wxid") String wxid,DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if ("txt".equals(msgType)) {
            return wxReplyTxtService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
        } else {
            return wxReplyNewsService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
        }
    }
}
