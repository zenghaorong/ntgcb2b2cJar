package com.aebiz.app.member.modules.services.impl;

import com.aebiz.app.member.modules.models.Member_coupon;
import com.aebiz.app.member.modules.services.MemberCouponService;
import com.aebiz.app.sales.modules.models.Sales_coupon;
import com.aebiz.app.sales.modules.services.SalesCouponLogService;
import com.aebiz.app.sales.modules.services.SalesCouponService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberCouponServiceImpl extends BaseServiceImpl<Member_coupon> implements MemberCouponService{
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private SalesCouponService salesCouponService;
    @Autowired
    private SalesCouponLogService salesCouponLogService;

    @Override
    public Map<String, Object> selectData(String accountId, Integer page, Integer rows, Integer status) {
        String countSql = " member_coupon mc " +
                "LEFT JOIN sales_coupon sc ON mc.couponId=sc.id " +
                "LEFT JOIN sales_rule_order sro ON sc.ruleId=sro.id " +
                "LEFT JOIN store_main sm ON sm.id=sro.storeId " +
                "WHERE mc.accountId=@accountId and mc.status=@status and mc.delFlag=0";

        String orderSql ="SELECT mc.id,mc.couponId,mc.status,mc.code, " +
                "sc.name scname,sc.ruleId, " +
                "sro.name sroname,sro.sartAt,sro.endAt,sro.conditions,sro.action_solution, " +
                "sm.storeName "+
                "FROM  " + countSql +" order by sro.endAt asc ";

        Sql sql = Sqls.queryRecord(orderSql+" limit "+(page-1)*rows+","+rows);
        sql.setParam("accountId", accountId);
        sql.setParam("status", status);
        dao().execute(sql);
        int totle = dao().count(Sqls.create(countSql).setParam("accountId", accountId).setParam("status", status).toString());
        List<Member_coupon> list = sql.getList(Member_coupon.class);

        Map<String,Object> map = new HashMap<String,Object>();
        int total = (totle+rows-1)/rows;
        map.put("totalPage", total);
        map.put("page", page);
        map.put("records", totle);
        map.put("count", list.size());
        map.put("rowList", list);
        return map;
    }

    @Transactional
    public void save(String storeId, String couponId, String accountId, int num) {
        Sales_coupon salesCoupon = salesCouponService.fetch(couponId);
        List<Member_coupon> memberCouponList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            Member_coupon memberCoupon = new Member_coupon();
            memberCoupon.setStoreId(storeId);
            memberCoupon.setCouponId(couponId);
            memberCoupon.setAccountId(accountId);
            memberCoupon.setCode(salesCoupon.getCodeprefix()+ StringUtil.getRndNumber(6));
            memberCouponList.add(memberCoupon);
        }
        this.insert(memberCouponList);
        salesCouponService.update(Chain.make("send_num", salesCoupon.getSend_num()+num), Cnd.where("id", "=", salesCoupon.getId()));
    }

    @Override
    public boolean isReceieved(String couponId, String accountId) {
        List<Member_coupon> memberCoupons = this.query(Cnd.where("couponId", "=",couponId).and("accountId", "=", accountId));
        return !Lang.isEmpty(memberCoupons);
    }

}
