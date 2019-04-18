package com.aebiz.app.web.modules.controllers.store.goods;

import com.aebiz.app.goods.modules.models.Goods_brand;
import com.aebiz.app.goods.modules.models.Goods_type;
import com.aebiz.app.goods.modules.services.GoodsBrandService;
import com.aebiz.app.goods.modules.services.GoodsTypeService;
import com.aebiz.app.store.modules.models.Store_apply_brand;
import com.aebiz.app.store.modules.models.Store_user;
import com.aebiz.app.store.modules.services.StoreApplyBrandService;
import com.aebiz.app.store.modules.services.StoreApplyTypeBrandService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wlp on 2017/4/28.
 */
@Controller
@RequestMapping("/store/goods/brand")
public class StoreBrandController {
    @Autowired
    private StoreApplyBrandService storeApplyBrandService;
    @Autowired
    private GoodsBrandService goodsBrandService;
    @Autowired
    private GoodsTypeService goodsTypeService;
    @Autowired
    private StoreApplyTypeBrandService storeApplyTypeBrandService;

    @RequestMapping("")
    @RequiresPermissions("store.goods.manager.brand")
    public String index() {
        return "pages/store/goods/brand/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("store.goods.manager.brand")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
        cnd.and("storeId","=",user.getStoreId());
        return storeApplyBrandService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("store.goods.manager.brand")
    public String add(HttpServletRequest req) {
        req.setAttribute("typeList", goodsTypeService.query(Cnd.orderBy().asc("opAt")));
        return "pages/store/goods/brand/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Goods_brand")
    @RequiresPermissions("store.goods.manager.brand.add")
    public Object addDo(Store_apply_brand storeApplyBrand, @RequestParam(value = "type", required = false) String[] type, HttpServletRequest req) {
        try {
            //check申请的品牌是否存在
            String brandName = storeApplyBrand.getBrandName();
            int num = goodsBrandService.count(Cnd.where("name","=",brandName).and("delFlag","=",false));
            if(num > 0){
                return Result.error(brandName+"已经存在,请申请别的品牌");
            }
            storeApplyBrandService.add(storeApplyBrand,type);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.goods.manager.brand")
    public String edit(@PathVariable String id,HttpServletRequest req) {
        Store_apply_brand storeApplyBrand = storeApplyBrandService.fetch(Cnd.where("delFlag","=",false).and("id","=",id));
        if(storeApplyBrand == null){
            return "redirect:/404";
        }
        storeApplyBrandService.fetchLinks(storeApplyBrand,"typeList");
        List<Goods_type> typeList = goodsTypeService.query(Cnd.orderBy().asc("opAt"));
        req.setAttribute("typeList", typeList);
        req.setAttribute("obj", storeApplyBrand);
        return "pages/store/goods/brand/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Goods_brand")
    @RequiresPermissions("store.goods.manager.brand.edit")
    public Object editDo(Store_apply_brand storeApplyBrand, @RequestParam(value = "type", required = false) String[] type, HttpServletRequest req) {
        try {
            storeApplyBrand.setOpBy(StringUtil.getStoreId());
            storeApplyBrand.setOpAt((int) (System.currentTimeMillis() / 1000));
            storeApplyBrandService.edit(storeApplyBrand,type);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Store_apply_brand")
    @RequiresPermissions("store.goods.manager.brand.del")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
        try {
            if(ids!=null&&ids.length>0){
                storeApplyBrandService.delete(ids);
                req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
            }else{
                //goodsBrandService.delete(storeApplyBrandService.fetch(id).getBrandId());
                storeApplyBrandService.delete(id);
                req.setAttribute("id", id);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
