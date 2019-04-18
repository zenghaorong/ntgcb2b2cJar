package com.aebiz.app.web.modules.controllers.platform.shop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.commons.utils.PinyinUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aebiz.app.shop.modules.models.Shop_area;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;

@Controller
@RequestMapping("/platform/shop/config/area")
public class ShopAreaController {
    private static final Log log = Logs.get();
    @Autowired
    private ShopAreaService shopAreaService;

    @RequestMapping("")
    @RequiresPermissions("shop.config.delivery.area")
    public String index(HttpServletRequest req) {
        req.setAttribute("obj", shopAreaService
                .query(Cnd.where("parentId", "=", "").or("parentId", "is", null).asc("location").asc("path")));
        return "pages/platform/shop/area/index";
    }

    @RequestMapping(value = {"/add/{id}", "/add"})
    @RequiresPermissions("shop.config.delivery.area")
    public String add(@PathVariable(required = false) String id, HttpServletRequest req) throws IOException {
        req.setAttribute("obj", Strings.isBlank(id) ? null : shopAreaService.fetch(id));
        return "pages/platform/shop/area/add";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addDo")
    @SJson
    @RequiresPermissions("shop.config.delivery.area.add")
    public Object addDo(Shop_area shoparea, String parentId) {
        try {
            shopAreaService.save(shoparea, parentId);
            shopAreaService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/child/{id}")
    @RequiresPermissions("shop.config.delivery.area")
    public String child(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", shopAreaService.query(Cnd.where("parentId", "=", id).asc("location").asc("path")));
        return "pages/platform/shop/area/child";
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("shop.config.delivery.area")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        Shop_area shoparea = shopAreaService.fetch(id);
        req.setAttribute("obj", shoparea);
        return "pages/platform/shop/area/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/editDo")
    @SJson
    @RequiresPermissions("shop.config.delivery.area.edit")
    public Object editDo(Shop_area shoparea, HttpServletRequest req) {
        try {
            shoparea.setOpBy(StringUtil.getUid());
            shoparea.setOpAt((int) (System.currentTimeMillis() / 1000));
            shopAreaService.updateIgnoreNull(shoparea);
            shopAreaService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete/{id}")
    @SJson
    @RequiresPermissions("shop.config.delivery.area.delete")
    public Object delete(@PathVariable String id, HttpServletRequest req) {
        try {
            Shop_area shoparea = shopAreaService.fetch(id);
            shopAreaService.deleteAndChild(shoparea);
            shopAreaService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/enable/{id}")
    @SJson
    @RequiresPermissions("shop.config.delivery.area.delete")
    public Object enable(@PathVariable String id, HttpServletRequest req) {
        try {
            shopAreaService.update(Chain.make("disabled", false), Cnd.where("id", "=", id));
            shopAreaService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/disable/{id}")
    @SJson
    @RequiresPermissions("shop.config.delivery.area.delete")
    public Object disable(@PathVariable String id, HttpServletRequest req) {
        try {
            shopAreaService.update(Chain.make("disabled", true), Cnd.where("id", "=", id));
            shopAreaService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/tree", "/tree/{pid}"})
    @SJson
    @RequiresPermissions("shop.config.delivery.area")
    public Object tree(@PathVariable(required = false) String pid, HttpServletRequest req) {
        List<Shop_area> list = shopAreaService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("path"));
        List<Map<String, Object>> tree = new ArrayList<>();
        for (Shop_area shoparea : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", shoparea.getId());
            obj.put("text", shoparea.getName());
            obj.put("children", shoparea.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }

    @RequestMapping("/sort")
    @RequiresPermissions("shop.config.delivery.area")
    public String sort(HttpServletRequest req) {
        List<Shop_area> list = shopAreaService.query(Cnd.orderBy().asc("location").asc("path"));
        List<Shop_area> firstMenus = new ArrayList<>();
        Map<String, List<Shop_area>> secondMenus = new HashMap<>();
        for (Shop_area menu : list) {
            if (menu.getPath().length() > 4) {
                List<Shop_area> s = secondMenus.get(StringUtil.getParentId(menu.getPath()));
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
        return "pages/platform/shop/area/sort";
    }

    @RequestMapping(value = "/sortDo/{ids}")
    @SJson
    @RequiresPermissions("shop.config.delivery.area.edit")
    public Object sortDo(@PathVariable String ids, HttpServletRequest req) {
        try {
            String[] menuIds = StringUtils.split(ids, ",");
            int i = 0;
            shopAreaService.dao().execute(Sqls.create("update shop_area set location=0"));
            for (String s : menuIds) {
                if (!Strings.isBlank(s)) {
                    shopAreaService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", s));
                    i++;
                }
            }
            shopAreaService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/build")
    @RequiresPermissions("shop.config.delivery.area")
    @SJson
    public Object build(HttpServletRequest req) {
        try {
            List<Shop_area> list = shopAreaService.query(Cnd.orderBy().asc("location").asc("path"));
            StringBuilder sbAll = new StringBuilder();
            StringBuilder sbShop = new StringBuilder();
            List<Shop_area> country = new ArrayList<>();//有几个国家？
            Map<String, List<Shop_area>> allMap = new HashMap<>();//全部地区
            Map<String, List<Shop_area>> shopMap = new HashMap<>();//启用的地区
            for (Shop_area area : list) {
                if (area.getPath().length() > 4) {
                    List<Shop_area> s = allMap.get(StringUtil.getParentId(area.getPath()));
                    if (s == null) s = new ArrayList<>();
                    s.add(area);
                    allMap.put(StringUtil.getParentId(area.getPath()), s);
                    if (!area.isDisabled()) {
                        shopMap.put(StringUtil.getParentId(area.getPath()), s);
                    }
                } else if (area.getPath().length() == 4) {
                    country.add(area);
                }
            }
            for (Shop_area area : country) {//组装全部地区数据
                doAeraData(area.getCode(), area.getPath(), "all", allMap);
                doAeraData(area.getCode(), area.getPath(), "shop", shopMap);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("globals.result.error");
        }
    }

    private void doAeraData(String country, String path, String type, Map<String, List<Shop_area>> map) throws Exception {
        String filePath = Globals.APP_ROOT + "/assets/common/vendor/city-picker/js/";
        String fileName = "data." + country + "." + type + ".js";
        List<Shop_area> list = map.get(path);
        StringBuilder sbData = new StringBuilder();
        StringBuilder sbCountry = new StringBuilder();
        sbData.append(getJson(list, map, type));
        sbCountry.append(getCountry(country, list, type));
        String tpl = Files.read(filePath + "data." + country + ".tpl");
        File file = new File(filePath + fileName);
        Files.createFileIfNoExists(file);
        Files.write(file, tpl.replace("<!--#COUNTRY#-->", sbCountry.toString()).replace("<!--#DATA#-->", sbData.toString()));
    }

    /**
     * 得到省份字母数据区块
     *
     * @param country
     * @param list
     * @return
     */
    private String getCountry(String country, List<Shop_area> list, String type) {
        StringBuilder sb = new StringBuilder();
        String ag = "abcdefg";
        String hk = "hijk";
        String ls = "lmnopqrs";
        String tz = "tuvwxyz";
        StringBuilder sbAG = new StringBuilder();
        StringBuilder sbHK = new StringBuilder();
        StringBuilder sbLS = new StringBuilder();
        StringBuilder sbTZ = new StringBuilder();
        sb.append(country + ":{");
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Shop_area area = list.get(i);
                String py = PinyinUtil.getPinYinHeadChar(area.getName()).substring(0, 1);
                log.debug("py:::" + py);
                if ("all".equalsIgnoreCase(type) || ("shop".equalsIgnoreCase(type) && !area.isDisabled())) {
                    if (ag.contains(py)) {
                        sbAG.append("{code: '" + area.getCode() + "', address: '" + area.getName() + "'},");
                    }
                    log.debug("sbAG:::" + sbAG.toString());
                    if (hk.contains(py)) {
                        sbHK.append("{code: '" + area.getCode() + "', address: '" + area.getName() + "'},");
                    }
                    if (ls.contains(py)) {
                        sbLS.append("{code: '" + area.getCode() + "', address: '" + area.getName() + "'},");
                    }
                    if (tz.contains(py)) {
                        sbTZ.append("{code: '" + area.getCode() + "', address: '" + area.getName() + "'},");
                    }
                }
            }
            if (sbAG.toString().length() > 1) {
                sb.append("'A-G': [" + sbAG.toString().substring(0, sbAG.toString().length() - 1) + "],\n");
            }
            if (sbHK.toString().length() > 1) {
                sb.append("'H-K': [" + sbHK.toString().substring(0, sbHK.toString().length() - 1) + "],\n");
            }
            if (sbLS.toString().length() > 1) {
                sb.append("'L-S': [" + sbLS.toString().substring(0, sbLS.toString().length() - 1) + "],\n");
            }
            if (sbTZ.toString().length() > 1) {
                sb.append("'T-Z': [" + sbTZ.toString().substring(0, sbTZ.toString().length() - 1) + "]\n");
            }
        }
        sb.append("},");
        return sb.toString();
    }

    /**
     * 得到城市数据区块
     *
     * @param list
     * @param map
     * @return
     */
    private String getJson(List<Shop_area> list, Map<String, List<Shop_area>> map, String type) {
        if (list != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                Shop_area area = list.get(i);
                List<Shop_area> list2 = map.get(area.getPath());
                if ("all".equalsIgnoreCase(type) || ("shop".equalsIgnoreCase(type) && !area.isDisabled())) {
                    if (area.isHasChildren()) {
                        sb.append(area.getCode() + ":{");
                        sb.append(getSubJson(list2, map, type));
                        sb.append("}");
                        sb.append(",\n");
                        sb.append(getJson(list2, map, type));
                    } else {
                        sb.append(getSubJson(list2, map, type));
                    }
                }
            }
            return sb.toString();
        }
        return "";
    }

    private String getSubJson(List<Shop_area> list, Map<String, List<Shop_area>> map, String type) {
        if (list != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                Shop_area area = list.get(i);
                if ("all".equalsIgnoreCase(type) || ("shop".equalsIgnoreCase(type) && !area.isDisabled())) {
                    sb.append(area.getCode() + ":'" + area.getName() + "'");
                    if (i < list.size() - 1) {
                        sb.append(",");
                    }
                }
            }
            return sb.toString();
        }
        return "";
    }
}
