package com.aebiz.app.sales.modules.services.impl;

import com.aebiz.app.goods.modules.models.em.GoodsSaleClientEnum;
import com.aebiz.app.goods.modules.services.GoodsPriceService;
import com.aebiz.app.sales.modules.commons.SalesRuleCondition;
import com.aebiz.app.sales.modules.models.em.SalesCheckStatusEnum;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.sales.modules.models.Sales_rule_goods;
import com.aebiz.app.sales.modules.services.SalesRuleGoodsService;
import com.aebiz.commons.utils.DateUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SalesRuleGoodsServiceImpl extends BaseServiceImpl<Sales_rule_goods> implements SalesRuleGoodsService {
    private static final Log log = Logs.get();
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private ShopAreaService shopAreaService;
    @Autowired
    private GoodsPriceService goodsPriceService;

    public void check(Sales_rule_goods salesRuleGoods) {
        Chain chain = Chain.make("checkStatus", salesRuleGoods.getCheckStatus());
        chain.add("opBy", salesRuleGoods.getOpBy());
        chain.add("opAt", salesRuleGoods.getOpAt());
        this.update(chain, Cnd.where("id", "=", salesRuleGoods.getId()));
    }

    public List<Sales_rule_goods> sales(String storeId, String sku, String countyCode, String memberLevelId) {
        return sales(storeId, sku, GoodsSaleClientEnum.PC.getKey(), countyCode, memberLevelId);
    }

    public List<Sales_rule_goods> sales(String storeId, String sku, int client, String countyCode, String memberLevelId) {

        int now = DateUtil.getTime(new Date());
        Cnd cnd = Cnd.NEW();
        cnd.and("disabled", "=", false);
        cnd.and("checkStatus", "=", SalesCheckStatusEnum.PASS.getKey());
        cnd.and("sartAt", "<=", now).and("endAt", ">=", now);
        if (Strings.isNotBlank(storeId)) {
            cnd.and("storeId", "=", storeId);
        }

        List<Sales_rule_goods> salesRuleGoods = this.query(cnd);
        SalesRuleCondition salesRuleCondition;
        if (!Lang.isEmpty(salesRuleGoods)) {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM (SELECT ");
            sb.append("product.sku AS goods_product_id, ");
            sb.append("product.sku AS goods_sku, ");
            sb.append("goods.name AS goods_name, ");
            sb.append("product.costPrice AS goods_cost, ");
            sb.append("product.salePrice AS goods_price, ");
            sb.append("goods.typeId AS goods_type_id, ");
            sb.append("goods.brandId AS goods_brand_id ");
            sb.append("FROM goods_product product ");
            sb.append("LEFT JOIN goods_main goods ON goods.id = product.goodsId ");
            sb.append("WHERE goods.sale = 1 and product.enabled = 1 and product.sku = @sku ");
            if (Strings.isNotBlank(storeId)) {
                sb.append("and product.storeId = @storeId ");
            }
            sb.append(") t ");

            Iterator<Sales_rule_goods> it = salesRuleGoods.iterator();
            while (it.hasNext()) {
                Sales_rule_goods sro = it.next();

                //过滤适用终端
                if (!isClient(client, sro.getLimit_client())) {
                    it.remove();
                    continue;
                }

                //过滤适用会员等级
                if (Strings.isNotBlank(memberLevelId) && !isMemberLevel(memberLevelId, sro.getLimit_members())) {
                    it.remove();
                    continue;
                }

                //过滤适用区域
                if (Strings.isNotBlank(countyCode) && !isArea(countyCode, sro.getLimit_areas())) {
                    it.remove();
                    continue;
                }

                //规则条件过滤
                salesRuleCondition = Json.fromJson(SalesRuleCondition.class, sro.getConditions());
                SqlExpressionGroup group = new SqlExpressionGroup();
                fillCnd(group, salesRuleCondition);
                String condition = Cnd.where(group).toString();
                condition = "WHERE ()".equals(Strings.trim(condition)) || "WHERE".equals(Strings.trim(condition)) ? "" : condition;
                Sql sql = Sqls.create(sb.toString() + condition);
                sql.setParam("storeId", storeId);
                sql.setParam("sku", sku);
                log.debug("商品促销sql: " + sql);
                sql.setCallback(Sqls.callback.maps());
                this.dao().execute(sql);
                if (Lang.isEmpty(sql.getList(Record.class))) {
                    it.remove();
                }
            }
        }
        return salesRuleGoods;
    }

    public int price(String sku, String countyCode, String memberLevelId, String salesId) {
        return price(sku, GoodsSaleClientEnum.PC.getKey(), countyCode, memberLevelId, salesId);
    }

    public int price(String sku, int client, String countyCode, String memberLevelId, String salesId) {

        final int salePrice = goodsPriceService.price(sku, countyCode, client, memberLevelId);

        if (Strings.isBlank(salesId)) {
            return salePrice;
        }
        Sales_rule_goods salesRuleGoods = this.fetch(salesId);
        if (Lang.isEmpty(salesRuleGoods)) {
            return salePrice;
        }

        //计算优惠后价格
        NutMap map = Json.fromJson(NutMap.class, salesRuleGoods.getAction_solution());
        int total_amount = 0;
        if (map.containsKey("tpl_sale_sol_subfixed")) {
            NutMap subfixed = map.getAs("tpl_sale_sol_subfixed", NutMap.class);
            total_amount = salePrice - subfixed.getInt("total_amount");
        } else if (map.containsKey("tpl_sale_sol_fixed")) {
            NutMap subfixed = map.getAs("tpl_sale_sol_fixed", NutMap.class);
            total_amount = subfixed.getInt("total_amount");
        } else if (map.containsKey("tpl_sale_sol_subpercent")) {
            NutMap subpercent = map.getAs("tpl_sale_sol_subpercent", NutMap.class);
            total_amount = salePrice - salePrice * subpercent.getInt("persent")/100;
        } else if (map.containsKey("tpl_sale_sol_percent")) {
            NutMap subpercent = map.getAs("tpl_sale_sol_percent", NutMap.class);
            total_amount = salePrice * subpercent.getInt("percent")/100;
        }
        return total_amount <= 0 ? salePrice : total_amount;
    }

    /**
     *  判断适用终端范围是否包含销售终端
     * @param client 销售终端
     * @param limit_client 适用终端范围(json串)
     * @return
     */
    private boolean isClient(int client, String limit_client) {
        if (Strings.isNotBlank(limit_client)) {
            return Strings.isin(Strings.splitIgnoreBlank(limit_client), String.valueOf(client));
        }
        return true;
    }

    /**
     * 判断适用会员等级范围是否包含会员等级
     * @param memberLevelId 适用会员等级范围
     * @param limit_members 会员等级
     * @return
     */
    private boolean isMemberLevel(String memberLevelId, String limit_members) {
        boolean isMemberLevel = false;
        if (Strings.isNotBlank(limit_members)) {
            List<NutMap> members = Json.fromJsonAsList(NutMap.class, limit_members);
            outMemberLoop:for (NutMap member : members) {
                for (String lv : member.getList("children", String.class)) {
                    isMemberLevel = lv.equals(memberLevelId);
                    if (isMemberLevel) {
                        break outMemberLoop;
                    }
                }
            }
        }
        return isMemberLevel;
    }

    /**
     * 判断适用区域范围是否包含销售区域
     * @param countyCode 区县代码
     * @param limit_areas 适用区域
     * @return
     */
    private boolean isArea(String countyCode, String limit_areas) {
        boolean isArea = false;
        if (Strings.isNotBlank(limit_areas)) {
            Map<String, Object> area = Json.fromJsonAsMap(Object.class, limit_areas);
            if ("1".equals(area.get("partitionBy"))) {

            } else if ("2".equals(area.get("partitionBy"))) {
                String cityCode = shopAreaService.getParentCode(countyCode);
                String provinceCode = shopAreaService.getParentCode(cityCode);
                List<LinkedHashMap<String, String>> areas = (List<LinkedHashMap<String, String>>)area.get("values");
                for (LinkedHashMap<String, String> map : areas) {
                    if (Strings.isNotBlank(map.get("city"))) {
                        isArea = cityCode.equals(map.get("city"));
                        if (isArea) {
                            break;
                        }
                    } else {
                        isArea =  provinceCode.equals(map.get("province"));
                        if (isArea) {
                            break;
                        }
                    }
                }
            }
        } else {
            isArea = true;
        }
        return isArea;
    }

    /**
     * 转换条件json串成sql
     * @param group
     * @param salesRuleCondition
     */
    private void fillCnd(SqlExpressionGroup group, SalesRuleCondition salesRuleCondition) {

        if (Lang.isEmpty(salesRuleCondition)) {
            return ;
        }

        if (salesRuleCondition.getType().startsWith("sale_goods_group_")) {
            boolean isNot = "0".equals(salesRuleCondition.getValue());
            if ("all".startsWith(salesRuleCondition.getAggregator())) {
                for (SalesRuleCondition condition : salesRuleCondition.getConditions()) {
                    SqlExpressionGroup temp = new SqlExpressionGroup();
                    fillCnd(temp, condition);
                    group.and(temp).setNot(isNot);
                }
            } else if ("any".startsWith(salesRuleCondition.getAggregator())) {
                for (SalesRuleCondition condition : salesRuleCondition.getConditions()) {
                    SqlExpressionGroup temp = new SqlExpressionGroup();
                    fillCnd(temp, condition);
                    group.or(temp).setNot(isNot);
                }
            }
        } else if (salesRuleCondition.getType().startsWith("sales_goods_item_")) {
            switch (salesRuleCondition.getOp()) {
                case "eq": {
                    group.andEquals(salesRuleCondition.getAttr(), Integer.valueOf(salesRuleCondition.getValue()));
                }break;
                case "ne": {
                    group.andNotEquals(salesRuleCondition.getAttr(), Integer.valueOf(salesRuleCondition.getValue()));
                }break;
                case "lt": {
                    group.andLT(salesRuleCondition.getAttr(), Integer.valueOf(salesRuleCondition.getValue()));
                }break;
                case "le": {
                    group.andLTE(salesRuleCondition.getAttr(), Integer.valueOf(salesRuleCondition.getValue()));
                }break;
                case "gt": {
                    group.andGT(salesRuleCondition.getAttr(), Integer.valueOf(salesRuleCondition.getValue()));
                }break;
                case "ge": {
                    group.andGTE(salesRuleCondition.getAttr(), Integer.valueOf(salesRuleCondition.getValue()));
                }break;
                case "in": {
                    String[] ids = Json.fromJsonAsArray(String.class, salesRuleCondition.getValue());
                    group.andIn(salesRuleCondition.getAttr(), ids);
                }break;
                case "ni": {
                    String[] ids = Json.fromJsonAsArray(String.class, salesRuleCondition.getValue());
                    group.andNotIn(salesRuleCondition.getAttr(), ids);
                }break;
                case "sw": {
                    group.andLikeL(salesRuleCondition.getAttr(), salesRuleCondition.getValue());
                }break;
                case "ew": {
                    group.andLikeR(salesRuleCondition.getAttr(), salesRuleCondition.getValue());
                }break;
                case "like": {
                    group.andLike(salesRuleCondition.getAttr(), salesRuleCondition.getValue());
                }break;
                case "nlike": {
                    group.andNotLike(salesRuleCondition.getAttr(), salesRuleCondition.getValue());
                }break;
                case "eqs": {
                    group.andEquals(salesRuleCondition.getAttr(), salesRuleCondition.getValue());
                }break;
                case "nes": {
                    group.andNotEquals(salesRuleCondition.getAttr(), salesRuleCondition.getValue());
                }break;
            }
        }
    }
}
