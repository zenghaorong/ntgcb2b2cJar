package com.aebiz.app.web.modules.controllers.platform.dec;

import com.aebiz.app.dec.modules.services.DecTemplatesPagesService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.dec.modules.models.Dec_templates_sub;
import com.aebiz.app.dec.modules.services.DecTemplatesSubService;
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
@RequestMapping("/platform/dec/templates/sub")
public class DecTemplatesSubController {
    private static final Log log = Logs.get();
    @Autowired
	private DecTemplatesSubService decTemplatesSubService;
    @Autowired
    private DecTemplatesPagesService decTemplatesPagesService;

    @RequestMapping("/{id}")
    @RequiresPermissions("dec.templates.sub")
	public String index(@PathVariable String id,HttpServletRequest req) {
        //根据页面id获取模板id
        String templateUuid=decTemplatesPagesService.fetch(id).getTemplateUuid();
        req.setAttribute("pageUuid",id);
        req.setAttribute("templateUuid",templateUuid);
		return "pages/platform/dec/templates/sub/index";
	}

	@RequestMapping("/data/{id}")
    @SJson("full")
    @RequiresPermissions("dec.templates.sub")
    public Object data(@PathVariable String id,@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return decTemplatesSubService.data(id,length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("dec.templates.sub")
    public String add() {
    	return "pages/platform/dec/templates/sub/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Dec_templates_sub")
    @RequiresPermissions("dec.templates.sub")
    public Object addDo(Dec_templates_sub decTemplatesSub, HttpServletRequest req) {
		try {
			decTemplatesSubService.insert(decTemplatesSub);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("dec.templates.sub")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", decTemplatesSubService.fetch(id));
		return "pages/platform/dec/templates/sub/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Dec_templates_sub")
    @RequiresPermissions("dec.templates.sub")
    public Object editDo(Dec_templates_sub decTemplatesSub, HttpServletRequest req) {
		try {
            decTemplatesSub.setOpBy(StringUtil.getUid());
			decTemplatesSub.setOpAt((int) (System.currentTimeMillis() / 1000));
			decTemplatesSubService.updateIgnoreNull(decTemplatesSub);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Dec_templates_sub")
    @RequiresPermissions("dec.templates.sub")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				decTemplatesSubService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				decTemplatesSubService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("dec.templates.sub")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", decTemplatesSubService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/dec/templates/sub/detail";
    }

}
