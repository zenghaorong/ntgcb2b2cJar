package com.aebiz.app.web.modules.controllers.platform.cms;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.cms.modules.models.Cms_navigation;
import com.aebiz.app.cms.modules.services.CmsNavigationService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/cms/navigation")
public class CmsNavigationController {
    private static final Log log = Logs.get();
    @Autowired
	private CmsNavigationService cmsNavigationService;

    @RequestMapping("")
    @RequiresPermissions("cms.content.navigation")
	public String index() {
		return "pages/platform/cms/navigation/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("cms.content.navigation")
    public Object data(DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
    	return cmsNavigationService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("cms.content.navigation")
    public String add() {
    	return "pages/platform/cms/navigation/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Cms_navigation")
    @RequiresPermissions("cms.content.navigation.add")
    public Object addDo(@RequestParam String at,Cms_navigation cmsNavigation, HttpServletRequest req) {
		try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int star= (int) (sdf.parse(at).getTime() / 1000);
            cmsNavigation.setCreateAt(star);
			cmsNavigationService.insert(cmsNavigation);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("cms.content.navigation")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", cmsNavigationService.fetch(id));
		return "pages/platform/cms/navigation/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Cms_navigation")
    @RequiresPermissions("cms.content.navigation.edit")
    public Object editDo(@RequestParam String at,Cms_navigation cmsNavigation, HttpServletRequest req) {
		try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int star= (int) (sdf.parse(at).getTime() / 1000);
            cmsNavigation.setCreateAt(star);
            cmsNavigation.setOpBy(StringUtil.getUid());
			cmsNavigation.setOpAt((int) (System.currentTimeMillis() / 1000));
			cmsNavigationService.updateIgnoreNull(cmsNavigation);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Cms_navigation")
    @RequiresPermissions("cms.content.navigation.del")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				cmsNavigationService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				cmsNavigationService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("cms.content.navigation")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", cmsNavigationService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/cms/navigation/detail";
    }

}
