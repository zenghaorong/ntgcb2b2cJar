package com.aebiz.app.web.modules.controllers.platform.cms;

import com.aebiz.app.cms.modules.models.Cms_link_class;
import com.aebiz.app.cms.modules.services.CmsLinkClassService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/platform/cms/link/class")
public class CmsLinkClassController {
    @Autowired
	private CmsLinkClassService cmsLinkClassService;

    @RequestMapping("")
	@RequiresPermissions("cms.link.class")
	public String index() {
		return "pages/platform/cms/link/class/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("cms.link.class")
    public Object data(DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
    	return cmsLinkClassService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("cms.link.class")
    public String add() {
    	return "pages/platform/cms/link/class/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "添加分类")
    @RequiresPermissions("cms.link.class.add")
    public Object addDo(Cms_link_class cmsLinkClass, HttpServletRequest req) {
		try {
			cmsLinkClassService.insert(cmsLinkClass);
            cmsLinkClassService.clearCache();
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("cms.link.class")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", cmsLinkClassService.fetch(id));
		return "pages/platform/cms/link/class/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "编辑分类")
    @RequiresPermissions("cms.link.class.edit")
    public Object editDo(Cms_link_class cmsLinkClass, HttpServletRequest req) {
		try {
            cmsLinkClass.setOpBy(StringUtil.getUid());
			cmsLinkClass.setOpAt((int) (System.currentTimeMillis() / 1000));
			cmsLinkClassService.updateIgnoreNull(cmsLinkClass);
            cmsLinkClassService.clearCache();
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "删除分类")
    @RequiresPermissions("cms.link.class.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				//ids转化为字符串存进数组 作用：区分单删还是多选
				String[] ssids=ids[0].split(",");
				for (int i = 0; i < ssids.length; i++) {
					cmsLinkClassService.delete(ssids[i]);
				}
			}else{
				cmsLinkClassService.delete(id);
			}
            cmsLinkClassService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
