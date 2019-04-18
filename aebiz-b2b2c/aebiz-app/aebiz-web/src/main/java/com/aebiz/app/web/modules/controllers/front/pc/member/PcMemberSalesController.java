package com.aebiz.app.web.modules.controllers.front.pc.member;

import com.aebiz.app.sales.modules.commons.SalesRuleProduct;
import com.aebiz.app.sales.modules.models.Sales_coupon;
import com.aebiz.app.sales.modules.models.Sales_rule_goods;
import com.aebiz.app.sales.modules.models.Sales_rule_order;
import com.aebiz.app.sales.modules.services.SalesCouponService;
import com.aebiz.app.sales.modules.services.SalesRuleGoodsService;
import com.aebiz.app.sales.modules.services.SalesRuleOrderService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 会员促销
 * Created by hechao on 2017/7/21.
 */
@Controller
@RequestMapping("/member/sales")
public class PcMemberSalesController {
    private static final Log log = Logs.get();

    @Autowired
    private SalesRuleGoodsService salesRuleGoodsService;
    @Autowired
    private SalesRuleOrderService salesRuleOrderService;
    @Autowired
    private SalesCouponService salesCouponService;

    /**
     * 获取商品促销信息
     * @param storeId 商家ID
     * @param sku 货品SKU
     * @param countyCode 销售区县Code
     * @param memberLevelId 会员等级ID
     * @return
     */
    @RequestMapping("/goods/sales")
    @SJson
    @RequiresAuthentication
    public Object goodsSales(@RequestParam(value = "storeId") String storeId,
                             @RequestParam(value = "sku") String sku,
                             @RequestParam(value = "countyCode") String countyCode,
                             @RequestParam(value = "memberLevelId") String memberLevelId) {
        try {
            List<Sales_rule_goods> salesRuleGoods = salesRuleGoodsService.sales(storeId, sku, countyCode, memberLevelId);
            return Result.success("globals.result.success", salesRuleGoods);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error("globals.result.error");
        }
    }

    /**
     * 获取商品促销价
     * @param sku 货品SKU
     * @param countyCode 销售区县Code
     * @param memberLevelId 会员等级ID
     * @param salesId 促销ID
     * @return
     */
    @RequestMapping("/goods/sales/price")
    @SJson
    @RequiresAuthentication
    public Object goodsSalesPrice(@RequestParam(value = "sku") String sku,
                                  @RequestParam(value = "countyCode") String countyCode,
                                  @RequestParam(value = "memberLevelId") String memberLevelId,
                                  @RequestParam(value = "salesId") String salesId) {
        try {
            return Result.success("globals.result.success", salesRuleGoodsService.price(sku, countyCode, memberLevelId, salesId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error("globals.result.error");
        }
    }

    /**
     * 获取订单促销信息
     *
     * @param storeId 商家ID
     * @param productList 货品列表[{"sku":"", "num": 10, "salesId": "id123"}]
     * @param countyCode 销售区县Code
     * @param memberLevelId 会员等级ID
     * @return
     */
    @RequestMapping("/order/sales")
    @SJson
    @RequiresAuthentication
    public Object orderSales(@RequestParam(value = "storeId") String storeId,
                       @RequestParam(value = "products") String productList,
                       @RequestParam(value = "countyCode") String countyCode,
                       @RequestParam(value = "memberLevelId") String memberLevelId) {
        try {
            List<SalesRuleProduct> products = Json.fromJsonAsList(SalesRuleProduct.class, productList);
            List<Sales_rule_order> salesRuleOrders = salesRuleOrderService.sales(storeId, products, countyCode, memberLevelId);
            return Result.success("globals.result.success", salesRuleOrders);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error("globals.result.error");
        }
    }

    /**
     * 获取订单促销价
     * @param productList 货品列表
     * @param client 销售终端
     * @param countyCode 销售区县Code
     * @param memberLevelId 会员等级ID
     * @return
     */
    @RequestMapping("/order/sales/price")
    @SJson
    @RequiresAuthentication
    public Object orderSalesPrice(@RequestParam(value = "products") String productList,
                                  @RequestParam(value = "client") Integer client,
                                  @RequestParam(value = "countyCode") String countyCode,
                                  @RequestParam(value = "memberLevelId") String memberLevelId,
                                  @RequestParam(value = "salesId") String salesId) {
        try {
            List<SalesRuleProduct> products = Json.fromJsonAsList(SalesRuleProduct.class, productList);
            return Result.success("globals.result.success", salesRuleOrderService.price(products, client, countyCode, memberLevelId, salesId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error("globals.result.error");
        }
    }

    /**
     * 获取订单促销信息
     *
     * @param storeId 商家ID
     * @param productList 货品列表[{"sku":"", "num": 10, "salesId": "id123"}]
     * @param countyCode 销售区县Code
     * @param memberLevelId 会员等级ID
     * @return
     */
    @RequestMapping("/coupon/sales")
    @SJson
    @RequiresAuthentication
    public Object couponSales(@RequestParam(value = "storeId") String storeId,
                             @RequestParam(value = "products") String productList,
                             @RequestParam(value = "countyCode") String countyCode,
                             @RequestParam(value = "memberLevelId") String memberLevelId) {
        try {
            List<SalesRuleProduct> products = Json.fromJsonAsList(SalesRuleProduct.class, productList);
            List<Sales_coupon> salesCoupons = salesCouponService.sales(storeId, products, countyCode, memberLevelId);
            return Result.success("globals.result.success", salesCoupons);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error("globals.result.error");
        }
    }

}
