package com.aebiz.app.web.modules.controllers.platform.order;

import com.aebiz.app.order.modules.models.Order_after_main;
import com.aebiz.app.order.modules.models.em.OrderAfterHandleTypeEnum;
import com.aebiz.app.order.modules.models.em.OrderAfterStateEnum;
import com.aebiz.app.order.modules.services.OrderAfterMainService;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.app.store.modules.services.StoreMainService;
import com.aebiz.app.sys.modules.services.SysDictService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.DateUtil;
import com.aebiz.commons.utils.MoneyUtil;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 自营的售后
 * Created by yangjian on 2017/6/7.
 */
@Controller
@RequestMapping("/platform/order/afterSale")
public class OrderAfterSaleController {
    private static final Log log = Logs.get();

    @Autowired
    private OrderAfterMainService orderAfterMainService;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private StoreMainService storeMainService;

    @RequestMapping("")
    @RequiresPermissions("platform.order.after.main")
    public String index(HttpServletRequest req) {
        // 获取售后原因、售后状态到页面
        req.setAttribute("reasons", sysDictService.getSubListByPath("0003"));
        req.setAttribute("states", OrderAfterStateEnum.values());
        return "pages/platform/order/after/main/index";
    }

    /**
     * 获取售后申请列表的数据
     *
     * @param length     长度
     * @param start      起始条数
     * @param draw       请求的次数
     * @param order      排序
     * @param columns    字段
     * @param id         售后单号，查询用
     * @param reason     售后原因，查询用
     * @param state      售后状态，查询用
     * @param applyTime1 申请时间1，查询用
     * @param applyTime2 申请时间2，查询用
     * @return 售后列表对象
     */
    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("platform.order.after.main")
    public Object data(@RequestParam("length") int length,
                       @RequestParam("start") int start,
                       @RequestParam("draw") int draw,
                       ArrayList<DataTableOrder> order,
                       ArrayList<DataTableColumn> columns,
                       @RequestParam(value = "id", required = false) String id,
                       @RequestParam(value = "storeId", required = false) String storeId,
                       @RequestParam(value = "reason", required = false) String reason,
                       @RequestParam(value = "state", required = false) String state,
                       @RequestParam(value = "applyTime1", required = false) String applyTime1,
                       @RequestParam(value = "applyTime2", required = false) String applyTime2,
                       @RequestParam(value = "applyType", required = false) Integer applyType)
    {
        Cnd cnd = Cnd.NEW();
        if(Strings.isNotBlank(storeId)){
            cnd.and("storeId", "=", storeId);
        }
        // 查询条件；有就添加
        if (!Strings.isEmpty(id)) {
            cnd.and("id", "=", id);
        }
        if (!Strings.isEmpty(reason)) {
            cnd.and("returnReason", "=", reason);
        }
        if (!Strings.isEmpty(state)) {
            cnd.and("afterSaleState", "=", state);
        }
        if (!Strings.isEmpty(applyTime1)) {
            cnd.and("applyTime", ">=", DateUtil.getTime(applyTime1 +" 00:00:00"));
        }
        if (!Strings.isEmpty(applyTime2)) {
            cnd.and("applyTime", "<=", DateUtil.getTime(applyTime2 + " 23:59:59"));
        }
        if(null != applyType && !Strings.isEmpty(applyType.toString())){
            cnd.and("applyType", "=", applyType);
        }
        cnd.desc("applyTime");
        NutMap nutMap = orderAfterMainService.data(length, start, draw, order, columns, cnd, null);
        List<Order_after_main> list = (List<Order_after_main>) nutMap.get("data");
        if (list != null) {
            for (Order_after_main afterMain : list) {
                String afterState = OrderAfterStateEnum.getValue(afterMain.getAfterSaleState());
                afterMain.setAfterSaleStateName(afterState);
            }
        }
        nutMap.put("data", list);
        return nutMap;
    }

    /**
     * 获取售后详情
     *
     * @param id  售后单号
     * @param req
     * @return 售后详情页(detail.html)
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("platform.order.after.main")
    public String detail(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            req.setAttribute("obj", orderAfterMainService.getAfterSaleDetail(id, ""));
        } else {
            req.setAttribute("obj", null);
        }
        return "pages/platform/order/after/main/detail";
    }

}

