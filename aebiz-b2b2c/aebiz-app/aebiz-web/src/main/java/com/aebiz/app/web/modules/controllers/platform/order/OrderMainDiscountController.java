package com.aebiz.app.web.modules.controllers.platform.order;

import com.aebiz.app.order.modules.models.Order_main_discount;
import com.aebiz.app.order.modules.services.OrderMainDiscountService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/order/main/discount")
public class OrderMainDiscountController {
    private static final Log log = Logs.get();
    @Autowired
	private OrderMainDiscountService orderMainDiscountService;

    @RequestMapping("")
    @RequiresPermissions("platform.order.main.discount")
	public String index() {
		return "pages/platform/order/main/discount/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("platform.order.main.discount")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return orderMainDiscountService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("platform.order.main.discount")
    public String add() {
    	return "pages/platform/order/main/discount/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Order_main_discount")
    @RequiresPermissions("platform.order.main.discount.add")
    public Object addDo(Order_main_discount orderMainDiscount, HttpServletRequest req) {
		try {
			orderMainDiscountService.insert(orderMainDiscount);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("platform.order.main.discount")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", orderMainDiscountService.fetch(id));
		return "pages/platform/order/main/discount/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Order_main_discount")
    @RequiresPermissions("platform.order.main.discount.edit")
    public Object editDo(Order_main_discount orderMainDiscount, HttpServletRequest req) {
		try {
            orderMainDiscount.setOpBy(StringUtil.getUid());
			orderMainDiscount.setOpAt((int) (System.currentTimeMillis() / 1000));
			orderMainDiscountService.updateIgnoreNull(orderMainDiscount);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Order_main_discount")
    @RequiresPermissions("platform.order.main.discount.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				orderMainDiscountService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				orderMainDiscountService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("platform.order.main.discount")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", orderMainDiscountService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/order/main/discount/detail";
    }

}
