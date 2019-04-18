package com.aebiz.app.web.modules.controllers.front.pc.order;

import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.models.em.OrderPayStatusEnum;
import com.aebiz.app.order.modules.services.OrderMainService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import org.nutz.dao.Cnd;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by ThinkPad on 2017/6/20.
 */
@Controller
@RequestMapping("/order")
public class PcOrderController {

    private static final Log log = Logs.get();

    @Autowired
    private OrderMainService orderMainService;
    /**
     * 根据订单组查询订单是否支付成功
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/isPaySuccess",method = RequestMethod.POST)
    @SJson
    public Object isPaySuccess(@RequestParam("groupId")String groupId){

        List<Order_main> orderMainList = orderMainService.query(Cnd.where("delFlag","=",false).and("groupId","=",groupId));
        if(orderMainList == null){
            return Result.error("订单不存在");
        }
        for(Order_main orderMain:orderMainList){
            if(OrderPayStatusEnum.PAYALL.getKey() != orderMain.getPayStatus()){
                return Result.error("订单未支付");
            }
        }
        return Result.success("globals.result.success");
    }

    /**
     * 跳转支付成功页面
     * @return
     */
    @RequestMapping(value = "/paySuccess/{id}",method = RequestMethod.GET)
    public String toPaySuccess(@PathVariable("id")String id,HttpServletRequest req){
        //订单支付成功
        req.setAttribute("orderId",id);
        req.setAttribute("status","success");
        return "pages/front/pc/order/countStatus";
    }

    @RequestMapping(value = "/orderFail", method = RequestMethod.GET)
    public String toOrderFail(HttpServletRequest req){
        //订单下单失败
        req.setAttribute("status","fail");
        return "pages/front/pc/order/countStatus";
    }


}
