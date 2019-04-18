package com.aebiz.app.web.modules.controllers.platform.store;

import com.aebiz.app.shop.modules.models.Shop_config;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.store.modules.models.Store_logistics_mode;
import com.aebiz.app.store.modules.services.StoreLogisticsModeService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/store/logistics/mode")
public class StoreLogisticsModeController {
    private static final Log log = Logs.get();
    @Autowired
	private StoreLogisticsModeService storeLogisticsModeService;

    @RequestMapping("")
    @RequiresPermissions("platform.store.logistics.mode")
	public String index(HttpServletRequest req) {
             Store_logistics_mode storelogisticsmode  = storeLogisticsModeService.fetch(Cnd.where("storeId","=",StringUtil.getStoreUid()));
        if (storelogisticsmode == null) {
            storelogisticsmode = new Store_logistics_mode();
            storelogisticsmode.setStoreId(StringUtil.getStoreUid());
            storeLogisticsModeService.insert(storelogisticsmode);
        }
        req.setAttribute("obj",storelogisticsmode);
		return "pages/platform/store/logistics/mode/index";
	}

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Store_logistics_mode")
    @RequiresPermissions("platform.store.logistics.mode.edit")
    public Object editDo(Store_logistics_mode storeLogisticsMode, HttpServletRequest req) {
		try {
            storeLogisticsMode.setOpBy(StringUtil.getUid());
			storeLogisticsMode.setOpAt((int) (System.currentTimeMillis() / 1000));
			storeLogisticsModeService.updateIgnoreNull(storeLogisticsMode);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }
}
