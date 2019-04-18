package com.aebiz.app.web.modules.controllers.platform.shop;

import com.aebiz.app.shop.modules.models.Shop_image;
import com.aebiz.app.shop.modules.services.ShopImageService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/platform/shop/config/image")
public class ShopImageController {
    private static final Log log = Logs.get();

    @Autowired
	private ShopImageService shopImageService;

    @RequestMapping("")
    @RequiresPermissions("shop.config.image")
	public String index() {

        return "pages/platform/shop/image/index";
	}

    @RequestMapping("/data")
    @RequiresPermissions("shop.config.image")
    public String loadData(HttpServletRequest req){
        Cnd cnd = Cnd.NEW();
        List<Shop_image> shopImageList = shopImageService.query(cnd);
        req.setAttribute("obj",shopImageList);
        return  "pages/platform/shop/image/data";
    }

    @RequestMapping("/save")
    @SJson
    @SLog(description = "Shop_image")
    @RequiresPermissions("shop.config.image.save")
    public Object addDo(Shop_image shopImage, HttpServletRequest req) {
		try {
            // TODO: 2017/5/3 后续可能考虑对图的宽高的值是否为空或者数值格式进行check
            shopImageService.updateIgnoreNull(shopImage);
            shopImageService.clearCache();
 			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }


}
