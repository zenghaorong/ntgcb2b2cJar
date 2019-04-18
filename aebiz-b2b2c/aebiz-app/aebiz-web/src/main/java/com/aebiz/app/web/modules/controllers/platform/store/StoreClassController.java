package com.aebiz.app.web.modules.controllers.platform.store;

import com.aebiz.app.store.modules.models.Store_class;
import com.aebiz.app.store.modules.services.StoreClassService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/platform/store/config/class")
public class StoreClassController {
    @Autowired
    private StoreClassService storeClassService;

    @RequestMapping("")
    @RequiresPermissions("store.config.class")
    public String index(HttpServletRequest req) {
        req.setAttribute("obj", storeClassService
                .query(Cnd.where("parentId", "=", "").or("parentId", "is", null).asc("location").asc("path")));
        return "pages/platform/store/config/class/index";
    }

    @RequestMapping(value = {"/add/{id}", "/add"})
    @RequiresPermissions("store.config.class")
    public String add(@PathVariable(required = false) String id, HttpServletRequest req) {
        req.setAttribute("obj", Strings.isBlank(id) ? null : storeClassService.fetch(id));
        return "pages/platform/store/config/class/add";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addDo")
    @SJson
    @RequiresPermissions("store.config.class.add")
    public Object addDo(Store_class dict, String parentId) {
        try {
            storeClassService.save(dict, parentId);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/child/{id}")
    @RequiresPermissions("store.config.class")
    public String child(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", storeClassService.query(Cnd.where("parentId", "=", id).asc("location").asc("path")));
        return "pages/platform/store/config/class/child";
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.config.class")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        Store_class c = storeClassService.fetch(id);
        if (c != null) {
            req.setAttribute("parentUnit", storeClassService.fetch(c.getParentId()));
        }
        req.setAttribute("obj", c);
        return "pages/platform/store/config/class/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/editDo")
    @SJson
    @RequiresPermissions("store.config.class.edit")
    public Object editDo(Store_class dict, HttpServletRequest req) {
        try {
            dict.setOpBy(StringUtil.getUid());
            dict.setOpAt((int) (System.currentTimeMillis() / 1000));
            storeClassService.updateIgnoreNull(dict);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete/{id}")
    @SJson
    @RequiresPermissions("store.config.class.delete")
    public Object delete(@PathVariable String id, HttpServletRequest req) {
        try {
            Store_class dict = storeClassService.fetch(id);
            storeClassService.deleteAndChild(dict);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/tree", "/tree/{pid}"})
    @SJson
    @RequiresPermissions("store.config.class")
    public Object tree(@PathVariable(required = false) String pid, HttpServletRequest req) {
        List<Store_class> list = storeClassService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("path"));
        List<Map<String, Object>> tree = new ArrayList<>();
        for (Store_class dict : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", dict.getId());
            obj.put("text", dict.getName());
            obj.put("children", dict.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }

    @RequestMapping("/sort")
    @RequiresPermissions("store.config.class")
    public String sort(HttpServletRequest req) {
        List<Store_class> list = storeClassService.query(Cnd.orderBy().asc("location").asc("path"));
        List<Store_class> firstMenus = new ArrayList<>();
        Map<String, List<Store_class>> secondMenus = new HashMap<>();
        for (Store_class menu : list) {
            if (menu.getPath().length() > 4) {
                List<Store_class> s = secondMenus.get(StringUtil.getParentId(menu.getPath()));
                if (s == null)
                    s = new ArrayList<>();
                s.add(menu);
                secondMenus.put(StringUtil.getParentId(menu.getPath()), s);
            } else if (menu.getPath().length() == 4) {
                firstMenus.add(menu);
            }
        }
        req.setAttribute("firstMenus", firstMenus);
        req.setAttribute("secondMenus", secondMenus);
        return "pages/platform/store/config/class/sort";
    }

    @RequestMapping(value = "/sortDo/{ids}")
    @SJson
    @RequiresPermissions("store.config.class.edit")
    public Object sortDo(@PathVariable String ids, HttpServletRequest req) {
        try {
            String[] menuIds = StringUtils.split(ids, ",");
            int i = 0;
            storeClassService.dao().execute(Sqls.create("update store_class set location=0"));
            for (String s : menuIds) {
                if (!Strings.isBlank(s)) {
                    storeClassService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", s));
                    i++;
                }
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
