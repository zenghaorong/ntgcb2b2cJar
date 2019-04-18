package com.aebiz.app.web.modules.controllers.front.pc.member;

import com.aebiz.app.member.modules.models.Member_address;
import com.aebiz.app.member.modules.models.Member_coupon;
import com.aebiz.app.member.modules.services.MemberAddressService;
import com.aebiz.app.member.modules.services.MemberCouponService;
import com.aebiz.app.order.commons.utils.OrderStatusUtil;
import com.aebiz.app.sales.modules.models.Sales_coupon;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.SpringUtil;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员优惠券管理
 */
@Controller
@RequestMapping("/member/coupon")
public class PcMemberCouponController {
    private static final Log log = Logs.get();

    @Autowired
    private MemberCouponService memberCouponService;

    /**
     * 到个人中心优惠券管理页面
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(HttpServletRequest request) {
        //获取当前会员的Id
        String accountId = StringUtil.getMemberUid();
        int count1 = memberCouponService.count(Cnd.where("accountId","=",accountId).and("status","=",0).and("delFlag","=",0));
        int count2 = memberCouponService.count(Cnd.where("accountId","=",accountId).and("status","=",1).and("delFlag","=",0));
        int count3 = memberCouponService.count(Cnd.where("accountId","=",accountId).and("status","=",2).and("delFlag","=",0));
        request.setAttribute("count1",count1);
        request.getSession().setAttribute("couponCount",count1);
        request.setAttribute("count2",count2);
        request.setAttribute("count3",count3);
        return "pages/front/pc/member/coupon";
    }


    @RequestMapping(value= "/data",method = RequestMethod.POST)
    @SJson
    public Object memberCoupon(@RequestParam("page")Integer page,@RequestParam("rows")Integer rows,@RequestParam("status")Integer status/*, @RequestParam(value = "id",required = false)String id*/, HttpServletRequest req){
        try {
            //获取当前会员的Id
            String accountId = StringUtil.getMemberUid();
            Map<String,Object> map  = memberCouponService.selectData(accountId,page,rows,status);
            return map;
        }catch (Exception e){
            return Result.error("member.coupon.select.fail");
        }
    }

    @SJson
    @RequestMapping(value= "/memberCouponDelete/{id}",method = RequestMethod.POST)
    public Object memberCouponDelete(@PathVariable("id")String id,HttpServletRequest request){
        try{
            Member_coupon member_coupon = memberCouponService.fetch(id);
            member_coupon.setDelFlag(true);
            memberCouponService.update(member_coupon);
            return Result.success("member.coupon.delete.success");
        }catch (Exception e){
            return Result.error("member.coupon.delete.fail");
        }

    }
}
