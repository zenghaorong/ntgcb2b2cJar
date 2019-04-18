package com.aebiz.app.web.modules.controllers.front.pc.order;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.member.modules.models.Member_level;
import com.aebiz.app.member.modules.models.Member_user;
import com.aebiz.app.member.modules.services.MemberAccountService;
import com.aebiz.app.member.modules.services.MemberLevelService;
import com.aebiz.app.member.modules.services.MemberUserService;
import com.aebiz.app.msg.modules.services.CommMsgService;
import com.aebiz.app.order.modules.models.Order_goods;
import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.services.OrderGoodsService;
import com.aebiz.app.order.modules.services.OrderMainService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.shop.modules.services.ShopExpressService;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.app.store.modules.services.StoreMainService;
import com.aebiz.app.web.commons.utils.KuaiDi100Util;
import com.aebiz.baseframework.base.Result;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Cnd;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取商品详情页
 * Created by ThinkPad on 2017/7/25.
 */
@Controller
@RequestMapping("/member/orderDetail")
public class OrderDetailController {
    private static final Log log = Logs.get();
    @Autowired
    private OrderMainService OrderMainService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private GoodsProductService goodsProductService;
    @Autowired
    private StoreMainService storeMainService;
    @Autowired
    private ShopExpressService shopExpressService;
    @Autowired
    private CommMsgService commMsgService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private MemberAccountService memberAccountService;
    @Autowired
    private MemberUserService memberUserService;
    @Autowired
    private MemberLevelService memberLevelService;
    @Autowired
    private ShopAreaService shopAreaService;
    @Autowired
    private PropertiesProxy config;

    /**
     * 获取订单详情
     * orderId订单id
     *
     * @param request
     * @return
     */
    @RequestMapping("getDetail")
    public String getdetai(@RequestParam("orderId") String orderId, HttpServletRequest request) {
        //获取会员id
        String accountId = StringUtil.getMemberUid();
        NutMap personal = NutMap.NEW();
        Subject subject = SecurityUtils.getSubject();
        Member_user user = memberUserService.fetch(Cnd.where("accountId", "=", accountId));
        Account_info accInfo = accountInfoService.fetch(accountId);
        if (accInfo != null) {
            Member_level memLevel = memberLevelService.fetch(user.getLevelId());
            personal.put("photo", accInfo.getImageUrl());
            personal.put("name", accInfo.getName());
            personal.put("levelName", memLevel.getName());
        }
        request.setAttribute("personal", personal);
        Order_main orderMain = OrderMainService.fetch(orderId);
        if (orderMain != null) {
            //未付款时间倒计时（前台页面展示）
            int orderAt = orderMain.getOrderAt();//下单时间
            int sysAt = (int) (System.currentTimeMillis() / 1000);//系统时间
            //将48小时转换为秒（默认未付款订单2小时关闭）
            int closeAt = 2 * 3600;
            if (sysAt - orderAt < closeAt) {
                request.setAttribute("closeAt", (orderAt + closeAt) - sysAt);
            } else {
                request.setAttribute("closeAt", 0);
            }
            request.setAttribute("orderMain", orderMain);
            request.setAttribute("shopAreaService", shopAreaService);
            //获取订单的商品信息列表
            List<Order_goods> orderGoods = orderGoodsService.query(Cnd.where("orderId", "=", orderId));
            List<Order_goods> products = new ArrayList<Order_goods>();
            List<Map> listMaps = new ArrayList<Map>();
            if (orderGoods != null && orderGoods.size() > 0) {
                for (Order_goods orderGood : orderGoods) {
                    String img = goodsProductService.getProductImage(orderGood.getSku());
                    orderGood.setImgUrl(img);
                    products.add(orderGood);
                }
            }
            request.setAttribute("ListProduct", products);
            String storeId = orderMain.getStoreId();
            Store_main storeMain = storeMainService.fetch(storeId);
            request.setAttribute("storeMain", storeMain);
            //订单物流信息
            if (Strings.isNotBlank(orderMain.getExpressId()) && Strings.isNotBlank(orderMain.getExpressNo())) {
                orderMain.setExpressInfo(KuaiDi100Util.getInfoUrl(shopExpressService.fetch(orderMain.getExpressId()).getCode(), orderMain.getExpressNo(),config.get("ext.kuaidi100_key","")));
            }
        } else {
            return "/404";
        }
        return "pages/front/pc/member/order/orderDetail";
    }

    /**
     * 提醒发货
     * orderId 订单id
     */
    @RequestMapping("remindShipment")
    public Object remindShipment(@RequestParam("orderId") String orderId) {
        try {
            //站内信(暂时没有配code值)
            commMsgService.sendMsg("", "", "", "", "", "", "", "", null);
            return Result.success("提醒成功");
        } catch (Exception e) {
            log.error(e);
            return Result.success("提醒失败");
        }
    }
}
