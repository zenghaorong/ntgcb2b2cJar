package com.aebiz.app.web.modules.controllers.platform.dec;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.dec.modules.models.Dec_templates_resource;
import com.aebiz.app.dec.modules.services.DecTemplatesResourceService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/dec/templates/resource")
public class DecTemplatesResourceController {
    private static final Log log = Logs.get();
    @Autowired
	private DecTemplatesResourceService decTemplatesResourceService;

    @RequestMapping("")
    @RequiresPermissions("dec.templates.resource")
	public String index() {
		return "pages/platform/dec/templates/resource/index";
	}

	@RequestMapping("/data/{folderUuid}")
    @SJson("full")
    @RequiresPermissions("dec.templates.resource")
    public Object data(@PathVariable String folderUuid,@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return decTemplatesResourceService.getData(folderUuid,length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("dec.templates.resource")
    public String add() {
    	return "pages/platform/dec/templates/resource/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Dec_templates_resource")
    @RequiresPermissions("dec.templates.resource")
    public Object addDo(Dec_templates_resource decTemplatesResource, HttpServletRequest req) {
		try {
			decTemplatesResourceService.insert(decTemplatesResource);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("dec.templates.resource")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", decTemplatesResourceService.fetch(id));
		return "pages/platform/dec/templates/resource/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Dec_templates_resource")
    @RequiresPermissions("dec.templates.resource")
    public Object editDo(Dec_templates_resource decTemplatesResource, HttpServletRequest req) {
		try {
            decTemplatesResource.setOpBy(StringUtil.getUid());
			decTemplatesResource.setOpAt((int) (System.currentTimeMillis() / 1000));
			decTemplatesResourceService.updateIgnoreNull(decTemplatesResource);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Dec_templates_resource")
    @RequiresPermissions("dec.templates.resource")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				decTemplatesResourceService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				decTemplatesResourceService.deleteByResouceId(id);

    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("dec.templates.resource")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", decTemplatesResourceService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/dec/templates/resource/detail";
    }
}
