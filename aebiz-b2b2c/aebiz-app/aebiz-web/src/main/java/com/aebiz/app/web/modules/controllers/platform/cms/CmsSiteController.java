package com.aebiz.app.web.modules.controllers.platform.cms;

import com.aebiz.app.cms.modules.models.Cms_site;
import com.aebiz.app.cms.modules.services.CmsSiteService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/platform/cms/site")
public class CmsSiteController{
    @Autowired
    private CmsSiteService cmsSiteService;

    @RequestMapping("")
    @RequiresPermissions("cms.site.settings")
    public String index(HttpServletRequest req) {
        Cms_site site = cmsSiteService.fetch("site");
        if (site == null) {
            site = new Cms_site();
            site.setId("site");
            cmsSiteService.insert(site);
            cmsSiteService.clearCache();
        }
        req.setAttribute("obj", site);
        return "pages/platform/cms/site/index";
    }

    @RequestMapping("/editDo")
    @SJson
    @RequiresPermissions("cms.site.settings.save")
    @SLog(description = "修改站点")
    public Object editDo(Cms_site site) {
        try {
            cmsSiteService.updateIgnoreNull(site);
            cmsSiteService.clearCache();
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
