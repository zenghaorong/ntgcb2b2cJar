package com.aebiz.app.web.modules.controllers.platform.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.aebiz.app.sys.modules.models.Sys_unit;
import com.aebiz.app.sys.modules.services.SysUnitService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.commons.utils.StringUtil;

@Controller
@RequestMapping("/platform/sys/unit")
public class SysUnitController {
    @Autowired
    private SysUnitService sysUnitService;

	@RequestMapping("")
	@RequiresPermissions("sys.manager.unit")
	public String index(HttpServletRequest req) {
		req.setAttribute("obj", sysUnitService
				.query(Cnd.where("parentId", "=", "").or("parentId", "is", null).asc("location").asc("path")));
		return "pages/platform/sys/unit/index";
	}

	@RequestMapping(value = { "/add/{id}", "/add" })
	@RequiresPermissions("sys.manager.unit")
	public String add(@PathVariable(required = false) String id, HttpServletRequest req) {
		req.setAttribute("obj", Strings.isBlank(id) ? null : sysUnitService.fetch(id));
		return "pages/platform/sys/unit/add";
	}
	
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("sys.manager.unit")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		req.setAttribute("obj", Strings.isBlank(id) ? null : sysUnitService.fetch(id));
		return "pages/platform/sys/unit/detail";
	}	

	@RequestMapping(method = RequestMethod.POST, value = "/addDo")
	@SJson
	@RequiresPermissions("sys.manager.unit.add")
	public Object addDo(Sys_unit unit, String parentId) {
		try {
			sysUnitService.save(unit, parentId);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
	}

	@RequestMapping("/child/{id}")
	@RequiresPermissions("sys.manager.unit")
	public String child(@PathVariable String id, HttpServletRequest req) {
		req.setAttribute("obj", sysUnitService.query(Cnd.where("parentId", "=", id).asc("location").asc("path")));
		return "pages/platform/sys/unit/child";
	}

	@RequestMapping("/edit/{id}")
	@RequiresPermissions("sys.manager.unit")
	public String edit(@PathVariable String id, HttpServletRequest req) {
		Sys_unit unit = sysUnitService.fetch(id);
		if (unit != null) {
			req.setAttribute("parentUnit", sysUnitService.fetch(unit.getParentId()));
		}
		req.setAttribute("obj", unit);
		return "pages/platform/sys/unit/edit";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/editDo")
	@SJson
	@RequiresPermissions("sys.manager.unit.edit")
	public Object editDo(Sys_unit unit, HttpServletRequest req) {
		try {
			unit.setOpBy(StringUtil.getUid());
			unit.setOpAt((int) (System.currentTimeMillis() / 1000));
			sysUnitService.updateIgnoreNull(unit);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/delete/{id}")
	@SJson
	@RequiresPermissions("sys.manager.unit.delete")
	public Object delete(@PathVariable String id, HttpServletRequest req) {
		try {
			Sys_unit unit = sysUnitService.fetch(id);
			sysUnitService.deleteAndChild(unit);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
	}

	@RequestMapping(value = { "/tree", "/tree/{pid}" })
	@SJson
	@RequiresPermissions("sys.manager.unit")
	public Object tree(@PathVariable(required = false) String pid, HttpServletRequest req) {
		Cnd cnd = Cnd.NEW();
		if (Strings.isBlank(pid)) {
			cnd.and("parentId", "=", "").or("parentId", "is", null);
		} else {
			cnd.and("parentId", "=", pid);
		}
		cnd.asc("path");
		List<Sys_unit> list = sysUnitService.query(cnd);
		List<Map<String, Object>> tree = new ArrayList<>();
		for (Sys_unit unit : list) {
			Map<String, Object> obj = new HashMap<>();
			obj.put("id", unit.getId());
			obj.put("text", unit.getName());
			obj.put("children", unit.isHasChildren());
			tree.add(obj);
		}
		return tree;
	}
}
