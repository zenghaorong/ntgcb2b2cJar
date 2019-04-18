package com.aebiz.app.web.modules.controllers.open.pay.wxpay;

import com.aebiz.app.order.commons.utils.OrderUtil;
import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.models.Order_pay_payment;
import com.aebiz.app.order.modules.models.em.OrderPayStatusEnum;
import com.aebiz.app.order.modules.services.OrderMainService;
import com.aebiz.app.order.modules.services.OrderPayPaymentService;
import com.aebiz.app.shop.modules.models.Shop_payment;
import com.aebiz.app.shop.modules.services.ShopPaymentService;
import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.lang.Streams;
import org.nutz.lang.Xmls;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.bean.WxPayUnifiedOrder;
import org.nutz.weixin.impl.WxApi2Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ThinkPad on 2017/6/19.
 */
@Controller
@RequestMapping("/open/pay/wxPay")
public class WxpayController {

    private static final Log log = Logs.get();

    @Autowired
    private ShopPaymentService shopPaymentService;

    @Autowired
    private OrderMainService orderMainService;

    @Autowired
    private OrderPayPaymentService orderPayPaymentService;

    /**
     * 订单组微信支付
     * @param groupId
     * @param payBody
     * @param req
     * @return
     */
    @RequestMapping("")
    @SJson
    public Object payWxPay(@RequestParam("groupId") String groupId,@RequestParam("payBody")String payBody,HttpServletRequest req){

        try{
            Shop_payment shopPayment = shopPaymentService.fetch(Cnd.where("code","=","wxpay"));
            NutMap payInfo = Json.fromJson(NutMap.class, shopPayment.getInfo());
            //创建一个wxApi2
            WxApi2Impl wxApi2 = new WxApi2Impl();
            WxPayUnifiedOrder order = new WxPayUnifiedOrder();
            order.setAppid(payInfo.getString("AppId"));
            order.setMch_id(payInfo.getString("Mchid"));
            order.setNonce_str(R.UU32());
            //商品描述
            order.setBody(payBody);
            //商家数据包，存入需要的订单组号
            order.setAttach(groupId);
            //商户订单号---存入规则-日期加6位随机数
            String tradeNo = groupId + OrderUtil.getRandNum(100000,999999);
            order.setOut_trade_no(tradeNo);
            //计算订单总金额
            List<Order_main> orderMainList = orderMainService.query("payMoney",Cnd.where("delFlag","=",false).and("groupId","=",groupId));
            if(orderMainList == null){
                return Result.error("wx.pay.order.tip.no");
            }
            int money = 0;
            for(Order_main orderMain :orderMainList){
                money += orderMain.getPayMoney();
            }
            order.setTotal_fee(money);
            order.setSpbill_create_ip(Globals.APP_IP);
            order.setNotify_url("http://" + Globals.APP_DOMAIN + "/open/pay/wxPay/payBack");//支付结果回调通知地址
            //交易类型
            order.setTrade_type("NATIVE");
            //商品Id
            order.setProduct_id(groupId);//NATIVE时填写
            NutMap resp = wxApi2.pay_unifiedorder(payInfo.getString("key"), order);
            String return_code = resp.getString("result_code", "");//SUCCESS
            if(!"SUCCESS".equals(return_code)){
                return Result.error(resp.getString("err_code_des", ""));
            }
            String code_url = resp.getString("code_url", "");
            log.debug("resp:::" + Json.toJson(resp));
            return Result.success("globals.result.success", code_url);
        }catch (Exception e){
            log.debug(e.getMessage(),e);
            return Result.error("globals.result.error");
        }
    }

