package com.aebiz.app.web.commons.quartz.job;

import com.aebiz.app.member.modules.models.Member_coupon;
import com.aebiz.app.member.modules.services.MemberCouponService;
import com.aebiz.app.member.modules.services.impl.MemberCouponServiceImpl;
import com.aebiz.app.sales.modules.models.Sales_coupon;
import com.aebiz.app.sales.modules.models.Sales_rule_order;
import com.aebiz.app.sales.modules.services.SalesCouponService;
import com.aebiz.app.sales.modules.services.SalesRuleOrderService;
import com.aebiz.app.sales.modules.services.impl.SalesCouponServiceImpl;
import com.aebiz.app.sales.modules.services.impl.SalesRuleOrderServiceImpl;
import com.aebiz.app.sys.modules.services.SysTaskService;
import com.aebiz.app.sys.modules.services.impl.SysTaskServiceImpl;
import com.aebiz.commons.utils.DateUtil;
import com.aebiz.commons.utils.SpringUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;

/**
 * 优惠券过期定时检查Job
 * Created by ThinkPad on 2017/7/21.
 */
public class CouponOverTimeJob implements Job{

    private static final Log log = Logs.get();

    private SysTaskService sysTaskService = SpringUtil.getBean("sysTaskServiceImpl", SysTaskServiceImpl.class);

    private MemberCouponService memberCouponService = SpringUtil.getBean("memberCouponServiceImpl",MemberCouponServiceImpl.class);

    private SalesCouponService salesCouponService = SpringUtil.getBean("salesCouponServiceImpl", SalesCouponServiceImpl.class);

    private SalesRuleOrderService salesRuleOrderService = SpringUtil.getBean("salesRuleOrderServiceImpl",SalesRuleOrderServiceImpl.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String taskId = context.getJobDetail().getKey().getName();
        try{
            log.debug("优惠券过期作业检查开始---------------");
            List<Member_coupon> memberCouponList = memberCouponService.query(Cnd.where("status","=",0).and("delFlag","=",false));

            if(memberCouponList != null){
                for(Member_coupon memberCoupon: memberCouponList){
                    //获取优惠券信息
                    Sales_coupon salesCoupon = salesCouponService.getField("ruleId",memberCoupon.getCouponId());
                    Sales_rule_order salesRuleOrder = salesRuleOrderService.fetch(salesCoupon.getRuleId());
                    if(salesRuleOrder != null){
                        Integer endAt = salesRuleOrder.getEndAt();
                        Integer nowTime = DateUtil.getTime(new Date());
                        if(nowTime > endAt){
                            //优惠券过期失效
                            memberCouponService.update(Chain.make("status",2),Cnd.where("id","=",memberCoupon.getId()));
                        }
                    }
                }
            }

            //由于优惠券有效期的调整已失效的优惠券重新有效
            memberCouponList = memberCouponService.query(Cnd.where("status","=",2).and("delFlag","=",false));
            if(memberCouponList != null){
                for(Member_coupon memberCoupon: memberCouponList){
                    //获取优惠券信息
                    Sales_coupon salesCoupon = salesCouponService.getField("ruleId",memberCoupon.getCouponId());
                    Sales_rule_order salesRuleOrder = salesRuleOrderService.fetch(salesCoupon.getRuleId());
                    if(salesRuleOrder != null){
                        Integer endAt = salesRuleOrder.getEndAt();
                        Integer nowTime = DateUtil.getTime(new Date());
                        if(nowTime > endAt){
                            //优惠券重新有效
                            memberCouponService.update(Chain.make("status",0),Cnd.where("id","=",memberCoupon.getId()));
                        }
                    }
                }
            }
            sysTaskService.update(Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行成功").add("nextAt", DateUtil.getTime(context.getNextFireTime())), Cnd.where("id", "=", taskId));
        }catch (Exception e){
            sysTaskService.update(Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行失败").add("nextAt", DateUtil.getTime(context.getNextFireTime())), Cnd.where("id", "=", taskId));
        }finally {
            log.debug("优惠券过期作业检查结束---------------");
        }
    }
}
