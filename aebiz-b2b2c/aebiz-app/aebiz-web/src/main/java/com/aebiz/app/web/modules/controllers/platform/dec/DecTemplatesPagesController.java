package com.aebiz.app.web.modules.controllers.platform.dec;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.dec.modules.models.Dec_templates_pages;
import com.aebiz.app.dec.modules.services.DecTemplatesPagesService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/dec/templates/pages")
public class DecTemplatesPagesController {
    private static final Log log = Logs.get();
    @Autowired
	private DecTemplatesPagesService decTemplatesPagesService;
    @RequestMapping("/pagesIndex/{id}")
    @RequiresPermissions("dec.templates.pages")
    public String pagesIndex(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("id",id);
        return "pages/platform/dec/templates/manager/TemplateManage";
    }
    @RequestMapping("/{id}")
    @RequiresPermissions("dec.templates.pages")
    public String s(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("id",id);
        return "pages/platform/dec/templates/pages/index";
    }

    @RequestMapping("/data/{id}")
    @SJson("full")
    @RequiresPermissions("dec.templates.pages")
    public Object data(@PathVariable String id,@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return decTemplatesPagesService.data(id,length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add/{id}")
    @RequiresPermissions("dec.templates.pages")
    public String add(@PathVariable String id,HttpServletRequest req) {
        req.setAttribute("templateUuid",id);
    	return "pages/platform/dec/templates/pages/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Dec_templates_pages")
    @RequiresPermissions("dec.templates.pages")
    public Object addDo(Dec_templates_pages decTemplatesPages, HttpServletRequest req) {
        try {
            boolean flag = decTemplatesPagesService.checkTemplatePageFileName(decTemplatesPages.getTemplateUuid(), decTemplatesPages.getPageFileName());
            if(!flag){
                decTemplatesPagesService.addPage(decTemplatesPages);
                return Result.success("globals.result.success");
            }else{
                return Result.error("该文件已存在");
            }
        }catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("dec.templates.pages")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", decTemplatesPagesService.fetch(id));
		return "pages/platform/dec/templates/pages/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Dec_templates_pages")
    @RequiresPermissions("dec.templates.pages")
    public Object editDo(Dec_templates_pages decTemplatesPages, HttpServletRequest req) {
		try {
            decTemplatesPages.setOpBy(StringUtil.getUid());
			decTemplatesPages.setOpAt((int) (System.currentTimeMillis() / 1000));
			decTemplatesPagesService.updateIgnoreNull(decTemplatesPages);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Dec_templates_pages")
    @RequiresPermissions("dec.templates.pages")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				decTemplatesPagesService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
                decTemplatesPagesService.deleteAllByPage(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("platform.dec.templates.pages")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", decTemplatesPagesService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/dec/templates/pages/detail";
    }
    //套用页面
    @RequestMapping("/setting/{id}")
    @RequiresPermissions("dec.templates.pages")
    public String setting(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", decTemplatesPagesService.fetch(id));
        return "pages/platform/dec/templates/pages/setting";
    }


    /**
     *套用模板页面
     */
    @RequestMapping("/adoptPage")
    public String adoptPage(@RequestParam String templateUuid,
                            @RequestParam String pageUuid, @RequestParam String pageName,
                            @RequestParam String pageType,
                            @RequestParam String pageFileName,
                            @RequestParam String description, HttpServletRequest request,
                            HttpServletResponse response)
            throws Exception {

        Dec_templates_pages m = new Dec_templates_pages();
        m.setTemplateUuid(templateUuid);
        m.setPageName(pageName);
        m.setPageType(Integer.parseInt(pageType));
        m.setPageFileName(pageFileName);
        m.setDescription(description);
        decTemplatesPagesService.saveAsNewPage(m, pageUuid);
        return "redirect:/platform/dec/templates/manager/pagesIndex?uuid=" + templateUuid;
    }

}
