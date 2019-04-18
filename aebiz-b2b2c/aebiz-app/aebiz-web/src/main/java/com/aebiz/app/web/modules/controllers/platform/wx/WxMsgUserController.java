package com.aebiz.app.web.modules.controllers.platform.wx;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.app.wx.modules.models.Wx_config;
import com.aebiz.app.wx.modules.models.Wx_msg;
import com.aebiz.app.wx.modules.models.Wx_msg_reply;
import com.aebiz.app.wx.modules.services.WxConfigService;
import com.aebiz.app.wx.modules.services.WxMsgReplyService;
import com.aebiz.app.wx.modules.services.WxMsgService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by gaoen 2017-2-16 10:59:47
 */

@Controller
@RequestMapping("/platform/wx/msg/user")
public class WxMsgUserController {
    private static final Log log = Logs.get();
    @Autowired
    WxMsgService wxMsgService;
    @Autowired
    WxConfigService wxConfigService;
    @Autowired
    WxMsgReplyService wxMsgReplyService;

    @RequestMapping(value = {"", "/{wxid}"})
    @RequiresPermissions("wx.user.list")
    public String index(@PathVariable(required = false) String wxid, HttpServletRequest req) {
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxid = list.get(0).getId();
        }
        req.setAttribute("wxid", wxid);
        req.setAttribute("wxList", list);
        return "pages/platform/wx/msg/user/index";
    }

    @RequestMapping(value = {"/data", "/data/{wxid}"})
    @SJson("full")
    @RequiresPermissions("wx.user.list")
    public Object data(@PathVariable(required = false) String wxid, @RequestParam("nickname") String nickname,
                       @RequestParam("content") String content, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        if (!Strings.isBlank(nickname)) {
            cnd.and("nickname", "like", "%" + nickname + "%");
        }
        if (!Strings.isBlank(content)) {
            cnd.and("content", "like", "%" + content + "%");
        }
        return wxMsgService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping(value = {"/reply", "/reply/{id}"})
    @RequiresPermissions("wx.user.list")
    public Object reply(@PathVariable(required = false) String id, @RequestParam("type") int type, HttpServletRequest req) {
        Wx_msg msg = wxMsgService.fetch(id);
        req.setAttribute("wxid", msg.getWxid());
        req.setAttribute("type", type);
        req.setAttribute("obj", msg);
        return "pages/platform/wx/msg/user/reply";
    }


    @RequestMapping("/replyData/{wxid}")
    @SJson("full")
    @RequiresPermissions("wx.user.list")
    public Object replyData(@PathVariable(required = false) String wxid, @RequestParam("openid") String openid, DataTable dataTable,
                            HttpServletRequest request) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        if (!Strings.isBlank(openid)) {
            cnd.and("openid", "=", openid);
        }
        cnd.desc("opAt");
        return wxMsgService.data(5, dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, "reply");
    }


    @RequestMapping("/replyDo")
    @SJson
    @RequiresPermissions("wx.user.list.sync")
    @SLog(description = "回复微信")
    public Object down(@RequestParam("wxid") String wxid,
                       @RequestParam("id") String id,
                       @RequestParam("nickname") String nickname,
                       @RequestParam("openid") String openid,
                       @RequestParam("content") String content,
                       HttpServletRequest req) {
        try {
            Wx_config config = wxConfigService.fetch(wxid);
            WxApi2 wxApi2 = wxConfigService.getWxApi2(wxid);
            long now = System.currentTimeMillis() / 1000;
            WxOutMsg msg = new WxOutMsg();
            msg.setCreateTime(now);
            msg.setFromUserName(config.getAppid());
            msg.setMsgType("text");
            msg.setToUserName(openid);
            msg.setContent(content);
            WxResp wxResp = wxApi2.send(msg);
            if (wxResp.errcode() != 0) {
                return Result.error(wxResp.errmsg());
            }
            Wx_msg_reply reply = new Wx_msg_reply();
            reply.setContent(content);
            reply.setType("text");
            reply.setMsgid(id);
            reply.setOpenid(openid);
            reply.setWxid(wxid);
            Wx_msg_reply reply1 = wxMsgReplyService.insert(reply);
            if (reply1 != null) {
                wxMsgService.update(org.nutz.dao.Chain.make("replyId", reply1.getId()), Cnd.where("id", "=", id));
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
