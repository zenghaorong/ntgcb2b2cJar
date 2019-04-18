package com.aebiz.app.web.modules.controllers.platform.wx;

import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.app.wx.modules.models.Wx_config;
import com.aebiz.app.wx.modules.models.Wx_mass;
import com.aebiz.app.wx.modules.models.Wx_mass_news;
import com.aebiz.app.wx.modules.models.Wx_mass_send;
import com.aebiz.app.wx.modules.services.WxConfigService;
import com.aebiz.app.wx.modules.services.WxMassNewsService;
import com.aebiz.app.wx.modules.services.WxMassSendService;
import com.aebiz.app.wx.modules.services.WxMassService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.bean.WxMassArticle;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by gaoen 2017-2-16 15:02:37
 */

@Controller
@RequestMapping("/platform/wx/msg/mass")
public class WxMassController {
    private static final Log log = Logs.get();
    @Autowired
    WxMassService wxMassService;
    @Autowired
    WxMassSendService wxMassSendService;
    @Autowired
    WxMassNewsService wxMassNewsService;
    @Autowired
    WxConfigService wxConfigService;
    @Autowired
    private PropertiesProxy config;


    @RequestMapping(value = {"", "/{wxid}"})
    @RequiresPermissions("wx.msg.mass")
    public String index(@PathVariable(required = false) String wxid, HttpServletRequest req) {
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxid = list.get(0).getId();
        }

