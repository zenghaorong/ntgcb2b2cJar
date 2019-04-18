package com.aebiz.app.web.modules.controllers.platform.order;

import com.aebiz.app.order.modules.models.Order_return;
import com.aebiz.app.order.modules.services.OrderReturnService;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/order/return")
public class OrderReturnController {
    private static final Log log = Logs.get();
    @Autowired
	private OrderReturnService orderReturnService;

    @RequestMapping("")
    @RequiresPermissions("platform.order.return")
	public String index() {
		return "pages/platform/order/return/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("platform.order.return")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return orderReturnService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("platform.order.return")
    public String add() {
    	return "pages/platform/order/return/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Order_return")
    @RequiresPermissions("platform.order.return.add")
    public Object addDo(Order_return orderReturn, HttpServletRequest req) {
		try {
			orderReturnService.insert(orderReturn);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("platform.order.return")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", orderReturnService.fetch(id));
		return "pages/platform/order/return/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Order_return")
    @RequiresPermissions("platform.order.return.edit")
    public Object editDo(Order_return orderReturn, HttpServletRequest req) {
		try {
            orderReturn.setOpBy(StringUtil.getUid());
			orderReturn.setOpAt((int) (System.currentTimeMillis() / 1000));
			orderReturnService.updateIgnoreNull(orderReturn);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Order_return")
    @RequiresPermissions("platform.order.return.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				orderReturnService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				orderReturnService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("platform.order.return")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", orderReturnService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/order/return/detail";
    }

}
