package com.aebiz.app.web.modules.controllers.front.pc.member;

import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.goods.modules.models.Goods_favorite;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.models.em.GoodsSaleClientEnum;
import com.aebiz.app.goods.modules.services.GoodsFavoriteService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.member.modules.models.Member_cart;
import com.aebiz.app.member.modules.models.Member_user;
import com.aebiz.app.member.modules.services.MemberAccountService;
import com.aebiz.app.member.modules.services.MemberCartService;
import com.aebiz.app.member.modules.services.MemberLevelService;
import com.aebiz.app.order.modules.models.Order_after_main;
import com.aebiz.app.order.modules.models.Order_after_refundment;
import com.aebiz.app.order.modules.models.Order_goods;
import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.services.OrderAfterMainService;
import com.aebiz.app.order.modules.services.OrderAfterRefundmentService;
import com.aebiz.app.order.modules.services.OrderGoodsService;
import com.aebiz.app.order.modules.services.OrderMainService;
import com.aebiz.app.store.modules.models.Store_favorite;
import com.aebiz.app.store.modules.services.StoreFavoriteService;
import com.aebiz.app.store.modules.services.StoreMainService;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Cnd;
import org.nutz.lang.Lang;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ThinkPad on 2017/7/26.
 */
@Controller
@RequestMapping("/member")
public class PcMemberController {

    private static final Log log = Logs.get();

    @Autowired
    private MemberAccountService memberAccountService;
    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private StoreFavoriteService storeFavoriteService;

    @Autowired
    private MemberLevelService memberLevelService;

    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private GoodsFavoriteService goodsFavoriteService;

    @Autowired
    private MemberCartService memberCartService;

    @Autowired
    private OrderMainService orderMainService;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private StoreMainService storeMainService;

    @Autowired
    private OrderAfterMainService orderAfterMainService;
    @Autowired
    private OrderAfterRefundmentService orderAfterRefundmentService;

    @RequestMapping(value = {"", "index"}, method = RequestMethod.GET)
    @RequiresAuthentication
    public String index(HttpServletRequest req) {

        //个人中心
        NutMap personal = NutMap.NEW();
        Subject subject = SecurityUtils.getSubject();
        Member_user memberUser = (Member_user) subject.getPrincipal();
        personal.put("photo", memberUser.getAccountInfo().getImageUrl());
        personal.put("name", memberUser.getAccountUser().getLoginname());
        personal.put("levelName", memberUser.getMemberLevel().getName());
        personal.put("security", memberAccountService.getSecurityScore(StringUtil.getMemberUid()));
        req.setAttribute("personal", personal);

        //订单
        NutMap order = NutMap.NEW();
        order.put("waitPay", getWaitPayOrder());//待付款
        order.put("waitTake", getWaitTakeOrder());//待收货
        order.put("waitComment", getWaitCommentOrder());//待评价
        req.setAttribute("order", order);

        //售后
        List<Order_after_main> orderAftMains = orderAfterMainService.query(Cnd.where("applyManId", "=", StringUtil.getMemberUid()).limit(1, 1).desc("applyTime"));
        Order_after_main orderAftMain = new Order_after_main();
        if (!Lang.isEmpty(orderAftMains)) {
            orderAftMain = orderAftMains.get(0);
        }
        req.setAttribute("afterSale", orderAftMain);
        Order_after_refundment orderAfterRefundment = orderAfterRefundmentService.fetch(Cnd.where("afterSaleId", "=", orderAftMain.getId()).and("orderId", "=", orderAftMain.getOrderId()));
        req.setAttribute("afterSaleRefundment", orderAfterRefundment);

        //购物车
        List<Member_cart> carts = memberCartService.query(Cnd.where("accountId", "=", StringUtil.getMemberUid()).limit(1, 2).desc("opAt"), "^(goodsMain|goodsProduct)$");
        req.setAttribute("carts", carts);

        //商品收藏
        List<Goods_favorite> goodsFavorites = goodsFavoriteService.query(Cnd.where("accountId", "=", StringUtil.getMemberUid()).limit(1, 8).desc("opAt"));
        if (!Lang.isEmpty(goodsFavorites)) {
            goodsFavorites = goodsFavorites.stream().map(gf -> {
                gf.setImg(goodsProductService.getProductImage(gf.getSku(), GoodsSaleClientEnum.PC));
                return gf;
            }).collect(Collectors.toList());
        }
        req.setAttribute("goodsFavorites", goodsFavorites);

        //店铺收藏
        List<Store_favorite> storeFavorites = storeFavoriteService.query(Cnd.where("accountId", "=", StringUtil.getMemberUid()).limit(1, 8).desc("opAt"));
        if (!Lang.isEmpty(storeFavorites)) {
            storeFavorites = storeFavorites.stream().map(sf -> {
                sf.setLog(storeMainService.fetch(sf.getStoreId()).getLogo());
                return sf;
            }).collect(Collectors.toList());
        }
        req.setAttribute("storeFavorites", storeFavorites);

        return "pages/front/pc/member/index";
    }

