package com.aebiz.app.web.modules.controllers.open.H5;

import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.cms.modules.models.Cms_video;
import com.aebiz.app.cms.modules.services.CmsVideoService;
import com.aebiz.app.goods.modules.models.Goods_image;
import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.services.GoodsImageService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.goods.modules.services.GoodsService;
import com.aebiz.app.member.modules.models.Member_address;
import com.aebiz.app.member.modules.models.Member_cart;
import com.aebiz.app.member.modules.services.MemberAddressService;
import com.aebiz.app.member.modules.services.MemberCartService;
import com.aebiz.app.order.modules.models.Order_goods;
import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.models.em.OrderTypeEnum;
import com.aebiz.app.order.modules.services.OrderGoodsService;
import com.aebiz.app.order.modules.services.OrderMainService;
import com.aebiz.app.sys.modules.services.SysDictService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Cnd;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author chengzhuming
 * @date 2019/4/1 19:52
 */
@Controller
@RequestMapping("/open/h5/order")
public class OrderController {
    private static final Log log = Logs.get();
    @Autowired
    private MemberAddressService memberAddressService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsImageService goodsImageService;

    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private OrderMainService orderMainService;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private CmsVideoService cmsVideoService;

    @Autowired
    private MemberCartService memberCartService;


    /**
     * 进入订单确认页
     */
    @RequestMapping("/orderConfirmation.html")
    public String orderConfirmation(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        try {
            Account_user accountUser = (Account_user) subject.getPrincipal();
            if (accountUser == null) {
                return "pages/front/h5/niantu/login";
            }
        }catch (Exception e){
            return "pages/front/h5/niantu/login";
        }
        String productList = request.getParameter("productList");
//        List<Map<String,Object>> list = (List<Map<String, Object>>) JSON.parse(productList);
//        String productIds = request.getParameter("productIds");
//        String num = request.getParameter("num");
//        String videoId = request.getParameter("videoId");
//
//        request.setAttribute("productIds",productIds);
//        request.setAttribute("num",num);
        request.setAttribute("productList",productList);
        return "pages/front/h5/niantu/orderConfirmation";
    }

    /**
     * 获取订单信息
     */
    @RequestMapping("/getOrderInfo.html")
    @SJson
    public Result getOrderInfo(HttpServletRequest request) {
        String id = request.getParameter("id");
        request.setAttribute("orderId",id);
        try {
        Subject subject = SecurityUtils.getSubject();
        Account_user accountUser = (Account_user) subject.getPrincipal();
        if(accountUser==null){
            return Result.error(-1,"请先登录");
        }
        //查询收获地址
        Cnd cnd = Cnd.NEW();
        cnd.and("accountId", "=", accountUser.getId() );
        cnd.and("defaultValue","=",true);
        List<Member_address> list = memberAddressService.query(cnd);


        //查询商品信息
        Cnd pCnd = Cnd.NEW();

        Order_main order = orderMainService.fetch(id);
        if(list!=null&&list.size()>0){
            order.setAddresses(list.get(0));
        }
        Cnd ogCnd = Cnd.NEW();
        ogCnd.and("orderId","=",order.getId());
            List<Order_goods> ogList = orderGoodsService.query(ogCnd);
            for (Order_goods og :
                    ogList) {
                Cnd imgCnd = Cnd.NEW();
                imgCnd.and("goodsId","=",og.getGoodsId());
                List<Goods_image> imgList = goodsImageService.query(imgCnd);
                og.setImgUrl(imgList.get(0).getImgAlbum());
            }
            order.setGoodsList(ogList);
        return Result.success("ok",order);
        }catch (Exception e){
            log.error("获取订单信息失败",e);
            return Result.error("fail");
        }
    }

    /**
     * 进入订单列表页
     */
    @RequestMapping("/goOrderList.html")
    public String goOrderList() {
        return "pages/front/h5/niantu/orderList";
    }

    /**
     * 进入订单详情页
     */
    @RequestMapping("/goOrderInfo.html")
    public String goOrderInfo() {
        return "pages/front/h5/niantu/orderInfo";
    }


