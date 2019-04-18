package com.aebiz.app.web.modules.controllers.store.sales;

import com.aebiz.app.goods.modules.services.*;
import com.aebiz.app.member.modules.models.Member_level;
import com.aebiz.app.member.modules.models.Member_type;
import com.aebiz.app.member.modules.services.MemberLevelService;
import com.aebiz.app.member.modules.services.MemberTypeService;
import com.aebiz.app.sales.modules.models.Sales_coupon;
import com.aebiz.app.sales.modules.models.Sales_rule_order;
import com.aebiz.app.sales.modules.services.SalesCouponService;
import com.aebiz.app.sales.modules.services.SalesRuleOrderService;
import com.aebiz.app.shop.modules.models.Shop_area;
import com.aebiz.app.shop.modules.services.ShopAreaManagementService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/store/sales/coupon")
public class StoreSalesCouponController {

    private static final Log log = Logs.get();

    @Autowired
	private SalesCouponService salesCouponService;

    @Autowired
    private MemberTypeService memberTypeService;

    @Autowired
    private MemberLevelService memberLevelService;

    @Autowired
    private ShopAreaManagementService shopAreaManagementService;

    @Autowired
    private ShopAreaService shopAreaService;

    @Autowired
    private SalesRuleOrderService salesRuleOrderService;
    @RequestMapping("")
    @RequiresPermissions("store.sales.coupon")
	public String index(HttpServletRequest req) {
		return "pages/store/sales/coupon/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("store.sales.coupon")
    public Object data(Sales_coupon salesCoupon,
                       @RequestParam(value = "p_startAt", required = false) String p_startAt,
                       @RequestParam(value = "p_endAt", required = false) String p_endAt,
                       @RequestParam(value = "p_enabled", required = false) String p_enabled,
                       @RequestParam(value = "p_checkStatus", required = false) String p_checkStatus,
                       DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
		String storeId=StringUtil.getStoreId();
		if(Strings.isNotBlank(storeId)){
            cnd.and("storeId", "=", storeId);
        }
        if (Strings.isNotBlank(salesCoupon.getName())) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(salesCoupon.getName()) + "%"));
        }
        if (Strings.isNotBlank(salesCoupon.getCodeprefix())) {
            cnd.and("codeprefix", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(salesCoupon.getCodeprefix()) + "%"));
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

    @RequestMapping("/add")
    @RequiresPermissions("store.sales.coupon")
    public String add(HttpServletRequest req) {
        //TODO 会员等级 待优化
        List<Member_type> mtl = memberTypeService.getMemberTypeList();
        List<Member_level> mll = memberLevelService.query(Cnd.NEW());
        Map<String, List<Member_level>> memberTypeMap = new HashMap<>();
        for (Member_type mt : mtl) {
            memberTypeMap.put(String.valueOf(mt.getId()), mll.parallelStream().filter(ml->ml.getTypeId()==mt.getId()).collect(Collectors.toList()));
        }
        //片区列表
        req.setAttribute("areaList", shopAreaManagementService.getShopAreaManagementList());
        //片区map<code,name>
        req.setAttribute("areaMap", shopAreaManagementService.getShopAreaManagementMap());
        req.setAttribute("memberTypeList", mtl);
        req.setAttribute("memberTypeMap", memberTypeMap);
        return "pages/store/sales/coupon/add2";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "保存优惠券")
    @RequiresPermissions("store.sales.coupon.add")
    public Object addDo(@RequestParam(value = "tmp_sartAt", required = false) String sartAt, @RequestParam(value = "tmp_endAt", required = false) String endAt, Sales_coupon salesCoupon, HttpServletRequest req) {
		try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (Strings.isNotBlank(sartAt)) {
                salesCoupon.getSalesRuleOrder().setSartAt((int) (sdf.parse(sartAt).getTime() / 1000));
            }
            if (Strings.isNotBlank(endAt)) {
                salesCoupon.getSalesRuleOrder().setEndAt((int) (sdf.parse(endAt).getTime() / 1000));
            }
            salesCoupon.setStoreId(StringUtil.getStoreId());
            salesCoupon.getSalesRuleOrder().setStoreId(salesCoupon.getStoreId());
			salesCouponService.save(salesCoupon);
			return Result.success("globals.result.success", salesCoupon.getId());
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.sales.coupon")
    public String edit(@PathVariable String id,HttpServletRequest req) {
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
		return "pages/store/sales/coupon/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "保存优惠券")
    @RequiresPermissions("store.sales.coupon.edit")
    public Object editDo(@RequestParam(value = "tmp_sartAt", required = false) String sartAt, @RequestParam(value = "tmp_endAt", required = false) String endAt, Sales_coupon salesCoupon, HttpServletRequest req) {
		try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (Strings.isNotBlank(sartAt)) {
                salesCoupon.getSalesRuleOrder().setSartAt((int) (sdf.parse(sartAt).getTime() / 1000));
            }
            if (Strings.isNotBlank(endAt)) {
                salesCoupon.getSalesRuleOrder().setEndAt((int) (sdf.parse(endAt).getTime() / 1000));
            }
            int now = (int) (System.currentTimeMillis() / 1000);
            salesCoupon.setStoreId(StringUtil.getStoreId());
            salesCoupon.setOpBy(StringUtil.getUid());
			salesCoupon.setOpAt(now);
            salesCoupon.getSalesRuleOrder().setStoreId(salesCoupon.getStoreId());
            salesCoupon.getSalesRuleOrder().setOpBy(StringUtil.getUid());
            salesCoupon.getSalesRuleOrder().setOpAt(now);
			salesCouponService.save(salesCoupon);
			return Result.success("globals.result.success", salesCoupon.getId());
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "删除优惠券")
    @RequiresPermissions("store.sales.coupon.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(Lang.isEmptyArray(ids)){
                salesCouponService.cascadeDelete(id);
            }else{
                salesCouponService.cascadeDelete(ids);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("store.sales.coupon")
	public String detail(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            Sales_coupon coupon=salesCouponService.fetch(id);
            req.setAttribute("obj", coupon);
            Sales_rule_order rule_order=salesRuleOrderService.fetch(coupon.getRuleId());
            req.setAttribute("ruleName", rule_order.getName());
        }else{
            req.setAttribute("obj", null);
        }
        return "pages/store/sales/coupon/detail";
    }

}
