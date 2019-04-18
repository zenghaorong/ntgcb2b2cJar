package com.aebiz.app.web.modules.controllers.platform.wx;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.app.wx.modules.models.Wx_reply_news;
import com.aebiz.app.wx.modules.services.WxReplyNewsService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.WhaleAdaptor;
import org.nutz.mvc.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by gaoen on 2017-2-17 17:18:42
 */
@Controller
@RequestMapping("/platform/wx/reply/news")
public class WxReplyNewsController {
    private static final Log log = Logs.get();
    @Autowired
    WxReplyNewsService wxReplyNewsService;

    @RequestMapping("")
    @RequiresPermissions("wx.reply")
    public String index() {
        return "pages/platform/wx/reply/news/index";
    }

    @RequestMapping("/add")
    @RequiresPermissions("wx.reply")
    public String add() {
        return "pages/platform/wx/reply/news/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @RequiresPermissions("wx.reply.news.add")
    @SLog(description = "添加回复图文")
    public Object addDo(Wx_reply_news news, HttpServletRequest req) {
        try {
            wxReplyNewsService.insert(news);
            return Result.success("globals.result.success");

        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("wx.reply")
    public Object edit(@PathVariable(required = false) String id, HttpServletRequest req) {
        req.setAttribute("obj", wxReplyNewsService.fetch(id));
        return "pages/platform/wx/reply/news/edit";

    }

    @RequestMapping("/editDo")
    @SJson
    @RequiresPermissions("wx.reply.news.edit")
    @SLog(description = "修改回复图文")
    @AdaptBy(type = WhaleAdaptor.class)
    public Object editDo(Wx_reply_news news, HttpServletRequest req) {
        try {
            wxReplyNewsService.updateIgnoreNull(news);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/delete/{id}")
    @SJson
    @RequiresPermissions("wx.reply.news.delete")
    @SLog(description = "删除回复图文")
    public Object delete(@PathVariable(required = false) String id, HttpServletRequest req) {
        try {
            req.setAttribute("title", wxReplyNewsService.fetch(id).getTitle());
            wxReplyNewsService.delete(id);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/delete")
    @SJson
    @RequiresPermissions("wx.reply.news.delete")
    @SLog(description = "删除回复图文")
    public Object deletes(@RequestParam("ids") String id, HttpServletRequest req) {
        try {
            wxReplyNewsService.delete(StringUtils.split(id, ","));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("wx.reply")
    public Object data(DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        return wxReplyNewsService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }
}
