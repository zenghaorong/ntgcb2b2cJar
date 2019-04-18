package com.aebiz.app.web.modules.controllers.front.pc.order;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.models.em.GoodsSaleClientEnum;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.order.modules.models.Order_goods;
import com.aebiz.app.order.modules.models.Order_goods_feedback;
import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.services.OrderGoodsFeedbackService;
import com.aebiz.app.order.modules.services.OrderGoodsService;
import com.aebiz.app.order.modules.services.OrderMainService;
import com.aebiz.app.store.modules.models.Store_feedback;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.app.store.modules.services.StoreFeedbackService;
import com.aebiz.app.store.modules.services.StoreMainService;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ThinkPad on 2017/7/24.
 */
@Controller
@RequestMapping("/member/personalOrder")
public class OrderFeedbackController {
    private static final Log log = Logs.get();

    @Autowired
    private OrderMainService orderMainService;
    @Autowired
    private StoreMainService storeMainService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private GoodsProductService goodsProductService;
    @Autowired
    private StoreFeedbackService storeFeedbackService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private OrderGoodsFeedbackService orderGoodsFeedbackService;
    /**
     * 订单商品评价页面
     * @param req
     * @return
     */
    @RequestMapping("/getOrderDetail")
    public String getOrderDetail(HttpServletRequest req){
        String orderId=req.getParameter("orderId");
        //判断商品和店铺是否评价  0代表直接评价页面。1 代表只评论的店铺  2代表评论的商品（这一块主要前台显示）
        String storeHasfeed=req.getParameter("storeHasfeed");
        if(storeHasfeed ==null || storeHasfeed==""){
            storeHasfeed="0";
        }
        req.setAttribute("storeHasfeed",storeHasfeed);
        if(Strings.isEmpty(orderId)){
            return "/404";
        }
        Order_main orderMain=orderMainService.fetch(orderId);
        if(orderMain !=null){
            //订单主表
            req.setAttribute("orderMain",orderMain);
            String storeId=orderMain.getStoreId();
            //店铺评价分数
            Store_feedback storeFeedback=storeFeedbackService.fetch(Cnd.where("storeId","=",storeId));
            if(storeFeedback !=null){
                req.setAttribute("storeFeedback",storeFeedback);
            }else{
                Store_feedback feedback1=new Store_feedback();
                feedback1.setDescriptionScore(5.0);
                feedback1.setServiceScore(5.0);
                feedback1.setSpeedScore(5.0);
                req.setAttribute("storeFeedback",feedback1);
            }
            //店铺评价状态  0 未评价  1已评价
            int status=orderMain.getFeedStatus();
            req.setAttribute("storefeedStatus",status);
            //商户主要信息
            Store_main storeMain=storeMainService.fetch(storeId);
            if(storeMain !=null){
                req.setAttribute("storeMain",storeMain);
            }
            //获取订单的商品信息列表
            List<Order_goods> orderGoods=orderGoodsService.query(Cnd.where("orderId","=",orderId).and("feedScore","=","0"));
            List<Order_goods> orderGoods1=new ArrayList<Order_goods>();
            List<Map> listMaps=new ArrayList<Map>();
            if(orderGoods !=null && orderGoods.size()>0){
                for(Order_goods orderGood:orderGoods){
                    String image=goodsProductService.getProductImage(orderGood.getSku(), GoodsSaleClientEnum.PC);
                    orderGood.setImgUrl(image);
                    orderGoods1.add(orderGood);
                    /*//返回格式是map集合。map对应存的货品主表和订单商品表
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("orderGood",orderGood);
                    String productId=orderGood.getProductId();
                    Goods_product product=goodsProductService.fetch(productId);
                    map.put("product",product);
                    listMaps.add(map);*/
                }
            }
            req.setAttribute("ListProduct",orderGoods1);
            //订单会员
            String accountId=orderMain.getAccountId();
            Account_info accountInfo=accountInfoService.fetch(accountId);
            req.setAttribute("accountInfo",accountInfo);
        }else{
            return "/404";
        }
        return "pages/front/pc/order/newEvaluate";
    }

