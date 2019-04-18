package com.aebiz.app.web.modules.controllers.store.order;

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
@RequestMapping("/store/order/afterSale")
public class StoreOrderAfterSaleController {
    private static final Log log = Logs.get();

    @Autowired
    private OrderAfterMainService orderAfterMainService;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private StoreMainService storeMainService;

    @RequestMapping("")
    @RequiresPermissions("store.order.after.main")
    public String index(HttpServletRequest req) {
        // 获取售后原因、售后状态到页面
        req.setAttribute("reasons", sysDictService.getSubListByPath("0003"));
        req.setAttribute("states", OrderAfterStateEnum.values());
        return "pages/store/order/after/main/index";
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
    @RequiresPermissions("store.order.after.main")
    public Object data(@RequestParam("length") int length,
                       @RequestParam("start") int start,
                       @RequestParam("draw") int draw,
                       ArrayList<DataTableOrder> order,
                       ArrayList<DataTableColumn> columns,
                       @RequestParam(value = "id", required = false) String id,
                       @RequestParam(value = "reason", required = false) String reason,
                       @RequestParam(value = "state", required = false) String state,
                       @RequestParam(value = "applyTime1", required = false) String applyTime1,
                       @RequestParam(value = "applyTime2", required = false) String applyTime2,
                       @RequestParam(value = "applyType", required = false) Integer applyType)
    {
        Cnd cnd = Cnd.NEW();
        cnd.and("storeId", "=", StringUtil.getStoreId());
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
    @RequiresPermissions("store.order.after.main")
    public String detail(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            req.setAttribute("obj", orderAfterMainService.getAfterSaleDetail(id, StringUtil.getStoreId()));
        } else {
            req.setAttribute("obj", null);
        }
        return "pages/store/order/after/main/detail";
    }

    /**
     * 处理售后
     *
     * @param afterSaleId
     * @param flag
     * @param req
     * @return
     */
    @RequestMapping(value = "/toSellerHandle", method = RequestMethod.POST)
    @RequiresPermissions("store.order.after.main.edit")
    public String toSellerHandle(@RequestParam(value = "afterSaleId") String afterSaleId,
                                 @RequestParam(value = "flag") Integer flag,
                                 HttpServletRequest req){
        Order_after_main orderAfterMain = orderAfterMainService.fetch(Cnd.where("id", "=", afterSaleId).and("storeId", "=", StringUtil.getStoreId()));
        if(null != orderAfterMain){
            req.setAttribute("obj", orderAfterMain);
            req.setAttribute("flag", flag);
        }else{
            req.setAttribute("obj", null);
        }
        return "pages/store/order/after/main/sellerHandle";
    }

    /**
     * 卖家处理：如同意/拒绝买家的申请、审查确认买家退的货有没有问题，描述是否符合、
     * 申请平台仲裁、退换货等所有需要卖家处理的都调用这个方法
     *
     * @param afterSaleId 售后单号
     * @param applyType   申请类型：因为不同类型的处理是不一样，所以要知道申请类型
     * @param flag        自定义标识，用于区别卖家的操作，如同意是1，拒绝是2
     * @param note        备注说明：卖家填写的
     * @param voucher     凭证：卖家上传的
     * @return Result     成功或者失败
     */
    @RequestMapping("/sellerHandle")
    @SJson
    @SLog(description = "Order_after_main")
    @RequiresPermissions("store.order.after.main.edit")
    public Result sellerHandle(@RequestParam(value = "afterSaleId") String afterSaleId,
                               @RequestParam(value = "applyType") Integer applyType,
                               @RequestParam(value = "flag") Integer flag,
                               @RequestParam(value = "note", required = false) String note,
                               @RequestParam(value = "returnMoney", required = false) String returnMoney ,
                               @RequestParam(value = "voucher" ,required = false) String voucher) {
        try {
            //凭证最多三张
            String[] vouchers = StringUtils.split(Strings.sNull(voucher), ",");
            Integer returnMoney1 = 0;
            if(Strings.isNotBlank(returnMoney)){
                returnMoney1 = MoneyUtil.yuanToFen(returnMoney);
            }
            String opBy = StringUtil.getUid();
            orderAfterMainService.sellerHandle(afterSaleId, applyType, flag, returnMoney1, note, vouchers, Strings.sNull(opBy), OrderAfterHandleTypeEnum.SELF.getKey(), Strings.sNull(StringUtil.getUsername()));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}

