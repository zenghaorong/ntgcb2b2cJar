package com.aebiz.app.web.modules.controllers.platform.goods;

import com.aebiz.app.goods.modules.models.*;
import com.aebiz.app.goods.modules.services.*;
import com.aebiz.app.member.modules.services.MemberLevelService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
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
import java.util.List;

@Controller
@RequestMapping("/platform/goods/type")
public class GoodsTypeController {

    @Autowired
    private GoodsTypeService goodsTypeService;

    @Autowired
    private GoodsBrandService goodsBrandService;

    @Autowired
    private GoodsTypeBrandService goodsTypeBrandService;

    @Autowired
    private GoodsTypePropsService goodsTypePropsService;

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private GoodsSpecValuesService goodsSpecValuesService;

    @Autowired
    private GoodsTypeParamgService goodsTypeParamgService;

    @Autowired
    private GoodsClassService goodsClassService;

    @RequestMapping("")
    @RequiresPermissions("goods.conf.type")
    public String index() {
        return "pages/platform/goods/type/index";
    }

    /**
     * 获取商品类型信息
     * @param id 类型ID
     * @return 类型JSON
     */
    @RequestMapping("/{id}")
    @SJson("full")
    @RequiresAuthentication
    public Object get(@PathVariable("id") String id) {
        return Result.success("", goodsTypeService.fetch(id));
    }

    /**
     * 获取商品类型的扩展属性
     *
     * @param id 类型ID
     * @return 扩展属性列表JSON
     */
    @RequestMapping("/{id}/props")
    @SJson("full")
    @RequiresAuthentication
    public Object getProps(@PathVariable("id")String id) {
        List<Goods_type_props> list = goodsTypePropsService.query(Cnd.where("typeId", "=", id).asc("location"));
        goodsTypePropsService.fetchLinks(list, "propsValues", Cnd.orderBy().asc("location"));
        return Result.success("", list);
    }

    /**
     * 获取商品类型的详细参数
     * @param id 类型ID
     * @return 详细参数列表
     */
    @RequestMapping("/{id}/params")
    @SJson("full")
    @RequiresAuthentication
    public Object getParam(@PathVariable("id")String id) {
        List<Goods_type_paramg> list = goodsTypeParamgService.query(Cnd.where("typeId", "=", id).asc("location"));
        goodsTypeParamgService.fetchLinks(list, "params", Cnd.orderBy().asc("location"));
        return Result.success("", list);
    }

    /**
     * 通过商品类型获取品牌
     * @param id 类型ID
     * @return 品牌列表JSON
     */
    @RequestMapping("/{id}/brands")
    @SJson("full")
    @RequiresAuthentication
    public Object getBrand(@PathVariable("id")String id) {
        List<Goods_type_brand> list = goodsTypeBrandService.query(Cnd.where("typeId", "=", id).asc("location"));
        goodsTypeBrandService.fetchLinks(list, "brand", Cnd.orderBy().asc("location"));
        return Result.success("", list);
    }

    @RequestMapping("/spec")
    @RequiresPermissions("goods.conf.type")
    public Object spec(HttpServletRequest req) {
        req.setAttribute("obj", goodsSpecService.query(Cnd.orderBy().asc("location")));
        return "pages/platform/goods/type/spec";
    }

    @RequestMapping("/spec_val/{id}")
    @SJson
    @RequiresPermissions("goods.conf.type")
    public Object spec_val(@PathVariable(required = false) String id) {
        return Result.success("", goodsSpecValuesService.query(Cnd.where("specId", "=", id).asc("location")));
    }

