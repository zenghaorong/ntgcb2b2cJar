package com.aebiz.app.web.modules.controllers.platform.self.sales;

import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.models.em.GoodsStatusEnum;
import com.aebiz.app.goods.modules.services.*;
import com.aebiz.app.member.modules.models.Member_level;
import com.aebiz.app.member.modules.models.Member_type;
import com.aebiz.app.member.modules.services.MemberLevelService;
import com.aebiz.app.member.modules.services.MemberTypeService;
import com.aebiz.app.sales.modules.models.Sales_rule_order;
import com.aebiz.app.sales.modules.services.SalesRuleOrderService;
import com.aebiz.app.shop.modules.models.Shop_area;
import com.aebiz.app.shop.modules.services.ShopAreaManagementService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.DateUtil;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@RequestMapping("/platform/self/sales/rule/order")
public class SelfSalesRuleOrderController {

    private static final Log log = Logs.get();

    @Autowired
	private SalesRuleOrderService salesRuleOrderService;

    @Autowired
    private MemberTypeService memberTypeService;

    @Autowired
    private MemberLevelService memberLevelService;

    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private ShopAreaManagementService shopAreaManagementService;

    @Autowired
    private ShopAreaService shopAreaService;

    @RequestMapping("")
    @RequiresPermissions("self.sales.rule.order")
	public String index(HttpServletRequest req) {
        return "pages/platform/self/sales/rule/order/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("self.sales.rule.order")
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
        cnd.and("delFlag", "=", false);
    	return salesRuleOrderService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/product/index")
    @RequiresPermissions("self.sales.rule.order")
    public String productIndex(HttpServletRequest req) {
        return "pages/platform/self/sales/rule/order/product";
    }

    @RequestMapping("/product/data")
    @SJson("full")
    @RequiresPermissions("self.sales.rule.order")
    public Object productsData(@ModelAttribute("goods") Goods_main goods, @ModelAttribute("product") Goods_product product, DataTable dataTable) {
        NutMap fieldMap = NutMap.NEW();
        fieldMap.put("storeId", "2017060000000001");
        fieldMap.put("sale", true);
        fieldMap.put("status", GoodsStatusEnum.SALE.getKey());
        fieldMap.put("delFlag", false);
        return goodsProductService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), fieldMap);
    }

    @RequestMapping("/products")
    @SJson("full")
    @RequiresPermissions("self.sales.rule.order")
    public Object products(@RequestParam("skus")String[] skus) {
        return goodsProductService.query("^(sku|name|)$", Cnd.where("sku", "in", skus));
    }

    @RequestMapping("/add")
    @RequiresPermissions("self.sales.rule.order")
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
        req.setAttribute("ruleList", salesRuleOrderService.query("^(name|limit_priority)$", Cnd.where("storeId", "=", "2017060000000001").and("type", "=", "order").and("disabled", "=", false).and("delFlag", "=", false).desc("limit_priority")));
        return "pages/platform/self/sales/rule/order/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "保存订单促销规则")
    @RequiresPermissions("self.sales.rule.order.add")
    public Object addDo(@RequestParam(value = "tmp_sartAt", required = false) String sartAt, @RequestParam(value = "tmp_endAt", required = false) String endAt, Sales_rule_order salesRuleOrder, HttpServletRequest req) {
		try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    if (Strings.isNotBlank(sartAt)) {
                salesRuleOrder.setSartAt((int) (sdf.parse(sartAt).getTime() / 1000));
            }
            if (Strings.isNotBlank(endAt)) {
                salesRuleOrder.setEndAt((int) (sdf.parse(endAt).getTime() / 1000));
            }
            salesRuleOrder.setStoreId("2017060000000001");
			salesRuleOrderService.insert(salesRuleOrder);
			return Result.success("globals.result.success", salesRuleOrder.getId());
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("self.sales.rule.order")
    public String edit(@PathVariable String id,HttpServletRequest req) {
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
		return "pages/platform/self/sales/rule/order/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "保存订单促销规则")
    @RequiresPermissions("self.sales.rule.order.edit")
    public Object editDo(@RequestParam(value = "tmp_sartAt", required = false) String sartAt, @RequestParam(value = "tmp_endAt", required = false) String endAt, Sales_rule_order salesRuleOrder, HttpServletRequest req) {
		try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (Strings.isNotBlank(sartAt)) {
                salesRuleOrder.setSartAt((int) (sdf.parse(sartAt).getTime() / 1000));
            }
            if (Strings.isNotBlank(endAt)) {
                salesRuleOrder.setEndAt((int) (sdf.parse(endAt).getTime() / 1000));
            }
            salesRuleOrder.setStoreId("2017060000000001");
            salesRuleOrder.setOpBy(StringUtil.getUid());
			salesRuleOrder.setOpAt((int) (System.currentTimeMillis() / 1000));
			salesRuleOrderService.updateIgnoreNull(salesRuleOrder);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "删除订单促销规则")
    @RequiresPermissions("self.sales.rule.order.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				salesRuleOrderService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				salesRuleOrderService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("self.sales.rule.order")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", salesRuleOrderService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/self/sales/rule/order/detail";
    }

}
