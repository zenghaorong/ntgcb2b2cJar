package com.aebiz.app.web.modules.controllers.platform.store;

import com.aebiz.app.store.modules.models.Store_config;
import com.aebiz.app.store.modules.services.StoreConfigService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/platform/store/config/config")
public class StoreConfigController {
    @Autowired
    private StoreConfigService storeConfigService;

    @RequestMapping("")
    @RequiresPermissions("store.config.config")
    public String index(HttpServletRequest req) {
        Store_config config = storeConfigService.fetch("system");
        if (config == null) {
            Store_config storeConfig = new Store_config();
            storeConfig.setId("system");
            config = storeConfigService.insert(storeConfig);
        }
        req.setAttribute("obj", config);
        return "pages/platform/store/config/config/edit";
    }


    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Store_config")
    @RequiresPermissions("store.config.config.save")
    public Object editDo(Store_config storeConfig, HttpServletRequest req) {
        try {
            storeConfig.setOpBy(StringUtil.getUid());
            storeConfig.setOpAt((int) (System.currentTimeMillis() / 1000));
            storeConfigService.updateIgnoreNull(storeConfig);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
