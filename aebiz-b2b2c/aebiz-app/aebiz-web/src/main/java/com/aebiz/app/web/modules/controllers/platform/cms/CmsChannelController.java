package com.aebiz.app.web.modules.controllers.platform.cms;

import com.aebiz.app.cms.modules.models.Cms_channel;
import com.aebiz.app.cms.modules.services.CmsChannelService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/platform/cms/channel")
public class CmsChannelController {
    @Autowired
    private CmsChannelService cmsChannelService;

    @RequestMapping("")
    @RequiresPermissions("cms.content.channel")
    public String index(HttpServletRequest req) {
        req.setAttribute("list", cmsChannelService
                .query(Cnd.where("parentId", "=", "").or("parentId", "is", null).asc("location").asc("path")));
        return "pages/platform/cms/channel/index";
    }

    @RequestMapping({"/add/{id}", "/add"})
    @RequiresPermissions("cms.content.channel")
    public String add(@PathVariable(required = false) String id, HttpServletRequest req) {
        req.setAttribute("obj", Strings.isBlank(id) ? null : cmsChannelService.fetch(id));
        return "pages/platform/cms/channel/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Cms_channel")
    @RequiresPermissions("cms.content.channel.add")
    public Object addDo(Cms_channel cmsChannel, String parentId) {
        try {
            cmsChannelService.save(cmsChannel, parentId);
            cmsChannelService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/child/{id}")
    @RequiresPermissions("cms.content.channel")
    public String child(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", cmsChannelService.query(Cnd.where("parentId", "=", id).asc("location").asc("path")));
        return "pages/platform/cms/channel/child";
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("cms.content.channel")
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
        return "pages/platform/cms/channel/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Cms_channel")
    @RequiresPermissions("cms.content.channel.edit")
    public Object editDo(Cms_channel cmsChannel, HttpServletRequest req) {
        try {
            cmsChannel.setOpBy(StringUtil.getUid());
            cmsChannel.setOpAt((int) (System.currentTimeMillis() / 1000));
            String path, parentpath;
            // 前台没有传path值 所以在此查取
            if (null != cmsChannel.getParentId() && "" != cmsChannel.getParentId()) {
                parentpath = cmsChannelService.fetch(Cnd.where("id", "=", cmsChannel.getParentId())).getPath();
                path = cmsChannelService.fetch(Cnd.where("id", "=", cmsChannel.getId())).getPath();
                cmsChannel.setPath(parentpath + path.substring(path.length() - 4, path.length()));
            } else {
                path = cmsChannelService.fetch(Cnd.where("id", "=", cmsChannel.getId())).getPath();
                cmsChannel.setPath(path.substring(path.length() - 4, path.length()));
            }
            cmsChannelService.updateIgnoreNull(cmsChannel);
            // 若上级目录以前无子集 则更改hasChildren
            cmsChannelService.update(Chain.make("hasChildren", true), Cnd.where("id", "=", cmsChannel.getParentId()));
            List<Cms_channel> cmsChannels = cmsChannelService.query(Cnd.where("hasChildren", "=", true));
            // 更改后父类无Children则更改其hasChildren为false
            for (Cms_channel s_cmsChannel : cmsChannels) {
                if (null == cmsChannelService.fetch(Cnd.where("parentId", "=", s_cmsChannel.getId()))) {
                    cmsChannelService.update(org.nutz.dao.Chain.make("hasChildren", false),
                            Cnd.where("id", "=", s_cmsChannel.getId()));
                }
            }
            cmsChannelService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/delete/{id}")
    @SJson
    @SLog(description = "Cms_channel")
    @RequiresPermissions("cms.content.channel.delete")
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
    @RequiresPermissions("cms.content.channel")
    public String sort(HttpServletRequest req) {
        List<Cms_channel> list = cmsChannelService.query(Cnd.orderBy().asc("location").asc("path"));
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
        return "pages/platform/cms/channel/sort";
    }

    @RequestMapping("/sortDo/{ids}")
    @SJson
    @RequiresPermissions("cms.content.channel.edit")
    public Object sortDo(@PathVariable String ids, HttpServletRequest req) {
        try {
            String[] menuIds = StringUtils.split(ids, ",");
            int i = 0;
            cmsChannelService.dao().execute(Sqls.create("update cms_channel set location=0"));
            for (String s : menuIds) {
                if (!Strings.isBlank(s)) {
                    cmsChannelService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", s));
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
    @RequiresPermissions("cms.content.channel.edit")
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
    @RequiresPermissions("cms.content.channel")
    public Object tree(@PathVariable(required = false) String pid, HttpServletRequest req) {
        List<Cms_channel> list = cmsChannelService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("path"));
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
    @RequiresPermissions("cms.content.channel.edit")
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
