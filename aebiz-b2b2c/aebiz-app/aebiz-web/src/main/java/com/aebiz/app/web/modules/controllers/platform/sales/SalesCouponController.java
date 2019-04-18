package com.aebiz.app.web.modules.controllers.platform.sales;

import com.aebiz.app.goods.modules.services.*;
import com.aebiz.app.member.modules.models.Member_level;
import com.aebiz.app.member.modules.models.Member_type;
import com.aebiz.app.member.modules.services.MemberLevelService;
import com.aebiz.app.member.modules.services.MemberTypeService;
import com.aebiz.app.sales.modules.models.Sales_rule_order;
import com.aebiz.app.sales.modules.models.em.SalesCheckStatusEnum;
import com.aebiz.app.sales.modules.services.SalesRuleOrderService;
import com.aebiz.app.shop.modules.models.Shop_area;
import com.aebiz.app.shop.modules.services.ShopAreaManagementService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.sales.modules.models.Sales_coupon;
import com.aebiz.app.sales.modules.services.SalesCouponService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.json.Json;
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
@RequestMapping("/platform/sales/coupon")
public class SalesCouponController {
    private static final Log log = Logs.get();
    @Autowired
    private SalesCouponService salesCouponService;
    @Autowired
    private MemberTypeService memberTypeService;
    @Autowired
    private MemberLevelService memberLevelService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsProductService goodsProductService;
    @Autowired
    private GoodsClassService goodsClassService;
    @Autowired
    private GoodsTypeService goodsTypeService;
    @Autowired
    private GoodsBrandService goodsBrandService;
    @Autowired
    private ShopAreaManagementService shopAreaManagementService;
    @Autowired
    private ShopAreaService shopAreaService;
    @Autowired
    private SalesRuleOrderService salesRuleOrderService;
    @RequestMapping("")
    @RequiresPermissions("sales.coupon.coupon")
	public String index(HttpServletRequest req) {
        req.setAttribute("salesCheckStatusEnumValues", SalesCheckStatusEnum.values());
		return "pages/platform/sales/coupon/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("sales.coupon.coupon")
    public Object data(Sales_coupon salesCoupon,
                       @RequestParam(value = "p_startAt", required = false) String p_startAt,
                       @RequestParam(value = "p_endAt", required = false) String p_endAt,
                       @RequestParam(value = "p_enabled", required = false) String p_enabled,
                       @RequestParam(value = "p_checkStatus", required = false) String p_checkStatus,
                       DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(salesCoupon.getName())) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + salesCoupon.getName() + "%"));
        }
        if (Strings.isNotBlank(salesCoupon.getCodeprefix())) {
            cnd.and("codeprefix", "like", Sqls.escapeSqlFieldValue("%" + salesCoupon.getCodeprefix() + "%"));
        }
        if (Strings.isNotBlank(salesCoupon.getType())) {
            cnd.and("type", "=", salesCoupon.getType());
        }
		if (Strings.isNotBlank(p_enabled)) {
		    cnd.and("disabled", "=", Boolean.valueOf(p_enabled));
        }
        if (Strings.isNotBlank(p_checkStatus)) {
            cnd.and("checkStatus", "=", Integer.valueOf(p_checkStatus));
        }
        cnd.and("delFlag", "=", false);
    	return salesCouponService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/check/{id}")
    @RequiresPermissions("sales.coupon.coupon")
    public String check(@PathVariable String id, HttpServletRequest req) {
        //TODO 会员等级 待优化
        List<Member_type> mtl = memberTypeService.query(Cnd.NEW());
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
        req.setAttribute("obj", salesCouponService.fetchLinks(salesCouponService.fetch(id), "salesRuleOrder"));
        return "pages/platform/sales/coupon/check";
    }

    @RequestMapping("/checkDo")
    @SJson
    @SLog(description = "审核优惠券")
    @RequiresPermissions("sales.coupon.coupon.edit")
    public Object checkDo(Sales_coupon salesCoupon, HttpServletRequest req) {
        try {
            salesCouponService.check(salesCoupon);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("sales.coupon.coupon")
	public String detail(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            Sales_coupon coupon=salesCouponService.fetch(id);
            req.setAttribute("obj", coupon);
            Sales_rule_order rule_order=salesRuleOrderService.fetch(coupon.getRuleId());
            String mem=rule_order.getLimit_members();
            Object ob=Json.fromJson(mem);
            if(!Lang.isEmpty(ob)){
                List<Map> maps=(List<Map>)ob;
                for(Map map:maps){
                    List<String> list =(List<String>)map.get("children");
                    if(!Lang.isEmpty(list)){
                        //for()
                    }
                }
             }
            req.setAttribute("rule", rule_order);
        }else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/sales/coupon/detail";
    }

}
