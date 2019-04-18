package com.aebiz.app.web.modules.controllers.platform.shop;

import com.aebiz.app.shop.modules.models.Shop_area;
import com.aebiz.app.shop.modules.models.Shop_area_management;
import com.aebiz.app.shop.modules.services.ShopAreaManagementService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
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
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/platform/shop/area/management")
public class ShopAreaManagementController {
    private static final Log log = Logs.get();
    @Autowired
	private ShopAreaManagementService shopAreaManagementService;
    @Autowired
    private ShopAreaService shopAreaService;

    @RequestMapping("")
    @RequiresPermissions("shop.area.management")
	public String index() {
		return "pages/platform/shop/area/management/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("shop.area.management")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw,             ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return shopAreaManagementService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("shop.area.management")
    public String add(HttpServletRequest req) {

        String id = shopAreaService.fetch(Cnd.where("code","=",86)).getId();
        List<Shop_area> areaList = shopAreaService.query(Cnd.where("parentId","=",id));
        req.setAttribute("areaList", areaList);
    	return "pages/platform/shop/area/management/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Shop_area_management")
    @RequiresPermissions("shop.area.management.add")
    public Object addDo(Shop_area_management shopAreaManagement, HttpServletRequest req) {
		try {
            shopAreaManagementService.insert(shopAreaManagement);
            shopAreaManagementService.clearCache();
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("shop.area.management")
    public String edit(@PathVariable String id,HttpServletRequest req) {
        String areaId = shopAreaService.fetch(Cnd.where("code","=",86)).getId();
        req.setAttribute("areaList", shopAreaService.query(Cnd.where("parentId","=",areaId)));
		req.setAttribute("obj", shopAreaManagementService.fetch(id));
		String[] str=shopAreaManagementService.fetch(id).getAreaCode().split(",");
		List<String> areaCode = Arrays.asList(str);
        req.setAttribute("areaCodeArray",str);
        return "pages/platform/shop/area/management/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Shop_area_management")
    @RequiresPermissions("shop.area.management.edit")
    public Object editDo(Shop_area_management shopAreaManagement, HttpServletRequest req) {
		try {
            shopAreaManagement.setOpBy(StringUtil.getUid());
			shopAreaManagement.setOpAt((int) (System.currentTimeMillis() / 1000));
            shopAreaManagementService.updateIgnoreNull(shopAreaManagement);
            shopAreaManagementService.clearCache();
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Shop_area_management")
    @RequiresPermissions("shop.area.management.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids,        HttpServletRequest req) {
		String[] id_ = {};
        try {
			if(ids!=null&&ids.length>0){
                for (int i=0;i<ids.length;i++){
                    id_ = ids[i].split(",");
                }
                shopAreaManagementService.delete(id_);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
                shopAreaManagementService.delete(id);
    			req.setAttribute("id", id);
			}
            shopAreaManagementService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("shop.area.management")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", shopAreaManagementService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/shop/area/management/detail";
    }
}
