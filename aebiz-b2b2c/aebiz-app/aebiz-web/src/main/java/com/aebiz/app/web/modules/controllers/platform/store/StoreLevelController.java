package com.aebiz.app.web.modules.controllers.platform.store;

import com.aebiz.app.store.modules.models.Store_level;
import com.aebiz.app.store.modules.services.StoreLevelService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/store/config/level")
public class StoreLevelController {

    @Autowired
	private StoreLevelService storeLevelService;

    @RequestMapping("")
	@RequiresPermissions("store.config.level")
	public String index() {
		return "pages/platform/store/config/level/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("store.config.level")
    public Object data(@RequestParam(value = "name", required = false) String name, DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(name)) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(name) + "%"));
        }
    	return storeLevelService.data(dataTable.getLength(), dataTable.getStart(),
                dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("store.config.level")
    public String add() {
    	return "pages/platform/store/config/level/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Store_level")
    @RequiresPermissions("store.config.level.add")
    public Object addDo(Store_level storeLevel, HttpServletRequest req) {
		try {
			storeLevelService.insert(storeLevel);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.config.level")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", storeLevelService.fetch(id));
		return "pages/platform/store/config/level/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Store_level")
    @RequiresPermissions("store.config.level.edit")
    public Object editDo(Store_level storeLevel, HttpServletRequest req) {
		try {
            storeLevel.setOpBy(StringUtil.getUid());
			storeLevel.setOpAt((int) (System.currentTimeMillis() / 1000));
			storeLevelService.updateIgnoreNull(storeLevel);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Store_level")
    @RequiresPermissions("store.config.level.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				storeLevelService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				storeLevelService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }


}
