package com.aebiz.app.web.modules.controllers.platform.shop;

import com.aebiz.app.shop.modules.models.Shop_config;
import com.aebiz.app.shop.modules.services.ShopConfigService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/platform/shop/config")
public class ShopConfigController {
    private static final Log log = Logs.get();
    @Autowired
	private ShopConfigService shopConfigService;

    @RequestMapping("")
    @RequiresPermissions("platform.shop.config")
	public String index(HttpServletRequest req){
        Shop_config config = shopConfigService.getShopConfig();
        if (config == null) {
            config = new Shop_config();
            config.setId("system");
            shopConfigService.insert(config);
           shopConfigService.clearCache();
        }
        req.setAttribute("obj", config);
		return "pages/platform/shop/config/index";
	}

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Shop_config")
    @RequiresPermissions("platform.shop.config")
    public Object editDo(@RequestParam String at, Shop_config shopConfig, HttpServletRequest req) {
		try {
		    if (""==shopConfig.getScoreExpireTime()||null==shopConfig.getScoreExpireTime()){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                int publishAt = (int) (sdf.parse(at).getTime() / 1000);
                shopConfig.setScoreExpireTime(String.valueOf(publishAt));
            }
            shopConfig.setOpBy(StringUtil.getUid());
			shopConfig.setOpAt((int) (System.currentTimeMillis() / 1000));
			shopConfigService.updateIgnoreNull(shopConfig);
            shopConfigService.clearCache();
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }
}
