package com.aebiz.app.web.modules.controllers.platform.shop;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.shop.modules.models.Shop_adv_position;
import com.aebiz.app.shop.modules.services.ShopAdvPositionService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Chain;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/shop/adv/position")
public class ShopAdvPositionController {

    private static final Log log = Logs.get();

    @Autowired
	private ShopAdvPositionService shopAdvPositionService;

    @RequestMapping("")
    @RequiresPermissions("platform.shop.adv.position")
	public String index() {
		return "pages/platform/shop/adv/position/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("platform.shop.adv.position")
    public Object data(@RequestParam(value = "name",required = false) String name,
                       @RequestParam(value = "type",required = false) String type,
                       @RequestParam(value = "display",required = false) String display,
                       DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(name)) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(name) + "%"));
        }
        if (Strings.isNotBlank(type)) {
            cnd.and("type", "=", type);
        }
        if (Strings.isNotBlank(display)) {
            cnd.and("display", "=", display);
        }
    	return shopAdvPositionService.data(dataTable.getLength(), dataTable.getStart(),
                dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("platform.shop.adv.position")
    public String add() {
    	return "pages/platform/shop/adv/position/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Shop_adv_position")
    @RequiresPermissions("platform.shop.adv.position.add")
    public Object addDo(Shop_adv_position shopAdvPosition, HttpServletRequest req) {
		try {
			shopAdvPositionService.insert(shopAdvPosition);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("platform.shop.adv.position")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", shopAdvPositionService.fetch(id));
		return "pages/platform/shop/adv/position/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Shop_adv_position")
    @RequiresPermissions("platform.shop.adv.position.edit")
    public Object editDo(Shop_adv_position shopAdvPosition, HttpServletRequest req) {
		try {
            shopAdvPosition.setOpBy(StringUtil.getUid());
			shopAdvPosition.setOpAt((int) (System.currentTimeMillis() / 1000));
			shopAdvPositionService.updateIgnoreNull(shopAdvPosition);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Shop_adv_position")
    @RequiresPermissions("platform.shop.adv.position.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				shopAdvPositionService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				shopAdvPositionService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("platform.shop.adv.position")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", shopAdvPositionService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/shop/adv/position/detail";
    }
    @RequestMapping("/enable/{id}")
    @SJson
    @RequiresPermissions("platform.shop.adv.position.delete")
    public Object enable(@PathVariable String id, HttpServletRequest req) {
        try {
            shopAdvPositionService.update(Chain.make("disabled", false), Cnd.where("id", "=", id));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/disable/{id}")
    @SJson
    @RequiresPermissions("platform.shop.adv.position.delete")
    public Object disable(@PathVariable String id, HttpServletRequest req) {
        try {
            shopAdvPositionService.update(Chain.make("disabled", true), Cnd.where("id", "=", id));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
