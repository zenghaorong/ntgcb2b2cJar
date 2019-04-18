package com.aebiz.app.web.modules.controllers.platform.shop;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.shop.modules.models.Shop_account;
import com.aebiz.app.shop.modules.services.ShopAccountService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
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
@RequestMapping("/platform/shop/config/account")
public class ShopAccountController {

    private static final Log log = Logs.get();

    @Autowired
	private ShopAccountService shopAccountService;

    @RequestMapping("")
    @RequiresPermissions("shop.config.pay.account")
	public String index() {
		return "pages/platform/shop/account/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("shop.config.pay.account")
    public Object data(@RequestParam(value = "type",required = false) String type,
                       @RequestParam(value = "bankName",required = false) String bankName,
                       @RequestParam(value = "accountName",required = false) String accountName,
                       DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(type)) {
            cnd.and("type", "=", type);
        }
        if (Strings.isNotBlank(bankName)) {
            cnd.and("bankName", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(bankName) + "%"));
        }
        if (Strings.isNotBlank(accountName)) {
            cnd.and("accountName", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(accountName) + "%"));
        }
    	return shopAccountService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(),
                dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("shop.config.pay.account")
    public String add() {
    	return "pages/platform/shop/account/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Shop_account")
    @RequiresPermissions("shop.config.pay.account.add")
    public Object addDo(Shop_account shopAccount, HttpServletRequest req) {
		try {
			shopAccountService.insert(shopAccount);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("shop.config.pay.account")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", shopAccountService.fetch(id));
		return "pages/platform/shop/account/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Shop_account")
    @RequiresPermissions("shop.config.pay.account.edit")
    public Object editDo(Shop_account shopAccount, HttpServletRequest req) {
		try {
            shopAccount.setOpBy(StringUtil.getUid());
			shopAccount.setOpAt((int) (System.currentTimeMillis() / 1000));
			shopAccountService.updateIgnoreNull(shopAccount);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Shop_account")
    @RequiresPermissions("shop.config.pay.account.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
                String[] ssids = ids[0].split("," );
				shopAccountService.delete(ssids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ssids));
			}else{
				shopAccountService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

   /* @RequestMapping("/detail/{id}")
    @RequiresPermissions("shop.config.pay.account")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", shopAccountService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/shop/account/detail";
    }
*/
}
