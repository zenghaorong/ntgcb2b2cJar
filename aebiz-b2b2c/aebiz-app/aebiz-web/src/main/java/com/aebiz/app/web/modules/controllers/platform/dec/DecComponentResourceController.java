package com.aebiz.app.web.modules.controllers.platform.dec;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.dec.modules.models.Dec_component_resource;
import com.aebiz.app.dec.modules.services.DecComponentResourceService;
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
@RequestMapping("/platform/dec/component/resource")
public class DecComponentResourceController {
    private static final Log log = Logs.get();
    @Autowired
	private DecComponentResourceService decComponentResourceService;

    @RequestMapping("")
    @RequiresPermissions("platform.dec.component.resource")
	public String index() {
		return "pages/platform/dec/component/resource/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("platform.dec.component.resource")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return decComponentResourceService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("platform.dec.component.resource")
    public String add() {
    	return "pages/platform/dec/component/resource/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Dec_component_resource")
    @RequiresPermissions("platform.dec.component.resource.add")
    public Object addDo(Dec_component_resource decComponentResource, HttpServletRequest req) {
		try {
			decComponentResourceService.insert(decComponentResource);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("platform.dec.component.resource")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", decComponentResourceService.fetch(id));
		return "pages/platform/dec/component/resource/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Dec_component_resource")
    @RequiresPermissions("platform.dec.component.resource.edit")
    public Object editDo(Dec_component_resource decComponentResource, HttpServletRequest req) {
		try {
            decComponentResource.setOpBy(StringUtil.getUid());
			decComponentResource.setOpAt((int) (System.currentTimeMillis() / 1000));
			decComponentResourceService.updateIgnoreNull(decComponentResource);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Dec_component_resource")
    @RequiresPermissions("platform.dec.component.resource.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				decComponentResourceService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				decComponentResourceService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("platform.dec.component.resource")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", decComponentResourceService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/dec/component/resource/detail";
    }

}
