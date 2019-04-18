package com.aebiz.app.web.modules.controllers.platform.shop;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.app.wx.modules.models.Wx_config;
import com.aebiz.app.wx.modules.services.WxConfigService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Sqls;
import org.nutz.lang.util.NutMap;
import com.aebiz.app.shop.modules.models.Shop_payment;
import com.aebiz.app.shop.modules.services.ShopPaymentService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/platform/shop/payment")
public class ShopPaymentController {

    private static final Log log = Logs.get();

    @Autowired
	private ShopPaymentService shopPaymentService;

    @Autowired
    private WxConfigService wxConfigService;

    @RequestMapping("")
    @RequiresPermissions("platform.shop.payment")
	public String index() {
		return "pages/platform/shop/payment/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("platform.shop.payment")
    public Object data(@RequestParam(value = "code",required = false) String code,
                       @RequestParam(value = "name",required = false) String name,
                       DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(code)) {
            cnd.and("code", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(code) + "%"));
        }
        if (Strings.isNotBlank(name)) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(name) + "%"));
        }
    	return shopPaymentService.data(dataTable.getLength(), dataTable.getStart(),
                dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("platform.shop.payment")
    public String add() {
    	return "pages/platform/shop/payment/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Shop_payment")
    @RequiresPermissions("platform.shop.payment.add")
    public Object addDo(Shop_payment shopPayment, HttpServletRequest req) {
		try {
			shopPaymentService.insert(shopPayment);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("platform.shop.payment")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", shopPaymentService.fetch(id));
		return "pages/platform/shop/payment/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Shop_payment")
    @RequiresPermissions("platform.shop.payment.edit")
    public Object editDo(Shop_payment shopPayment, HttpServletRequest req) {
		try {
            shopPayment.setOpBy(StringUtil.getUid());
			shopPayment.setOpAt((int) (System.currentTimeMillis() / 1000));
			shopPaymentService.updateIgnoreNull(shopPayment);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Shop_payment")
    @RequiresPermissions("platform.shop.payment.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				shopPaymentService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				shopPaymentService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("platform.shop.payment")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", shopPaymentService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/shop/payment/detail";
    }

    /**
     * 支付方式排序
     *
     * @param pk
     * @param name
     * @param value
     * @return
     */
    @RequestMapping("/location")
    @SJson
    @SLog(description = "Shop_payment")
    @RequiresPermissions("platform.shop.payment")
    public Object location(@RequestParam(required = false, value = "pk") String pk, @RequestParam(required = false, value = "name") String name, @RequestParam(required = false, value = "value") Integer value) {
        shopPaymentService.update(Chain.make("location", value), Cnd.where("id", "=", pk));
        NutMap nutMap = new NutMap();
        nutMap.addv("name", name);
        nutMap.addv("pk", pk);
        nutMap.addv("value", value);
        return nutMap;
    }
    @RequestMapping("/enable/{id}")
    @SJson
    @RequiresPermissions("platform.shop.payment.edit")
    public Object enable(@PathVariable String id, HttpServletRequest req) {
        try {
            shopPaymentService.update(Chain.make("disabled", false), Cnd.where("id", "=", id));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/disable/{id}")
    @SJson
    @RequiresPermissions("platform.shop.payment.edit")
    public Object disable(@PathVariable String id, HttpServletRequest req) {
        try {
            shopPaymentService.update(Chain.make("disabled", true), Cnd.where("id", "=", id));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    //支付方式配置
    @RequestMapping("/setting/{id}")
    @RequiresPermissions("platform.shop.payment")
    public String setting(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            req.setAttribute("model", shopPaymentService.fetch(id));
            req.setAttribute("code",id);
        }

        List<Wx_config> list= wxConfigService.query(Cnd.where("1", "=", "1"));
        req.setAttribute("obj",list);
        return "pages/platform/shop/payment/setting";
    }
    @RequestMapping("/setDo")
    @SJson
    @SLog(description = "Shop_payment")
    @RequiresPermissions("platform.shop.payment")
    public Object setDo(@RequestParam String payInfo, @RequestParam String paymentId, HttpServletRequest req) {
        try {
            shopPaymentService.update(Chain.make("info",payInfo),Cnd.where("id","=",paymentId));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