    /**
     * 评价
     */
    @RequestMapping("addFeedBack")
    public String addFeedBack(HttpServletRequest request){
        //获取评论人的id
        String accoundId= StringUtil.getMemberUid();
        String storeHasfeed="0";
        if(!Strings.isEmpty(accoundId)){
            //评论了多少个商品  0表示无商品评价。评论的是店铺。>=1表示评论了商品。也可能包含店铺
            int productFeedNum=Integer.parseInt(request.getParameter("productFeedNum"));
            //判断是否包含店铺评价 0表示没有 1表示有
            String isHasStore=request.getParameter("isHasStore");
            //商家id
            String storeId=request.getParameter("storeId");
            //商家评论描述打分
            String storeDescScore=request.getParameter("storeDescScore");
            //商家评论服务打分
            String storeSerScore=request.getParameter("storeSerScore");
            //商家评论物流打分
            String storeSpeScore=request.getParameter("storeSpeScore");
            //获取订单id
            String orderId=request.getParameter("orderId");
            //是否匿名评价
            String isOpen=request.getParameter("isOpen");
            //false表示不匿名 true代表匿名
            boolean feedOpen=false;
            if(isOpen=="0"){
                feedOpen=false;
            }else{
                feedOpen=true;
            }
            Order_main orderMain=orderMainService.fetch(orderId);
            //评价时间
            int feedAt=(int) (System.currentTimeMillis() / 1000);
            if("1".equals(isHasStore)){
                //说明有店铺的评价
                if(!Strings.isEmpty(storeDescScore) && !Strings.isEmpty(storeSerScore) && !Strings.isEmpty(storeSpeScore)){
                    //对店铺评价分数
                    Store_feedback storeFeedback=storeFeedbackService.fetch(Cnd.where("storeId","=",storeId));
                    if(storeFeedback !=null){
                        double descriptionScore1=storeFeedback.getDescriptionScore();
                        double serviceScore1=storeFeedback.getServiceScore();
                        double speedScore1=storeFeedback.getSpeedScore();
                        int num=storeFeedback.getFeedAccountNum();
                        double allDec=((descriptionScore1*num)+Double.parseDouble(storeDescScore))/num+1;
                        double allSer=((serviceScore1*num)+Double.parseDouble(storeSerScore))/num+1;
                        double allSpe=((speedScore1*num)+Double.parseDouble(storeSpeScore))/num+1;
                        storeFeedback.setSpeedScore(allSpe);
                        storeFeedback.setServiceScore(allSer);
                        storeFeedback.setDescriptionScore(allDec);
                        storeFeedback.setFeedAccountNum(num+1);
                        storeFeedbackService.update(storeFeedback);
                        orderMain.setFeedAt(feedAt);
                        orderMain.setFeedStatus(1);
                        orderMainService.update(orderMain);
                    }else{
                        Store_feedback storeFeedback1=new Store_feedback();
                        storeFeedback1.setFeedAccountNum(1);
                        storeFeedback1.setSpeedScore(Double.parseDouble(storeSpeScore));
                        storeFeedback1.setDescriptionScore(Double.parseDouble(storeDescScore));
                        storeFeedback1.setServiceScore(Double.parseDouble(storeSerScore));
                        storeFeedback1.setStoreId(storeId);
                        storeFeedbackService.insert(storeFeedback1);
                        orderMain.setFeedAt(feedAt);
                        orderMain.setFeedStatus(1);
                        orderMainService.update(orderMain);
                    }
                    storeHasfeed="1";
                }
                for(int i=0;i<productFeedNum;i++){
                    int x=i+1;
                    //订单商品id
                    String orderProductId=request.getParameter("orderProductId"+x);
                    Order_goods orderGood=orderGoodsService.fetch(orderProductId);
                    Order_goods_feedback goodsFeedback=new Order_goods_feedback();
                    //商品评价内容
                    String feedNote=request.getParameter("productContent"+x);
                    //以,分割,不要带域名的相对路径
                    String feedImage=request.getParameter("productImages"+x);
                    //商品评分
                    String appScore=request.getParameter("productScore"+x);
                    //商品评分是必填  如果没有评分，说明该商品未评价
                    if(!Strings.isEmpty(appScore)){
                        //商品id
                        String productId=orderGood.getProductId();
                        //goodsId
                        Goods_product product=goodsProductService.fetch(productId);
                        String goodId=product.getGoodsId();
                        //商品sku
                        String sku=orderGood.getSku();
                        //商品spec
                        String spec=orderGood.getSpec();
                        goodsFeedback.setAccountId(accoundId);
                        goodsFeedback.setAppScore(Integer.parseInt(appScore));
                        goodsFeedback.setFeedImage(feedImage);
                        goodsFeedback.setFeedAt(feedAt);
                        goodsFeedback.setFeedOpen(feedOpen);
                        goodsFeedback.setFeedNote(feedNote);
                        goodsFeedback.setOrderId(orderId);
                        goodsFeedback.setProductId(productId);
                        goodsFeedback.setSku(sku);
                        goodsFeedback.setSpec(spec);
                        goodsFeedback.setGoodsId(goodId);
                        goodsFeedback.setStoreId(storeId);
                        orderGoodsFeedbackService.insert(goodsFeedback);
                        Order_goods good= orderGoodsService.fetch(orderProductId);
                        good.setFeedAt(feedAt);
                        good.setFeedScore(Integer.parseInt(appScore));
                        orderGoodsService.update(good);
                        storeHasfeed="2";
                    }

                }
            }else if("0".equals(isHasStore)){
                //说明没有店铺的评价
                for(int i=0;i<productFeedNum;i++){
                    int x=i+1;
                    //订单商品id
                    String orderProductId=request.getParameter("orderProductId"+x);
                    Order_goods orderGood=orderGoodsService.fetch(orderProductId);
                    Order_goods_feedback goodsFeedback=new Order_goods_feedback();
                    //商品评价内容
                    String feedNote=request.getParameter("productContent"+x);
                    //以,分割,不要带域名的相对路径
                    String feedImage=request.getParameter("productImages"+x);
                    //商品评分
                    String appScore=request.getParameter("productScore"+x);
                    //商品评分是必填  如果没有评分，说明该商品未评价
                    if(!Strings.isEmpty(appScore)){
                        //商品id
                        String productId=orderGood.getProductId();
                        //goodsId
                        Goods_product product=goodsProductService.fetch(productId);
                        String goodId=product.getGoodsId();
                        //商品sku
                        String sku=orderGood.getSku();
                        //商品spec
                        String spec=orderGood.getSpec();
                        goodsFeedback.setAccountId(accoundId);
                        goodsFeedback.setAppScore(Integer.parseInt(appScore));
                        goodsFeedback.setFeedImage(feedImage);
                        goodsFeedback.setFeedAt(feedAt);
                        goodsFeedback.setFeedOpen(feedOpen);
                        goodsFeedback.setFeedNote(feedNote);
                        goodsFeedback.setOrderId(orderId);
                        goodsFeedback.setProductId(productId);
                        goodsFeedback.setSku(sku);
                        goodsFeedback.setSpec(spec);
                        goodsFeedback.setGoodsId(goodId);
                        goodsFeedback.setStoreId(storeId);
                        orderGoodsFeedbackService.insert(goodsFeedback);
                        Order_goods good= orderGoodsService.fetch(orderProductId);
                        good.setFeedAt(feedAt);
                        good.setFeedScore(Integer.parseInt(appScore));
                        orderGoodsService.update(good);
                        storeHasfeed="2";
                    }

                }
            }
            return "redirect:/member/personalOrder/getOrderDetail?orderId="+orderId+"&storeHasfeed="+storeHasfeed;
        }else{
            return "/member/login";
        }
    }
}
