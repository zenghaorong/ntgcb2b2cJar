package com.aebiz.app.sales.modules.services.impl;

import com.aebiz.app.goods.modules.models.em.GoodsSaleClientEnum;
import com.aebiz.app.member.modules.models.Member_coupon;
import com.aebiz.app.member.modules.services.MemberCouponService;
import com.aebiz.app.sales.modules.commons.SalesRuleCondition;
import com.aebiz.app.sales.modules.commons.SalesRuleProduct;
import com.aebiz.app.sales.modules.models.Sales_coupon;
import com.aebiz.app.sales.modules.models.Sales_rule_order;
import com.aebiz.app.sales.modules.models.em.SalesCheckStatusEnum;
import com.aebiz.app.sales.modules.services.SalesCouponService;
import com.aebiz.app.sales.modules.services.SalesRuleGoodsService;
import com.aebiz.app.sales.modules.services.SalesRuleOrderService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.commons.utils.DateUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class SalesCouponServiceImpl extends BaseServiceImpl<Sales_coupon> implements SalesCouponService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    @Autowired
    private SalesRuleOrderService salesRuleOrderService;
    @Autowired
    private SalesRuleGoodsService salesRuleGoodsService;

    @Autowired
    private MemberCouponService memberCouponService;

    @Autowired
    private SalesCouponService salesCouponService;

    @Override
    @Transactional
    public void save(Sales_coupon coupon) {
        if (Strings.isBlank(coupon.getId())) {
            this.dao().insertWith(coupon, "salesRuleOrder");
        } else {
            //重新设置启用状态
            int nowAt = DateUtil.getTime(new Date());
            if (coupon.getSalesRuleOrder().getSartAt() <= nowAt && nowAt <= coupon.getSalesRuleOrder().getEndAt()) {
                coupon.setDisabled(false);
                coupon.getSalesRuleOrder().setDisabled(false);
            }
            this.dao().updateIgnoreNull(coupon);
            salesRuleOrderService.updateIgnoreNull(coupon.getSalesRuleOrder());
        }
    }

    @Override
    @Transactional
    public void check(Sales_coupon coupon) {
        Chain chain = Chain.make("checkStatus", coupon.getCheckStatus());
        chain.add("opBy", coupon.getOpBy());
        chain.add("opAt", coupon.getOpAt());
        this.update(chain, Cnd.where("id", "=", coupon.getId()));
    }

    @Override
    @Transactional
    public void cascadeDelete(String id) {
        Sales_coupon coupon = this.fetch(id);
        salesRuleOrderService.vDelete(coupon.getRuleId());
        this.vDelete(id);
    }

    @Override
    @Transactional
    public void cascadeDelete(String[] ids) {
        for (String id : ids) {
            cascadeDelete(id);
        }
    }

    @Override
    @Transactional
    public List<Sales_coupon> sales(String storeId, List<SalesRuleProduct> products, String countyCode, String memberLevelId) {
        return sales(storeId,  products, GoodsSaleClientEnum.PC.getKey(), countyCode, memberLevelId);
    }

    @Override
    @Transactional
    public List<Sales_coupon> sales(String storeId, List<SalesRuleProduct> products, int client, String countyCode, String memberLevelId) {
        //取当前有效的促销规则
        int nowAt = DateUtil.getTime(new Date());
        StringBuilder sb = new StringBuilder("select * from sales_coupon sc ");
        sb.append("where ");
        sb.append("exists(select 1 from sales_rule_order sro where sro.sartAt<=@nowAt and sro.endAt>=@nowAt and sro.id=sc.ruleId) ");
        sb.append("and exists(select 1 from member_coupon mc,member_user mu where mc.couponId=sc.id and mc.accountId=mu.accountId and mu.levelId=@memberLevelId) ");
        sb.append("and sc.storeId=@storeId and sc.disabled=false and sc.checkStatus=@checkStatus ");
        Sql sql = Sqls.queryEntity(sb.toString());
        sql.setParam("nowAt", nowAt);
        sql.setParam("storeId", storeId);
        sql.setParam("disabled", storeId);
        sql.setParam("checkStatus", SalesCheckStatusEnum.PASS.getKey());
        sql.setParam("storeId", storeId);
        sql.setParam("memberLevelId", memberLevelId);
        sql.setEntity(this.dao().getEntity(Sales_coupon.class));
        this.dao().execute(sql);
        List<Sales_coupon> salesCoupons = this.fetchLinks(sql.getList(Sales_coupon.class), "salesRuleOrder");

        //过滤订单总价满足的规则
        SalesRuleCondition salesRuleCondition;
        if (!Lang.isEmpty(salesCoupons)) {

            //设置价格
            for (SalesRuleProduct salesRuleProduct : products) {
                salesRuleProduct.setPrice(salesRuleGoodsService.price(salesRuleProduct.getSku(), client, countyCode, memberLevelId, salesRuleProduct.getSalesId()));
            }

            Iterator<Sales_coupon> it = salesCoupons.iterator();
            while (it.hasNext()) {
                Sales_rule_order sro = it.next().getSalesRuleOrder();
                salesRuleCondition = Json.fromJson(SalesRuleCondition.class, sro.getConditions());
                //计算商品总价
                int totalSum = products.stream().map(product->product.getNum()*product.getPrice()).reduce(Integer::sum).get();
                //计算商品总数
                int numSum = products.stream().map(product->product.getNum()).reduce(Integer::sum).get();

                switch (sro.getCnd_template()) {
                    //自定义模板
                    case "tpl_sale_order_cnd_userdefined": {

                    }break;
                    //当订单商品总价满X，对指定的商品(货品)优惠
                    case "tpl_sale_order_cnd_subtotalselectgoods": {

                    }break;
                    //当订单商品总价满X，对所有商品优惠
                    case "tpl_sale_order_cnd_allorderallgoods": {
                        int ruleTotal = Integer.valueOf(salesRuleCondition.getValue());
                        if (totalSum < ruleTotal) {
                            break;
                        }
                    }break;
                    //当订单商品数量满X，给予优惠
                    case "tpl_sale_order_cnd_itemsquanityallgoods": {
                        int ruleTotal = Integer.valueOf(salesRuleCondition.getValue());
                        if (numSum < ruleTotal) {
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
        return salesCoupons;
    }

    /**
     * 获取店铺的优惠券（根据账号绑定是否领取）
     * @param storeId
     * @param accountId
     * @return
     */
    @Override
    public List<Sales_coupon> getCouponList(String storeId, String accountId) {
        //查询当前会员可用的优惠券
        List<Member_coupon>  memberCouponList =  memberCouponService.query("couponId",Cnd.where("accountId","=",accountId).and("storeId","=",storeId).and("status","=",0).and("delFlag","=",false));
        String [] couponIdArray = new String[]{};
        for(Member_coupon memberCoupon :memberCouponList){
            couponIdArray = ArrayUtils.add(couponIdArray,memberCoupon.getCouponId());
        }
        //会员可用的优惠券
        //List<Sales_coupon> memberSaleCouponList = salesCouponService.query(Cnd.where("id","in",couponIdArray));
        //查询店铺可用的优惠券
        StringBuilder sb  = new StringBuilder("select sc.* from sales_coupon sc,sales_rule_order sro where sc.ruleId = sro.id " +
                " and sc.disabled = false and sc.checkStatus = 2 and sc.storeId=@storeId and sro.sartAt <= @nowAt and sro.endAt >= @nowAt");
        //组装orderSql
        int nowAt = DateUtil.getTime(new Date());
        Sql orderSql = Sqls.queryEntity(sb.toString());
        orderSql.setParam("nowAt", nowAt);
        orderSql.setParam("storeId", storeId);
        orderSql.setEntity(this.dao().getEntity(Sales_coupon.class));
        this.dao().execute(orderSql);
        //店铺可用的优惠券
        List<Sales_coupon> storeSalesCouponList = orderSql.getList(Sales_coupon.class);
        for(Sales_coupon salesCoupon:storeSalesCouponList){
            Sales_rule_order salesRuleOrder = salesRuleOrderService.fetch(salesCoupon.getRuleId());
            salesCoupon.setSalesRuleOrder(salesRuleOrder);
            if(ArrayUtils.contains(couponIdArray,salesCoupon.getId())){
                salesCoupon.setHasReceive(true);
            }else{
                salesCoupon.setHasReceive(false);
            }
        }
        return storeSalesCouponList ;
    }

    @Override
    public boolean isReceieveable(String couponId, int num) {
        Sales_coupon salesCoupon = salesCouponService.fetch(couponId);
        return salesCoupon.getLimit_num() - salesCoupon.getSend_num() - num > 0;
    }
}
