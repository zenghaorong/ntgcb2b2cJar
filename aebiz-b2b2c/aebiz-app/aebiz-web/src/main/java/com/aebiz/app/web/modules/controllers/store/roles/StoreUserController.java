package com.aebiz.app.web.modules.controllers.store.roles;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.store.modules.models.Store_user;
import com.aebiz.app.store.modules.services.StoreUserService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("/store/roles/user")
public class StoreUserController {
    private static final Log log = Logs.get();
    @Autowired
	private StoreUserService storeUserService;

    @RequestMapping("")
    @RequiresPermissions("store.sys.manager.user")
	public String index() {
		return "pages/store/roles/user/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("store.sys.manager.user")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
        Cnd cnd = Cnd.NEW();
		cnd.and("storeId","=",user.getStoreId());
    	return storeUserService.data(length, start, draw, order, columns, cnd, "accountInfo");
    }

    @RequestMapping("/add")
    @RequiresPermissions("store.sys.manager.user")
    public String add() {
    	return "pages/store/roles/user/add";
    }

    @InitBinder("accountInfo")
    public void initBinderStore_main(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("accountInfo.");
    }

    @InitBinder("accountUser")
    public void initBinderStore_company(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("accountUser.");
    }


    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Store_user")
    @RequiresPermissions("store.sys.manager.user.add")
    public Object addDo(@ModelAttribute("accountInfo")Account_info accountInfo,
                        @ModelAttribute("accountUser")Account_user accountUser, HttpServletRequest req) {
		try {
            storeUserService.addDo(accountInfo,accountUser);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.sys.manager.user")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", storeUserService.fetch(id));
		return "pages/store/roles/user/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Store_user")
    @RequiresPermissions("store.sys.manager.user.edit")
    public Object editDo(Store_user storeUser, HttpServletRequest req) {
		try {
            storeUser.setOpBy(StringUtil.getUid());
			storeUser.setOpAt((int) (System.currentTimeMillis() / 1000));
			storeUserService.updateIgnoreNull(storeUser);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Store_user")
    @RequiresPermissions("store.sys.manager.user.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				storeUserService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				storeUserService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("store.sys.manager.user")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", storeUserService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/store/roles/user/detail";
    }

}