    /**
     * 进入收银台
     */
    @RequestMapping("/checkoutCounter.html")
    public String checkoutCounter(HttpServletRequest request,String productList,String addressId) {

        List<Map<String,Object>> list = (List<Map<String, Object>>) JSON.parse(productList);

        Subject subject = SecurityUtils.getSubject();
        Account_user accountUser = (Account_user) subject.getPrincipal();
        if(accountUser==null){
            return "pages/front/h5/niantu/login";
        }

        //查询收货信息
        Member_address member_address = memberAddressService.fetch(addressId);

        for(Map<String,Object> map:list) {
            String id = (String) map.get("productId");
            String num = (String) map.get("num");
            Goods_main good = goodsService.fetch(id);
            Order_main order_main = new Order_main();
            Order_goods order_goods = new Order_goods();

            order_main.setAccountId(accountUser.getId());
            order_main.setStoreId(good.getStoreId());
            Cnd proCnd = Cnd.NEW();
            proCnd.and("goodsId", "=", good.getId());
            List<Goods_product> gpList = goodsProductService.query(proCnd);
            if (gpList != null && gpList.size() > 0) {
                Goods_product goods_product = gpList.get(0);
                Integer salePrice = goods_product.getSalePrice();
                int n = Integer.parseInt(num);
                int totalMoney = salePrice * n;
                order_main.setGoodsMoney(totalMoney);
                order_main.setGoodsFreeMoney(0);
                String freight = sysDictService.getNameByCode("freight");
                int freightMoney = Integer.parseInt(freight) * 100;
                int freeMoney = 0;
                order_main.setPayMoney(totalMoney + freightMoney);
                order_main.setFreightMoney(freightMoney);
                order_main.setFreeMoney(0);
                order_main.setPayStatus(0);
                order_main.setOrderStatus(0);
                order_main.setOrderAt(DateUtil.getTime(new Date()));
                order_main.setDeliveryAddress(member_address.getAddress());
                order_main.setDeliveryMobile(member_address.getMobile());
                order_main.setOrderType(OrderTypeEnum.product_order_type.getKey());
                Order_main order = orderMainService.insert(order_main);
                order_goods.setOrderId(order.getId());
                order_goods.setAccountId(order.getAccountId());
                order_goods.setGoodsId(good.getId());
                order_goods.setStoreId(order_main.getStoreId());
                order_goods.setGoodsName(good.getName());
                order_goods.setProductId(goods_product.getId());
                order_goods.setSku(goods_product.getSku());
                order_goods.setName(goods_product.getName());
                order_goods.setBuyNum(n);
                order_goods.setSalePrice(goods_product.getSalePrice());
                order_goods.setBuyPrice(goods_product.getSalePrice() - freeMoney);
                order_goods.setTotalMoney(goods_product.getSalePrice() * n);
                order_goods.setFreeMoney(freeMoney);
                order_goods.setPayMoney(goods_product.getSalePrice() * n - freeMoney);
                orderGoodsService.insert(order_goods);
                request.setAttribute("orderId", order.getId());
            }
        }
        return "pages/front/h5/niantu/checkoutCounter";
    }

    /**
     * 视频下单进入订单确认页
     */
    @RequestMapping("/videoOrderConfirmation.html")
    public String videoOrderConfirmation(HttpServletRequest request,String videoId) {
        Subject subject = SecurityUtils.getSubject();
        Account_user accountUser = (Account_user) subject.getPrincipal();
        if (accountUser == null) {
            return "pages/front/h5/niantu/login";
        }
        request.setAttribute("videoId",videoId);
        return "pages/front/h5/niantu/videoOrderConfirmation";
    }

    /**
     * 视频下单进入收银台
     */
    @RequestMapping("/videoCheckoutCounter.html")
    public String videoCheckoutCounter(HttpServletRequest request,String videoId) {
        Subject subject = SecurityUtils.getSubject();
        Account_user accountUser = (Account_user) subject.getPrincipal();
        if(accountUser==null){
            return "pages/front/h5/niantu/login";
        }
        Cms_video cms_video=cmsVideoService.fetch(videoId);
        Order_main order_main = new Order_main();
        order_main.setAccountId(accountUser.getId());
        order_main.setStoreId(cms_video.getStoreId());
        order_main.setGoodsMoney(cms_video.getPrice().intValue());
        order_main.setGoodsFreeMoney(0);
        order_main.setPayMoney(cms_video.getPrice().intValue());
        order_main.setFreightMoney(0);
        order_main.setFreeMoney(0);
        order_main.setPayStatus(0);
        order_main.setOrderStatus(0);
        order_main.setOrderAt(DateUtil.getTime(new Date()));
        order_main.setOrderType(OrderTypeEnum.video_order_type.getKey());
        Order_main order = orderMainService.insert(order_main);
        request.setAttribute("order",order);
        return "pages/front/h5/niantu/checkoutCounter";
    }

    @RequestMapping("/submitOrder.html")
    public String submitOrder(HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        try {
            Account_user accountUser = (Account_user) subject.getPrincipal();
            if (accountUser == null) {
                return "pages/front/h5/niantu/login";
            }
        }catch (Exception e){
            return "pages/front/h5/niantu/login";
        }
        List<Map<String,Object>> pList = new ArrayList<>();
        String cartIds = request.getParameter("cartIds");
        try{
            String[] ids = StringUtils.split(cartIds, ";");
            for (int i = 0 ; i<ids.length;i++){
                Map<String,Object> p = new HashMap<>();
                Member_cart cart = memberCartService.fetch(ids[i]);
                p.put("productId",cart.getGoodsId());
                p.put("num",cart.getNum().toString());
                pList.add(p);
                cart.setDelFlag(true);
                memberCartService.update(cart);
            }
        }catch (Exception e){

        }
        request.setAttribute("productList",JSON.toJSONString(pList));
        return "pages/front/h5/niantu/orderConfirmation";
    }
}
