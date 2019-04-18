package com.aebiz.app.web.modules.controllers.platform.sys;

import com.aebiz.app.sys.modules.models.Sys_menu;
import com.aebiz.app.sys.modules.services.SysMenuService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/platform/sys/menu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @RequestMapping(value = {"/{system}", ""})
    @RequiresPermissions("sys.manager.menu")
    public String index(@PathVariable(required = false) String system, HttpServletRequest req) {
        if (Strings.isEmpty(system)) {
            system = "platform";
        }

        req.setAttribute("system", system);
        req.setAttribute("obj", sysMenuService.query(Cnd.where("system", "=", system).and("type", "=", "menu").and("parentId", "=", "").or("parentId", "is", null).asc("location").asc("path")));
        return "pages/platform/sys/menu/index";
    }

    @RequestMapping("/child/{id}")
    @RequiresPermissions("sys.manager.menu")
    public String child(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", sysMenuService.query(Cnd.where("parentId", "=", id).and("type", "=", "menu").asc("location").asc("path")));
        return "pages/platform/sys/menu/child";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("sys.manager.menu")
    public Object data(DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        return sysMenuService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping(value = {"/add/{pid}", "/add"})
    @RequiresPermissions("sys.manager.menu")
    public String add(@PathVariable(required = false) String pid, HttpServletRequest req) {
        if (!Strings.isEmpty(pid)) {
            Sys_menu sys_menu = sysMenuService.fetch(pid);
            req.setAttribute("obj", sys_menu);
        }
        req.setAttribute("system", "platform");
        return "pages/platform/sys/menu/add";
    }

    @RequestMapping(value = {"/addDo/{setRoleVal}", "/addDo"})
    @SJson
    @RequiresPermissions("sys.manager.menu.add")
    @SLog(description = "Sys_menu")
    public Object addDo(Sys_menu sysMenu,
                        @PathVariable(required = false) String setRoleVal,
                        @RequestParam(required = false, value = "listChildMenu.name") List<String> listChildMenuName,
                        @RequestParam(required = false, value = "listChildMenu.permission") List<String> listChildMenuPermission,
                        @RequestParam(required = false, value = "listChildMenu.disabled") List<String> listChildMenuDisabled,
                        HttpServletRequest req) {
        try {
            sysMenu.setIsShow(true);
            Sys_menu sys_menu = sysMenuService.save(sysMenu);
            if (!Strings.isEmpty(setRoleVal) && setRoleVal.equals("0") && listChildMenuName != null && listChildMenuPermission != null) {
                List<Sys_menu> listChildMenu = toListChildMenu(listChildMenuName, listChildMenuPermission, listChildMenuDisabled);
                for (Sys_menu child : listChildMenu) {
                    child.setParentId(sys_menu.getId());
                    child.setType("data");
                    child.setIsShow(false);
                    child.setSystem(sys_menu.getSystem());
                    sysMenuService.saveData(child);
                }

            }

            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    private List<Sys_menu> toListChildMenu(List<String> listChildMenuName, List<String> listChildMenuPermission, List<String> listChildMenuDisabled) {
        List<Sys_menu> result = new ArrayList<>();
        for (int i = 0; i < listChildMenuName.size(); i++) {
            Sys_menu sys_menu = new Sys_menu();
            sys_menu.setName(listChildMenuName.get(i));
            sys_menu.setPermission(listChildMenuPermission.get(i));
            if (listChildMenuDisabled.get(i).equals("0")) {
                sys_menu.setDisabled(false);
            } else {
                sys_menu.setDisabled(true);

            }
            result.add(sys_menu);
        }

        return result;
    }

    @RequestMapping("/addChildData")
    @SJson
    @RequiresPermissions("sys.manager.menu.add")
    @SLog(description = "Sys_menu")
    public Object addChildData(@RequestParam("pid") String pid, @RequestParam("name") String name, @RequestParam("system") String system, @RequestParam("permission") String permission, HttpServletRequest req) {
        try {
            Sys_menu sysMenu = sysMenuService.addChildData(pid, name, permission, system);
            return sysMenu;
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.menu")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        Sys_menu menu = sysMenuService.fetch(id);
        if (menu != null) {
            req.setAttribute("obj", menu);
            req.setAttribute("parentMenu", sysMenuService.fetch(menu.getParentId()));
            req.setAttribute("childList", sysMenuService.query(Cnd.where("parentId", "=", menu.getId()).and("type", "=", "data").asc("location").asc("path")));
        }

        return "pages/platform/sys/menu/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @RequiresPermissions("sys.manager.menu.edit")
    @SLog(description = "Sys_menu")
    public Object editDo(Sys_menu sysMenu, HttpServletRequest req) {
        try {
            sysMenu.setOpAt((int) (System.currentTimeMillis() / 1000));
            sysMenuService.updateIgnoreNull(sysMenu);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }


    @RequestMapping(value = {"/tree", "/tree/{pid}"})
    @SJson
    @RequiresPermissions("sys.manager.menu")
    public Object tree(@PathVariable(required = false) String pid, HttpServletRequest req) {
        String system = req.getParameter("system");
        List<Sys_menu> list = new ArrayList<>();
//        if (!Strings.isEmpty(system)) {
//            list = sysMenuService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).and("system", "=", system).asc("path"));
//        } else {
//            list = sysMenuService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("path"));
//        }
        Cnd cnd = Cnd.NEW();
        if (Strings.isBlank(pid)) {
            cnd.and("parentId", "=", "").or("parentId", "is", null);
        } else {
            cnd.and("parentId", "=", pid);
        }
        cnd.asc("path");
        list = sysMenuService.query(cnd);
        List<Map<String, Object>> tree = new ArrayList<>();
        for (Sys_menu menu : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", menu.getId());
            obj.put("text", menu.getName());
            obj.put("children", menu.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }


    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @RequiresPermissions("sys.manager.menu.delete")
    @SLog(description = "Sys_menu")
    public Object delete(@PathVariable(required = false) String id, HttpServletRequest req) {

        try {
            Sys_menu menu = sysMenuService.fetch(id);
            sysMenuService.deleteAndChild(menu);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }

    }


    @RequestMapping("/enable/{id}")
    @SJson
    @RequiresPermissions("sys.manager.menu.edit")
    @SLog(description = "启用菜单")
    public Object enable(@PathVariable String id, HttpServletRequest req) {
        try {
            sysMenuService.update(Chain.make("disabled", false), Cnd.where("id", "=", id));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/disable/{id}")
    @SJson
    @RequiresPermissions("sys.manager.menu.edit")
    @SLog(description = "禁用菜单")
    public Object disable(@PathVariable String id, HttpServletRequest req) {
        try {
            sysMenuService.update(Chain.make("disabled", true), Cnd.where("id", "=", id));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/sort")
    @RequiresPermissions("sys.manager.menu")
    public String sort(HttpServletRequest req) {
        List<Sys_menu> list = sysMenuService.query(Cnd.orderBy().asc("location").asc("path"));
        List<Sys_menu> firstMenus = new ArrayList<>();
        Map<String, List<Sys_menu>> secondMenus = new HashMap<>();
        for (Sys_menu menu : list) {
            if (menu.getPath().length() > 4) {
                List<Sys_menu> s = secondMenus.get(StringUtil.getParentId(menu.getPath()));
                if (s == null) s = new ArrayList<>();
                s.add(menu);
                secondMenus.put(StringUtil.getParentId(menu.getPath()), s);
            } else if (menu.getPath().length() == 4) {
                firstMenus.add(menu);
            }
        }
        req.setAttribute("firstMenus", firstMenus);
        req.setAttribute("secondMenus", secondMenus);
        return "pages/platform/sys/menu/sort";
    }

    @RequestMapping("/sortDo")
    @SJson
    @RequiresPermissions("sys.manager.menu.edit")
    public Object sortDo(@RequestParam("ids") String ids, HttpServletRequest req) {
        try {
            String[] menuIds = StringUtils.split(ids, ",");
            int i = 0;
            sysMenuService.dao().execute(Sqls.create("update sys_menu set location=0"));
            for (String s : menuIds) {
                if (!Strings.isBlank(s)) {
                    sysMenuService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", s));
                    i++;
                }
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
