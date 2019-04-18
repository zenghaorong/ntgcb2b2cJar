package com.aebiz.app.web.modules.controllers.platform.dec;

import com.aebiz.app.dec.modules.models.Dec_templates_manager;
import com.aebiz.app.dec.modules.services.DecTemplatesManagerService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.dec.modules.models.Dec_templates_style;
import com.aebiz.app.dec.modules.services.DecTemplatesStyleService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/platform/dec/templates/style")
public class DecTemplatesStyleController {
    private static final Log log = Logs.get();
    @Autowired
	private DecTemplatesStyleService decTemplatesStyleService;

    @Autowired
    private DecTemplatesManagerService decTemplatesManagerService;
    @RequestMapping("/{versionType}")
    @RequiresPermissions("dec.style")
	public String index(@PathVariable String versionType,HttpServletRequest req) {
        req.setAttribute("versionType",versionType);
		return "pages/platform/dec/templates/style/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("dec.style")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return decTemplatesStyleService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("dec.style")
    public String add(HttpServletRequest req) {
        req.setAttribute("versionType",req.getParameter("versionType"));
        Cnd cnd=Cnd.NEW();
        List<Dec_templates_manager> list=(List<Dec_templates_manager>) decTemplatesManagerService.query(cnd.where(null));
        req.setAttribute("list",list);
        return "pages/platform/dec/templates/style/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Dec_templates_style")
    @RequiresPermissions("dec.style.add")
    public Object addDo(Dec_templates_style decTemplatesStyle, HttpServletRequest req) {
		try {
			decTemplatesStyleService.insert(decTemplatesStyle);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("dec.style")
    public String edit(@PathVariable String id,HttpServletRequest req) {
        req.setAttribute("versionType",req.getParameter("versionType"));
        Cnd cnd=Cnd.NEW();
        List<Dec_templates_manager> list=(List<Dec_templates_manager>) decTemplatesManagerService.query(cnd.where(null));
        req.setAttribute("list",list);
		req.setAttribute("obj", decTemplatesStyleService.fetch(id));
		return "pages/platform/dec/templates/style/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Dec_templates_style")
    @RequiresPermissions("dec.style.edit")
    public Object editDo(Dec_templates_style decTemplatesStyle, HttpServletRequest req) {
		try {
            decTemplatesStyle.setOpBy(StringUtil.getUid());
			decTemplatesStyle.setOpAt((int) (System.currentTimeMillis() / 1000));
			decTemplatesStyleService.updateIgnoreNull(decTemplatesStyle);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Dec_templates_style")
    @RequiresPermissions("dec.style")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				decTemplatesStyleService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				decTemplatesStyleService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("dec.style")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            Dec_templates_style style=decTemplatesStyleService.fetch(id);
            Dec_templates_manager manager=decTemplatesManagerService.fetch(style.getTemplateUuid());
            style.setTemplateName(manager.getTemplateZhName());
            req.setAttribute("obj", style);
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/dec/templates/style/detail";
    }

    /**
     * 当前模板的皮肤列表
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/toChooseList/{id}")
    @RequiresPermissions("dec.style")
    public String toChooseList(@PathVariable String id,
                               HttpServletRequest request) {
        Cnd cnd=Cnd.NEW();
        List<Dec_templates_style> templateSkinList = decTemplatesStyleService.query(cnd.where("templateUuid","=",id));
        request.setAttribute("id",id);
       request.setAttribute("list",templateSkinList);
        return "pages/platform/dec/templates/style/styleIndex";
    }


    @RequestMapping("/getData/{id}")
    @SJson("full")
    @RequiresPermissions("dec.style")
    public Object getData(@PathVariable String id,@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return decTemplatesStyleService.getData(id,length, start, draw, order, columns, cnd, null);
    }

    /**
     *为该模板启用这个皮肤。该模板下其他皮肤为禁用
     *
     */
    @RequestMapping("/enable/{id}")
    @SJson
    public Object enable(@PathVariable String id){
        try {
            Dec_templates_style m=decTemplatesStyleService.fetch(id);
            String templateUuid=m.getTemplateUuid();
            List<Dec_templates_style> styleList=decTemplatesStyleService.query(Cnd.where("templateUuid","=",templateUuid));
            if(styleList !=null && styleList.size()>0){
                for(int i=0;i<styleList.size();i++){
                    styleList.get(i).setDisabled(false);
                    decTemplatesStyleService.update(styleList.get(i));
                }
            }
            m.setDisabled(true);
            m.setOpBy(StringUtil.getUid());
            m.setOpAt((int) (System.currentTimeMillis() / 1000));
            decTemplatesStyleService.update(m);
            return Result.success("globals.result.success");
        }catch (Exception e){
            return Result.error("globals.result.error");
        }
    }
}
