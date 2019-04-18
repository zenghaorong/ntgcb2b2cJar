package com.aebiz.app.web.modules.controllers.platform.store;

import com.aebiz.app.store.modules.models.Store_menu;
import com.aebiz.app.store.modules.models.Store_role;
import com.aebiz.app.store.modules.services.StoreMenuService;
import com.aebiz.app.store.modules.services.StoreRoleService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/platform/store/config/role")
public class StoreRoleController {

    private static final Log log = Logs.get();

    @Autowired
    private StoreRoleService storeRoleService;

    @Autowired
    private StoreMenuService storeMenuService;

    @RequestMapping("")
    @RequiresPermissions("store.config.role")
    public String index() {
        return "pages/platform/store/config/role/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("store.config.role")
    public Object data(@RequestParam(value = "name", required = false) String name, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(name)) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(name) + "%"));
        }
        return storeRoleService.data(dataTable.getLength(), dataTable.getStart(),
                dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("store.config.role")
    public String add() {
        return "pages/platform/store/config/role/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Store_role")
    @RequiresPermissions("store.config.role.add")
    public Object addDo(Store_role storeRole, HttpServletRequest req) {
        try {
            storeRoleService.add(storeRole);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.config.role")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", storeRoleService.fetch(id));
        return "pages/platform/store/config/role/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Store_role")
    @RequiresPermissions("store.config.role.edit")
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
    @RequiresPermissions("store.config.role.delete")
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
    @RequiresPermissions("store.config.role.edit")
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
    @RequiresPermissions("store.config.role.edit")
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
    @RequiresPermissions("store.config.role.edit")
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
    @RequiresPermissions("store.config.role")
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

        return "pages/platform/store/config/role/editMenu";
    }

    @RequestMapping(value = {"/menu", "/menu/{id}"})
    @RequiresPermissions("store.config.role")
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
        return "pages/platform/store/config/role/menu";
    }

    @RequestMapping("/editMenuDo/{roleid}")
    @SJson
    @RequiresPermissions("store.config.role.edit")
    public Object editMenuDo(@RequestParam(required = false) String ids, @PathVariable String roleid, HttpServletRequest req) {
        try {
            String[] idss = StringUtils.split(ids, ",");
            storeRoleService.dao().clear("store_role_menu", Cnd.where("roleid", "=", roleid));
            for (String s : idss) {
                if (!Strings.isEmpty(s)) {
                    storeRoleService.insert("store_role_menu", Chain.make("roleId", roleid).add("menuId", s));
                }
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
