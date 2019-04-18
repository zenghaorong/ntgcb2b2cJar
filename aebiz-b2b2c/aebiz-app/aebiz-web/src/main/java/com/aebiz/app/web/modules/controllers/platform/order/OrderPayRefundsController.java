package com.aebiz.app.web.modules.controllers.platform.order;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.order.modules.models.Order_pay_refunds;
import com.aebiz.app.order.modules.services.OrderPayRefundsService;
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
@RequestMapping("/platform/order/pay/refunds")
public class OrderPayRefundsController {
    private static final Log log = Logs.get();
    @Autowired
	private OrderPayRefundsService orderPayRefundsService;

    @RequestMapping("")
    @RequiresPermissions("platform.order.pay.refunds")
	public String index() {
		return "pages/platform/order/pay/refunds/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("platform.order.pay.refunds")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return orderPayRefundsService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("platform.order.pay.refunds")
    public String add() {
    	return "pages/platform/order/pay/refunds/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Order_pay_refunds")
    @RequiresPermissions("platform.order.pay.refunds.add")
    public Object addDo(Order_pay_refunds orderPayRefunds, HttpServletRequest req) {
		try {
			orderPayRefundsService.insert(orderPayRefunds);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("platform.order.pay.refunds")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", orderPayRefundsService.fetch(id));
		return "pages/platform/order/pay/refunds/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Order_pay_refunds")
    @RequiresPermissions("platform.order.pay.refunds.edit")
    public Object editDo(Order_pay_refunds orderPayRefunds, HttpServletRequest req) {
		try {
            orderPayRefunds.setOpBy(StringUtil.getUid());
			orderPayRefunds.setOpAt((int) (System.currentTimeMillis() / 1000));
			orderPayRefundsService.updateIgnoreNull(orderPayRefunds);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Order_pay_refunds")
    @RequiresPermissions("platform.order.pay.refunds.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				orderPayRefundsService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				orderPayRefundsService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("platform.order.pay.refunds")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", orderPayRefundsService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/order/pay/refunds/detail";
    }

}
