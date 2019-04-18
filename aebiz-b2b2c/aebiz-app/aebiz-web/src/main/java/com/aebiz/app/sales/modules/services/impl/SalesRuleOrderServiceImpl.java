package com.aebiz.app.sales.modules.services.impl;

import com.aebiz.app.goods.modules.models.em.GoodsSaleClientEnum;
import com.aebiz.app.goods.modules.services.GoodsPriceService;
import com.aebiz.app.sales.modules.commons.SalesRuleCondition;
import com.aebiz.app.sales.modules.commons.SalesRuleProduct;
import com.aebiz.app.sales.modules.commons.utils.ActionSolutionUtil;
import com.aebiz.app.sales.modules.commons.vo.StoreSalesVO;
import com.aebiz.app.sales.modules.models.Sales_rule_order;
import com.aebiz.app.sales.modules.models.em.SalesCheckStatusEnum;
import com.aebiz.app.sales.modules.services.SalesRuleGoodsService;
import com.aebiz.app.sales.modules.services.SalesRuleOrderService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.commons.utils.DateUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class SalesRuleOrderServiceImpl extends BaseServiceImpl<Sales_rule_order> implements SalesRuleOrderService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    @Autowired
    private ShopAreaService shopAreaService;
    @Autowired
    private GoodsPriceService goodsPriceService;
    @Autowired
    private SalesRuleGoodsService salesRuleGoodsService;

    public void check(Sales_rule_order salesRuleOrder) {
        Chain chain = Chain.make("checkStatus", salesRuleOrder.getCheckStatus());
        chain.add("opBy", salesRuleOrder.getOpBy());
        chain.add("opAt", salesRuleOrder.getOpAt());
        this.update(chain, Cnd.where("id", "=", salesRuleOrder.getId()));
    }

    public List<Sales_rule_order> sales(String storeId, List<SalesRuleProduct> products, String countyCode, String memberLevelId) {
        return sales(storeId, products, GoodsSaleClientEnum.PC.getKey(), countyCode, memberLevelId);
    }

    public List<Sales_rule_order> sales(String storeId, List<SalesRuleProduct> products, int client, String countyCode, String memberLevelId) {

        //取当前有效的促销规则
        int now = DateUtil.getTime(new Date());
        Cnd cnd = Cnd.NEW();
        cnd.and("storeId", "=", storeId);
        cnd.and("disabled", "=", false);
        cnd.and("checkStatus", "=", SalesCheckStatusEnum.PASS.getKey());
        cnd.and("sartAt", "<=", now).and("endAt", ">=", now);
        List<Sales_rule_order> salesRuleOrders = this.query(cnd);

        //过滤订单总价满足的规则
        SalesRuleCondition salesRuleCondition;
        if (!Lang.isEmpty(salesRuleOrders)) {

            //设置价格
            for (SalesRuleProduct salesRuleProduct : products) {
                salesRuleProduct.setPrice(salesRuleGoodsService.price(salesRuleProduct.getSku(), client, countyCode, memberLevelId, salesRuleProduct.getSalesId()));
            }

            Iterator<Sales_rule_order> it = salesRuleOrders.iterator();
            while (it.hasNext()) {
                Sales_rule_order sro = it.next();
                salesRuleCondition = Json.fromJson(SalesRuleCondition.class, sro.getConditions());
                //计算商品总价
                int totalSum = products.stream().map(product->product.getNum()*product.getPrice()).reduce(Integer::sum).get();
                //计算商品总数
                int numSum = products.stream().map(product->product.getNum()).reduce(Integer::sum).get();

                switch (sro.getCnd_template()) {
                    //自定义模板
                    case "tpl_sale_order_cnd_userdefined": {
                        break;
                    }
                    //当订单商品总价满X，对指定的商品(货品)优惠
                    case "tpl_sale_order_cnd_subtotalselectgoods": {
                        break;
                    }
                    //当订单商品总价满X，对所有商品优惠
                    case "tpl_sale_order_cnd_allorderallgoods": {
                        int ruleTotal = Integer.valueOf(salesRuleCondition.getConditions().get(0).getValue());
                        if (totalSum < ruleTotal) {
                            it.remove();
                            break;
                        }
                    }break;
                    //当订单商品数量满X，给予优惠
                    case "tpl_sale_order_cnd_itemsquanityallgoods": {
                        int ruleTotal = Integer.valueOf(salesRuleCondition.getValue());
                        if (numSum < ruleTotal) {
                            it.remove();
                            break;
                        }
                    }
                    //对所有订单给予优惠
                    case "tpl_sale_order_cnd_subtotalallgoods": {

                    }break;
                    default: it.remove();
                }
            }
        }
        return salesRuleOrders;
    }

    public int price(List<SalesRuleProduct> products, String countyCode, String memberLevelId, String salesId) {
        return price(products, GoodsSaleClientEnum.PC.getKey(), countyCode, memberLevelId, salesId);
    }

    public int price(List<SalesRuleProduct> products, int client, String countyCode, String memberLevelId, String salesId) {
        //设置价格
        for (SalesRuleProduct salesRuleProduct : products) {
            salesRuleProduct.setPrice(salesRuleGoodsService.price(salesRuleProduct.getSku(), client, countyCode, memberLevelId, salesRuleProduct.getSalesId()));
        }
        Optional<Integer> sum = products.stream().map(product->product.getNum()*product.getPrice()).reduce(Integer::sum);
        int total = sum.get();

        if (Strings.isBlank(salesId)) {
            return total;
        }
        Sales_rule_order salesRuleOrder = this.fetch(salesId);
        if (Lang.isEmpty(salesRuleOrder)) {
            return total;
        }

        //计算优惠后价格
        int total_amount = ActionSolutionUtil.getSalesPrice(total, salesRuleOrder.getAction_solution());
        return total_amount <= 0 ? total : total_amount;
    }

    @Override
    public StoreSalesVO storeSales(String salesId, Integer score, Integer price, Integer freightMoney) {

        StoreSalesVO storeSalesVO = new StoreSalesVO();
        storeSalesVO.setSalesId(salesId);
        storeSalesVO.setScore(score);
        storeSalesVO.setFreeMoney(0);//优惠金额初始为0
        storeSalesVO.setFreightMoney(freightMoney);
        storeSalesVO.setType(0);//订单促销

        //计算优惠
        Sales_rule_order salesRuleOrder = this.fetch(salesId);
        NutMap map = Json.fromJson(NutMap.class, salesRuleOrder.getAction_solution());
        if (map.containsKey("tpl_sale_sol_shipfree")) {
            storeSalesVO.setFreightMoney(0);
        } else if (map.containsKey("tpl_sale_sol_subfixed")) {
            NutMap subfixed = map.getAs("tpl_sale_sol_subfixed", NutMap.class);
            storeSalesVO.setFreeMoney(subfixed.getInt("total_amount"));
        } else if (map.containsKey("tpl_sale_sol_fixed")) {

        } else if (map.containsKey("tpl_sale_sol_subpercent")) {
            NutMap subpercent = map.getAs("tpl_sale_sol_subpercent", NutMap.class);
            storeSalesVO.setFreeMoney(price * subpercent.getInt("percent")/100);
        } else if (map.containsKey("tpl_sale_sol_percent")) {
            NutMap subpercent = map.getAs("tpl_sale_sol_percent", NutMap.class);
            storeSalesVO.setFreeMoney(price - (price * subpercent.getInt("percent")/100));
        }
        return  storeSalesVO;
    }

}
