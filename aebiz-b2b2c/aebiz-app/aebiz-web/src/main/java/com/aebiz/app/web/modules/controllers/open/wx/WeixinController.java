package com.aebiz.app.web.modules.controllers.open.wx;


import com.aebiz.app.wx.modules.services.WxConfigService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.web.session.HttpServletSession;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.util.Wxs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by gaoen on 2017-2-21
 */
@Controller
@RequestMapping("/open/weixin")
public class WeixinController {
    private static final Log log = Logs.get();
    @Autowired
    WxConfigService wxConfigService;
    @Autowired
    WxHandler wxHandler;

    public WeixinController() {
        if (log.isDebugEnabled())
            Wxs.enableDevMode(); // 开启debug模式,这样就会把接收和发送的内容统统打印,方便查看
    }

    @RequestMapping(value = {"/api", "/api/{key}"})
    public void msgIn(@PathVariable(required = true) String key, HttpServletRequest request, HttpServletResponse response) throws Throwable {
        Wxs.handle(wxHandler, request, key).render(request, response, null);
    }

}
