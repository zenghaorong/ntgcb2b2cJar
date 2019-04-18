package com.aebiz.app.web.modules.controllers.platform.shop;

import com.aebiz.app.shop.modules.models.Shop_express;
import com.aebiz.app.shop.modules.services.ShopExpressService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/shop/config/express")
public class ShopExpressController {

    @Autowired
    private ShopExpressService shopLogisticsService;

    @RequestMapping("")
    @RequiresPermissions("shop.config.delivery.express")
    public String index() {
        return "pages/platform/shop/express/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("shop.config.delivery.express")
    public Object data(@RequestParam(value = "name",required = false) String name,
                       @RequestParam(value = "code",required = false) String code,
                       @RequestParam(value = "url",required = false) String url,
                       DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(name)) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(name) + "%"));
        }
        if (Strings.isNotBlank(code)) {
            cnd.and("code", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(code) + "%"));
        }
        if (Strings.isNotBlank(url)) {
            cnd.and("url", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(url) + "%"));
        }
        return shopLogisticsService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(),
                dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("shop.config.delivery.express")
    public String add(HttpServletRequest req) {
        return "pages/platform/shop/express/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Shop_express")
    @RequiresPermissions("shop.config.delivery.express.add")
    public Object addDo(Shop_express shopLogistics, @RequestParam(required = false) String codecheck, @RequestParam(required = false) String codecustom, HttpServletRequest req) {
        try {
            if ("1".equals(codecheck) && null != codecustom) {
                shopLogistics.setCode(codecustom);
            }
            shopLogisticsService.insert(shopLogistics);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("shop.config.delivery.express")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", shopLogisticsService.fetch(id));
        return "pages/platform/shop/express/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Shop_express")
    @RequiresPermissions("shop.config.delivery.express.edit")
    public Object editDo(Shop_express shopLogistics, HttpServletRequest req) {
        try {
            shopLogistics.setOpBy(StringUtil.getUid());
            shopLogistics.setOpAt((int) (System.currentTimeMillis() / 1000));
            shopLogisticsService.updateIgnoreNull(shopLogistics);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Shop_express")
    @RequiresPermissions("shop.config.delivery.express.delete")
    public Object delete(@PathVariable(required = false) String id,
                         @RequestParam(value = "ids", required = false) String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                // ids转化为字符串存进数组 作用：区分单删还是多选
                String[] ssids = ids[0].split(",");
                for (int i = 0; i < ssids.length; i++) {
                    shopLogisticsService.delete(ssids[i]);
                }
            } else {
                shopLogisticsService.delete(id);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("shop.config.delivery.express")
    public String detail(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            req.setAttribute("obj", shopLogisticsService.fetch(id));
        } else {
            req.setAttribute("obj", null);
        }
        return "pages/platform/shop/express/detail";
    }

    /**
     * 商品排序
     *
     * @param pk
     * @param name
     * @param value
     * @return
     */
    @RequestMapping("/location")
    @SJson
    @SLog(description = "Shop_express")
    @RequiresPermissions("shop.config.delivery.express")
    public Object location(@RequestParam(required = false, value = "pk") String pk, @RequestParam(required = false, value = "name") String name, @RequestParam(required = false, value = "value") Integer value) {
        shopLogisticsService.update(Chain.make("location", value), Cnd.where("id", "=", pk));
        NutMap nutMap = new NutMap();
        nutMap.addv("name", name);
        nutMap.addv("pk", pk);
        nutMap.addv("value", value);
        return nutMap;
    }

}
