package com.aebiz.app.web.modules.controllers.platform.shop;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import com.aebiz.baseframework.page.datatable.DataTable;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aebiz.app.shop.modules.models.Shop_currency;
import com.aebiz.app.shop.modules.services.ShopCurrencyService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;

@Controller
@RequestMapping("/platform/shop/config/currency")
public class ShopCurrencyController {

    @Autowired
	private ShopCurrencyService shopCurrencyService;

    @RequestMapping("")
	@RequiresPermissions("shop.config.pay.currency")
	public String index() {
		return "pages/platform/shop/currency/index";
	}

	@RequestMapping("/data")
    @SJson("full")
	@RequiresPermissions("shop.config.pay.currency")
    public Object data(@RequestParam(value = "name",required = false) String name,
					   @RequestParam(value = "defaulted",required = false) String defaulted,
					   DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
		if (Strings.isNotBlank(name)) {
			cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(name) + "%"));
		}
		if (Strings.isNotBlank(defaulted)) {
			cnd.and("defaulted", "=", "1".equals(defaulted) ? true : false);
		}
    	return shopCurrencyService.data(dataTable.getLength(), dataTable.getStart(),
				dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
	@RequiresPermissions("shop.config.pay.currency")
    public String add(HttpServletRequest req) {
    	return "pages/platform/shop/currency/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Shop_currency")
    @RequiresPermissions("shop.config.pay.currency.add")
    public Object addDo(Shop_currency shopCurrency, HttpServletRequest req) {
		try {
 			shopCurrencyService.insert(shopCurrency);
 			if(shopCurrency.isDefaultValue()){
				//默认货币只一种
				shopCurrencyService.updateDefaultCurrency(shopCurrency);
			}
			shopCurrencyService.clearCache();
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("shop.config.pay.currency")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", shopCurrencyService.fetch(id));
		return "pages/platform/shop/currency/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Shop_currency")
    @RequiresPermissions("shop.config.pay.currency.edit")
    public Object editDo(Shop_currency shopCurrency, HttpServletRequest req) {
		try {
            shopCurrency.setOpBy(StringUtil.getUid());
			shopCurrency.setOpAt((int) (System.currentTimeMillis() / 1000));
			shopCurrencyService.updateIgnoreNull(shopCurrency);
			if(shopCurrency.isDefaultValue()){
				//默认货币只一种
				shopCurrencyService.updateDefaultCurrency(shopCurrency);
			}
			shopCurrencyService.clearCache();
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Shop_currency")
    @RequiresPermissions("shop.config.pay.currency.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				// ids转化为字符串存进数组 作用：区分单删还是多选
				String[] ssids = ids[0].split(",");
				for (int i = 0; i < ssids.length; i++) {
					shopCurrencyService.delete(ssids[i]);
				}
			}else{
				shopCurrencyService.delete(id);
			}
			shopCurrencyService.clearCache();
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
	@RequiresPermissions("shop.config.pay.currency")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", shopCurrencyService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/shop/currency/detail";
    }

}
