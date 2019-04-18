package com.aebiz.app.web.modules.controllers.store.sys;

import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountLoginService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.store.modules.models.Store_menu;
import com.aebiz.app.store.modules.models.Store_user;
import com.aebiz.app.store.modules.services.StoreMenuService;
import com.aebiz.app.store.modules.services.StoreUserService;
import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wizzer on 2017/1/18.
 */
@Controller
@RequestMapping("/store/home")
public class StoreHomeController {

    @Autowired
    private StoreMenuService storeMenuService;
    @Autowired
    private StoreUserService storeUserService;
    @Autowired
    private AccountUserService accountUserService;
    @Autowired
    private AccountLoginService accountLoginService;

    @RequestMapping("")
    public String home() {
        return "pages/store/sys/home";
    }

    @RequestMapping("/left")
    @RequiresAuthentication
    public String left(@Param("url") String url, HttpServletRequest req) {
        String path = "";
        String perpath = "";
        if (!Strings.isBlank(Globals.APP_BASE)) {
            url = Strings.sBlank(url).substring(Globals.APP_BASE.length());
        }
        if (Strings.sBlank(url).indexOf("?") > 0)
            url = url.substring(0, url.indexOf("?"));
        Store_menu menu = storeMenuService.fetch(Cnd.where("href", "=", url));
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
        return "pages/store/sys/left";
    }

    @RequestMapping("/path")
    @RequiresAuthentication
    public String path(@Param("url") String url, HttpServletRequest req) {
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
            Store_menu menu = storeMenuService.fetch(Cnd.where("href", "in", list).desc("href").desc("path"));
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
        return "pages/store/sys/left";
    }

    @RequestMapping("/submenu")
    @RequiresAuthentication
    @SJson
    public Object submenu(@Param("pid") String pid, HttpServletRequest req) {
        try {
            if (!Strings.isBlank(pid)) {
                List<Store_menu> list = storeMenuService.query(Cnd.where("parentId", "=", pid).and("disabled", "=", false).and("type", "=", "menu").asc("location").asc("path"));
                for (Store_menu menu : list) {
                    if (menu.isHasChildren()) {
                        List<Store_menu> sublist = storeMenuService.query(Cnd.where("parentId", "=", menu.getId()).and("disabled", "=", false).and("type", "=", "menu").asc("location").asc("path"));
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

    @RequestMapping("/pass")
    @RequiresAuthentication
    public String pass() {
        return "pages/store/sys/pass";
    }

    @RequestMapping("/mode")
    @RequiresAuthentication
    public String mode() {
        return "pages/store/sys/mode";
    }

    @RequestMapping("/modeDo")
    @RequiresAuthentication
    @SJson
    public Object modeDo(@RequestParam("mode") String mode, HttpServletRequest req) {
        try {
            storeUserService.update(Chain.make("loginPjax", "true".equals(mode)), Cnd.where("id", "=", StringUtil.getStoreUid()));
            Subject subject = SecurityUtils.getSubject();
            Store_user user = (Store_user) subject.getPrincipal();
            if ("true".equals(mode)) {
                user.setLoginPjax(true);
            } else {
                user.setLoginPjax(false);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/doChangePassword")
    @RequiresAuthentication
    @SJson
    public Object doChangePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, HttpServletRequest req) {
        Subject subject = SecurityUtils.getSubject();
        Store_user user = (Store_user) subject.getPrincipal();
        Account_user accountUser=accountUserService.fetch(Cnd.where("accountId","=",user.getAccountId()));
        String old = new Sha256Hash(oldPassword, accountUser.getSalt(), 1024).toBase64();
        if (old.equals(accountUser.getPassword())) {
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash(newPassword, salt, 1024).toBase64();
            accountUser.setSalt(salt);
            accountUser.setPassword(hashedPasswordBase64);
            accountUserService.update(Chain.make("salt", salt).add("password", hashedPasswordBase64), Cnd.where("id", "=", accountUser.getId()));
            return Result.success("sys.login.error.success");
        } else {
            return Result.error("sys.login.error.oldpassword");
        }
    }
}
