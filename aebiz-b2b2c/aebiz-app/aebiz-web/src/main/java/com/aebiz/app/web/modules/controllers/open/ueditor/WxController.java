package com.aebiz.app.web.modules.controllers.open.ueditor;

import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.app.wx.modules.services.WxConfigService;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.DateUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by Wizzer on 2016/7/9.
 */
@Controller
@RequestMapping("/open/ueditor/wx")
public class WxController {
    private static final Log log = Logs.get();
    @Autowired
    private WxConfigService wxConfigService;
    @Autowired
    private PropertiesProxy config;


    @RequestMapping("/index")
    @SJson
    public Object index(@RequestParam(value = "action", required = false) String action, HttpServletRequest req) {
        return Json.fromJson(Files.read(Globals.APP_ROOT + "/assets/platform/vendor/ueditor/nutz/configWx.json").replace("$base", Globals.APP_BASE));
    }

    @RequestMapping(value = "/uploadimage", method = RequestMethod.POST)
    @SJson
    @RequiresAuthentication
    public Object uploadimage(@RequestParam("Filedata") MultipartFile tf, HttpServletRequest req) {
        String wxid = Strings.sBlank(req.getSession(true).getAttribute("wxid"));
        NutMap nutMap = new NutMap();
        if (!tf.isEmpty()) {
            List<String> imageList = config.getList("upload.suffix.image", ",");
            if (imageList.contains(tf.getOriginalFilename().substring(tf.getOriginalFilename().indexOf(".") + 1))) {
                try {
                    WxApi2 wxApi2 = wxConfigService.getWxApi2(wxid);
                    String p = Globals.APP_ROOT;
                    String f = Globals.APP_UPLOAD_PATH + "/image/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + R.UU32() + tf.getOriginalFilename().substring(tf.getOriginalFilename().indexOf("."));
                    File file = Files.createFileIfNoExists(p + f);
                    tf.transferTo(file);
                    WxResp resp = wxApi2.uploadimg(file);
                    if (resp.errcode() != 0) {
                        nutMap.addv("state", "FAIL");
                        return nutMap;
                    }
                    nutMap.addv("state", "SUCCESS");
                    nutMap.addv("url", resp.get("url"));
                    nutMap.addv("original", tf.getOriginalFilename());
                    nutMap.addv("type", tf.getOriginalFilename().substring(tf.getOriginalFilename().indexOf(".") + 1));
                    nutMap.addv("size", tf.getSize());
                    return nutMap;
                } catch (Exception e) {
                    e.printStackTrace();
                    return nutMap.addv("state", "FAIL");
                }
            } else {
                return nutMap.addv("state", "FAIL");
            }
        }
        return nutMap.addv("state", "FAIL");

    }
}
