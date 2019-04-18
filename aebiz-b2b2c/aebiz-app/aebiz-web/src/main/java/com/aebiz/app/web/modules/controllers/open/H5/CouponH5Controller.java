package com.aebiz.app.web.modules.controllers.open.H5;

import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.cms.modules.models.Cms_video;
import com.aebiz.app.member.modules.models.Member_coupon;
import com.aebiz.app.member.modules.services.MemberCouponService;
import com.aebiz.app.sales.modules.models.Sales_coupon;
import com.aebiz.app.sales.modules.services.SalesCouponService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Cnd;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @Auther: zenghaorong
 * @Date: 2019/4/10  22:02
 * @Description: 优惠劵 控制层
 */
@Controller
@RequestMapping("/open/h5/coupon")
public class CouponH5Controller {

    private static final Log log = Logs.get();

    @Autowired
    private MemberCouponService memberCouponService;
    @Autowired
    private SalesCouponService salesCouponService;

    /***
     * 订单确认页查询可用优惠劵列表
     * @param productNum 商品数量
     * @param price 商品总价格
     * @param productType 商品类型 （1.黏土商品 2.视频商品）
     * @return
     */
    @RequestMapping("getMyOrderCoupon.html")
    @SJson
    public Result getMyOrderCoupon(Integer productNum,double price,String productType){
        try {
            Subject subject = SecurityUtils.getSubject();
            Account_user accountUser = (Account_user) subject.getPrincipal();
            Cnd cnd = Cnd.NEW();
            cnd.and("delFlag", "=", 0 );
            cnd.and("accountId", "=", accountUser.getId() );
            List<Member_coupon> member_couponList = memberCouponService.query(cnd);
            for(Member_coupon member_coupon : member_couponList){
                Sales_coupon sales_coupon = salesCouponService.fetch(member_coupon.getCouponId());
                //判断优惠劵类型
                if("1".equals(sales_coupon.getType())){ //满减劵
                    if(sales_coupon.getConditionAmount()!=null) {
                        if (price >= sales_coupon.getConditionAmount()) {
                            member_coupon.setSales_coupon(sales_coupon);
                        }
                    }
                }

                //判断为黏土商品
                if("1".equals(productType)){
                    if("2".equals(sales_coupon.getType())){ //免运费劵
                        if(sales_coupon.getProductQuantityRule()!=null) {
                            if (productNum >= sales_coupon.getProductQuantityRule()) {
                                member_coupon.setSales_coupon(sales_coupon);
                            }
                        }
                    }
                    if("3".equals(sales_coupon.getType())){ //折扣劵
                        if(sales_coupon.getProductQuantityRule()!=null) {
                            if (productNum >= sales_coupon.getProductQuantityRule()) {
                                member_coupon.setSales_coupon(sales_coupon);
                            }
                        }
                    }
                }


            }
            return Result.success("ok",member_couponList);
        } catch (Exception e) {
            log.error("获取订单可用优惠劵异常",e);
            return Result.error("fail");
        }
    }

    /**
     * 进入领劵中心
     */
    @RequestMapping("receiveCoupon.html")
    public String receiveCoupon(String id){
        Subject subject = SecurityUtils.getSubject();
        Account_user accountUser = (Account_user) subject.getPrincipal();
        if (accountUser == null) {
            return "pages/front/h5/niantu/login";
        }
        return "pages/front/h5/niantu/receiveCoupon";
    }

    /**
     * 领取优惠劵接口
     */
    @RequestMapping("receive.html")
    @SJson
    public Result receive(String couponId){
        try {
            Subject subject = SecurityUtils.getSubject();
            Account_user accountUser = (Account_user) subject.getPrincipal();

            //查询优惠劵详情信息
            Sales_coupon sales_coupon = salesCouponService.fetch(couponId);

            //查询本人优惠劵
            Cnd cndC = Cnd.NEW();
            cndC.and("couponId", "=", couponId );
            cndC.and("accountId", "=", accountUser.getId());
            List<Member_coupon> member_couponList = memberCouponService.query(cndC);
            if(member_couponList!=null) {
                if (sales_coupon.getLimit_num() >= member_couponList.size()) {
                    return Result.error(10001,"您已达到当前优惠劵的领取上限");
                }
            }

            //绑定该用户
            Member_coupon member_coupon = new Member_coupon();
            member_coupon.setAccountId(accountUser.getId());
            member_coupon.setCouponId(couponId);
            int codeTime=getSecondTimestamp(new Date());
            String random =  getStringRandom(4);
            member_coupon.setCode(sales_coupon.getCodeprefix()+codeTime+random);
            memberCouponService.insert(member_coupon);
            return Result.success("ok");
        } catch (Exception e) {
            log.error("获取领劵中心优惠劵列表异常",e);
            return Result.error("fail");
        }
    }

    /**
     * 获取领劵中心优惠劵列表
     */
    @RequestMapping("couponList.html")
    @SJson
    public Result couponList(){
        try {
            int time = getSecondTimestamp(new Date());
            Cnd cnd = Cnd.NEW();
            cnd.and("delFlag", "=", 0 );
            cnd.and("disabled", "=", 0);
            cnd.and("startTime", "<", time);
            cnd.and("endTime", ">", time);
            List<Sales_coupon> list= salesCouponService.query(cnd);
            return Result.success("ok",list);
        } catch (Exception e) {
            log.error("获取领劵中心优惠劵列表异常",e);
            return Result.error("fail");
        }
    }


    /***
     * 获取我的优惠劵
     * @return
     */
    @RequestMapping("getMyCoupon.html")
    @SJson
    public Result getMyCoupon(){
        try {
            Subject subject = SecurityUtils.getSubject();
            Account_user accountUser = (Account_user) subject.getPrincipal();
            Cnd cnd = Cnd.NEW();
            cnd.and("accountId", "=", accountUser.getId() );
            List<Member_coupon> member_couponList = memberCouponService.query(cnd);
            for(Member_coupon member_coupon : member_couponList) {
                Sales_coupon sales_coupon = salesCouponService.fetch(member_coupon.getCouponId());
                member_coupon.setSales_coupon(sales_coupon);
            }
            return Result.success("ok",member_couponList);
        } catch (Exception e) {
            log.error("获取订单可用优惠劵异常",e);
            return Result.error("fail");
        }
    }

    /**
     * 进入我的优惠劵
     */
    @RequestMapping("goMyCoupon.html")
    public String goMyCoupon(){
        Subject subject = SecurityUtils.getSubject();
        Account_user accountUser = (Account_user) subject.getPrincipal();
        if (accountUser == null) {
            return "pages/front/h5/niantu/login";
        }
        return "pages/front/h5/niantu/myCoupon";
    }

    /**
     * 获取精确到秒的时间戳
     * @return
     */
    public static int getSecondTimestamp(Date date){
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0,length-3));
        } else {
            return 0;
        }
    }

    //生成随机数字和字母,
    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }


}
