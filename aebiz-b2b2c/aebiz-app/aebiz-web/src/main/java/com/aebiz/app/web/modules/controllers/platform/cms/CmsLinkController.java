package com.aebiz.app.web.modules.controllers.platform.cms;

import com.aebiz.app.cms.modules.models.Cms_link;
import com.aebiz.app.cms.modules.models.Cms_link_class;
import com.aebiz.app.cms.modules.services.CmsLinkClassService;
import com.aebiz.app.cms.modules.services.CmsLinkService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/platform/cms/link/link")
public class CmsLinkController {
    @Autowired
    private CmsLinkService cmsLinkService;
    @Autowired
    private CmsLinkClassService cmsLinkClassService;

    @RequestMapping({"", "/index/", "/index/{classId}"})
    @RequiresPermissions("cms.link.link")
    public String index(@PathVariable(required = false) String classId, HttpServletRequest req) {
        List<Cms_link_class> list = cmsLinkClassService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(classId)) {
            classId = list.get(0).getId();
        }
        req.setAttribute("list", list);
        req.setAttribute("classId", Strings.sBlank(classId));
        return "pages/platform/cms/link/link/index";
    }

    @RequestMapping({"/data", "/data/{classId}"})
    @SJson("full")
    @RequiresPermissions("cms.link.link")
    public Object data(@PathVariable(required = false) String classId, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(classId)) {
            cnd.and("classId", "=", classId);
        }
        return cmsLinkService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping({"/add", "/add/{classId}"})
    @RequiresPermissions("cms.link.link")
    public String add(@PathVariable(required = false) String classId, HttpServletRequest req) {
        List<Cms_link_class> list = cmsLinkClassService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(classId)) {
            classId = list.get(0).getId();
        }
        req.setAttribute("classId", Strings.isBlank(classId));
        req.setAttribute("list", list);
        return "pages/platform/cms/link/link/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Cms_link")
    @RequiresPermissions("cms.link.link.add")
    public Object addDo(Cms_link cmsLink, HttpServletRequest req) {
        try {
            cmsLinkService.insert(cmsLink);
            cmsLinkClassService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("cms.link.link")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        List<Cms_link_class> list = cmsLinkClassService.query(Cnd.NEW());
        Cms_link cms_link = cmsLinkService.fetch(id);
        req.setAttribute("obj", cms_link);
        req.setAttribute("classId", cms_link.getClassId());
        req.setAttribute("list", list);
        return "pages/platform/cms/link/link/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Cms_link")
    @RequiresPermissions("cms.link.link.edit")
    public Object editDo(Cms_link cmsLink, HttpServletRequest req) {
        try {
            cmsLink.setOpBy(StringUtil.getUid());
            cmsLink.setOpAt((int) (System.currentTimeMillis() / 1000));
            cmsLinkService.updateIgnoreNull(cmsLink);
            cmsLinkClassService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Cms_link")
    @RequiresPermissions("cms.link.link.delete")
    public Object delete(@PathVariable(required = false) String id,
                         @RequestParam(value = "ids", required = false) String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                // ids转化为字符串存进数组 作用：区分单删还是多选
                String[] ssids = ids[0].split(",");
                for (int i = 0; i < ssids.length; i++) {
                    cmsLinkService.delete(ssids[i]);
                }
            } else {
                cmsLinkService.delete(id);
            }
            cmsLinkClassService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
