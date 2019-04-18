package com.aebiz.app.web.modules.controllers.platform.goods;

import com.aebiz.app.goods.modules.models.*;
import com.aebiz.app.goods.modules.services.*;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/platform/goods/spec")
public class GoodsSpecController {

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private GoodsSpecValuesService goodsSpecValuesService;

    @Autowired
    private GoodsTypeSpecService goodsTypeSpecService;

    @Autowired
    private GoodsTypeSpecValuesService goodsTypeSpecValuesService;

    @Autowired
    private GoodsTypeService goodsTypeService;

    @RequestMapping("/image")
    @RequiresPermissions("goods.conf.spec")
    public String index(@RequestParam("w") int w, @RequestParam("h") int h, HttpServletRequest req) {
        req.setAttribute("w", w);
        req.setAttribute("h", h);
        return "pages/platform/goods/spec/image";
    }

    @RequestMapping("")
    @RequiresPermissions("goods.conf.spec")
    public String index() {
        return "pages/platform/goods/spec/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("goods.conf.spec")
    public Object data(@RequestParam(value = "name",required = false) String name, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(name)) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(name) + "%"));
        }
        return goodsSpecService.data(dataTable.getLength(), dataTable.getStart(),
                dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }


    @RequestMapping("/add")
    @RequiresPermissions("goods.conf.spec")
    public String add() {
        return "pages/platform/goods/spec/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @RequiresPermissions("goods.conf.spec.add")
    @SLog(description = "新建商品规格")
    public Object addDo(Goods_spec goodsSpec, @RequestParam(value = "spec_value", required = false) String[] spec_value, @RequestParam(value = "spec_alias", required = false) String[] spec_alias, @RequestParam(value = "spec_picurl", required = false) String[] spec_picurl, HttpServletRequest req) {
        try {
            if (spec_value == null || spec_picurl == null) {
                return Result.error("规格值不能为空");
            }

            goodsSpecService.add(goodsSpec, spec_value, spec_alias, spec_picurl);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("goods.conf.spec")
    public Object edit(@PathVariable(required = false) String id, HttpServletRequest req) {
        Goods_spec goods_spec = goodsSpecService.fetch(id);
        goodsSpecService.fetchLinks(goods_spec, "specValues");
        req.setAttribute("obj", goods_spec);

        return "pages/platform/goods/spec/edit";
    }

    @RequestMapping("/canNotDel/{id}")
    @SJson
    public Object canNotDel(@PathVariable String id, HttpServletRequest req) {
        Map<String, Object> resutlMap = new HashMap<>();
        resutlMap.put("code", "1");
        resutlMap.put("msg", "可以删除");
        List<Goods_type_spec_values> goods_type_spec_values = goodsTypeSpecValuesService.query(Cnd.where("specValueId", "=", id));
        if (goods_type_spec_values.size() == 0) {
            return resutlMap;
        }

        List<String> typeIds = new ArrayList<>(goods_type_spec_values.size());
        for (Goods_type_spec_values temp : goods_type_spec_values) {
            typeIds.add(temp.getTypeId());
        }

        if (typeIds.size() == 0) {
            return resutlMap;
        }

        List<Goods_type> types = goodsTypeService.query(Cnd.where("id", "in", typeIds));
        String msg = "在 ";
        for (Goods_type temp : types) {
            msg += temp.getName() + " ";
        }

        msg += "类型中已使用，不允许删除";
        resutlMap.put("code", "0");
        resutlMap.put("msg", msg);
        return resutlMap;

    }

    @RequestMapping("/editDo")
    @SJson
    @RequiresPermissions("goods.conf.spec.edit")
    @SLog(description = "修改商品规格")
    public Object editDo(Goods_spec goodsSpec, @RequestParam(value = "spec_value", required = false) String[] spec_value, @RequestParam(value = "spec_alias", required = false) String[] spec_alias, @RequestParam("spec_picurl") String[] spec_picurl, @RequestParam("spec_value_id") String[] spec_value_id, HttpServletRequest req) {
        try {
            goodsSpecService.update(goodsSpec, spec_value, spec_alias, spec_picurl, spec_value_id, StringUtil.getUid());
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }


    @RequestMapping({"/delete", "/delete/{id}"})
    @SJson
    @RequiresPermissions("goods.conf.spec.delete")
    @SLog(description = "删除商品规格")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids[]", required = false) String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                return Result.error("不允许批量删除");
//                goodsSpecService.deleteSpec(ids);
            } else {
                List<Goods_type_spec> templist = goodsTypeSpecService.query(Cnd.where("specId", "=", id));
                if (templist.size() > 0) {
                    String errMsg = "";
                    for (Goods_type_spec goodsSpecTemp : templist) {
                        errMsg += " " + goodsTypeService.fetch(goodsSpecTemp.getTypeId()).getName();
                    }

                    return Result.error("在" + errMsg + " 类型中已使用，不允许删除");
                }

                goodsSpecService.deleteSpec(id);

            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }


}
