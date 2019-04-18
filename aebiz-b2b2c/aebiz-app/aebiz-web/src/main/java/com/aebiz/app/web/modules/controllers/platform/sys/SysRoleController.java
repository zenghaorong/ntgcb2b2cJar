package com.aebiz.app.web.modules.controllers.platform.sys;

import com.aebiz.app.sys.modules.models.Sys_menu;
import com.aebiz.app.sys.modules.models.Sys_role;
import com.aebiz.app.sys.modules.models.Sys_unit;
import com.aebiz.app.sys.modules.services.SysMenuService;
import com.aebiz.app.sys.modules.services.SysRoleService;
import com.aebiz.app.sys.modules.services.SysUnitService;
import com.aebiz.app.sys.modules.services.SysUserService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
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
@RequestMapping("/platform/sys/role")
public class SysRoleController {
    private static final Log log = Logs.get();
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUnitService sysUnitService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysUserService sysUserService;


    @RequestMapping("")
    @RequiresPermissions("sys.manager.role")
    public String index() {
        return "pages/platform/sys/role/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("sys.manager.role")
    public Object data(@RequestParam(required = false) String unitid, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(unitid) && !"root".equals(unitid)) {
            cnd.and("unitid", "=", unitid);
        }

        return sysRoleService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping(value = {"/add/{id}", "/add"})
    @RequiresPermissions("sys.manager.role")
    public Object add(@PathVariable(required = false) String id, HttpServletRequest req) {
        List<Sys_menu> list = sysMenuService.query(Cnd.where("system", "=", "platform").orderBy().asc("location").asc("path"));
        List<Sys_menu> datas = sysRoleService.getDatas(null);
        List<NutMap> menus = new ArrayList<>();
        for (Sys_menu menu : list) {
            NutMap map = new NutMap();
            for (Sys_menu bt : datas) {
                if (menu.getPath().equals(bt.getPath().substring(0, bt.getPath().length() - 4))) {
                    menu.setHasChildren(true);
                    break;
                }
            }

            map.put("id", menu.getId());
            map.put("text", menu.getName());
            map.put("icon", Strings.sBlank(menu.getIcon()));
            map.put("parent", "".equals(Strings.sNull(menu.getParentId())) ? "#" : menu.getParentId());
            map.put("data", menu.getHref());
            if (menu.getSystem().equals("platform")) {
                menus.add(map);

            }
        }

        req.setAttribute("menus", Json.toJson(menus));
        if (!Strings.isBlank(id)) {
            req.setAttribute("obj", sysUnitService.fetch(id));
        }

        return "pages/platform/sys/role/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @RequiresPermissions("sys.manager.role.add")//sys.manager.role.add
    @SLog(description = "Sys_role")
    public Object addDo(Sys_role sysRole, HttpServletRequest req) {
        String menuIds = req.getParameter("menuIds");
        if (Strings.isBlank(menuIds)) {
            return Result.error("菜单权限不能为空");
        }

        try {
            int num = sysRoleService.count(Cnd.where("code", "=", sysRole.getCode().trim()));
            if (num > 0) {
                return Result.error("sys.role.code");
            }

            String[] ids = StringUtils.split(menuIds, ",");
            if ("root".equals(sysRole.getUnitid())) {
                sysRole.setUnitid("");
            }

            Sys_role r = sysRoleService.insert(sysRole);
            for (String s : ids) {
                if (!Strings.isEmpty(s)) {
                    sysRoleService.insert("sys_role_menu", org.nutz.dao.Chain.make("roleId", r.getId()).add("menuId", s));
                }
            }

            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.role")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        Sys_role role = sysRoleService.fetch(id);
        req.setAttribute("unit", sysUnitService.fetch(role.getUnitid()));
        req.setAttribute("obj", role);
        return "pages/platform/sys/role/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @RequiresPermissions("sys.manager.role.edit")
    @SLog(description = "Sys_role")
    public Object editDo(Sys_role sysRole, HttpServletRequest req) {
        String oldCode = req.getParameter("oldCode");
        try {
            if (!Strings.sBlank(oldCode).equals(sysRole.getCode())) {
                int num = sysRoleService.count(Cnd.where("code", "=", sysRole.getCode().trim()));
                if (num > 0) {
                    return Result.error("sys.role.code");
                }
            }

            if ("root".equals(sysRole.getUnitid()))
                sysRole.setUnitid("");
            sysRole.setOpBy(Strings.sNull(StringUtil.getUid()));
            sysRole.setOpAt((int) (System.currentTimeMillis() / 1000));
            sysRoleService.updateIgnoreNull(sysRole);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @RequiresPermissions("sys.manager.role.delete")
    @SLog(description = "Sys_role")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids", required = false) String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                sysRoleService.del(ids);
            } else {
                sysRoleService.del(id);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.role")
    public String detail(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            req.setAttribute("obj", sysRoleService.fetch(id));
        } else {
            req.setAttribute("obj", null);
        }
        return "pages/platform/sys/role/detail";
    }


    @RequestMapping(value = {"/menu", "/menu/{id}"})
    @RequiresPermissions("sys.manager.role")
    public Object menu(@PathVariable(required = false) String id, HttpServletRequest req) {
        Sys_role role = sysRoleService.fetch(id);
        List<Sys_menu> menus = sysRoleService.getMenusAndButtons(id);
        List<Sys_menu> datas = sysRoleService.getDatas(role.getId());
        List<Sys_menu> firstMenus = new ArrayList<>();
        List<Sys_menu> secondMenus = new ArrayList<>();
        for (Sys_menu menu : menus) {
            for (Sys_menu bt : datas) {
                if (menu.getPath().equals(bt.getPath().substring(0, bt.getPath().length() - 4))) {
                    menu.setHasChildren(true);
                    break;
                }
            }
            if (menu.getPath().length() == 4) {
                firstMenus.add(menu);
            } else {
                secondMenus.add(menu);
            }
        }

        req.setAttribute("userFirstMenus", firstMenus);
        req.setAttribute("userSecondMenus", secondMenus);
        req.setAttribute("jsonSecondMenus", Json.toJson(secondMenus));
        req.setAttribute("obj", role);
        return "pages/platform/sys/role/menu";
    }

    @RequestMapping(value = {"/tree", "/tree/{pid}"})
    @SJson
    @RequiresPermissions("sys.manager.role")
    public Object tree(@PathVariable(required = false) String pid) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isBlank(pid)) {
            cnd.and("parentId", "=", "").or("parentId", "is", null);
        } else {
            cnd.and("parentId", "=", pid);
        }
        cnd.asc("path");
        List<Sys_unit> list = sysUnitService.query(cnd);
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Object> obj = new HashMap<>();
        if (Strings.isBlank(pid)) {
            obj.put("id", "root");
            obj.put("text", "系统角色");
            obj.put("children", false);
            tree.add(obj);
        }

        for (Sys_unit unit : list) {
            obj = new HashMap<>();
            obj.put("id", unit.getId());
            obj.put("text", unit.getName());
            obj.put("children", unit.isHasChildren());
            tree.add(obj);
        }

        return tree;
    }

    @RequestMapping("/editMenu/{roleId}")
    @RequiresPermissions("sys.manager.role")
    public Object editMenu(@PathVariable String roleId, HttpServletRequest req) {
        StringBuilder roleMenuIds = new StringBuilder();
        List<Sys_menu> list = sysMenuService.query(Cnd.where("system", "=", "platform").asc("location").asc("path"));
        List<Sys_menu> datas = sysRoleService.getDatas(null);
        List<Sys_menu> roleMenu = sysRoleService.getMenusAndButtons(roleId);
        for (Sys_menu m : roleMenu) {
            roleMenuIds.append(m.getId() + "#");
        }

        String roleMenuId = roleMenuIds.toString();
        log.debug(roleMenuId);
        List<NutMap> menus = new ArrayList<>();
        for (Sys_menu menu : list) {
            NutMap map = new NutMap();
            for (Sys_menu bt : datas) {
                if (menu.getPath().equals(bt.getPath().substring(0, bt.getPath().length() - 4))) {
                    menu.setHasChildren(true);
                    break;
                }
            }
            map.put("id", menu.getId());
            map.put("text", menu.getName());
            map.put("icon", Strings.sBlank(menu.getIcon()));
            map.put("parent", "".equals(Strings.sNull(menu.getParentId())) ? "#" : menu.getParentId());
            map.put("data", menu.getHref());
            if ((menu.getPath().length() >= 16 || !menu.isHasChildren()) && roleMenuId.contains(menu.getId() + "#")) {
                map.put("state", NutMap.NEW().addv("selected", true));
            } else {
                map.put("state", NutMap.NEW().addv("selected", false));
            }

            if (menu.getSystem().equals("platform")) {
                menus.add(map);

            }
        }
        req.setAttribute("menus", Json.toJson(menus));
        if (!Strings.isBlank(roleId)) {
            req.setAttribute("obj", sysRoleService.fetch(roleId));
        }

        return "pages/platform/sys/role/editMenu";
    }

    @RequestMapping("/editMenuDo/{roleid}")
    @SJson
    @RequiresPermissions("sys.manager.role.menu")
    public Object editMenuDo(@RequestParam(required = false) String ids, @PathVariable String roleid, HttpServletRequest req) {
        try {
            String[] idss = StringUtils.split(ids, ",");
            sysRoleService.dao().clear("sys_role_menu", Cnd.where("roleid", "=", roleid));
            for (String s : idss) {
                if (!Strings.isEmpty(s)) {
                    sysRoleService.insert("sys_role_menu", org.nutz.dao.Chain.make("roleId", roleid).add("menuId", s));
                }
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/editUser/{roleid}")
    @RequiresAuthentication
    @RequiresPermissions("sys.manager.role.user")
    public Object editUser(@PathVariable String roleid, HttpServletRequest req) {
        req.setAttribute("obj", sysRoleService.fetch(roleid));
        return "pages/platform/sys/role/editUser";
    }

    @RequestMapping("/userData")
    @SJson("full")
    @RequiresPermissions("sys.manager.role")
    public Object userData(@RequestParam("roleid") String roleid, @RequestParam("loginname") String loginname,
                           @RequestParam("username") String username, DataTable dataTable) {
        String sql = "SELECT a.* FROM sys_user a,sys_user_role b WHERE a.id=b.userId ";
        if (!Strings.isBlank(roleid)) {
            sql += " and b.roleId='" + roleid + "'";
        }

        if (!Strings.isBlank(loginname)) {
            sql += " and a.loginname like '%" + loginname + "%'";
        }

        if (!Strings.isBlank(username)) {
            sql += " and a.username like '%" + username + "%'";
        }

        String s = sql;
        if (dataTable.getOrders() != null && dataTable.getOrders().size() > 0 && dataTable.getColumns() != null && dataTable.getColumns().size() > 0) {
            for (DataTableOrder o : dataTable.getOrders()) {
                DataTableColumn col = dataTable.getColumns().get(o.getColumn());
                s += " order by a." + Sqls.escapeSqlFieldValue(col.getData()).toString() + " " + o.getDir();
            }
        }

        return sysRoleService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), Sqls.create(sql), Sqls.create(s));
    }

    @RequestMapping("/selectUser")
    @RequiresPermissions("sys.manager.role")
    public Object selectUser(HttpServletRequest req) {
        return "pages/platform/sys/role/selectUser";
    }

    @RequestMapping("/selectData")
    @SJson("full")
    @RequiresPermissions("sys.manager.role")
    public Object selectData(@RequestParam("roleid") String roleid,
                             @RequestParam("name") String name, @RequestParam("length") int length,
                             @RequestParam("start") int start, @RequestParam("draw") int draw,
                             ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        String sql = "SELECT a.* FROM sys_user a WHERE 1=1 ";
        if (!Strings.isBlank(roleid)) {
            sql += " and a.id NOT IN(SELECT b.userId FROM sys_user_role b WHERE b.roleId='" + roleid + "')";
        }
        if (!Strings.isBlank(name)) {
            sql += " and (a.loginname like '%" + name + "%' or a.username like '%" + name + "%') ";
        }
        String s = sql;
        if (order != null && order.size() > 0) {
            for (DataTableOrder o : order) {
                DataTableColumn col = columns.get(o.getColumn());
                s += " order by a." + Sqls.escapeSqlFieldValue(col.getData()).toString() + " " + o.getDir();
            }
        }
        return sysRoleService.data(length, start, draw, Sqls.create(sql), Sqls.create(s));
    }

    @RequestMapping("/enable/{roleid}")
    @SJson
    @SLog(description = "系统角色")
    @RequiresPermissions("sys.manager.role.edit")
    public Object enable(@PathVariable String roleid, HttpServletRequest req) {
        try {
            sysRoleService.update(org.nutz.dao.Chain.make("disabled", false), Cnd.where("id", "=", roleid));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/disable/{roleid}")
    @SJson
    @SLog(description = "系统角色")
    @RequiresPermissions("sys.manager.role.edit")
    public Object disable(@PathVariable String roleid, HttpServletRequest req) {
        try {
            sysRoleService.update(org.nutz.dao.Chain.make("disabled", true), Cnd.where("id", "=", roleid));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/pushUser")
    @SJson
    @SLog(description = "添加用户到角色")
    @RequiresPermissions("sys.manager.role")
    public Object pushUser(@RequestParam("menuIds") String menuIds, @RequestParam("roleid") String roleid, HttpServletRequest req) {
        try {
            String[] ids = StringUtils.split(menuIds, ",");
            for (String s : ids) {
                if (!Strings.isEmpty(s)) {
                    sysRoleService.insert("sys_user_role", org.nutz.dao.Chain.make("roleId", roleid).add("userId", s));
                }
            }
            return Result.success("globals.result.success");

        } catch (Exception e) {
            return Result.error("globals.result.error");

        }
    }


    @RequestMapping("/delUser")
    @SJson
    @SLog(description = "从角色中删除用户")
    @RequiresPermissions("sys.manager.role")
    public Object delUser(@RequestParam("menuIds") String menuIds, @RequestParam("roleid") String roleid, HttpServletRequest req) {
        try {
            String[] ids = StringUtils.split(menuIds, ",");
            sysRoleService.dao().clear("sys_user_role", Cnd.where("userId", "in", ids).and("roleId", "=", roleid));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

}
