package com.aebiz.app.web.modules.controllers.platform.goods;

import com.aebiz.app.goods.modules.services.GoodsTypeService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.goods.modules.models.Goods_class;
import com.aebiz.app.goods.modules.services.GoodsClassService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/platform/goods/class")
public class GoodsClassController {
    @Autowired
    private GoodsClassService goodsClassService;
    @Autowired
    private GoodsTypeService goodsTypeService;

    @RequestMapping("")
    @RequiresPermissions("goods.conf.class")
    public String index(HttpServletRequest req) {
        List<Goods_class> list = goodsClassService.query(Cnd.where("parentId", "=", "").or("parentId", "is", null).asc("location").asc("path"));
        goodsClassService.fetchLinks(list, "goodsType");
        req.setAttribute("obj", list);
        return "pages/platform/goods/class/index";
    }

    /**
     * 获取商品分类及分类的商品类型
     *
     * @param id 商品分类ID
     * @return 商品分类JSON
     */
    @RequestMapping("/{id}")
    @SJson("full")
    @RequiresAuthentication
    public Object getClass(@PathVariable("id") String id) {
        return Result.success("", goodsClassService.fetchLinks(goodsClassService.fetch(id), "goodsType"));
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("goods.conf.class")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return goodsClassService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("goods.conf.class")
    public String add(@RequestParam(value = "pid", required = false) String pid, HttpServletRequest req) {
        if (!Strings.isEmpty(pid)) {
            req.setAttribute("pobj", goodsClassService.fetch(pid));
        }
        req.setAttribute("typeList", goodsTypeService.query(Cnd.NEW()));
        return "pages/platform/goods/class/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "新建商品分类")
    @RequiresPermissions("goods.conf.class.add")
    public Object addDo(Goods_class goodsClass, @RequestParam("parentId") String parentId, HttpServletRequest req) {
        try {
            goodsClassService.save(goodsClass, parentId);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("goods.conf.class")
    public String edit(@PathVariable String id, HttpServletRequest req) {

        Goods_class obj = goodsClassService.fetch(id);
        if (!Strings.isEmpty(obj.getParentId())) {
            req.setAttribute("pobj", goodsClassService.fetch(obj.getParentId()));
        }
        req.setAttribute("typeList", goodsTypeService.query(Cnd.NEW()));
        req.setAttribute("obj", obj);
        return "pages/platform/goods/class/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "修改商品分类")
    @RequiresPermissions("goods.conf.class.edit")
    public Object editDo(Goods_class goodsClass, HttpServletRequest req) {
        try {
            goodsClass.setOpAt((int) (System.currentTimeMillis() / 1000));
            goodsClassService.updateIgnoreNull(goodsClass);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "删除商品分类")
    @RequiresPermissions("goods.conf.class.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids", required = false) String[] ids, HttpServletRequest req) {
        try {
            Goods_class obj = goodsClassService.fetch(id);
            goodsClassService.deleteAndChild(obj);
            req.setAttribute("name", obj.getName());
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/enable/{id}")
    @SJson
    @RequiresPermissions("goods.conf.class.edit")
    @SLog(description = "启用商品分类")
    public Object enable(@PathVariable String id, HttpServletRequest req) {
        try {
            req.setAttribute("name", goodsClassService.fetch(id).getName());
            goodsClassService.update(org.nutz.dao.Chain.make("disabled", false), Cnd.where("id", "=", id));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/disable/{id}")
    @SJson
    @RequiresPermissions("goods.conf.class.edit")
    @SLog(description = "禁用商品分类")
    public Object disable(@PathVariable(required = false) String id, HttpServletRequest req) {
        try {
            req.setAttribute("name", goodsClassService.fetch(id).getName());
            goodsClassService.update(org.nutz.dao.Chain.make("disabled", true), Cnd.where("id", "=", id));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/tree")
    @SJson
    @RequiresAuthentication
    public Object tree(@RequestParam(value = "pid", required = false) String pid) {
        List<Goods_class> list = goodsClassService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("location").asc("path"));
        List<Map<String, Object>> tree = new ArrayList<>();
        for (Goods_class menu : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", menu.getId());
            obj.put("text", menu.getName());
            obj.put("children", menu.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }

    @RequestMapping("/child/{id}")
    @RequiresPermissions("goods.conf.class")
    public Object child(@PathVariable String id, HttpServletRequest req) {
        List<Goods_class> list = goodsClassService.query(Cnd.where("parentId", "=", id).asc("location").asc("path"));
        for (Goods_class goodsClass : list) {
            goodsClassService.fetchLinks(goodsClass, "goodsType");
        }
        req.setAttribute("obj", list);
        return "pages/platform/goods/class/child";

    }

    @RequestMapping("/sort")
    @RequiresPermissions("goods.conf.class")
    public String sort(HttpServletRequest req) {
        List<Goods_class> list = goodsClassService.query(Cnd.orderBy().asc("location").asc("path"));
        List<Goods_class> firstMenus = new ArrayList<>();
        Map<String, List<Goods_class>> secondMenus = new HashMap<>();
        for (Goods_class menu : list) {
            if (menu.getPath().length() > 4) {
                List<Goods_class> s = secondMenus.get(StringUtil.getParentId(menu.getPath()));
                if (s == null) s = new ArrayList<>();
                s.add(menu);
                secondMenus.put(StringUtil.getParentId(menu.getPath()), s);
            } else if (menu.getPath().length() == 4) {
                firstMenus.add(menu);
            }
        }
        req.setAttribute("firstMenus", firstMenus);
        req.setAttribute("secondMenus", secondMenus);
        return "pages/platform/goods/class/sort";
    }

    @RequestMapping("/sortDo")
    @SJson
    @RequiresPermissions("goods.conf.class.edit")
    public Object sortDo(@RequestParam("ids") String ids, HttpServletRequest req) {
        try {
            String[] menuIds = StringUtils.split(ids, ",");
            int i = 0;
            goodsClassService.dao().execute(Sqls.create("update Goods_class set location=0"));
            for (String s : menuIds) {
                if (!Strings.isBlank(s)) {
                    goodsClassService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", s));
                    i++;
                }
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

}
