package com.aebiz.app.web.modules.controllers.platform.sys;

import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.app.sys.modules.models.Sys_menu;
import com.aebiz.app.sys.modules.services.SysMenuService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wizzer on 2017/1/18.
 */
@Controller
@RequestMapping("/platform/home")
public class SysHomeController {

    @Autowired
    private SysMenuService sysMenuService;

    @RequestMapping("")
    public String home() {
        return "pages/platform/sys/home";
    }

    @RequestMapping("/left")
    @RequiresAuthentication
    public String left(@PathVariable(required = false) String url, HttpServletRequest req) {
        String path = "";
        String perpath = "";
        if (!Strings.isBlank(Globals.APP_BASE)) {
            url = Strings.sBlank(url).substring(Globals.APP_BASE.length());
        }
        if (Strings.sBlank(url).indexOf("?") > 0)
            url = url.substring(0, url.indexOf("?"));
        Sys_menu menu = sysMenuService.fetch(Cnd.where("href", "=", url));
        if (menu != null) {
            if (menu.getPath().length() > 8) {
                path = menu.getPath().substring(0, 8);
                perpath = menu.getPath().substring(0, 4);
            } else if (menu.getPath().length() == 8) {
                perpath = menu.getPath().substring(0, 4);
            }
            req.setAttribute("mpath", menu.getPath());
        }
        req.setAttribute("path", path);
        req.setAttribute("perpath", perpath);
        return "pages/platform/sys/left";
    }

    @RequestMapping("/path")
    @RequiresAuthentication
    public String path(@PathVariable(required = false) String url, HttpServletRequest req) {
        if (Strings.sBlank(url).indexOf("//") > 0) {
            String[] u = url.split("//");
            String s = u[1].substring(u[1].indexOf("/"));
            if (Strings.sBlank(s).indexOf("?") > 0)
                s = s.substring(0, s.indexOf("?"));
            if (!Strings.isBlank(Globals.APP_BASE)) {
                s = s.substring(Globals.APP_BASE.length());
            }
            String[] urls = s.split("/");
            List<String> list = new ArrayList<>();
            if (urls.length > 5) {
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3] + "/" + urls[4] + "/" + urls[5]);
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3] + "/" + urls[4]);
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3]);
                list.add("/" + urls[1] + "/" + urls[2]);
                list.add("/" + urls[1]);
            } else if (urls.length == 5) {
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3] + "/" + urls[4]);
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3]);
                list.add("/" + urls[1] + "/" + urls[2]);
                list.add("/" + urls[1]);
            } else if (urls.length == 4) {
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3]);
                list.add("/" + urls[1] + "/" + urls[2]);
                list.add("/" + urls[1]);
            } else if (urls.length == 3) {
                list.add("/" + urls[1] + "/" + urls[2]);
                list.add("/" + urls[1]);
            } else if (urls.length == 2) {
                list.add("/" + urls[1]);
            } else list.add(url);
            String path = "";
            String perpath = "";
            Sys_menu menu = sysMenuService.fetch(Cnd.where("href", "in", list).desc("href").desc("path"));
            if (menu != null) {
                if (menu.getPath().length() > 8) {
                    path = menu.getPath().substring(0, 8);
                    perpath = menu.getPath().substring(0, 4);
                } else if (menu.getPath().length() == 8) {
                    perpath = menu.getPath().substring(0, 4);
                }
                req.setAttribute("mpath", menu.getPath());
            }
            req.setAttribute("path", path);
            req.setAttribute("perpath", perpath);
        }
        return "pages/platform/sys/left";
    }

    @RequestMapping("/submenu")
    @RequiresAuthentication
    @SJson
    public Object submenu(@PathVariable(required = false) String pid, HttpServletRequest req) {
        try {
            if (!Strings.isBlank(pid)) {
                List<Sys_menu> list = sysMenuService.query(Cnd.where("parentId", "=", pid).and("disabled", "=", false).and("type", "=", "menu").asc("location").asc("path"));
                for (Sys_menu menu : list) {
                    if (menu.isHasChildren()) {
                        List<Sys_menu> sublist = sysMenuService.query(Cnd.where("parentId", "=", menu.getId()).and("disabled", "=", false).and("type", "=", "menu").asc("location").asc("path"));
                        menu.setSubList(sublist);
                    } else {
                        menu.setSubList(new ArrayList<>());
                    }
                }
                return Result.success("globals.result.success", list);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
