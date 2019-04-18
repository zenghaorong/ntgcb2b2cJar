package com.aebiz.app.web.modules.controllers.store.roles;

import com.aebiz.app.store.modules.models.Store_menu;
import com.aebiz.app.store.modules.models.Store_role;
import com.aebiz.app.store.modules.models.Store_user;
import com.aebiz.app.store.modules.services.StoreMenuService;
import com.aebiz.app.store.modules.services.StoreRoleService;
import com.aebiz.app.store.modules.services.StoreUserService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
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
import java.util.List;

@Controller
@RequestMapping("/store/roles/role")
public class ChildStoreRoleController {
    private static final Log log = Logs.get();
    @Autowired
    private StoreRoleService storeRoleService;
    @Autowired
    private StoreMenuService storeMenuService;

    @Autowired
    private StoreUserService storeUserService;

    @RequestMapping("")
    @RequiresPermissions("store.sys.manager.role")
    public String index() {
        return "pages/store/roles/role/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("store.sys.manager.role")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return storeRoleService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("store.sys.manager.role")
    public String add() {
        return "pages/store/roles/role/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Store_role")
    @RequiresPermissions("store.sys.manager.role.add")
    public Object addDo(Store_role storeRole, HttpServletRequest req) {
        try {
            storeRoleService.add(storeRole);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.sys.manager.role")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", storeRoleService.fetch(id));
        return "pages/store/roles/role/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Store_role")
    @RequiresPermissions("store.sys.manager.role.edit")
    public Object editDo(Store_role storeRole, HttpServletRequest req) {
        try {
            storeRoleService.edit(storeRole);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Store_role")
    @RequiresPermissions("store.sys.manager.role.delete")
    public Object delete(@PathVariable(required = true) String id, HttpServletRequest req) {
        try {
            storeRoleService.del(id);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/enable/{roleid}")
    @SJson
    @SLog(description = "Store_role")
    @RequiresPermissions("store.sys.manager.role.edit")
    public Object enable(@PathVariable String roleid, HttpServletRequest req) {
        try {
            storeRoleService.update(Chain.make("disabled", false), Cnd.where("id", "=", roleid));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/disable/{roleid}")
    @SJson
    @SLog(description = "Store_role")
    @RequiresPermissions("store.sys.manager.role.edit")
    public Object disable(@PathVariable String roleid, HttpServletRequest req) {
        try {
            storeRoleService.update(Chain.make("disabled", true), Cnd.where("id", "=", roleid));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/setDefault/{roleid}")
    @SJson
    @SLog(description = "Store_role")
    @RequiresPermissions("store.sys.manager.role.edit")
    public Object setDefault(@PathVariable String roleid, HttpServletRequest req) {
        try {
            Store_role storeRole = storeRoleService.fetch(roleid);
            storeRole.setDisabled(false);//设为默认时需启用
            storeRole.setDefaultValue(true);
            storeRoleService.edit(storeRole);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/editMenu/{roleId}")
    @RequiresPermissions("store.sys.manager.role")
    public Object editMenu(@PathVariable String roleId, HttpServletRequest req) {
        StringBuilder roleMenuIds = new StringBuilder();
        List<Store_menu> list = storeMenuService.query(Cnd.where("system", "=", "store").asc("location").asc("path"));
        List<Store_menu> datas = storeRoleService.getDatas(null);
        List<Store_menu> roleMenu = storeRoleService.getMenusAndButtons(roleId);
        for (Store_menu m : roleMenu) {
            roleMenuIds.append(m.getId() + "#");
        }

        String roleMenuId = roleMenuIds.toString();
        List<NutMap> menus = new ArrayList<>();
        for (Store_menu menu : list) {
            NutMap map = new NutMap();
            for (Store_menu bt : datas) {
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

            if (menu.getSystem().equals("store")) {
                menus.add(map);
            }
        }
        req.setAttribute("menus", Json.toJson(menus));
        if (!Strings.isBlank(roleId)) {
            req.setAttribute("obj", storeRoleService.fetch(roleId));
        }

        return "pages/store/roles/role/editMenu";
    }

    @RequestMapping(value = {"/menu", "/menu/{id}"})
    @RequiresPermissions("store.sys.manager.role")
    public Object menu(@PathVariable(required = false) String id, HttpServletRequest req) {
        Store_role role = storeRoleService.fetch(id);
        List<Store_menu> menus = storeRoleService.getMenusAndButtons(id);
        List<Store_menu> datas = storeRoleService.getDatas(role.getId());
        List<Store_menu> firstMenus = new ArrayList<>();
        List<Store_menu> secondMenus = new ArrayList<>();
        for (Store_menu menu : menus) {
            for (Store_menu bt : datas) {
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
        return "pages/store/roles/role/menu";
    }

    @RequestMapping("/editMenuDo/{roleid}")
    @SJson
    @RequiresPermissions("store.sys.manager.role.edit")
    public Object editMenuDo(@RequestParam(required = false) String ids, @PathVariable String roleid, HttpServletRequest req) {
        try {
            storeRoleService.editMenuDo(ids,roleid);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/editUser/{roleid}")
    @RequiresAuthentication
    @RequiresPermissions("store.sys.manager.role")
    public Object editUser(@PathVariable String roleid, HttpServletRequest req) {
        req.setAttribute("obj",storeRoleService.fetch(roleid));
        return "pages/store/roles/role/editUser";
    }

    @RequestMapping("/userData")
    @SJson("full")
    @RequiresPermissions("store.sys.manager.role")
    public Object userData(@RequestParam("roleid") String roleid, @RequestParam("length") int length,
                             @RequestParam("start") int start, @RequestParam("draw") int draw,
                             ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
        String storeId = user.getStoreId();
        String sql = "SELECT u.id, a.nickname, a.name, a.sex FROM store_user u, account_info a WHERE u.accountId = a.id and u.storeId ='" + storeId + "'";
        if (!Strings.isBlank(roleid)) {
            sql += " and u.id IN(SELECT b.userId FROM store_user_role b WHERE b.roleId='" + roleid + "')";
        }
        String s = sql;
        if (order != null && order.size() > 0) {
            for (DataTableOrder o : order) {
                DataTableColumn col = columns.get(o.getColumn());
                s += " order by a." + Sqls.escapeSqlFieldValue(col.getData()).toString() + " " + o.getDir();
            }
        }
        return storeUserService.data(length, start, draw, Sqls.create(sql), Sqls.create(s));
    }

    @RequestMapping("/selectUser")
    @RequiresPermissions("store.sys.manager.role")
    public Object selectUser(HttpServletRequest req) {
        return "pages/store/roles/role/selectUser";
    }

    @RequestMapping("/selectData")
    @SJson("full")
    @RequiresPermissions("store.sys.manager.role")
    public Object selectData(@RequestParam("roleid") String roleid,
                             @RequestParam("name") String name,@RequestParam("length") int length,
                             @RequestParam("start") int start, @RequestParam("draw") int draw,
                             ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
        String storeId = user.getStoreId();
        String sql = "SELECT u.id, a.nickname, a.name, a.sex FROM store_user u, account_info a WHERE u.accountId = a.id and u.storeId ='" + storeId + "'";
        if (!Strings.isBlank(roleid)) {
            sql += " and u.id NOT IN(SELECT b.userId FROM store_user_role b WHERE b.roleId='" + roleid + "')";
        }

        if (!Strings.isBlank(name)) {
            sql += " and a.name like '%" + name + "%'";
        }

        String s = sql;
        if (order != null && order.size() > 0) {
            for (DataTableOrder o : order) {
                DataTableColumn col = columns.get(o.getColumn());
                s += " order by a." + Sqls.escapeSqlFieldValue(col.getData()).toString() + " " + o.getDir();
            }
        }
        return storeUserService.data(length, start, draw, Sqls.create(sql), Sqls.create(s));
    }

    @RequestMapping("/pushUser")
    @SJson
    @SLog(description = "添加用户到角色")
    @RequiresPermissions("store.sys.manager.role")
    public Object pushUser(@RequestParam("menuIds") String menuIds, @RequestParam("roleid") String roleid, HttpServletRequest req) {
        try {
            String[] ids = StringUtils.split(menuIds, ",");
            for (String s : ids) {
                if (!Strings.isEmpty(s)) {
                    storeRoleService.insert("store_user_role", Chain.make("roleId", roleid).add("userId", s));
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
    @RequiresPermissions("store.sys.manager.role")
    public Object delUser(@RequestParam("menuIds") String menuIds, @RequestParam("roleid") String roleid, HttpServletRequest req) {
        try {
            String[] ids = StringUtils.split(menuIds, ",");
            storeRoleService.dao().clear("store_user_role", Cnd.where("userId", "in", ids).and("roleId", "=", roleid));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
