package com.aebiz.app.web.modules.controllers.store.sales;

import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.models.em.GoodsStatusEnum;
import com.aebiz.app.goods.modules.services.*;
import com.aebiz.app.member.modules.models.Member_level;
import com.aebiz.app.member.modules.models.Member_type;
import com.aebiz.app.member.modules.services.MemberLevelService;
import com.aebiz.app.member.modules.services.MemberTypeService;
import com.aebiz.app.sales.modules.models.Sales_rule_goods;
import com.aebiz.app.sales.modules.services.SalesRuleGoodsService;
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
@RequestMapping("/store/sales/rule/goods")
public class StoreSalesRuleGoodsController {

    private static final Log log = Logs.get();

    @Autowired
	private SalesRuleGoodsService salesRuleGoodsService;

    @Autowired
    private MemberTypeService memberTypeService;

    @Autowired
    private MemberLevelService memberLevelService;

    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private GoodsTypeService goodsTypeService;

    @Autowired
    private GoodsBrandService goodsBrandService;

    @Autowired
    private ShopAreaManagementService shopAreaManagementService;

    @Autowired
    private ShopAreaService shopAreaService;

    @RequestMapping("")
    @RequiresPermissions("store.sales.rule.goods")
	public String index(HttpServletRequest req) {
        return "pages/store/sales/rule/goods/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("store.sales.rule.goods")
    public Object data(Sales_rule_goods salesRuleGoods,
                       @RequestParam(value = "p_startAt", required = false) String p_startAt,
                       @RequestParam(value = "p_endAt", required = false) String p_endAt,
                       @RequestParam(value = "p_enabled", required = false) String p_enabled,
                       @RequestParam(value = "p_checkStatus", required = false) String p_checkStatus,
                       DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
		String storeId=StringUtil.getStoreId();
        if (Strings.isNotBlank(storeId)) {
            cnd.and("storeId", "=",storeId);
        }
        if (Strings.isNotBlank(salesRuleGoods.getName())) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(salesRuleGoods.getName()) + "%"));
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
        cnd.and("delFlag", "=", false);
    	return salesRuleGoodsService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/product")
    @RequiresPermissions("store.sales.rule.goods")
    public String productIndex(HttpServletRequest req) {
        return "pages/store/sales/rule/goods/product";
    }

    @RequestMapping("/product/data")
    @SJson("full")
    @RequiresPermissions("store.sales.rule.goods")
    public Object productsData(@ModelAttribute("goods") Goods_main goods, @ModelAttribute("product") Goods_product product, DataTable dataTable) {
        NutMap fieldMap = NutMap.NEW();
        fieldMap.put("storeId", StringUtil.getStoreId());
        fieldMap.put("sale", true);
        fieldMap.put("status", GoodsStatusEnum.SALE.getKey());
        fieldMap.put("delFlag", false);
        return goodsProductService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), fieldMap);
    }

    @RequestMapping("/products")
    @SJson("full")
    @RequiresPermissions("store.sales.rule.goods")
    public Object products(@RequestParam("skus")String[] skus) {
        return goodsProductService.query("^(sku|name|)$", Cnd.where("sku", "in", skus));
    }

    @RequestMapping("/brand")
    @RequiresPermissions("store.sales.rule.goods")
    public String brandIndex(HttpServletRequest req) {
        return "pages/store/sales/rule/goods/brand";
    }

    @RequestMapping("/brand/data")
    @SJson("full")
    @RequiresPermissions("store.sales.rule.goods")
    public Object brandData(DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        return goodsBrandService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/brands")
    @SJson("full")
    @RequiresPermissions("store.sales.rule.goods")
    public Object brands(@RequestParam("ids")String[] ids) {
        return goodsBrandService.query("^(id|name)$", Cnd.where("id", "in", ids));
    }

    @RequestMapping("/type")
    @RequiresPermissions("store.sales.rule.goods")
    public String typeIndex(HttpServletRequest req) {
        return "pages/store/sales/rule/goods/type";
    }

    @RequestMapping("/type/data")
    @SJson("full")
    @RequiresPermissions("store.sales.rule.goods")
    public Object typeData(DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        return goodsTypeService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/types")
    @SJson("full")
    @RequiresPermissions("store.sales.rule.goods")
    public Object types(@RequestParam("ids")String[] ids) {
        return goodsTypeService.query("^(id|name|physical)$", Cnd.where("id", "in", ids));
    }

    @RequestMapping("/add")
    @RequiresPermissions("store.sales.rule.goods")
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
        req.setAttribute("ruleList", salesRuleGoodsService.query("^(name|limit_priority)$", Cnd.where("storeId", "=", StringUtil.getStoreId()).and("disabled", "=", false).and("delFlag", "=", false).desc("limit_priority")));

        return "pages/store/sales/rule/goods/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "保存商品规则")
    @RequiresPermissions("store.sales.rule.goods.add")
    public Object addDo(@RequestParam(value = "tmp_sartAt", required = false) String sartAt, @RequestParam(value = "tmp_endAt", required = false) String endAt, Sales_rule_goods salesRuleGoods, HttpServletRequest req) {
		try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (Strings.isNotBlank(sartAt)) {
                salesRuleGoods.setSartAt((int) (sdf.parse(sartAt).getTime() / 1000));
            }
            if (Strings.isNotBlank(endAt)) {
                salesRuleGoods.setEndAt((int) (sdf.parse(endAt).getTime() / 1000));
            }
            salesRuleGoods.setStoreId(StringUtil.getStoreId());
			salesRuleGoodsService.insert(salesRuleGoods);
			return Result.success("globals.result.success", salesRuleGoods.getId());
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.sales.rule.goods")
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
        req.setAttribute("ruleList", salesRuleGoodsService.query("^(name|limit_priority)$", Cnd.where("storeId", "=", StringUtil.getStoreId()).and("id", "!=", id).and("disabled", "=", false).and("delFlag", "=", false).desc("limit_priority")));
		req.setAttribute("obj", salesRuleGoodsService.fetch(id));
		return "pages/store/sales/rule/goods/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "保存订单规则")
    @RequiresPermissions("store.sales.rule.goods.edit")
    public Object editDo(@RequestParam(value = "tmp_sartAt", required = false) String sartAt, @RequestParam(value = "tmp_endAt", required = false) String endAt, Sales_rule_goods salesRuleGoods, HttpServletRequest req) {
		try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (Strings.isNotBlank(sartAt)) {
                salesRuleGoods.setSartAt((int) (sdf.parse(sartAt).getTime() / 1000));
            }
            if (Strings.isNotBlank(endAt)) {
                salesRuleGoods.setEndAt((int) (sdf.parse(endAt).getTime() / 1000));
            }
            salesRuleGoods.setStoreId(StringUtil.getStoreId());
            salesRuleGoods.setOpBy(StringUtil.getUid());
			salesRuleGoods.setOpAt((int) (System.currentTimeMillis() / 1000));
			salesRuleGoodsService.updateIgnoreNull(salesRuleGoods);
			return Result.success("globals.result.success", salesRuleGoods.getId());
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "删除规则")
    @RequiresPermissions("store.sales.rule.goods.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				salesRuleGoodsService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				salesRuleGoodsService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("store.sales.rule.goods")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", salesRuleGoodsService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/store/sales/rule/goods/detail";
    }

}
