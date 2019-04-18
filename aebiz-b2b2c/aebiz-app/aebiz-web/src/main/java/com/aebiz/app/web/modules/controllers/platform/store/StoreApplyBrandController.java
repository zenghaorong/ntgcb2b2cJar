package com.aebiz.app.web.modules.controllers.platform.store;

import com.aebiz.app.goods.modules.models.Goods_type;
import com.aebiz.app.goods.modules.services.GoodsTypeService;
import com.aebiz.app.store.modules.commons.utils.StoreUtil;
import com.aebiz.app.store.modules.models.Store_apply_brand;
import com.aebiz.app.store.modules.models.Store_apply_type_brand;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.app.store.modules.services.StoreApplyBrandService;
import com.aebiz.app.store.modules.services.StoreMainService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.DateUtil;
import com.aebiz.commons.utils.SpringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/platform/store/apply/brand")
public class StoreApplyBrandController {
    private static final Log log = Logs.get();
    @Autowired
	private StoreApplyBrandService storeApplyBrandService;

    @Autowired
    private StoreMainService storeMainService;

    @Autowired
    private GoodsTypeService goodsTypeService;


    @RequestMapping("")
    @RequiresPermissions("store.apply.brand")
	public String index(HttpServletRequest req) {
        int waitVerifyNum = storeApplyBrandService.count(Cnd.where("delFlag","=",false).and("status","=",0));
        //审核通过的数量
        int verifyOkNum = storeApplyBrandService.count(Cnd.where("delFlag","=",false).and("status","=",1));
        //审核不通过的数量
        int verifyNoNum = storeApplyBrandService.count(Cnd.where("delFlag","=",false).and("status","=",2));
        req.setAttribute("waitVerifyNum",waitVerifyNum);
        req.setAttribute("verifyOkNum",verifyOkNum);
        req.setAttribute("verifyNoNum",verifyNoNum);
        return "pages/platform/store/apply/brand/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("store.apply.brand")
    public Object data(@RequestParam(value = "startAt",required = false)String startAt,@RequestParam(value = "endAt",required = false)String endAt, @RequestParam(value = "storeName",required = false)String storeName, @RequestParam("status") int status,@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        cnd.and("status","=",status);
        if(Strings.isNotBlank(startAt)){
            int startTime = DateUtil.getTime(startAt);
            cnd.and("applyAt",">=",startTime);
        }
        if(Strings.isNotBlank(endAt)){
            int endTime = DateUtil.getTime(endAt);
            cnd.and("applyAt","<",endTime);
        }
        if(Strings.isNotBlank(storeName)){
            Store_main storeMain = storeMainService.fetch(Cnd.where("delFlag","=",false).and("storeName","=",storeName));
            if(storeMain == null){
                cnd.and("storeId","=","-1");
            }else{
                cnd.and("storeId","=",storeMain.getId());
            }
        }
    	return storeApplyBrandService.data(length, start, draw, order, columns, cnd, "storeMain");
    }

    @RequestMapping("/add")
    @RequiresPermissions("store.apply.brand")
    public String add() {
    	return "pages/platform/store/apply/brand/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Store_apply_brand")
    @RequiresPermissions("store.apply.brand.add")
    public Object addDo(Store_apply_brand storeApplyBrand, HttpServletRequest req) {
		try {
			storeApplyBrandService.insert(storeApplyBrand);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    /**
     * 跳转审核页面
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.apply.brand")
    public String edit(@PathVariable String id,HttpServletRequest req) {
        Store_apply_brand storeApplyBrand = storeApplyBrandService.fetch(Cnd.where("delFlag","=",false).and("id","=",id));
		if(storeApplyBrand == null){
            return "redirect:404";
        }
        storeApplyBrandService.fetchLinks(storeApplyBrand,"^(storeMain|typeList)$");
        List<Store_apply_type_brand> storeApplyTypeBrands = storeApplyBrand.getTypeList();
        String [] typeId = new String[]{};
        for(Store_apply_type_brand storeApplyTypeBrand:storeApplyTypeBrands){
            typeId = ArrayUtils.add(typeId,storeApplyTypeBrand.getTypeId());
        }
        List<Goods_type> typeList = goodsTypeService.query(Cnd.where("id","in",typeId));
        req.setAttribute("typeList", typeList);
        req.setAttribute("obj", storeApplyBrand);
		return "pages/platform/store/apply/brand/edit";
    }

    /**
     *品牌审核
     * @param storeApplyBrand
     * @param req
     * @return
     */
    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Store_apply_brand")
    @RequiresPermissions("store.apply.brand.edit")
    public Object editDo(Store_apply_brand storeApplyBrand, HttpServletRequest req) {
		try {
            storeApplyBrandService.verify(storeApplyBrand);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }



    @RequestMapping("/detail/{id}")
    @RequiresPermissions("store.apply.brand")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            Store_apply_brand storeApplyBrand = storeApplyBrandService.fetch(Cnd.where("delFlag","=",false).and("id","=",id));
            if(storeApplyBrand == null){
                return "redirect:404";
            }
            storeApplyBrandService.fetchLinks(storeApplyBrand,"^(storeMain|typeList)$");
            List<Store_apply_type_brand> storeApplyTypeBrands = storeApplyBrand.getTypeList();
            String [] typeId = new String[]{};
            for(Store_apply_type_brand storeApplyTypeBrand:storeApplyTypeBrands){
                typeId = ArrayUtils.add(typeId,storeApplyTypeBrand.getTypeId());
            }
            List<Goods_type> typeList = goodsTypeService.query(Cnd.where("id","in",typeId));
            req.setAttribute("typeList", typeList);
            req.setAttribute("storeStatus", SpringUtil.getBean(StoreUtil.class));
            req.setAttribute("obj", storeApplyBrand);
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/store/apply/brand/detail";
    }

}
