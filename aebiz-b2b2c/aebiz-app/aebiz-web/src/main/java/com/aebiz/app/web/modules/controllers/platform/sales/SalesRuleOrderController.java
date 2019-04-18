package com.aebiz.app.web.modules.controllers.platform.sales;

import com.aebiz.app.goods.modules.services.*;
import com.aebiz.app.member.modules.models.Member_level;
import com.aebiz.app.member.modules.models.Member_type;
import com.aebiz.app.member.modules.services.MemberLevelService;
import com.aebiz.app.member.modules.services.MemberTypeService;
import com.aebiz.app.sales.modules.models.em.SalesCheckStatusEnum;
import com.aebiz.app.shop.modules.models.Shop_area;
import com.aebiz.app.shop.modules.services.ShopAreaManagementService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.commons.utils.DateUtil;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.sales.modules.models.Sales_rule_order;
import com.aebiz.app.sales.modules.services.SalesRuleOrderService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/platform/sales/rule/order")
public class SalesRuleOrderController {

    private static final Log log = Logs.get();

    @Autowired
    private SalesRuleOrderService salesRuleOrderService;

    @Autowired
    private MemberTypeService memberTypeService;

    @Autowired
    private MemberLevelService memberLevelService;

    @Autowired
    private ShopAreaManagementService shopAreaManagementService;

    @Autowired
    private ShopAreaService shopAreaService;

    @RequestMapping("")
    @RequiresPermissions("sales.rule.order")
	public String index(HttpServletRequest req) {
        req.setAttribute("salesCheckStatusEnumValues", SalesCheckStatusEnum.values());
        return "pages/platform/sales/rule/order/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("sales.rule.order")
    public Object data(Sales_rule_order salesRuleOrder,
                       @RequestParam(value = "p_startAt", required = false) String p_startAt,
                       @RequestParam(value = "p_endAt", required = false) String p_endAt,
                       @RequestParam(value = "p_enabled", required = false) String p_enabled,
                       @RequestParam(value = "p_checkStatus", required = false) String p_checkStatus,
                        DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(salesRuleOrder.getName())) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(salesRuleOrder.getName()) + "%"));
        }
        if (Strings.isNotBlank(p_startAt)) {
            cnd.and("sartAt", ">=", DateUtil.getTime(p_startAt));
        }
        if (Strings.isNotBlank(p_endAt)) {
            cnd.and("endAt", "<=", DateUtil.getTime(p_endAt));
        }
        if (Strings.isNotBlank(p_enabled)) {
            cnd.and("disabled", "=", Boolean.valueOf(p_enabled));
        }
        if (Strings.isNotBlank(p_checkStatus)) {
            cnd.and("checkStatus", "=", Integer.valueOf(p_checkStatus));
        }
        cnd.and("type", "=", "order");
    	return salesRuleOrderService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/check/{id}")
    @RequiresPermissions("sales.rule.order")
    public String check(@PathVariable String id, HttpServletRequest req) {
        //TODO 会员等级 待优化
        List<Member_type> mtl = memberTypeService.getMemberTypeList();
        List<Member_level> mll = memberLevelService.query(Cnd.NEW());
        Map<String, List<Member_level>> memberTypeMap = new HashMap<>();
        for (Member_type mt : mtl) {
            memberTypeMap.put(String.valueOf(mt.getId()), mll.parallelStream().filter(ml->ml.getTypeId()==mt.getId()).collect(Collectors.toList()));
        }

        //获取省市列表
        List<Shop_area> provinceCityList = new ArrayList<>();
        Map<String, String> provinceCityMap = new HashMap<>();
        List<Shop_area> shopAreaList = shopAreaService.query("^(code|name|path|hasChildren|location)$",Cnd.where("disabled", "=", false).asc("location"));
        if (!Lang.isEmpty(shopAreaList)) {
            provinceCityList.addAll(shopAreaList.parallelStream().filter(p->p.getPath().length() <= 12).collect(Collectors.toList()));
            //组装省市Map
            provinceCityMap.putAll(shopAreaList.parallelStream().collect(Collectors.toMap(Shop_area::getCode, area->area.getName())));
        }
        req.setAttribute("memberTypeList", mtl);
        req.setAttribute("memberTypeMap", memberTypeMap);
        //片区列表
        req.setAttribute("areaList", shopAreaManagementService.getShopAreaManagementList());
        //片区map<code,name>
        req.setAttribute("areaMap", shopAreaManagementService.getShopAreaManagementMap());
        req.setAttribute("provinceCityList", provinceCityList);
        req.setAttribute("provinceCityMap", provinceCityMap);
        req.setAttribute("ruleList", salesRuleOrderService.query("^(name|limit_priority)$", Cnd.where("storeId", "=", "2017060000000001").and("id", "!=", id).and("type", "=", "order").and("disabled", "=", false).and("delFlag", "=", false).desc("limit_priority")));
        req.setAttribute("obj", salesRuleOrderService.fetch(id));
		return "pages/platform/sales/rule/order/check";
    }

    @RequestMapping("/checkDo")
    @SJson
    @SLog(description = "审核订单促销")
    @RequiresPermissions("sales.rule.order.edit")
    public Object checkDo(Sales_rule_order salesRuleOrder, HttpServletRequest req) {
		try {
            salesRuleOrder.setOpBy(StringUtil.getUid());
			salesRuleOrder.setOpAt((int) (System.currentTimeMillis() / 1000));
			salesRuleOrderService.check(salesRuleOrder);
			return Result.success("globals.result.success");
		} catch (Exception e) {
            log.error(e.getMessage());
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("sales.rule.order")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", salesRuleOrderService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/sales/rule/order/detail";
    }

}
