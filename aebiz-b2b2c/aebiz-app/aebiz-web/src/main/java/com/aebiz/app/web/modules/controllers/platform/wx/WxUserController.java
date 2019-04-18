package com.aebiz.app.web.modules.controllers.platform.wx;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.app.wx.modules.models.Wx_config;
import com.aebiz.app.wx.modules.services.WxConfigService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.app.wx.modules.models.Wx_user;
import com.aebiz.app.wx.modules.services.WxUserService;
import com.aebiz.baseframework.view.annotation.SJson;
import com.vdurmont.emoji.EmojiParser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.lang.*;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/platform/wx/user")
@RequiresPermissions("wx.user.list")
public class WxUserController {
    private static final Log log = Logs.get();
    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private WxConfigService wxConfigService;


    @RequestMapping(value = {"/index", "/index/{wxid}"})
    @RequiresPermissions("wx.user.list")
    public String index(@PathVariable(required = false) String wxid, HttpServletRequest req) {
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxid = list.get(0).getId();
        }
        req.setAttribute("wxid", wxid);
        req.setAttribute("wxList", list);
        return "pages/platform/wx/user/index";

    }

    @RequestMapping(value = {"/data", "/data/{wxid}"})
    @SJson("full")
    @RequiresPermissions("wx.user.list")
    public Object data(@PathVariable(required = false) String wxid,
                       @RequestParam("nickname") String nickname, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        if (!Strings.isBlank(nickname)) {
            cnd.and("nickname", "like", "%" + nickname + "%");
        }
        return wxUserService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping(value = {"/down", "/down/{wxid}"})
    @SJson
    @RequiresPermissions("wx.user.list.sync")
    @SLog(description = "同步微信会员")
    public Object down(@PathVariable(required = false) String wxid, HttpServletRequest req) {
        try {
            Wx_config config = wxConfigService.fetch(wxid);
            req.setAttribute("appname", config.getAppname());
            WxApi2 wxApi2 = wxConfigService.getWxApi2(wxid);
            wxApi2.user_get(new Each<String>() {
                public void invoke(int index, String _ele, int length)
                        throws ExitLoop, ContinueLoop, LoopException {
                    WxResp resp = wxApi2.user_info(_ele, "zh_CN");
                    log.info(Json.toJson(resp));
                    log.debug(index
                            + " : "
                            + _ele
                            + ", nickname: "
                            + resp.user().getNickname());
                    Wx_user usr = Json.fromJson(Wx_user.class, Json.toJson(resp.user()));
                    usr.setOpAt((int) (System.currentTimeMillis() / 1000));
                    usr.setNickname(EmojiParser.parseToAliases(usr.getNickname(), EmojiParser.FitzpatrickAction.REMOVE));
                    usr.setSubscribeAt((int) (resp.user().getSubscribe_time()));
                    usr.setWxid(wxid);
                    if (wxUserService.fetch(Cnd.where("wxid", "=", wxid).and("openid", "=", usr.getOpenid())) == null)
                        wxUserService.insert(usr);
                }
            });
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }


}