        req.setAttribute("wxid2", wxid);
        req.setAttribute("wxList", list);
        return "pages/platform/wx/msg/mass/index";

    }

    @RequestMapping(value = {"/massData", "/massData/{wxid}"})
    @SJson("full")
    @RequiresPermissions("wx.msg.mass")
    public Object massData(@PathVariable(required = false) String wxid, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }

        return wxMassService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping(value = {"/news", "/news/{wxid}"})
    @RequiresPermissions("wx.msg.mass")
    public String news(@PathVariable(required = false) String wxid, HttpServletRequest req) {
        req.setAttribute("wxid", wxid);
        return "pages/platform/wx/msg/mass/news";
    }

    @RequestMapping(value = {"/newsData", "/newsData/{wxid}"})
    @SJson("full")
    @RequiresPermissions("wx.msg.mass")
    public Object newsData(@PathVariable(required = false) String wxid,DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        return wxMassNewsService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }


    @RequestMapping(value = {"/deleteNews", "/deleteNews/{wxid}"})
    @SJson
    @RequiresPermissions("wx.msg.mass.delNews")
    @SLog(description  = "删除图文")
    public Object deleteNews(@PathVariable(required = false) String id, HttpServletRequest req) {
        try {
            req.setAttribute("title", wxMassNewsService.fetch(id).getTitle());
            wxMassNewsService.delete(id);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/addNews", "/addNews/{wxid}"})
    @RequiresPermissions("wx.msg.mass")
    public String add(@PathVariable(required = false) String wxid, HttpServletRequest req) {
        req.setAttribute("wxid", wxid);
        req.getSession().setAttribute("wxid", wxid);
        return "pages/platform/wx/msg/mass/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @RequiresPermissions("wx.msg.mass.addNews")
    @SLog(description  = "添加图文")
    public Object addDo(Wx_mass_news news, HttpServletRequest req) {
        try {
            wxMassNewsService.insert(news);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @RequestMapping(value = {"/newsDetail", "/newsDetail/{id}"})
    @RequiresPermissions("wx.msg.mass")
    public Object newsDetail(@PathVariable(required = false) String id, HttpServletRequest req) {
        req.setAttribute("obj", wxMassNewsService.fetch(id));
        return "pages/platform/wx/msg/mass/detail";
    }


    @RequestMapping(value = "/uploadThumb/{wxid}" , method = RequestMethod.POST)
    @SJson
    @RequiresPermissions("wx.msg.mass")
    public Object uploadThumb(@PathVariable(required = false) String wxid,
                              @RequestParam("Filedata") MultipartFile tf,
                              HttpServletRequest req) {
        try {
            if (tf == null) {
                return Result.error("空文件");
            } else {
                String p = Globals.APP_ROOT;
                String f = Globals.APP_UPLOAD_PATH + "/image/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + R.UU32() + tf.getOriginalFilename().substring(tf.getOriginalFilename().indexOf("."));
                File file = Files.createFileIfNoExists(p + f);
                tf.transferTo(file);
                WxApi2 wxApi2 = wxConfigService.getWxApi2(wxid);
                WxResp resp = wxApi2.media_upload("thumb", file);
                if (resp.errcode() != 0) {
                    return Result.error(resp.errmsg());
                }
                return Result.success("上传成功", resp.get("thumb_media_id"));
            }
        } catch (Exception e) {
            return Result.error("系统错误");
        } catch (Throwable e) {
            return Result.error("图片格式错误");
        }
    }


    @RequestMapping(value = {"/send", "/send/{wxid}"})
    @RequiresPermissions("wx.msg.mass")
    public String send(@PathVariable(required = false) String wxid, HttpServletRequest req) {
        req.setAttribute("wxid", wxid);
        return "pages/platform/wx/msg/mass/send";
    }

    @RequestMapping(value = {"/select", "/select/{wxid}"})
    @RequiresPermissions("wx.msg.mass")
    public String select(@PathVariable(required = false) String wxid, HttpServletRequest req) {
        req.setAttribute("wxid", wxid);
        return "pages/platform/wx/msg/mass/select";
    }

    @RequestMapping("/sendDo")
    @SJson
    @RequiresPermissions("wx.msg.mass.pushNews")
    @SLog(description = "群发消息")
    public Object sendDo(Wx_mass mass, @RequestParam("content") String content, @RequestParam("openids") String openids, HttpServletRequest req) {
        try {
            WxApi2 wxApi2 = wxConfigService.getWxApi2(mass.getWxid());
            WxOutMsg outMsg = new WxOutMsg();
            if ("news".equals(mass.getType())) {
                String[] ids = StringUtils.split(content, ",");
                int i = 0;
                for (String id : ids) {
                    wxMassNewsService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", id));
                    i++;
                }
                List<Wx_mass_news> newsList = wxMassNewsService.query(Cnd.where("id", "in", ids).asc("location"));
                List<WxMassArticle> articles = Json.fromJsonAsList(WxMassArticle.class, Json.toJson(newsList));
                WxResp resp = wxApi2.uploadnews(articles);
                String media_id = resp.media_id();
                mass.setMedia_id(media_id);
                outMsg.setMedia_id(media_id);
                outMsg.setMsgType("mpnews");
            }
            if ("text".equals(mass.getType())) {
                outMsg.setContent(content);
                outMsg.setMsgType("text");
            }
            WxResp resp;
            if ("all".equals(mass.getScope())) {
                resp = wxApi2.mass_sendall(true, null, outMsg);
            } else {
                String[] ids = StringUtils.split(openids, ",");
                resp = wxApi2.mass_send(Arrays.asList(ids), outMsg);
            }
            mass.setStatus(resp.errcode() == 0 ? 1 : 2);
            Wx_mass wxMass = wxMassService.insert(mass);
            Wx_mass_send send = new Wx_mass_send();
            send.setWxid(wxMass.getWxid());
            send.setMassId(wxMass.getId());
            send.setErrCode(String.valueOf(resp.errcode()));
            send.setMsgId(resp.getString("msg_id"));
            if (!"all".equals(mass.getScope())) {
                send.setReceivers(content);
            }
            send.setErrMsg(resp.getString("errmsg"));
            send.setStatus(resp.errcode() == 0 ? 1 : 2);
            wxMassSendService.insert(send);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("globals.result.error");
        } catch (Throwable e) {
            e.printStackTrace();
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value ={"/sendDetail", "/sendDetail/{id}"} )
    @RequiresPermissions("wx.msg.mass")
    public String sendDetail(@PathVariable(required = false) String id, HttpServletRequest req) {
        Wx_mass mass = wxMassService.fetch(id);
        if ("news".equals(mass.getType())) {
            String[] ids = StringUtils.split(mass.getContent(), ",");
            req.setAttribute("news", wxMassNewsService.query(Cnd.where("id", "in", ids).asc("location")));
        }

        req.setAttribute("send", wxMassSendService.fetch(Cnd.where("massId", "=", mass.getId())));
        req.setAttribute("obj", mass);
        return "pages/platform/wx/msg/mass/sendDetail";
    }
}
