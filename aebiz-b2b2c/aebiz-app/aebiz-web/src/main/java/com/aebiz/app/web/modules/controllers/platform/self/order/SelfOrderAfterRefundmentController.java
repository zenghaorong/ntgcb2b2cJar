package com.aebiz.app.web.modules.controllers.platform.self.order;

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
@RequestMapping("/platform/self/order/afterSale/refundment")
public class SelfOrderAfterRefundmentController {
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
    @RequiresPermissions("self.order.afterSale.refundment")
    public String index(HttpServletRequest request) {
        request.setAttribute("afterSaleReason", sysDictService.getSubListByPath("0003"));
        request.setAttribute("refundStyle", OrderAfterBackMoneyStyleEnum.values());
        request.setAttribute("refundState", OrderAfterRefundStateEnum.values());
        return "pages/platform/self/order/after/refundment/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("self.order.afterSale.refundment")
    public Object data(@RequestParam(value = "id", required = false) String id,
                       @RequestParam(value = "afterSaleId", required = false) String afterSaleId,
                       @RequestParam(value = "refundReason", required = false) String refundReason,
                       @RequestParam(value = "refundStyle", required = false) Integer refundStyle,
                       @RequestParam(value = "refundState", required = false) Integer refundState,
                       @RequestParam(value = "applyTime1", required = false) String applyTime1,
                       @RequestParam(value = "applyTime2", required = false) String applyTime2,
                       DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        Store_main self_store = storeMainService.fetch(Cnd.where("self", "=", 1));
        cnd.and("storeId", "=", self_store.getId());

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

    @RequestMapping("/add")
    @RequiresPermissions("self.order.afterSale.refundment")
    public String add() {
        return "pages/platform/self/order/after/refundment/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Order_after_main")
    @RequiresPermissions("self.order.afterSale.refundment.add")
    public Object addDo(Order_after_refundment orderAfterRefundment, HttpServletRequest req) {
        try {
            orderAfterRefundmentService.insert(orderAfterRefundment);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("self.order.afterSale.refundment")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", orderAfterRefundmentService.fetch(id));
        return "pages/platform/self/order/after/refundment/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Order_after_main")
    @RequiresPermissions("self.order.afterSale.refundment.edit")
    public Object editDo(Order_after_refundment orderAfterRefundment, HttpServletRequest req) {
        try {
            orderAfterRefundment.setOpBy(StringUtil.getUid());
            orderAfterRefundment.setOpAt((int) (System.currentTimeMillis() / 1000));
            orderAfterRefundmentService.updateIgnoreNull(orderAfterRefundment);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Order_after_main")
    @RequiresPermissions("self.order.afterSale.refundment.delete")
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
    @RequiresPermissions("self.order.afterSale.refundment")
    public String detail(@PathVariable String id, HttpServletRequest req) {
        //自营店铺
        Store_main self_store = storeMainService.fetch(Cnd.where("self", "=", 1));
        if (!Strings.isBlank(id)) {
            Order_after_refundment orderAfterRefundment = orderAfterRefundmentService.fetch(id);
            NutMap nutMap = orderAfterMainService.getAfterSaleDetail(orderAfterRefundment.getAfterSaleId(), self_store.getId());
            nutMap.put("orderAfterRefundment", orderAfterRefundment);
            req.setAttribute("obj", nutMap);
        } else {
            req.setAttribute("obj", null);
        }
        return "pages/platform/self/order/after/refundment/detail";
    }

    /**
     * 财务处理
     *
     * @param refundmentId
     * @param flag
     * @param req
     * @return
     */
    @RequestMapping(value = "/toFinanceHandle",method = RequestMethod.POST)
    @RequiresPermissions("self.order.afterSale.refundment")
    public String toFinanceHandle(@RequestParam(value = "refundmentId") String refundmentId,
                                  @RequestParam(value = "flag") Integer flag,
                                  HttpServletRequest req){
        //自营店铺
        Store_main self_store = storeMainService.fetch(Cnd.where("self", "=", 1));
        Order_after_refundment orderAfterRefundment = orderAfterRefundmentService.fetch(Cnd.where("id", "=", refundmentId).and("storeId", "=", self_store.getId()));
        orderAfterRefundmentService.fetchLinks(orderAfterRefundment, "orderAfterMain");
        if(null != orderAfterRefundment){
            String orderId = orderAfterRefundment.getOrderAfterMain().getOrderId();
            Order_main orderMain = orderMainService.fetch(orderId);
            req.setAttribute("payTypeName", OrderPayTypeEnum.getValue(orderMain.getPayType()));
            req.setAttribute("flag", flag);
            req.setAttribute("obj", orderAfterRefundment);
        }else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/self/order/after/refundment/financeHandle";
    }

    /**
     * 卖家财务处理：审核通过则待财务退款，审核不通过说明结束
     *
     * @param refundmentId 退款单单号
     * @param flag        自定义标识，1审核通过，2审核不通过，3确认已打款
     * @param refundStyle 退款方式
     * @param note        备注说明：财务填写说明
     * @param voucher     凭证：财务上传的
     * @return Result     成功或者失败
     */
    @RequestMapping("/financeHandle")
    @SJson
    @SLog(description = "Order_after_main")
    @RequiresPermissions("self.order.afterSale.refundment")
    public Result sellerHandle(@RequestParam(value = "refundmentId") String refundmentId,
                               @RequestParam(value = "flag") Integer flag,
                               @RequestParam(value = "refundStyle", required = false)  String refundStyle,
                               @RequestParam(value = "note", required = false) String note,
                               @RequestParam(value = "voucher" ,required = false) String voucher) {
        try {
            //凭证最多三张
            String[] vouchers = StringUtils.split(Strings.sNull(voucher), ",");
            String opBy = StringUtil.getUid();
            orderAfterRefundmentService.financeHandle(refundmentId, flag, refundStyle, note, vouchers, Strings.sNull(opBy), OrderAfterHandleTypeEnum.SELF.getKey(), Strings.sNull(StringUtil.getUsername()));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }


}