    /**
     * 待付款订单
     *
     * @return
     */
    private NutMap getWaitPayOrder() {
        Cnd cnd = Cnd.where("accountId", "=", StringUtil.getMemberUid()).and("payStatus", "=", 0).and("orderStatus", "=", 1);
        List<Order_main> waitPayOrderMains = orderMainService.query(cnd.limit(1, 1).desc("opAt"));
        Order_main waitPayOrderMain = null;
        NutMap waitPayMap = NutMap.NEW();
        int waitPayNum = orderMainService.count(cnd);
        if (!Lang.isEmpty(waitPayOrderMains)) {
            waitPayOrderMain = waitPayOrderMains.get(0);
            List<Order_goods> orderGoods = orderGoodsService.query(Cnd.where("orderId", "=", waitPayOrderMain.getId()), "^(storeMain)$");
            if (!Lang.isEmpty(orderGoods)) {
                Order_goods orderGood = orderGoods.get(0);
                orderGood.setImgUrl(goodsProductService.getProductImage(orderGood.getSku()));
                Goods_product product = goodsProductService.fetch(orderGood.getProductId());
                waitPayMap.put("orderProduct", orderGood);
                waitPayMap.put("product", product);
            }
        }
        NutMap waitPay = NutMap.NEW();
        waitPay.put("num", waitPayNum);
        waitPay.put("order", waitPayOrderMain);
        waitPay.put("product", waitPayMap);
        return waitPay;
    }

    /**
     * 待收货订单
     *
     * @return
     */
    private NutMap getWaitTakeOrder() {
        Cnd cnd = Cnd.where("accountId", "=", StringUtil.getMemberUid()).and("deliveryStatus", "=", 3).and("getStatus", "=", 0).and("orderStatus", "=", 1);
        List<Order_main> waitTakeOrderMains = orderMainService.query(cnd.limit(1, 1).desc("opAt"));
        Order_main waitTakeOrderMain = null;
        NutMap waitTakeMap = NutMap.NEW();
        int waitTakeNum = orderMainService.count(cnd);
        if (!Lang.isEmpty(waitTakeOrderMains)) {
            waitTakeOrderMain = waitTakeOrderMains.get(0);
            List<Order_goods> orderGoods = orderGoodsService.query(Cnd.where("orderId", "=", waitTakeOrderMain.getId()));
            if (!Lang.isEmpty(orderGoods)) {
                Order_goods orderGood = orderGoods.get(0);
                orderGood.setImgUrl(goodsProductService.getProductImage(orderGood.getSku()));
                Goods_product product = goodsProductService.fetch(orderGood.getProductId());
                waitTakeMap.put("orderProduct", orderGood);
                waitTakeMap.put("product", product);
            }
        }
        NutMap waitTake = NutMap.NEW();
        waitTake.put("num", waitTakeNum);
        waitTake.put("order", waitTakeOrderMain);
        waitTake.put("product", waitTakeMap);
        return waitTake;
    }

    /**
     * 待评价订单
     *
     * @return
     */
    private NutMap getWaitCommentOrder() {
        Cnd cnd = Cnd.where("accountId", "=", StringUtil.getMemberUid()).and("getStatus", "=", 1).and("feedStatus", "=", 0).and("orderStatus", "=", 1);
        List<Order_main> waitCommentOrderMains = orderMainService.query(cnd.limit(1, 1).desc("opAt"));
        Order_main waitCommentOrderMain = null;
        NutMap waitCommentMap = NutMap.NEW();
        int waitCommentNum = orderMainService.count(cnd);
        if (!Lang.isEmpty(waitCommentOrderMains)) {
            waitCommentOrderMain = waitCommentOrderMains.get(0);
            List<Order_goods> orderGoods = orderGoodsService.query(Cnd.where("orderId", "=", waitCommentOrderMain.getId()));
            if (!Lang.isEmpty(orderGoods)) {
                Order_goods orderGood = orderGoods.get(0);
                orderGood.setImgUrl(goodsProductService.getProductImage(orderGood.getSku()));
                Goods_product product = goodsProductService.fetch(orderGood.getProductId());
                waitCommentMap.put("orderProduct", orderGood);
                waitCommentMap.put("product", product);
            }
        }
        NutMap waitComment = NutMap.NEW();
        waitComment.put("num", waitCommentNum);
        waitComment.put("order", waitCommentOrderMain);
        waitComment.put("product", waitCommentMap);
        return waitComment;
    }


}
