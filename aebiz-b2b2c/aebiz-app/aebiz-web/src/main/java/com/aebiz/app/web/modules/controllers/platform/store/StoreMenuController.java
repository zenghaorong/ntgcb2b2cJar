package com.aebiz.app.web.modules.controllers.platform.store;

import com.aebiz.app.store.modules.models.Store_menu;
import com.aebiz.app.store.modules.services.StoreMenuService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
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
@RequestMapping("/platform/store/config/menu")
public class StoreMenuController {
    @Autowired
    private StoreMenuService storeMenuService;

    @RequestMapping(value = {"/{system}", ""})
    @RequiresPermissions("store.config.menu")
    public String index(@PathVariable(required = false) String system, HttpServletRequest req) {
        if (Strings.isEmpty(system)) {
            system = "store";
        }
        req.setAttribute("system", system);
        req.setAttribute("obj", storeMenuService.query(Cnd.where("system", "=", system).and("type", "=", "menu").and("parentId", "=", "").or("parentId", "is", null).asc("location").asc("path")));
        return "pages/platform/store/config/menu/index";
    }

    @RequestMapping("/child/{id}")
    @RequiresPermissions("store.config.menu")
    public String child(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", storeMenuService.query(Cnd.where("parentId", "=", id).and("type", "=", "menu").asc("location").asc("path")));
        return "pages/platform/store/config/menu/child";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("store.config.menu")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return storeMenuService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping(value = {"/add/{pid}", "/add"})
    @RequiresPermissions("store.config.menu")
    public String add(@PathVariable(required = false) String pid, HttpServletRequest req) {
        if (!Strings.isEmpty(pid)) {
            Store_menu store_menu = storeMenuService.fetch(pid);
            req.setAttribute("obj", store_menu);
        }
        req.setAttribute("system", "store");
        return "pages/platform/store/config/menu/add";
    }

    @RequestMapping(value = {"/addDo/{setRoleVal}", "/addDo"})
    @SJson
    @RequiresPermissions("store.config.menu.add")
    @SLog(description = "Store_menu")
    public Object addDo(Store_menu sysMenu,
                        @PathVariable(required = false) String setRoleVal,
                        @RequestParam(required = false, value = "listChildMenu.name") List<String> listChildMenuName,
                        @RequestParam(required = false, value = "listChildMenu.permission") List<String> listChildMenuPermission,
                        @RequestParam(required = false, value = "listChildMenu.disabled") List<String> listChildMenuDisabled,
                        HttpServletRequest req) {
        try {
            sysMenu.setIsShow(true);
            Store_menu store_menu = storeMenuService.save(sysMenu);
            if (!Strings.isEmpty(setRoleVal) && setRoleVal.equals("0") && listChildMenuName != null && listChildMenuPermission != null) {
                List<Store_menu> listChildMenu = toListChildMenu(listChildMenuName, listChildMenuPermission, listChildMenuDisabled);
                for (Store_menu child : listChildMenu) {
                    child.setParentId(store_menu.getId());
                    child.setType("data");
                    child.setIsShow(false);
                    child.setSystem(store_menu.getSystem());
                    storeMenuService.saveData(child);
                }

            }

            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    private List<Store_menu> toListChildMenu(List<String> listChildMenuName, List<String> listChildMenuPermission, List<String> listChildMenuDisabled) {
        List<Store_menu> result = new ArrayList<>();
        for (int i = 0; i < listChildMenuName.size(); i++) {
            Store_menu store_menu = new Store_menu();
            store_menu.setName(listChildMenuName.get(i));
            store_menu.setPermission(listChildMenuPermission.get(i));
            if (listChildMenuDisabled.get(i).equals("0")) {
                store_menu.setDisabled(false);
            } else {
                store_menu.setDisabled(true);

            }
            result.add(store_menu);
        }

        return result;
    }

    @RequestMapping("/addChildData")
    @SJson
    @RequiresPermissions("store.config.menu.add")
    @SLog(description = "Store_menu")
    public Object addChildData(@RequestParam("pid") String pid, @RequestParam("name") String name, @RequestParam("system") String system, @RequestParam("permission") String permission, HttpServletRequest req) {
        try {
            Store_menu sysMenu = storeMenuService.addChildData(pid, name, permission, system);
            return sysMenu;
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.config.menu")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        Store_menu menu = storeMenuService.fetch(id);
        if (menu != null) {
            req.setAttribute("obj", menu);
            req.setAttribute("parentMenu", storeMenuService.fetch(menu.getParentId()));
            req.setAttribute("childList", storeMenuService.query(Cnd.where("parentId", "=", menu.getId()).and("type", "=", "data").asc("location").asc("path")));
        }

        return "pages/platform/store/config/menu/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @RequiresPermissions("store.config.menu.edit")
    @SLog(description = "Store_menu")
    public Object editDo(Store_menu sysMenu, HttpServletRequest req) {
        try {
            sysMenu.setOpAt((int) (System.currentTimeMillis() / 1000));
            storeMenuService.updateIgnoreNull(sysMenu);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }


    @RequestMapping(value = {"/tree", "/tree/{pid}"})
    @SJson
    @RequiresPermissions("store.config.menu")
    public Object tree(@PathVariable(required = false) String pid, HttpServletRequest req) {
        List<Store_menu> list = storeMenuService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).and("system", "=", "store").asc("path"));

        List<Map<String, Object>> tree = new ArrayList<>();
        for (Store_menu menu : list) {
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
    @RequiresPermissions("store.config.menu.delete")
    @SLog(description = "Store_menu")
    public Object delete(@PathVariable(required = false) String id, HttpServletRequest req) {

        try {
            Store_menu menu = storeMenuService.fetch(id);
            req.setAttribute("name", menu.getName());
            storeMenuService.deleteAndChild(menu);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }

    }


    @RequestMapping("/enable/{id}")
    @SJson
    @RequiresPermissions("store.config.menu.edit")
    @SLog(description = "Store_menu")
    public Object enable(@PathVariable String id, HttpServletRequest req) {
        try {
            storeMenuService.update(Chain.make("disabled", false), Cnd.where("id", "=", id));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/disable/{id}")
    @SJson
    @RequiresPermissions("store.config.menu.edit")
    @SLog(description = "Store_menu")
    public Object disable(@PathVariable String id, HttpServletRequest req) {
        try {
            storeMenuService.update(Chain.make("disabled", true), Cnd.where("id", "=", id));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/sort")
    @RequiresPermissions("store.config.menu")
    public String sort(HttpServletRequest req) {
        List<Store_menu> list = storeMenuService.query(Cnd.orderBy().asc("location").asc("path"));
        List<Store_menu> firstMenus = new ArrayList<>();
        Map<String, List<Store_menu>> secondMenus = new HashMap<>();
        for (Store_menu menu : list) {
            if (menu.getPath().length() > 4) {
                List<Store_menu> s = secondMenus.get(StringUtil.getParentId(menu.getPath()));
                if (s == null) s = new ArrayList<>();
                s.add(menu);
                secondMenus.put(StringUtil.getParentId(menu.getPath()), s);
            } else if (menu.getPath().length() == 4) {
                firstMenus.add(menu);
            }
        }
        req.setAttribute("firstMenus", firstMenus);
        req.setAttribute("secondMenus", secondMenus);
        return "pages/platform/store/config/menu/sort";
    }

    @RequestMapping("/sortDo")
    @SJson
    @RequiresPermissions("sys.manager.menu.edit")
    public Object sortDo(@RequestParam("ids") String ids, HttpServletRequest req) {
        try {
            String[] menuIds = StringUtils.split(ids, ",");
            int i = 0;
            storeMenuService.dao().execute(Sqls.create("update store_menu set location=0"));
            for (String s : menuIds) {
                if (!Strings.isBlank(s)) {
                    storeMenuService.update(Chain.make("location", i), Cnd.where("id", "=", s));
                    i++;
                }
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

}
