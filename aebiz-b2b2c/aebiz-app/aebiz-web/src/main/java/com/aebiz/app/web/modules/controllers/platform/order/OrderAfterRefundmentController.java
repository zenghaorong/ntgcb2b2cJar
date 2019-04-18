package com.aebiz.app.web.modules.controllers.platform.order;

import com.aebiz.app.order.modules.models.Order_after_refundment;
import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.models.em.OrderAfterBackMoneyStyleEnum;
import com.aebiz.app.order.modules.models.em.OrderAfterHandleTypeEnum;
import com.aebiz.app.order.modules.models.em.OrderAfterRefundStateEnum;
import com.aebiz.app.order.modules.models.em.OrderPayTypeEnum;
import com.aebiz.app.order.modules.services.OrderAfterMainService;
import com.aebiz.app.order.modules.services.OrderAfterRefundmentService;
import com.aebiz.app.order.modules.services.OrderMainService;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.app.store.modules.services.StoreMainService;
import com.aebiz.app.sys.modules.services.SysDictService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.DateUtil;
import com.aebiz.commons.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 退款单
 */
@Controller
@RequestMapping("/platform/order/afterSale/refundment")
public class OrderAfterRefundmentController {
    private static final Log log = Logs.get();
    @Autowired
    private OrderAfterMainService orderAfterMainService;
    @Autowired
    private OrderMainService orderMainService;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private OrderAfterRefundmentService orderAfterRefundmentService;
    @Autowired
    private  StoreMainService  storeMainService;

    @RequestMapping("")
    @RequiresPermissions("platform.order.afterSale.refundment")
    public String index(HttpServletRequest request) {
        request.setAttribute("afterSaleReason", sysDictService.getSubListByPath("0003"));
        request.setAttribute("refundStyle", OrderAfterBackMoneyStyleEnum.values());
        request.setAttribute("refundState", OrderAfterRefundStateEnum.values());
        return "pages/platform/order/after/refundment/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("platform.order.afterSale.refundment")
    public Object data(@RequestParam(value = "id", required = false) String id,
                       @RequestParam(value = "storeId", required = false) String storeId,
                       @RequestParam(value = "afterSaleId", required = false) String afterSaleId,
                       @RequestParam(value = "refundReason", required = false) String refundReason,
                       @RequestParam(value = "refundStyle", required = false) Integer refundStyle,
                       @RequestParam(value = "refundState", required = false) Integer refundState,
                       @RequestParam(value = "applyTime1", required = false) String applyTime1,
                       @RequestParam(value = "applyTime2", required = false) String applyTime2,
                       DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if(Strings.isNotBlank(storeId)){
            cnd.and("storeId", "=", storeId);
        }
        if (!Strings.isEmpty(id)) {
            cnd.and("id", "=", id);
        }
        if (!Strings.isEmpty(afterSaleId)) {
            cnd.and("afterSaleId", "=", afterSaleId);
        }
        if (!Strings.isEmpty(refundReason)) {
            cnd.and("refundReason", "=", refundReason);
        }
        if (null != refundStyle) {
            cnd.and("refundStyle", "=", refundStyle);
        }
        if (null != refundState) {
            cnd.and("refundState", "=", refundState);
        }
        if (!Strings.isEmpty(applyTime1)) {
            cnd.and("applyTime", ">=", DateUtil.getTime(applyTime1 +" 00:00:00"));
        }
        if (!Strings.isEmpty(applyTime2)) {
            cnd.and("applyTime", "<=", DateUtil.getTime(applyTime2 + " 23:59:59"));
        }
        NutMap nutMap = orderAfterRefundmentService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
        if (nutMap.get("data") != null) {
            List<Order_after_refundment> refundment = (List<Order_after_refundment>) nutMap.get("data");
            for(Order_after_refundment m : refundment){
                orderAfterRefundmentService.fetchLinks(m, "orderAfterMain");
                if(null != m.getRefundStyle())
                    m.setRefundStyleName(OrderAfterBackMoneyStyleEnum.getValue(m.getRefundStyle()));
                if(null != m.getRefundState())
                    m.setRefundStateName(OrderAfterRefundStateEnum.getValue(m.getRefundState()));
            }
            nutMap.put("data", refundment);
        }
        return nutMap;
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Order_after_main")
    @RequiresPermissions("platform.order.afterSale.refundment.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids", required = false) String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                orderAfterRefundmentService.delete(ids);
                req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
            } else {
                orderAfterRefundmentService.delete(id);
                req.setAttribute("id", id);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = "/detail/{id}")
    @RequiresPermissions("platform.order.afterSale.refundment")
    public String detail(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            Order_after_refundment orderAfterRefundment = orderAfterRefundmentService.fetch(id);
            NutMap nutMap = orderAfterMainService.getAfterSaleDetail(orderAfterRefundment.getAfterSaleId(), "");
            nutMap.put("orderAfterRefundment", orderAfterRefundment);
            req.setAttribute("obj", nutMap);
        } else {
            req.setAttribute("obj", null);
        }
        return "pages/platform/order/after/refundment/detail";
    }


}
