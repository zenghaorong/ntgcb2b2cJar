package com.aebiz.app.web.modules.controllers.platform.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.aebiz.baseframework.view.annotation.SJson;
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
import com.aebiz.app.sys.modules.models.Sys_dict;
import com.aebiz.app.sys.modules.services.SysDictService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.commons.utils.StringUtil;

/**
 * Created by 王怀先 on 2017/1/19.
 */
@Controller
@RequestMapping("/platform/sys/dict")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;

    @RequestMapping("")
    @RequiresPermissions("sys.manager.dict")
    public String index(HttpServletRequest req) {
        req.setAttribute("obj", sysDictService
                .query(Cnd.where("parentId", "=", "").or("parentId", "is", null).asc("location").asc("path")));
        return "pages/platform/sys/dict/index";
    }

    @RequestMapping(value = {"/add/{id}", "/add"})
    @RequiresPermissions("sys.manager.dict")
    public String add(@PathVariable(required = false) String id, HttpServletRequest req) {
        req.setAttribute("obj", Strings.isBlank(id) ? null : sysDictService.fetch(id));
        return "pages/platform/sys/dict/add";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addDo")
    @SJson
    @RequiresPermissions("sys.manager.dict.add")
    public Object addDo(Sys_dict dict, String parentId) {
        try {
            sysDictService.save(dict, parentId);
            sysDictService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/child/{id}")
    @RequiresPermissions("sys.manager.dict")
    public String child(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", sysDictService.query(Cnd.where("parentId", "=", id).asc("location").asc("path")));
        return "pages/platform/sys/dict/child";
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.dict")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        Sys_dict dict = sysDictService.fetch(id);
        if (dict != null) {
            req.setAttribute("parentUnit", sysDictService.fetch(dict.getParentId()));
        }
        req.setAttribute("obj", dict);
        return "pages/platform/sys/dict/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/editDo")
    @SJson
    @RequiresPermissions("sys.manager.dict.edit")
    public Object editDo(Sys_dict dict, HttpServletRequest req) {
        try {
            dict.setOpBy(StringUtil.getUid());
            dict.setOpAt((int) (System.currentTimeMillis() / 1000));
            sysDictService.updateIgnoreNull(dict);
            sysDictService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete/{id}")
    @SJson
    @RequiresPermissions("sys.manager.dict.delete")
    public Object delete(@PathVariable String id, HttpServletRequest req) {
        try {
            Sys_dict dict = sysDictService.fetch(id);
            sysDictService.deleteAndChild(dict);
            sysDictService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/tree", "/tree/{pid}"})
    @SJson
    @RequiresPermissions("sys.manager.dict")
    public Object tree(@PathVariable(required = false) String pid, HttpServletRequest req) {
        List<Sys_dict> list = sysDictService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("path"));
        List<Map<String, Object>> tree = new ArrayList<>();
        for (Sys_dict dict : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", dict.getId());
            obj.put("text", dict.getName());
            obj.put("children", dict.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }

    @RequestMapping("/sort")
    @RequiresPermissions("sys.manager.dict")
    public String sort(HttpServletRequest req) {
        List<Sys_dict> list = sysDictService.query(Cnd.orderBy().asc("location").asc("path"));
        List<Sys_dict> firstMenus = new ArrayList<>();
        Map<String, List<Sys_dict>> secondMenus = new HashMap<>();
        for (Sys_dict menu : list) {
            if (menu.getPath().length() > 4) {
                List<Sys_dict> s = secondMenus.get(StringUtil.getParentId(menu.getPath()));
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
        return "pages/platform/sys/dict/sort";
    }

    @RequestMapping(value = "/sortDo/{ids}")
    @SJson
    @RequiresPermissions("sys.manager.dict.edit")
    public Object sortDo(@PathVariable String ids, HttpServletRequest req) {
        try {
            String[] menuIds = StringUtils.split(ids, ",");
            int i = 0;
            sysDictService.dao().execute(Sqls.create("update sys_dict set location=0"));
            for (String s : menuIds) {
                if (!Strings.isBlank(s)) {
                    sysDictService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", s));
                    i++;
                }
            }
            sysDictService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