    @RequestMapping("/next/{id}")
    @RequiresPermissions("goods.conf.type")
    public Object next(@PathVariable(required = false) String id, HttpServletRequest req) {
        req.setAttribute("obj", goodsTypeService.fetch(id));
        return "pages/platform/goods/type/next";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("goods.conf.type")
    public Object data(@RequestParam(value = "name",required = false) String name, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(name)) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(name) + "%"));
        }
        return goodsTypeService.data(dataTable.getLength(), dataTable.getStart(),
                dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("goods.conf.type")
    public String add(@RequestParam("physical") int physical,
                      @RequestParam("hasBrand") int hasBrand,
                      @RequestParam("hasProp") int hasProp,
                      @RequestParam("hasSpec") int hasSpec,
                      @RequestParam("hasParam") int hasParam,
                      @RequestParam("hasTab") int hasTab,
                      HttpServletRequest req) {
        req.setAttribute("physical", physical);
        req.setAttribute("hasBrand", hasBrand);
        req.setAttribute("hasProp", hasProp);
        req.setAttribute("hasSpec", hasSpec);
        req.setAttribute("hasParam", hasParam);
        req.setAttribute("hasTab", hasTab);
        req.setAttribute("brandList", goodsBrandService.query(Cnd.orderBy().asc("location")));

        return "pages/platform/goods/type/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @RequiresPermissions("goods.conf.type.add")
    @SLog(description = "新建商品类型")
    public Object addDo(Goods_type goods_type,
                        @RequestParam(value = "price_min", required = false) int[] price_min,
                        @RequestParam(value = "price_max", required = false) int[] price_max,
                        @RequestParam(value = "brand", required = false) String[] brand,
                        @RequestParam(value = "props_name", required = false) String[] props_name,
                        @RequestParam(value = "props_type", required = false) String[] props_type,
                        @RequestParam(value = "props_values", required = false) String[] props_values,
                        @RequestParam(value = "specId", required = false) String[] specId,
                        @RequestParam(value = "specValIds", required = false) String[] specValIds,
                        @RequestParam(value = "specValUrls", required = false) String[] specValUrls,
                        @RequestParam(value = "specValText", required = false) String[] specValText,
                        @RequestParam(value = "group_name", required = false) String[] group_name,
                        @RequestParam(value = "group_params", required = false) String[] group_params,
                        @RequestParam(value = "tab_name", required = false) String[] tab_name,
                        @RequestParam(value = "tab_note", required = false) String[] tab_note, HttpServletRequest req) {
        try {
            goodsTypeService.add(goods_type, brand, props_name, props_type, props_values, specId, specValIds, specValUrls, specValText, group_name, group_params, tab_name, tab_note);
            goodsTypeService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("goods.conf.type")
    public Object edit(@PathVariable(required = false) String id,
                       @RequestParam("physical") int physical,
                       @RequestParam("hasBrand") int hasBrand,
                       @RequestParam("hasProp") int hasProp,
                       @RequestParam("hasSpec") int hasSpec,
                       @RequestParam("hasParam") int hasParam,
                       @RequestParam("hasTab") int hasTab,
                       HttpServletRequest req) {
        Goods_type obj = goodsTypeService.fetch(id);
        req.setAttribute("physical", physical);
        req.setAttribute("hasBrand", hasBrand);
        req.setAttribute("hasProp", hasProp);
        req.setAttribute("hasSpec", hasSpec);
        req.setAttribute("hasParam", hasParam);
        req.setAttribute("hasTab", hasTab);
        req.setAttribute("brandList", goodsBrandService.query(Cnd.orderBy().asc("location")));
        goodsTypeService.fetchLinks(obj, null, Cnd.orderBy().asc("opAt"));
        goodsTypeService.fetchLinks(obj.getPropsList(), null, Cnd.orderBy().asc("location"));
        goodsTypeService.fetchLinks(obj.getSpecList(), null, Cnd.orderBy().asc("location"));
        goodsTypeService.fetchLinks(obj.getParamgList(), null, Cnd.orderBy().asc("location"));
        req.setAttribute("obj", obj);
        return "pages/platform/goods/type/edit";

    }

    @RequestMapping("/editDo")
    @SJson
    @RequiresPermissions("goods.conf.type.edit")
    @SLog(description = "修改商品类型")
    public Object editDo(Goods_type goodsType,
                         @RequestParam(value = "brand", required = false) String[] brand,
                         @RequestParam(value = "props_name", required = false) String[] props_name,
                         @RequestParam(value = "props_type", required = false) String[] props_type,
                         @RequestParam(value = "props_values", required = false) String[] props_values,
                         @RequestParam(value = "specId", required = false) String[] specId,
                         @RequestParam(value = "specValueId", required = false) String[] specValIds,
                         @RequestParam(value = "specValUrls", required = false) String[] specValUrls,
                         @RequestParam(value = "specValText", required = false) String[] specValText,
                         @RequestParam(value = "group_name", required = false) String[] group_name,
                         @RequestParam(value = "group_params", required = false) String[] group_params,
                         @RequestParam(value = "tab_name", required = false) String[] tab_name,
                         @RequestParam(value = "tab_note", required = false) String[] tab_note, HttpServletRequest req) {
        try {
            goodsTypeService.update(goodsType, brand, props_name, props_type, props_values, specId, specValIds, specValUrls, specValText, group_name, group_params, tab_name, tab_note, StringUtil.getUid());
            goodsTypeService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }


    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "删除商品类型")
    @RequiresPermissions("goods.conf.type.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids[]", required = false) String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                return Result.error("不允许批量删除");
//                goodsTypeService.deleteType(ids);
            } else {
                List<Goods_class> classList = goodsClassService.query(Cnd.where("typeId", "=", id));
                if (classList.size() > 0) {
                    String errMsg = "";
                    for (Goods_class goodsClass : classList) {
                        errMsg += " " + goodsClass.getName();
                    }

                    return Result.error("在" + errMsg + " 分类中已使用，不允许删除");
                }

                goodsTypeService.deleteType(id);
            }
            goodsTypeService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

}