    /**
     * 订单微信支付
     * @param orderId
     * @param payBody
     * @param req
     * @return
     */
    @RequestMapping("/order")
    @SJson
    public Object wxPayOrder(@RequestParam("orderId") String orderId,@RequestParam("payBody")String payBody,HttpServletRequest req){
        try{
            Shop_payment shopPayment = shopPaymentService.fetch(Cnd.where("code","=","wxpay"));
            NutMap payInfo = Json.fromJson(NutMap.class, shopPayment.getInfo());
            //创建一个wxApi2
            WxApi2Impl wxApi2 = new WxApi2Impl();
            WxPayUnifiedOrder order = new WxPayUnifiedOrder();
            order.setAppid(payInfo.getString("AppId"));
            order.setMch_id(payInfo.getString("Mchid"));
            order.setNonce_str(R.UU32());
            //商品描述
            order.setBody(payBody);
            //商家数据包，存入需要的订单组号
            order.setAttach(orderId);
            //商户订单号---存入规则-日期加6位随机数
            String tradeNo = orderId + OrderUtil.getRandNum(100000,999999);
            order.setOut_trade_no(tradeNo);
            //计算订单总金额
            Order_main orderMain  = orderMainService.getField("payMoney",Cnd.where("delFlag","=",false).and("id","=",orderId));
            if(orderMain == null){
                return Result.error("wx.pay.order.tip.no");
            }
            int money =  orderMain.getPayMoney();
            order.setTotal_fee(money);
            order.setSpbill_create_ip(Globals.APP_IP);
            order.setNotify_url("http://" + Globals.APP_DOMAIN + "/open/pay/wxPay/payOrderBack");//支付结果回调通知地址
            //交易类型
            order.setTrade_type("NATIVE");
            //商品Id
            order.setProduct_id(orderId);//NATIVE时填写
            NutMap resp = wxApi2.pay_unifiedorder(payInfo.getString("key"), order);
            String return_code = resp.getString("result_code", "");//SUCCESS
            if(!"SUCCESS".equals(return_code)){
                return Result.error(resp.getString("err_code_des", ""));
            }
            String code_url = resp.getString("code_url", "");
            log.debug("resp:::" + Json.toJson(resp));
            return Result.success("globals.result.success", code_url);
        }catch (Exception e){
            log.debug(e.getMessage(),e);
            return Result.error("globals.result.error");
        }
    }


    /**
     * 订单组支付回调
     * @param reader
     * @return
     */
    @RequestMapping(value = "/payBack")
    @SJson
    public Object wxPayBack(Reader reader){
        NutMap res = Xmls.xmlToMap(Streams.readAndClose(reader));
        Map<String, Object> map = new HashMap<>();
        if("SUCCESS".equals(res.getString("result_code"))){
            //取传递的订单组号
            String groupId = res.getString("attach");
            //查询订单列表
            List<Order_main> orderMainList = orderMainService.query(Cnd.where("delFlag","=",false).and("groupId","=",groupId));
            //统计订单总金额
            if(orderMainList == null){
                map.put("return_code", "FAIL");
                return Xmls.mapToXml(map);
            }
            //生成订单支付明细
            for(Order_main orderMain :orderMainList){
                Order_pay_payment orderPayPayment = new Order_pay_payment();
                orderPayPayment.setGroupId(orderMain.getGroupId());
                orderPayPayment.setOrderId(orderMain.getId());
                orderPayPayment.setAccountId(orderMain.getAccountId());
                orderPayPayment.setStoreId(orderMain.getStoreId());
                orderPayPayment.setPayMoney(orderMain.getPayMoney());
                //查询微信支付方式
                Shop_payment shopPayment = shopPaymentService.fetch(Cnd.where("code","=","wxpay"));
                orderPayPayment.setPayCode(shopPayment.getCode());
                orderPayPayment.setPayName(shopPayment.getName());
                orderPayPayment.setPayAccount(res.getString("appid"));
                orderPayPayment.setPayIp(Globals.APP_IP);
                orderPayPayment.setReturn_url(Globals.APP_PROTOCOL + Globals.APP_DOMAIN + "/wxPay/wxPayBack");
                orderPayPayment.setTrade_no(res.getString("transaction_id"));
                orderPayPayment.setNote(orderMain.getNote());
                orderPayPayment.setPayAt(orderMain.getPayAt());
                orderPayPayment.setFinishAt((int)(System.currentTimeMillis()/1000));
                orderPayPayment.setPaySucess(true);
                orderPayPayment.setOpBy(StringUtil.getMemberUid());
                orderPayPayment.setOpAt((int)(System.currentTimeMillis()/1000));
                orderPayPayment.setDelFlag(false);
                orderPayPaymentService.insert(orderPayPayment);
                orderMain.setPayStatus(OrderPayStatusEnum.PAYALL.getKey());
                orderMainService.update(Chain.make("payStatus",OrderPayStatusEnum.PAYALL.getKey()).add("payAt",(int)(System.currentTimeMillis()/1000)),Cnd.where("id","=",orderMain.getId()));
                // TODO: 2017/6/20  这里需要给商家发短信或者其它渠道的方式
            }
            map.put("return_code", "SUCCESS");
        }else {
            map.put("return_code", "FAIL");//支付失败
        }

        log.debug("res::" + Json.toJson(res));
        return Xmls.mapToXml(map);
    }

