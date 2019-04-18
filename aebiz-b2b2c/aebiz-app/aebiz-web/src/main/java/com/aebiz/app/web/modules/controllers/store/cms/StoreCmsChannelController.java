package com.aebiz.app.web.modules.controllers.store.cms;

import com.aebiz.app.cms.modules.models.Cms_channel;
import com.aebiz.app.cms.modules.services.CmsChannelService;
import com.aebiz.app.store.modules.models.Store_user;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/store/cms/channel")
public class StoreCmsChannelController {
    @Autowired
    private CmsChannelService cmsChannelService;

    @RequestMapping("")
    @RequiresPermissions("store.cms.channel")
    public String index(HttpServletRequest req) {
        Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
        req.setAttribute("list", cmsChannelService
                .query(Cnd.where("storeId", "=", user.getStoreId()).and("parentId", "=", "").or("parentId", "is", null).asc("location").asc("path")));
        return "pages/store/cms/channel/index";
    }

    @RequestMapping({"/add/{id}", "/add"})
    @RequiresPermissions("store.cms.channel")
    public String add(@PathVariable(required = false) String id, HttpServletRequest req) {
        req.setAttribute("obj", Strings.isBlank(id) ? null : cmsChannelService.fetch(id));
        return "pages/store/cms/channel/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Cms_channel")
    @RequiresPermissions("store.cms.channel.add")
    public Object addDo(Cms_channel cmsChannel, String parentId) {
        try {
            Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
            cmsChannel.setStoreId(user.getStoreId());
            cmsChannelService.save(cmsChannel, parentId);
            cmsChannelService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/child/{id}")
    @RequiresPermissions("store.cms.channel")
    public String child(@PathVariable String id, HttpServletRequest req) {
        Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
        req.setAttribute("obj", cmsChannelService.query(Cnd.where("storeId", "=", user.getStoreId()).and("parentId", "=", id).asc("location").asc("path")));
        return "pages/store/cms/channel/child";
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.cms.channel")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        Cms_channel channel = cmsChannelService.fetch(id);
        if (null != channel) {
            if (null != channel.getParentId()) {
                Cms_channel pchannel = cmsChannelService.fetch(channel.getParentId());
                if (null != pchannel) {
                    channel.setParentId(pchannel.getName());
                }
            }
            req.setAttribute("obj", channel);
        }
        return "pages/store/cms/channel/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Cms_channel")
    @RequiresPermissions("store.cms.channel.edit")
    public Object editDo(Cms_channel cmsChannel, HttpServletRequest req) {
        try {
            cmsChannelService.editDo(cmsChannel);
            cmsChannelService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/delete/{id}")
    @SJson
    @SLog(description = "Cms_channel")
    @RequiresPermissions("store.cms.channel.delete")
    public Object delete(@PathVariable String id, HttpServletRequest req) {
        try {
            Cms_channel channel = cmsChannelService.fetch(id);
            cmsChannelService.deleteAndChild(channel);
            cmsChannelService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/sort")
    @RequiresPermissions("store.cms.channel")
    public String sort(HttpServletRequest req) {
        Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
        List<Cms_channel> list = cmsChannelService.query(Cnd.where("storeId", "=", user.getStoreId()).asc("location").asc("path"));
        List<Cms_channel> firstMenus = new ArrayList<>();
        Map<String, List<Cms_channel>> secondMenus = new HashMap<>();
        for (Cms_channel menu : list) {
            if (menu.getPath().length() > 4) {
                List<Cms_channel> s = secondMenus.get(StringUtil.getParentId(menu.getPath()));
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
        return "pages/store/cms/channel/sort";
    }

    @RequestMapping("/sortDo/{ids}")
    @SJson
    @RequiresPermissions("store.cms.channel.edit")
    public Object sortDo(@PathVariable String ids, HttpServletRequest req) {
        try {
            String[] menuIds = StringUtils.split(ids, ",");
            int i = 0;
            cmsChannelService.dao().execute(Sqls.create("update cms_channel set location=0"));
            for (String s : menuIds) {
                if (!Strings.isBlank(s)) {
                    cmsChannelService.update(Chain.make("location", i), Cnd.where("id", "=", s));
                    i++;
                }
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/enable/{id}")
    @SJson
    @RequiresPermissions("store.cms.channel.edit")
    @SLog(description = "启用菜单")
    public Object enable(@PathVariable String id, HttpServletRequest req) {
        try {
            cmsChannelService.update(Chain.make("disabled", false), Cnd.where("id", "=", id));
            cmsChannelService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/tree", "/tree/{pid}"})
    @SJson
    @RequiresPermissions("store.cms.channel")
    public Object tree(@PathVariable(required = false) String pid, HttpServletRequest req) {
        Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
        List<Cms_channel> list = cmsChannelService.query(Cnd.where("storeId", "=", user.getStoreId()).and("parentId", "=", Strings.sBlank(pid)).asc("path"));
        List<Map<String, Object>> tree = new ArrayList<>();
        for (Cms_channel channel : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", channel.getId());
            obj.put("text", channel.getName());
            obj.put("children", channel.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }

    @RequestMapping("/disable/{id}")
    @SJson
    @RequiresPermissions("store.cms.channel.edit")
    @SLog(description = "禁用菜单")
    public Object disable(@PathVariable String id, HttpServletRequest req) {
        try {
            cmsChannelService.update(Chain.make("disabled", true), Cnd.where("id", "=", id));
            cmsChannelService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
