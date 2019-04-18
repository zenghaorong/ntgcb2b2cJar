package com.aebiz.app.web.modules.controllers.platform.msg;

import com.aebiz.baseframework.base.Result;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.msg.modules.models.Msg_conf_email;
import com.aebiz.app.msg.modules.services.MsgConfEmailService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/platform/msg/conf/email")
public class MsgConfEmailController {
    @Autowired
	private MsgConfEmailService msgConfEmailService;

    @RequestMapping("")
	@RequiresAuthentication
    @RequiresPermissions("mall.msgconfig.sysset.add")
	public String index(HttpServletRequest req) {
        System.out.print("geteamilinfomation.....");
        String emailType = "1";
        Msg_conf_email email=msgConfEmailService.fetch(Cnd.NEW().limit(1,1));
        if(email==null){
            email=new Msg_conf_email();
            msgConfEmailService.insert(email);
            emailType = "0";
        }
        msgConfEmailService.clearCache();
        req.setAttribute("obj",email);
        req.setAttribute("emailType",emailType);
        return "pages/platform/msg/info/conf/email";
	}

    @RequestMapping("/save")
    @SJson("SJson")
    @RequiresPermissions("mall.msgconfig.sysset.add")
    public Object save(HttpServletRequest req,Msg_conf_email email) {
        try {
            System.out.print(email.getSmtpUrl()+"*************");
            email.setOpBy(StringUtil.getUid());
            email.setOpAt((int) (System.currentTimeMillis() / 1000));
            msgConfEmailService.update(email);
            msgConfEmailService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