    /**
     * 订单支付回调
     * @param reader
     * @return
     */
    @RequestMapping(value = "/payOrderBack")
    @SJson
    public Object wxPayOrderBack(Reader reader){
        NutMap res = Xmls.xmlToMap(Streams.readAndClose(reader));
        Map<String, Object> map = new HashMap<>();
        if("SUCCESS".equals(res.getString("result_code"))){
            //取传递的订单号
            String orderId = res.getString("attach");
            //查询订单列表
            Order_main orderMain = orderMainService.fetch(Cnd.where("delFlag","=",false).and("id","=",orderId));
            //统计订单总金额
            if(orderMain == null){
                map.put("return_code", "FAIL");
                return Xmls.mapToXml(map);
            }
            //生成订单支付明细
            Order_pay_payment orderPayPayment = new Order_pay_payment();
            orderPayPayment.setGroupId(orderMain.getGroupId());
            orderPayPayment.setOrderId(orderMain.getId());
            orderPayPayment.setAccountId(orderMain.getAccountId());
            orderPayPayment.setStoreId(orderMain.getStoreId());
            orderPayPayment.setPayMoney(orderMain.getPayMoney());
            //查询微信支付方式
            Shop_payment shopPayment = shopPaymentService.fetch(Cnd.where("code","=","wxpay"));
            orderPayPayment.setPayCode(shopPayment.getCode());
            orderPayPayment.setPayName(shopPayment.getName());
            orderPayPayment.setPayAccount(res.getString("appid"));
            orderPayPayment.setPayIp(Globals.APP_IP);
            orderPayPayment.setReturn_url(Globals.APP_PROTOCOL + Globals.APP_DOMAIN + "/wxPay/wxPayBack");
            orderPayPayment.setTrade_no(res.getString("transaction_id"));
            orderPayPayment.setNote(orderMain.getNote());
            orderPayPayment.setPayAt(orderMain.getPayAt());
            orderPayPayment.setFinishAt((int)(System.currentTimeMillis()/1000));
            orderPayPayment.setPaySucess(true);
            orderPayPayment.setOpBy(StringUtil.getMemberUid());
            orderPayPayment.setOpAt((int)(System.currentTimeMillis()/1000));
            orderPayPayment.setDelFlag(false);
            orderPayPaymentService.insert(orderPayPayment);
            orderMain.setPayStatus(OrderPayStatusEnum.PAYALL.getKey());
            orderMainService.update(Chain.make("payStatus",OrderPayStatusEnum.PAYALL.getKey()).add("payAt",(int)(System.currentTimeMillis()/1000)),Cnd.where("id","=",orderMain.getId()));
            // TODO: 2017/6/20  这里需要给商家发短信或者其它渠道的方式
            map.put("return_code", "SUCCESS");
        }else {
            map.put("return_code", "FAIL");//支付失败
        }

        log.debug("res::" + Json.toJson(res));
        return Xmls.mapToXml(map);
    }

}
