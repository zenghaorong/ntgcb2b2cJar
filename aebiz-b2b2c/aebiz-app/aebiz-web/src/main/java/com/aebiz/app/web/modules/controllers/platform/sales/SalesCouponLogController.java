package com.aebiz.app.web.modules.controllers.platform.sales;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.sales.modules.models.Sales_coupon_log;
import com.aebiz.app.sales.modules.services.SalesCouponLogService;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/sales/coupon/log")
public class SalesCouponLogController {
    private static final Log log = Logs.get();
    @Autowired
	private SalesCouponLogService salesCouponLogService;

    @RequestMapping("")
    @RequiresPermissions("sales.coupon.log")
	public String index() {
		return "pages/platform/sales/coupon/log/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("sales.coupon.log")
    public Object data(DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
    	return salesCouponLogService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("sales.coupon.log")
    public String add() {
    	return "pages/platform/sales/coupon/log/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Sales_coupon_log")
    @RequiresPermissions("sales.coupon.log.add")
    public Object addDo(Sales_coupon_log salesCouponLog, HttpServletRequest req) {
		try {
			salesCouponLogService.insert(salesCouponLog);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("sales.coupon.log")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", salesCouponLogService.fetch(id));
		return "pages/platform/sales/coupon/log/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Sales_coupon_log")
    @RequiresPermissions("sales.coupon.log.edit")
    public Object editDo(Sales_coupon_log salesCouponLog, HttpServletRequest req) {
		try {
            salesCouponLog.setOpBy(StringUtil.getUid());
			salesCouponLog.setOpAt((int) (System.currentTimeMillis() / 1000));
			salesCouponLogService.updateIgnoreNull(salesCouponLog);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Sales_coupon_log")
    @RequiresPermissions("sales.coupon.log.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				salesCouponLogService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				salesCouponLogService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("sales.coupon.log")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", salesCouponLogService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/sales/coupon/log/detail";
    }

}
