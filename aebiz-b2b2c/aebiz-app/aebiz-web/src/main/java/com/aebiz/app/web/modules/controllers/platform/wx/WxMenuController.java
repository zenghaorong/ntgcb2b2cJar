package com.aebiz.app.web.modules.controllers.platform.wx;

import com.aebiz.app.cms.modules.services.CmsChannelService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.app.wx.modules.models.Wx_config;
import com.aebiz.app.wx.modules.services.WxConfigService;
import com.aebiz.app.wx.modules.services.WxReplyService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.wx.modules.models.Wx_menu;
import com.aebiz.app.wx.modules.services.WxMenuService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.Param;
import org.nutz.weixin.bean.WxMenu;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/platform/wx/conf/menu")
public class WxMenuController {
    @Autowired
    private WxMenuService wxMenuService;
    @Autowired
    private WxConfigService wxConfigService;
    @Autowired
    private WxReplyService wxReplyService;
    @Autowired
    private CmsChannelService cmsChannelService;

    @RequestMapping(value = {"", "/{wxid}"})
    @RequiresPermissions("wx.conf.menu")
    public String index(String wxid, HttpServletRequest req) {
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxid = list.get(0).getId();
        }
        List<Wx_menu> menus = wxMenuService.query(Cnd.where("wxid", "=", wxid).asc("location").asc("path"));
        List<Wx_menu> firstMenus = new ArrayList<>();
        Map<String, List<Wx_menu>> secondMenus = new HashMap<>();
        for (Wx_menu menu : menus) {
            if (menu.getPath().length() > 4) {
                List<Wx_menu> s = secondMenus.get(StringUtil.getParentId(menu.getPath()));
                if (s == null) s = new ArrayList<>();
                s.add(menu);
                secondMenus.put(StringUtil.getParentId(menu.getPath()), s);
            } else if (menu.getPath().length() == 4) {
                firstMenus.add(menu);
            }
        }
        req.setAttribute("firstMenus", firstMenus);
        req.setAttribute("secondMenus", secondMenus);
        req.setAttribute("wxList", list);
        req.setAttribute("wxid", Strings.sBlank(wxid));
        return "pages/platform/wx/menu/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("wx.conf.menu")
    public Object data(DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        return wxMenuService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add/{wxid}")
    @RequiresPermissions("wx.conf.menu")
    public String add(@PathVariable String wxid, HttpServletRequest req) {
        req.setAttribute("wxid", wxid);
        req.setAttribute("menus", wxMenuService.query(Cnd.where("wxid", "=", wxid).and(Cnd.exps("parentId", "=", "").or("parentId", "is", null)).asc("location")));
        req.setAttribute("config", wxConfigService.fetch(wxid));
        return "pages/platform/wx/menu/add";
    }

    @RequestMapping("/checkDo")
    @SJson
    @RequiresPermissions("wx.conf.menu")
    public Object checkDo(@Param("wxid") String wxid, @Param("parentId") String parentId, HttpServletRequest req) {
        int count = wxMenuService.count(Cnd.where("wxid", "=", Strings.sBlank(wxid)).and("parentId", "=", Strings.sBlank(parentId)));
        if (Strings.isBlank(parentId) && count > 2) {
            return Result.error("只可设置三个一级菜单");
        }
        if (!Strings.isBlank(parentId) && count > 4) {
            return Result.error("只可设置五个二级菜单");
        }
        return Result.success("");
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Wx_menu")
    @RequiresPermissions("wx.conf.menu.add")
    public Object addDo(@Param("..") Wx_menu menu, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            if (Strings.isBlank(menu.getWxid())) {
                return Result.error("请选择公众号");
            }
            wxMenuService.save(menu, parentId);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("wx.conf.menu")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", wxMenuService.fetch(id));
        return "pages/platform/wx/menu/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Wx_menu")
    @RequiresPermissions("wx.conf.menu.edit")
    public Object editDo(Wx_menu menu, HttpServletRequest req) {
        try {
            wxMenuService.updateIgnoreNull(menu);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Wx_menu")
    @RequiresPermissions("wx.conf.menu.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids", required = false) String[] ids, HttpServletRequest req) {
        try {
            Wx_menu menu = wxMenuService.fetch(id);
            req.setAttribute("menuName", menu.getMenuName());
            wxMenuService.deleteAndChild(menu);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("wx.conf.menu")
    public String detail(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            req.setAttribute("obj", wxMenuService.fetch(id));
        } else {
            req.setAttribute("obj", null);
        }
        return "pages/platform/wx/menu/detail";
    }

    @RequestMapping("/pushMenu/{wxid}")
    @SJson
    @RequiresPermissions("wx.conf.menu.push")
    @SLog(description = "Wx_menu")
    public Object pushMenu(@PathVariable String wxid, HttpServletRequest req) {
        try {
            Wx_config config = wxConfigService.fetch(wxid);
            WxApi2 wxApi2 = wxConfigService.getWxApi2(wxid);
            List<Wx_menu> list = wxMenuService.query(Cnd.where("wxid", "=", wxid).asc("location"));
            req.setAttribute("name", config.getAppname());
            List<Wx_menu> firstMenus = new ArrayList<>();
            Map<String, List<Wx_menu>> secondMenus = new HashMap<>();
            for (Wx_menu menu : list) {
                if (menu.getPath().length() > 4) {
                    List<Wx_menu> s = secondMenus.get(StringUtil.getParentId(menu.getPath()));
                    if (s == null) s = new ArrayList<>();
                    s.add(menu);
                    secondMenus.put(StringUtil.getParentId(menu.getPath()), s);
                } else if (menu.getPath().length() == 4) {
                    firstMenus.add(menu);
                }
            }
            List<WxMenu> m1 = new ArrayList<>();
            for (Wx_menu firstMenu : firstMenus) {
                WxMenu xm1 = new WxMenu();
                if (firstMenu.isHasChildren()) {
                    List<WxMenu> m2 = new ArrayList<>();
                    xm1.setName(firstMenu.getMenuName());
                    if (secondMenus.get(firstMenu.getPath()).size() > 0) {
                        for (Wx_menu secondMenu : secondMenus.get(firstMenu.getPath())) {
                            WxMenu xm2 = new WxMenu();
                            if ("view".equals(secondMenu.getMenuType())) {
                                xm2.setType(secondMenu.getMenuType());
                                xm2.setUrl(secondMenu.getUrl());
                                xm2.setName(secondMenu.getMenuName());
                            } else if ("click".equals(secondMenu.getMenuType())) {
                                xm2.setType(secondMenu.getMenuType());
                                xm2.setKey(secondMenu.getMenuKey());
                                xm2.setName(secondMenu.getMenuName());
                            } else if ("miniprogram".equals(secondMenu.getMenuType())) {
                                xm2.setType(secondMenu.getMenuType());
                                xm2.setName(secondMenu.getMenuName());
                                xm2.setUrl(secondMenu.getUrl());
                                xm2.setAppid(secondMenu.getAppid());
                                xm2.setPagepath(secondMenu.getPagepath());
                            } else {
                                xm2.setName(secondMenu.getMenuName());
                                xm2.setType("click");
                                xm2.setKey(secondMenu.getMenuName());
                            }
                            m2.add(xm2);
                        }
                        xm1.setSubButtons(m2);
                    }
                    m1.add(xm1);
                } else {
                    WxMenu xm2 = new WxMenu();
                    if ("view".equals(firstMenu.getMenuType())) {
                        xm2.setType(firstMenu.getMenuType());
                        xm2.setUrl(firstMenu.getUrl());
                        xm2.setName(firstMenu.getMenuName());
                    } else if ("click".equals(firstMenu.getMenuType())) {
                        xm2.setType(firstMenu.getMenuType());
                        xm2.setKey(firstMenu.getMenuKey());
                        xm2.setName(firstMenu.getMenuName());
                    } else if ("miniprogram".equals(firstMenu.getMenuType())) {
                        xm2.setType(firstMenu.getMenuType());
                        xm2.setName(firstMenu.getMenuName());
                        xm2.setUrl(firstMenu.getUrl());
                        xm2.setAppid(firstMenu.getAppid());
                        xm2.setPagepath(firstMenu.getPagepath());
                    } else {
                        xm2.setName(firstMenu.getMenuName());
                        xm2.setType("click");
                        xm2.setKey(firstMenu.getMenuName());
                    }
                    m1.add(xm2);
                }
            }
            WxResp wxResp = wxApi2.menu_create(m1);
            if (wxResp.errcode() != 0) {
                return Result.error(wxResp.errmsg());
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }


    @RequestMapping("/keyword/{wxid}")
    @RequiresPermissions("wx.conf.menu")
    public String keyword(@PathVariable String wxid, HttpServletRequest req) {
        req.setAttribute("wxid", wxid);
        return "pages/platform/wx/menu/keyword";
    }

    @RequestMapping("/keywordData")
    @SJson("full")
    @RequiresPermissions("wx.conf.menu")
    public Object keywordData(@Param("wxid") String wxid, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
            cnd.and("type", "=", "keyword");
        }

        return wxReplyService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }


    @RequestMapping("/cms/{type}")
    @RequiresPermissions("wx.conf.menu")
    public String cms(@PathVariable String type, HttpServletRequest req) {
        req.setAttribute("type", type);
        return "pages/platform/wx/menu/cms";
    }

    @RequestMapping("/cmsData/{type}")
    @SJson("full")
    @RequiresPermissions("wx.conf.menu")
    public Object cmsData(@PathVariable String type, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if ("channel".equals(type)) {
            return cmsChannelService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
        } else {
            return cmsChannelService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
        }
    }
}
