package com.aebiz.app.web.modules.controllers.front.pc.goods;

import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.goods.commons.utils.GoodsUtil;
import com.aebiz.app.goods.commons.vo.GoodsProductVO;
import com.aebiz.app.goods.modules.models.Goods_image;
import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.models.em.GoodsSaleClientEnum;
import com.aebiz.app.goods.modules.services.GoodsFavoriteService;
import com.aebiz.app.goods.modules.services.GoodsPriceService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.member.modules.models.Member_address;
import com.aebiz.app.member.modules.models.Member_cart;
import com.aebiz.app.member.modules.models.Member_user;
import com.aebiz.app.member.modules.services.MemberAddressService;
import com.aebiz.app.member.modules.services.MemberCartService;
import com.aebiz.app.member.modules.services.MemberCouponService;
import com.aebiz.app.member.modules.services.MemberUserService;
import com.aebiz.app.order.commons.constant.OrderConsts;
import com.aebiz.app.order.commons.utils.OrderUtil;
import com.aebiz.app.order.commons.vo.CartCountVO;
import com.aebiz.app.order.commons.vo.OrderGoodsVO;
import com.aebiz.app.order.commons.vo.OrderStoreVO;
import com.aebiz.app.order.modules.models.Order_goods;
import com.aebiz.app.order.modules.models.Order_goods_feedback;
import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.services.OrderGoodsFeedbackService;
import com.aebiz.app.order.modules.services.OrderGoodsService;
import com.aebiz.app.order.modules.services.OrderMainService;
import com.aebiz.app.sales.modules.commons.SalesRuleProduct;
import com.aebiz.app.sales.modules.models.Sales_coupon;
import com.aebiz.app.sales.modules.models.Sales_rule_goods;
import com.aebiz.app.sales.modules.models.Sales_rule_order;
import com.aebiz.app.sales.modules.services.SalesCouponService;
import com.aebiz.app.sales.modules.services.SalesRuleGoodsService;
import com.aebiz.app.sales.modules.services.SalesRuleOrderService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.store.modules.commons.vo.StoreFreightProduct;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.app.store.modules.services.StoreFavoriteService;
import com.aebiz.app.store.modules.services.StoreFreightService;
import com.aebiz.app.store.modules.services.StoreMainService;
import com.aebiz.app.web.commons.es.EsService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.Pagination;
import com.aebiz.baseframework.rabbit.RabbitProducer;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.CookieUtil;
import com.aebiz.commons.utils.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.nutz.dao.Cnd;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/goods")
public class PcGoodsController {

    private static final Log log = Logs.get();

    //为了防止与其他的网站的Cookie名字冲突,所以加上工程名
    private static final String CART_COOKIE_NAME = "aebizCart";//项目的购物车cookie名称

    private static final String CART_SESSION_NAME = "aebizCurrentCart";

    @Autowired
    private EsService esService;

    @Autowired
    private PropertiesProxy config;

    @Autowired
    private OrderGoodsFeedbackService orderGoodsFeedbackService;

    @Autowired
    private StoreMainService storeMainService;

    @Autowired
    private StoreFavoriteService storeFavoriteService;

    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private MemberCartService memberCartService;

    @Autowired
    private MemberAddressService memberAddressService;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private OrderMainService orderMainService;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private GoodsFavoriteService goodsFavoriteService;

    @Autowired
    private GoodsPriceService goodsPriceService;

    @Autowired
    private StoreFreightService storeFreightService;

    @Autowired
    private ShopAreaService shopAreaService;

    @Autowired
    private SalesCouponService salesCouponService;

    @Autowired
    private SalesRuleGoodsService salesRuleGoodsService;

    @Autowired
    private MemberCouponService memberCouponService;

    @Autowired
    private SalesRuleOrderService salesRuleOrderService;

    @Autowired
    private MemberUserService memberUserService;

    @Autowired
    private RabbitProducer rabbitProducer;

    /**
     * 商品详情页
     * @param sku
     * @param req
     * @return
     */
    @RequestMapping(value = "/{sku}",method = RequestMethod.GET )
    public String index(@PathVariable("sku") String sku, HttpServletRequest req){
        QueryBuilder query = QueryBuilders.matchQuery("productList.sku",sku);
        SearchRequestBuilder srb = esService.getClient().prepareSearch(config.get("es.index.name"))
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setTypes("goods")
                .setQuery(query);
        SearchResponse response = srb.execute().actionGet();
        SearchHits hits = response.getHits();
        if(hits.getTotalHits() == 1){
            Map<String, Object> source = hits.getHits()[0].getSource();
            Goods_main goodsMain = Lang.map2Object(source,Goods_main.class);
            req.setAttribute("goodsId",goodsMain.getId());
            //获取默认货品的信息
            List<Goods_product> productList = goodsMain.getProductList();
            if(productList != null){
                List<Goods_product> enabledProductList = new ArrayList<>();
                for(Goods_product goodsProduct :productList){
                    if(goodsProduct.isEnabled()){
                        enabledProductList.add(goodsProduct);
                    }
                    if(sku.equals(goodsProduct.getSku())){
                        //选择的sku是被禁用的货品,跳转404
                        if(!goodsProduct.isEnabled()){
                            return "pages/front/pc/goods/noexist";
                        }
                        req.setAttribute("defaultProduct",goodsProduct);
                        int count =  goodsFavoriteService.count(Cnd.where("delFlag","=",false).and("sku","=",goodsProduct.getSku()));
                        boolean isCollected = false;
                        if(count > 0){
                            isCollected = true;
                        }
                        req.setAttribute("buzz",count);
                        req.setAttribute("isCollected",isCollected);
                    }
                }
                goodsMain.setGoodsProducts(enabledProductList);
            }
            //获取PC端图片
            List<Goods_image> goodsImageList = goodsMain.getImageList();
            //过滤出PC端的图片
            GoodsUtil.getClientGoodsImage(goodsImageList,GoodsSaleClientEnum.PC.getKey());
            req.setAttribute("obj",goodsMain);
            int feedBackNum =  orderGoodsFeedbackService.count(Cnd.where("delFlag","=",false).and("sku","=",sku));
            req.setAttribute("feedBackNum",feedBackNum);
            String accountId = StringUtil.getMemberUid();
            List<Member_cart> memberCartList  = null;
            int cartNum = 0;
            int totalMoney = 0;
            if(Strings.isNotBlank(accountId)){
                //查询购物车的信息
                cartNum = memberCartService.count(Cnd.where("delFlag","=",false).and("accountId","=",accountId));
                if(cartNum > 0 ){
                    Pagination page = memberCartService.listPage(1,3,Cnd.where("delFlag","=",false).and("accountId","=",accountId));
                    memberCartList = (List<Member_cart>) page.getList();
                }
            }else{
                //查询Cookie的购物车事件
                //会员未登陆,购物车取cookie数据
                String cookieVal =  CookieUtil.getCookie(req,CART_COOKIE_NAME);
                if(Strings.isNotBlank(cookieVal)){
                    memberCartList =Json.fromJsonAsList(Member_cart.class,cookieVal);
                    cartNum = memberCartList.size();
                }
            }
            req.setAttribute("cartNum",cartNum);
            if(memberCartList != null){
                for(Member_cart memberCart : memberCartList){
                    totalMoney += (memberCart.getPrice()*memberCart.getNum());
                    memberCartService.fetchLinks(memberCart,"^(goodsMain|goodsProduct)$");
                }
            }
            req.setAttribute("cartList",memberCartList);
            req.setAttribute("totalMoney",totalMoney);
            //全部的评价数量
            int countAll = orderGoodsFeedbackService.count(Cnd.where("goodsId","=",goodsMain.getId()).and("delFlag","=",false));
            req.setAttribute("feedbackNum",countAll);

        }else{
            return "pages/front/pc/goods/noexist";
        }
        return  "pages/front/pc/goods/detail";
    }

    /**
     * 加载评论信息
     * @param goodsId
     * @param isHas
     * @param req
     * @return
     */
    @RequestMapping(value="/feedbackInfo",method = RequestMethod.POST)
    public String toFeedback(@RequestParam("goodsId")String goodsId,@RequestParam("isHas")Integer isHas,HttpServletRequest req){
        //商品的评价
        List<Order_goods_feedback> orderGoodsFeedbacks = new ArrayList<>();
        if(isHas > 0){
            //表示全部
            orderGoodsFeedbacks = orderGoodsFeedbackService.query(Cnd.where("goodsId","=",goodsId).and("delFlag","=",false));
        }else{
            //表示查询有图的
            orderGoodsFeedbacks = orderGoodsFeedbackService.query(Cnd.where("goodsId","=",goodsId).and("delFlag","=",false).andNot("feedImage","is",null));
        }
        if(orderGoodsFeedbacks != null){
            orderGoodsFeedbackService.fetchLinks(orderGoodsFeedbacks,"accountInfo");
            for(Order_goods_feedback orderGoodsFeedback:orderGoodsFeedbacks){
               /*Account_user accountUser =  accountUserService.fetch(Cnd.where("accountInfo","=",orderGoodsFeedback.getAccountId()));
                orderGoodsFeedback.setAccountUser(accountUser);*/
            }
        }
        req.setAttribute("orderGoodsFeedbacks",orderGoodsFeedbacks);
        //全部的评价数量
        int countAll = orderGoodsFeedbackService.count(Cnd.where("goodsId","=",goodsId).and("delFlag","=",false));
        req.setAttribute("countAll",countAll);
        //查询有图的评价数量
        int countHasImage = orderGoodsFeedbackService.count(Cnd.where("goodsId","=",goodsId).and("delFlag","=",false).andNot("feedImage","is",null));
        req.setAttribute("countHasImage",countHasImage);
        return "pages/front/pc/goods/feedback";
    }

    /**
     * 获取货品的评论数量
     * @param sku
     * @return
     */
    @RequestMapping(value="/feedbackNumBySku",method = RequestMethod.POST)
    @SJson
    public Object getFeedBackNumBySku(@RequestParam("sku") String sku){
        return orderGoodsFeedbackService.getFeedbackNumBySku(sku);
    }

    /**
     * 收藏店铺
     * @param storeId
     * @return
     */
    @RequestMapping(value = "/storeCollect",method = RequestMethod.POST)
    @SJson
    public Object storeCollect(@RequestParam("storeId") String storeId){
        String accountId = StringUtil.getMemberUid();
        if(Strings.isBlank(accountId)){
            return Result.error("当前会员未登陆,请先登陆");
        }
        if(Strings.isBlank(storeId)){
            return Result.error("店铺不存在");
        }
        Store_main storeMain = storeMainService.fetch(storeId);
        if(storeMain == null){
            return Result.error("店铺不存在");
        }
        try {
            int count =  storeFavoriteService.count(Cnd.where("delFlag","=",false).and("accountId","=",accountId).and("storeId","=",storeId));
            if(count <= 0){//若店铺没有收藏过,则收藏
                storeFavoriteService.saveData(accountId,storeMain);
            }
            return Result.success("globals.result.success");
        }catch (Exception e){
            log.debug(e.getMessage(),e);
            return Result.error("globals.result.error");
        }
    }

    /**
     * 货品收藏
     * @param sku
     * @return
     */
    @RequestMapping(value = "/productCollect",method = RequestMethod.POST)
    @SJson
    public Object productCollect(@RequestParam("sku") String sku){
        if(Strings.isBlank(sku)){
            return Result.error("货品sku为空");
        }
        String accountId = StringUtil.getMemberUid();
        if(Strings.isBlank(accountId)){
            return Result.error("当前会员未登陆,请先登陆");
        }
        try {
            int count = goodsFavoriteService.count(Cnd.where("sku","=",sku).and("delFlag","=",false).and("accountId","=",accountId));
            if(count > 0){
                return Result.success("globals.result.success");
            }
            Goods_product goodsProduct = goodsProductService.fetch(Cnd.where("sku","=",sku).and("delFlag","=",false));
            if(goodsProduct == null){
                return Result.error("货品不存在");
            }
            goodsProductService.fetchLinks(goodsProduct,"goodsMain");
            goodsFavoriteService.saveData(accountId,goodsProduct);
            return Result.success("globals.result.success");
        }catch (Exception e){
            log.debug(e.getMessage(),e);
            return Result.error("globals.result.error");
        }


    }

    /**
     * 加入购物车
     * @param sku
     * @param buyNum
     * @return
     */
    @RequestMapping(value = "/addCart",method = RequestMethod.POST)
    @SJson
    public Object addCart(@RequestParam("sku") String sku, @RequestParam("buyNum")Integer buyNum, HttpServletRequest req, HttpServletResponse res){
        try{
            List<Member_cart> memberCartList = new ArrayList<>();
            NutMap map=new NutMap();
            String accountId = StringUtil.getMemberUid();
            Goods_product goodsProduct = goodsProductService.fetch(Cnd.where("delFlag","=",false).and("sku","=",sku));
            if(goodsProduct == null){
                return Result.error("选择的货品不存在");
            }
            Integer price = goodsPriceService.getSalePrice(goodsProduct.getSku(),null,null,null, GoodsSaleClientEnum.PC.getKey(),0,null);
            String imageUrl = goodsProductService.getProductImage(goodsProduct.getSku());
            //是否重复添加相同货品的标识
            boolean existedProduct = true;
            //这里判断用户是否登陆
            if(Strings.isBlank(accountId)){
                String [] cartSku = new String[]{};
                String [] cartNum = new String[]{};
                if(Strings.isBlank(CookieUtil.getCookie(req, OrderConsts.CART_COOKIE_SKU))){
                    //memberCartList.add(member_cart);
                    cartSku = ArrayUtils.add(cartSku,sku);
                    cartNum = ArrayUtils.add(cartNum,String.valueOf(buyNum+"_"+sku));
                }else{
                    String cookieSku = CookieUtil.getCookie(req,OrderConsts.CART_COOKIE_SKU);
                    cartSku =Json.fromJsonAsArray(String.class,cookieSku);
                    String cookieNum = CookieUtil.getCookie(req,OrderConsts.CART_COOKIE_NUM);
                    cartNum = Json.fromJsonAsArray(String.class,cookieNum);
                    //取出cookie的集合,判断当前加入购物车的货品已经存在cookie集合？
                    //1.不存在,则添加 2.存在,更新数量
                    boolean flag = true;
                    for(String s: cartSku){
                        if(sku.equals(s)){
                            //更新数量
                            int cartIndex = ArrayUtils.indexOf(cartSku,s);
                            String numSku=cartNum[cartIndex];
                            String num=(numSku.split("_"))[0];
                            cartNum[cartIndex] =(buyNum+Integer.parseInt(num))+"_"+sku;
                            flag =false;
                            existedProduct = false;
                            break;
                        }
                    }
                    //加入集合中
                    if(flag){
                        cartSku = ArrayUtils.add(cartSku,sku);
                        cartNum = ArrayUtils.add(cartNum,String.valueOf(buyNum+"_"+sku));
                    }
                }
                //更新cookie或加入cookie
                CookieUtil.setCookie(res,OrderConsts.CART_COOKIE_SKU,Json.toJson(cartSku));
                CookieUtil.setCookie(res,OrderConsts.CART_COOKIE_NUM,Json.toJson(cartNum));



                //会员未登陆,购物车取cookie数据
                String [] cookieCartSku = null;
                //获取cartSku
                String cookieSkuStr =Json.toJson(cartSku);
                if(Strings.isNotBlank(cookieSkuStr)){
                    cookieCartSku = Json.fromJsonAsArray(String.class,cookieSkuStr);
                }
                String [] cookieCartNum = null;
                //获取cartNum
                String cookieNumStr = Json.toJson(cartNum);
                if(Strings.isNotBlank(cookieNumStr)){
                    cookieCartNum = Json.fromJsonAsArray(String.class,cookieNumStr);
                }
                if(cookieCartSku != null && cookieCartSku.length > 0){
                    for(String sku1:cookieCartSku){
                        Goods_product product = goodsProductService.getBySku(sku1);
                        if(product != null){
                            Member_cart member_cart = new Member_cart();
                            Integer price1 = goodsPriceService.getSalePrice(sku1,null,null,null, GoodsSaleClientEnum.PC.getKey(),0,null);
                            String imageUrl1 = goodsProductService.getProductImage(sku1);
                            member_cart.setSku(sku1);
                            int index = ArrayUtils.indexOf(cookieCartSku,sku1);
                            String numSku=cookieCartNum[index];
                            String num=(numSku.split("_"))[0];
                            member_cart.setNum(Integer.parseInt(num));
                            member_cart.setPrice(price1);
                            member_cart.setImgurl(imageUrl1);
                            member_cart.setStoreMain(product.getStoreMain());
                            member_cart.setGoodsMain(product.getGoodsMain());
                            member_cart.setGoodsProduct(product);
                            memberCartList.add(member_cart);
                        }
                    }
                }
            }else{
                //用户已经登陆则直接操作数据库
                int count = memberCartService.count(Cnd.where("delFlag","=",false).and("accountId","=",accountId).and("sku","=",sku));
                if(count ==  0){//添加货品到购物车
                    memberCartService.saveCart(accountId,goodsProduct,price,buyNum,imageUrl);
                }else{ //修改购物车货品的数量
                    Member_cart memberCart = memberCartService.fetch(Cnd.where("delFlag","=",false).and("accountId","=",accountId).and("sku","=",sku));
                    memberCart.setNum(memberCart.getNum()+buyNum);
                    memberCart.setOpAt((int)(System.currentTimeMillis()/1000));
                    memberCart.setOpBy(StringUtil.getUid());
                    memberCartService.update(memberCart);
                    existedProduct = false;
                }
                memberCartList = memberCartService.query(Cnd.where("delFlag","=",false).and("accountId","=",accountId),"^(storeMain|goodsMain|goodsProduct)$");
            }
            map.put("existedProduct",existedProduct);
            map.put("memberCartList",memberCartList);
            return Result.success("globals.result.success",map);
        }catch (Exception e){
            log.debug(e.getMessage(),e);
            return Result.error("globals.result.error");
        }
    }

    /**
     * 跳转至购物车页面
     * @param req
     * @return
     */
    @RequestMapping(value = "/cart",method = RequestMethod.GET)
    public String toCart(HttpServletRequest req){

        String accountId = StringUtil.getMemberUid();
        List<Member_cart> memberCartList = new ArrayList<>();
        Map<String,List<Member_cart>> map = new HashMap<>();
        Map<String,Object> cartMap = new HashMap<>();
        if(Strings.isBlank(accountId)){
            //会员未登陆,购物车取cookie数据
            String [] cookieCartSku = null;
            //获取cartSku
            String cookieSkuStr = CookieUtil.getCookie(req, OrderConsts.CART_COOKIE_SKU);
            if(Strings.isNotBlank(cookieSkuStr)){
                cookieCartSku = Json.fromJsonAsArray(String.class,cookieSkuStr);
            }
            String [] cookieCartNum = null;
            //获取cartNum
            String cookieNumStr = CookieUtil.getCookie(req,OrderConsts.CART_COOKIE_NUM);
            if(Strings.isNotBlank(cookieNumStr)){
                cookieCartNum = Json.fromJsonAsArray(String.class,cookieNumStr);
            }
            if(cookieCartSku != null && cookieCartSku.length > 0){
                for(String sku:cookieCartSku){
                    Goods_product product = goodsProductService.getBySku(sku);
                    if(product != null){
                        Member_cart member_cart = new Member_cart();
                        Integer price = goodsPriceService.getSalePrice(sku,null,null,null, GoodsSaleClientEnum.PC.getKey(),0,null);
                        String imageUrl = goodsProductService.getProductImage(sku);
                        member_cart.setSku(sku);
                        int index = ArrayUtils.indexOf(cookieCartSku,sku);
                        String skuNum=cookieCartNum[index];
                        member_cart.setNum(Integer.parseInt(skuNum.split("_")[0]));
                        member_cart.setPrice(price);
                        member_cart.setImgurl(imageUrl);
                        member_cart.setStoreMain(product.getStoreMain());
                        member_cart.setGoodsMain(product.getGoodsMain());
                        member_cart.setGoodsProduct(product);
                        memberCartList.add(member_cart);
                    }
                }
            }
        }else{
            memberCartList = memberCartService.query(Cnd.where("delFlag","=",false).and("accountId","=",accountId),"^(storeMain|goodsMain|goodsProduct)$");
        }
        map = OrderUtil.storeCart(memberCartList);
        loadCart(map,cartMap,accountId,false,null);
        req.setAttribute("storeCart",cartMap);
        return   "pages/front/pc/cart/cart";
    }

    private void loadCart(Map<String,List<Member_cart>> map,Map<String,Object> cartMap,String accountId,boolean isCountFreight,String countyCode){
        for(String storeId : map.keySet()){
            Map<String,Object> storeInfo = new HashMap<>();
            //存入购物车信息
            List<Member_cart> nowCartList = map.get(storeId);
            if(nowCartList != null){
                for(Member_cart memberCart:nowCartList){
                    Integer price = goodsPriceService.price(memberCart.getSku(),null, GoodsSaleClientEnum.PC.getKey(),null);
                    //重新取价格,如果商品还没有绑定促销价，则从价格中心取一次价格
                    if(Strings.isBlank(memberCart.getSalesId())){
                        memberCart.setPrice(price);
                    }
                    memberCart.setSalesPrice(price);
                    //查询促销信息列表
                    //todo 目前销售区域没有限制
                    List<Sales_rule_goods> salesRuleGoodsList = salesRuleGoodsService.sales(storeId,memberCart.getSku(),GoodsSaleClientEnum.PC.getKey(),null,null);
                    for(Sales_rule_goods salesRuleGoods:salesRuleGoodsList){
                       int salesPrice = salesRuleGoodsService.price(memberCart.getSku(),null,null,salesRuleGoods.getId());
                       //获取促销价
                       salesRuleGoods.setSalesPrice(salesPrice);
                    }
                    memberCart.setSalesRuleGoodsList(salesRuleGoodsList);
                }
            }
            storeInfo.put("cartList",nowCartList);
            //优惠券信息列表
            List<Sales_coupon> salesCouponVOList = salesCouponService.getCouponList(storeId,accountId);
            storeInfo.put("couponList",salesCouponVOList);
            cartMap.put(storeId,storeInfo);
            if(isCountFreight){
                //放入店铺的运费信息
                List<StoreFreightProduct> storeFreightProductList = new ArrayList<>();
                List<SalesRuleProduct> productList = new ArrayList<>();
                if(nowCartList != null){
                    for(Member_cart memberCart:nowCartList){
                        //拼装订单运费信息
                        StoreFreightProduct storeFreightProduct = new StoreFreightProduct();
                        storeFreightProduct.setSku(memberCart.getSku());
                        storeFreightProduct.setNum(memberCart.getNum());
                        storeFreightProductList.add(storeFreightProduct);
                        //拼装订单促销信息
                        SalesRuleProduct salesRuleProduct = new SalesRuleProduct();
                        salesRuleProduct.setSku(memberCart.getSku());
                        salesRuleProduct.setNum(memberCart.getNum());
                        salesRuleProduct.setSalesId(memberCart.getSalesId());
                        productList.add(salesRuleProduct);
                    }
                }

                //计算运费
                int storeFreightMoney = storeFreightService.countFreight(storeFreightProductList,Strings.isNotBlank(countyCode) ? countyCode.substring(0,2)+"0000" : "",null,null,storeId);
                storeInfo.put("freightMoney",storeFreightMoney);

                //加载订单优惠信息
                //获取会员等级
                Member_user memberUser = memberUserService.getField("levelId",Cnd.where("accountId","=",accountId).and("delFlag","=",false));
                List<Sales_rule_order> salesRuleOrderList = salesRuleOrderService.sales(storeId,productList,countyCode,memberUser.getLevelId());
                storeInfo.put("orderSales",salesRuleOrderList);
                //加载优惠券信息
                List<Sales_coupon> salesCouponList = salesCouponService.sales(storeId,productList,countyCode,memberUser.getLevelId());
                storeInfo.put("orderCoupon",salesCouponList);
                cartMap.put(storeId,storeInfo);
            }
        }
    }

    /**
     * 会员领取优惠券
     * @param storeId
     * @param couponId
     * @return
     */
    @RequestMapping(value = "/couponSave")
    @SJson
    public Object couponSave(@RequestParam("storeId")String storeId,@RequestParam("couponId")String couponId){
        try{
            String accountId = StringUtil.getMemberUid();
            if(Strings.isBlank(accountId)){
                //会员未登录
                return Result.error(101,"会员未登录");
            }
            if (!salesCouponService.isReceieveable(couponId, 1)) {
                return Result.error(102,"优惠券已领完");
            }
            //领取优惠券
            memberCouponService.save(storeId,couponId,accountId,1);
            return  Result.success("globals.result.success");
        }catch (Exception e){
            log.debug(e.getMessage(),e);
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = "/cartForComp")
    @SJson
    public String cartForComp(HttpServletRequest req){
        String accountId = StringUtil.getMemberUid();
        List<Member_cart> memberCartList = new ArrayList<>();
        Map<String,List<Member_cart>> map = new HashMap<>();
        Map<String,Object> cartMap = new HashMap<>();
        if(Strings.isBlank(accountId)){
            //会员未登陆,购物车取cookie数据
            String [] cookieCartSku = null;
            //获取cartSku
            String cookieSkuStr = CookieUtil.getCookie(req, OrderConsts.CART_COOKIE_SKU);
            if(Strings.isNotBlank(cookieSkuStr)){
                cookieCartSku = Json.fromJsonAsArray(String.class,cookieSkuStr);
            }
            String [] cookieCartNum = null;
            //获取cartNum
            String cookieNumStr = CookieUtil.getCookie(req,OrderConsts.CART_COOKIE_NUM);
            if(Strings.isNotBlank(cookieNumStr)){
                cookieCartNum = Json.fromJsonAsArray(String.class,cookieNumStr);
            }
            if(cookieCartSku != null && cookieCartSku.length > 0){
                for(String sku:cookieCartSku){
                    Goods_product product = goodsProductService.getBySku(sku);
                    if(product != null){
                        Member_cart member_cart = new Member_cart();
                        Integer price = goodsPriceService.getSalePrice(sku,null,null,null, GoodsSaleClientEnum.PC.getKey(),0,null);
                        String imageUrl = goodsProductService.getProductImage(sku);
                        member_cart.setSku(sku);
                        int index = ArrayUtils.indexOf(cookieCartSku,sku);
                        String skuNum=cookieCartNum[index];
                        member_cart.setNum(Integer.parseInt(skuNum.split("_")[0]));
                        member_cart.setPrice(price);
                        member_cart.setImgurl(imageUrl);
                        member_cart.setStoreMain(product.getStoreMain());
                        member_cart.setGoodsMain(product.getGoodsMain());
                        member_cart.setGoodsProduct(product);
                        memberCartList.add(member_cart);
                    }
                }
            }
            map = OrderUtil.storeCart(memberCartList);
        }else{
            memberCartList = memberCartService.query(Cnd.where("delFlag","=",false).and("accountId","=",accountId),"^(storeMain|goodsMain|goodsProduct)$");
            map = OrderUtil.storeCart(memberCartList);
        }
        loadCart(map,cartMap,accountId,false,null);
        return   Json.toJson(map);
    }

    /**
     * 删除购物数据
     * @param sku
     * @return
     */
    @RequestMapping(value = "/delCartInfo",method = RequestMethod.POST)
    @SJson
    public Object delCartInfo(@RequestParam(value = "sku", required = false)String sku,@RequestParam(value = "skus", required = false)String[] skus, HttpServletRequest req,HttpServletResponse res){
        try{
            String accountId = StringUtil.getMemberUid();
            if(Strings.isBlank(accountId)){
                //更新cookie
                if(Strings.isBlank(CookieUtil.getCookie(req,OrderConsts.CART_COOKIE_SKU))){
                    return Result.error("Cookie已失效");
                }
                String cookieVal =  CookieUtil.getCookie(req,OrderConsts.CART_COOKIE_SKU);
                String num =  CookieUtil.getCookie(req,OrderConsts.CART_COOKIE_NUM);
                List<String> numbers =Json.fromJsonAsList(String.class,num);
                List<String> cartSkus =Json.fromJsonAsList(String.class,cookieVal);
                Iterator<String> iter = cartSkus.iterator();
                Iterator<String> iterNum = numbers.iterator();
                while (iter.hasNext()){
                    String cartSku = iter.next();
                    if(cartSku.equals(sku)){
                        iter.remove();
                        break;
                    }
                }
                while (iterNum.hasNext()){
                    String number = iterNum.next();
                    String skuNum=number.split("_")[0];
                    if(number.split("_")[1].equals(sku)){
                        iterNum.remove();
                        break;
                    }
                }
                CookieUtil.setCookie(res,OrderConsts.CART_COOKIE_SKU,Json.toJson(cartSkus));
                CookieUtil.setCookie(res,OrderConsts.CART_COOKIE_NUM,Json.toJson(numbers));
            }else{
                if (!Lang.isEmpty(skus)) {
                    memberCartService.clear(Cnd.where("sku","in",skus).and("accountId","=",accountId));
                } else {
                    //更新库
                    memberCartService.clear(Cnd.where("sku","=",sku).and("accountId","=",accountId));
                }
            }
            return Result.success("globals.result.success");
        }catch (Exception e){
            log.debug(e.getMessage(),e);
            return Result.error("globals.result.error");
        }
    }

    /**
     * 更新购物车货品的数量
     * @param sku
     * @return
     */
    @RequestMapping(value = "/updateCartInfoNum",method = RequestMethod.POST)
    @SJson
    public Object updateCartInfoNum(@RequestParam("sku")String sku,@RequestParam("num")Integer num,HttpServletRequest req,HttpServletResponse res){
        try{
            List<Member_cart> memberCartList=new ArrayList<Member_cart>();
            String accountId = StringUtil.getMemberUid();
            int allCartNum=0;
            if(Strings.isBlank(accountId)){
                String cartSku=CookieUtil.getCookie(req,OrderConsts.CART_COOKIE_SKU);
                //更新cookie
                if(Strings.isBlank(cartSku)){
                    return Result.error("Cookie已失效");
                }
                String cookieNum =  CookieUtil.getCookie(req,OrderConsts.CART_COOKIE_NUM);
                List<String> cartNums =Json.fromJsonAsList(String.class,cookieNum);
                if(!Lang.isEmpty(cartNums)){
                    for(int x=0;x<cartNums.size();x++){
                        String cartNum=cartNums.get(x);
                        String cookieSku=cartNum.split("_")[1];
                        if(cookieSku.equals(sku)){
                            String newNum=num+"_"+sku;
                            cartNums.set(x,newNum);
                            cartNum=num+"_"+sku;
                        }
                        allCartNum +=Integer.parseInt( cartNum.split("_")[0]);
                    }
                }
                CookieUtil.setCookie(res,OrderConsts.CART_COOKIE_NUM,Json.toJson(cartNums));



                //会员未登陆,购物车取cookie数据
                String [] cookieCartSku = null;
                //获取cartSku
                String cookieSkuStr =Json.toJson(cartSku);
                if(Strings.isNotBlank(cookieSkuStr)){
                    cookieCartSku = Json.fromJsonAsArray(String.class,cookieSkuStr);
                }
                String [] cookieCartNum = null;
                //获取cartNum
                String cookieNumStr = Json.toJson(cartNums);
                if(Strings.isNotBlank(cookieNumStr)){
                    cookieCartNum = Json.fromJsonAsArray(String.class,cookieNumStr);
                }
                if(cookieCartSku != null && cookieCartSku.length > 0){
                    for(String sku1:cookieCartSku){
                        Goods_product product = goodsProductService.getBySku(sku1);
                        if(product != null){
                            Member_cart member_cart = new Member_cart();
                            Integer price1 = goodsPriceService.getSalePrice(sku1,null,null,null, GoodsSaleClientEnum.PC.getKey(),0,null);
                            String imageUrl1 = goodsProductService.getProductImage(sku1);
                            member_cart.setSku(sku1);
                            int index = ArrayUtils.indexOf(cookieCartSku,sku1);
                            String numSku=cookieCartNum[index];
                            String num1=(numSku.split("_"))[0];
                            member_cart.setNum(Integer.parseInt(num1));
                            member_cart.setPrice(price1);
                            member_cart.setImgurl(imageUrl1);
                            member_cart.setStoreMain(product.getStoreMain());
                            member_cart.setGoodsMain(product.getGoodsMain());
                            member_cart.setGoodsProduct(product);
                            memberCartList.add(member_cart);
                        }
                    }
                }
            }else{
                //更新库
                Member_cart memberCart = memberCartService.fetch(Cnd.where("accountId","=",accountId).and("sku","=",sku));
                if(memberCart == null){
                    return Result.error("当前会员购物车无这条数据");
                }
                memberCart.setNum(num);
                memberCart.setOpBy(StringUtil.getUid());
                memberCart.setOpAt((int)(System.currentTimeMillis()/1000));
                memberCartService.update(memberCart);
                memberCartList=memberCartService.query(Cnd.where("accountId","=",accountId));
            }
            return Result.success("globals.result.success",memberCartList);
        }catch (Exception e){
            log.debug(e.getMessage(),e);
            return Result.error("globals.result.error");
        }
    }

    /**
     * 购物车结算判断当前是否登陆
     * @return
     */
    @RequestMapping(value = "/isLogin",method = RequestMethod.POST)
    @SJson
    public Object isLogin(){
        //判断当前会员是否登陆
        String accountId = StringUtil.getMemberUid();
        if(Strings.isNotBlank(accountId)){
            return Result.success("globals.result.success");
        }
        return Result.error("globals.result.error");
    }


    /**
     * 到下单页面
     * @param
     * @return
     */
    @RequestMapping(value = "/placeOrder")
    public String toPlaceOrder( HttpServletRequest req){
        //check参数
        String num = req.getParameter("num");
        String cartInfo = req.getParameter("cartInfo");
        if(Strings.isBlank(cartInfo)){
            return  "redirect:/404";
        }
        List<CartCountVO> cartCountVOList = Json.fromJsonAsList(CartCountVO.class,cartInfo);
        String [] skuArray = new String[]{};
        for(CartCountVO cartCountVO:cartCountVOList){
            skuArray = ArrayUtils.add(skuArray,cartCountVO.getSku());
        }
        Map<String,Object> cartMap = new HashMap<>();
        //获取会员的默认地址
        String accountId = StringUtil.getMemberUid();
        List<Member_cart> memberCartList = new ArrayList<>();
        if(Strings.isNotBlank(num)){
            //传了num的值，表示立即购买
            Goods_product goodsProduct = goodsProductService.fetch(Cnd.where("sku","in",skuArray));
            Member_cart memberCart = new Member_cart();
            memberCart.setAccountId(accountId);
            memberCart.setStoreId(goodsProduct.getStoreId());
            memberCart.setGoodsId(goodsProduct.getGoodsId());
            memberCart.setProductId(goodsProduct.getId());
            memberCart.setSku(goodsProduct.getSku());
            memberCart.setNum(Integer.parseInt(num));
            memberCart.setPrice(goodsPriceService.getSalePrice(goodsProduct.getSku(),null,null,null, GoodsSaleClientEnum.PC.getKey(),0,null));
            memberCart.setImgurl(goodsProductService.getProductImage(goodsProduct.getSku()));
            //查询店铺
            Store_main storeMain = storeMainService.fetch(goodsProduct.getStoreId());
            memberCart.setStoreMain(storeMain);
            memberCartList.add(memberCart);
            HttpSession session = req.getSession();
            session.setAttribute(CART_SESSION_NAME,memberCartList);
        }else{
            memberCartList = memberCartService.query(Cnd.where("delFlag","=",false).and("accountId","=",accountId).and("sku","in",skuArray),"^(storeMain|goodsMain|goodsProduct)$");
        }

        //查询会员所有可用的地址，按照是否默认与添加时间排序
        List<Member_address> memberAddressList =
                memberAddressService.query(Cnd.where("delFlag","=",false).and("accountId","=",accountId).desc("defaultValue").desc("opAt"));
        //取出第一条会员可用的地址
        Member_address memberAddress = null;
        if(memberAddressList != null && memberAddressList.size() > 0){
            memberAddress = memberAddressList.get(0);
        }
        req.setAttribute("memberAddress",memberAddress);
        req.setAttribute("addressList",memberAddressList);

        //绑定促销信息
        for(Member_cart memberCart:memberCartList){
            if(ArrayUtils.contains(skuArray,memberCart.getSku())){
                //存在促销信息,绑定促销价
                for(CartCountVO cartCountVO:cartCountVOList){
                    if(memberCart.getSku().equals(cartCountVO.getSku())){
                        int salesPrice = salesRuleGoodsService.price(memberCart.getSku(),null,null,cartCountVO.getSalesId());
                        memberCart.setPrice(salesPrice);
                        memberCart.setSalesId(cartCountVO.getSalesId());
                    }
                }
            }
        }
        Map<String,List<Member_cart>> storeCartMap = OrderUtil.storeCart(memberCartList);

        //加载商品信息
        loadCart(storeCartMap,cartMap,accountId,true, memberAddress != null ? memberAddress.getCounty() : null);

        req.setAttribute("storeCart",cartMap);
        req.setAttribute("area",shopAreaService);
        //查询会员
        Account_user accountUser = accountUserService.fetch(Cnd.where("accountId","=",accountId));
        req.setAttribute("accountUser",accountUser);
        return "pages/front/pc/order/count";
    }

    @RequestMapping(value = "/getStoreSales",method = RequestMethod.POST)
    @SJson
    public Object getStoreSales(@RequestParam("orderStoreInfo")String orderStoreInfo,@RequestParam("countyCode")String countyCode){
        try{
            Map<String,Object> storeInfo = new HashMap<>();
            OrderStoreVO orderStoreVO = Json.fromJson(OrderStoreVO.class,orderStoreInfo);
            List<OrderGoodsVO> orderGoodsVOList = orderStoreVO.getGoodsList();
            List<SalesRuleProduct> productList = new ArrayList<>();
            for(OrderGoodsVO orderGoodsVO:orderGoodsVOList){
                SalesRuleProduct salesRuleProduct = new SalesRuleProduct();
                salesRuleProduct.setSku(orderGoodsVO.getSku());
                salesRuleProduct.setNum(orderGoodsVO.getNum());
                salesRuleProduct.setSalesId(orderGoodsVO.getGoodsSalesId());
                productList.add(salesRuleProduct);
            }
            String accountId = StringUtil.getMemberUid();
            Member_user memberUser = memberUserService.getField("levelId",Cnd.where("accountId","=",accountId).and("delFlag","=",false));
            List<Sales_rule_order> salesRuleOrderList = salesRuleOrderService.sales(orderStoreVO.getId(),productList,countyCode,memberUser.getLevelId());
            storeInfo.put("orderSales",salesRuleOrderList);
            //加载优惠券信息
            List<Sales_coupon> salesCouponList = salesCouponService.sales(orderStoreVO.getId(),productList,countyCode,memberUser.getLevelId());
            storeInfo.put("orderCoupon",salesCouponList);
            return Result.success("globals.result.success",storeInfo);
        }catch (Exception e){
            log.debug(e.getMessage(),e);
            return Result.error("globals.result.error");
        }
    }

    /**
     * 创建订单
     * @param memberAddressId
     * @param storeInfo
     * @param payType
     * @return
     */
    @RequestMapping(value = "/createOrder",method = RequestMethod.POST)
    @SJson
    public Object createOrder(@RequestParam("memberAddressId")String memberAddressId,@RequestParam("storeInfo")String storeInfo,@RequestParam("payType") Integer payType,HttpServletRequest req){
        try{
            //查询当前订单的收货地址
            Member_address memberAddress = memberAddressService.fetch(memberAddressId);
            if(memberAddress == null){
                return Result.error("收货地址为空");
            }
            boolean isNowBuy = false;
            //判断是否是立即购买的
            HttpSession session = req.getSession();
            if(session.getAttribute(CART_SESSION_NAME) != null){
                //销毁当前Session的数据
                isNowBuy =true;
                session.removeAttribute(CART_SESSION_NAME);
            }
            List<OrderStoreVO> storeVOList = Json.fromJsonAsList(OrderStoreVO.class,storeInfo);
            boolean isSale =  checkProuductStock(storeVOList);
            if(!isSale){
                return Result.error(102,"存在无库存可销售的货品");
            }
            String accountId = StringUtil.getMemberUid();
            Member_user memberUser = memberUserService.getField("levelId",Cnd.where("accountId","=",accountId).and("delFlag","=",false));
            String orderGroupId =  orderMainService.createOrder(memberAddress,storeVOList,payType,memberUser.getLevelId(),isNowBuy);
            return Result.success("globals.result.success",orderGroupId);
        }catch (Exception e){
            log.debug(e.getMessage(),e);
            return Result.error("globals.result.error");
        }
    }

    //检查订单中是否存在无库存可以销售的货品
    private boolean checkProuductStock(List<OrderStoreVO> storeVOList){
        //
        List<GoodsProductVO> goodsProductVOList = new ArrayList<>();
        for(OrderStoreVO orderStoreVO : storeVOList){
            List<OrderGoodsVO> orderGoodsVOList = orderStoreVO.getGoodsList();
            for(OrderGoodsVO orderGoodsVO :orderGoodsVOList){
                GoodsProductVO goodsProductVO = new GoodsProductVO();
                goodsProductVO.setSku(orderGoodsVO.getSku());
                goodsProductVO.setNum(orderGoodsVO.getNum());
                goodsProductVOList.add(goodsProductVO);
            }
        }
       return goodsProductService.validateStock(goodsProductVOList);
    }

    /**
     * 跳转至支付成功页面
     * @param id
     * @return
     */
    @RequestMapping(value = "/orderSuccess/{id}",method = RequestMethod.GET)
    public String orderSuccess(@PathVariable("id")String id){
        return  "pages/front/pc/order/orderSuccess";
    }


    /**
     * 跳转订单支付页面
     * @param id
     * @param req
     * @return
     */
    @RequestMapping(value = "/count/{id}",method = RequestMethod.GET)
    public String toCount(@PathVariable("id")String id,HttpServletRequest req){
        List<Order_main> orderMainList = orderMainService.query(Cnd.where("delFlag","=",false).and("groupId","=",id));
        if(orderMainList == null && orderMainList.size() < 1){
            return "redirect:/404";//非法的支付链接或者支付的订单信息不存在
        }
        //计算支付总金额
        int payMoney = 0;
        //计算商品件数总数量
        int goodsNum = 0;
        Order_goods orderGoods = new Order_goods();
        for(Order_main orderMain:orderMainList){
            payMoney += orderMain.getPayMoney();
            List<Order_goods> orderGoodsList = orderGoodsService.query(Cnd.where("delFlag","=",false).and("orderId","=",orderMain.getId()));
            for(Order_goods goods: orderGoodsList){
                goodsNum += goods.getBuyNum();
            }
            if(Strings.isBlank(orderGoods.getId())){
                orderGoods = orderGoodsList.get(0);
                orderGoodsService.fetchLinks(orderGoods,"storeMain");
            }
        }
        Order_main order = orderMainList.get(0);
        orderMainService.fetchLinks(order,"accountInfo");
        Account_user accountUser = accountUserService.fetch(Cnd.where("accountId","=",order.getAccountId()));
        req.setAttribute("groupId",id);
        req.setAttribute("order",order);
        req.setAttribute("accountUser",accountUser);
        req.setAttribute("goods",orderGoods);
        req.setAttribute("payMoney",payMoney);
        req.setAttribute("goodsNum",goodsNum);
        req.setAttribute("area",shopAreaService);
        return  "pages/front/pc/order/countWay";
    }


    /**
     * 到下单页面
     * @param
     * @return待确认  这一块到底是以订单组还是订单为一个
     */
    @RequestMapping(value = "/buyAgain")
    public String buyAgain( HttpServletRequest req){
        //check参数
        String orderId = req.getParameter("orderId");
        String num = req.getParameter("num");
        String cartInfo = req.getParameter("cartInfo");
        if(Strings.isBlank(orderId)){
            return  "redirect:/404";
        }else{
            orderMainService.fetch(orderId);
        }

        List<CartCountVO> cartCountVOList = Json.fromJsonAsList(CartCountVO.class,cartInfo);
        String [] skuArray = new String[]{};
        for(CartCountVO cartCountVO:cartCountVOList){
            skuArray = ArrayUtils.add(skuArray,cartCountVO.getSku());
        }
        Map<String,Object> cartMap = new HashMap<>();
        //获取会员的默认地址
        String accountId = StringUtil.getMemberUid();
        List<Member_cart> memberCartList = new ArrayList<>();
        if(Strings.isNotBlank(num)){
            //传了num的值，表示立即购买
            Goods_product goodsProduct = goodsProductService.fetch(Cnd.where("sku","in",skuArray));
            Member_cart memberCart = new Member_cart();
            memberCart.setAccountId(accountId);
            memberCart.setStoreId(goodsProduct.getStoreId());
            memberCart.setGoodsId(goodsProduct.getGoodsId());
            memberCart.setProductId(goodsProduct.getId());
            memberCart.setSku(goodsProduct.getSku());
            memberCart.setNum(Integer.parseInt(num));
            memberCart.setPrice(goodsPriceService.getSalePrice(goodsProduct.getSku(),null,null,null, GoodsSaleClientEnum.PC.getKey(),0,null));
            memberCart.setImgurl(goodsProductService.getProductImage(goodsProduct.getSku()));
            //查询店铺
            Store_main storeMain = storeMainService.fetch(goodsProduct.getStoreId());
            memberCart.setStoreMain(storeMain);
            memberCartList.add(memberCart);
            HttpSession session = req.getSession();
            session.setAttribute(CART_SESSION_NAME,memberCartList);
        }else{
            memberCartList = memberCartService.query(Cnd.where("delFlag","=",false).and("accountId","=",accountId).and("sku","in",skuArray),"^(storeMain|goodsMain|goodsProduct)$");
        }

        //查询会员所有可用的地址，按照是否默认与添加时间排序
        List<Member_address> memberAddressList =
                memberAddressService.query(Cnd.where("delFlag","=",false).and("accountId","=",accountId).desc("defaultValue").desc("opAt"));
        //取出第一条会员可用的地址
        Member_address memberAddress = null;
        if(memberAddressList != null && memberAddressList.size() > 0){
            memberAddress = memberAddressList.get(0);
        }
        req.setAttribute("memberAddress",memberAddress);
        req.setAttribute("addressList",memberAddressList);

        //绑定促销信息
        for(Member_cart memberCart:memberCartList){
            if(ArrayUtils.contains(skuArray,memberCart.getSku())){
                //存在促销信息,绑定促销价
                for(CartCountVO cartCountVO:cartCountVOList){
                    if(memberCart.getSku().equals(cartCountVO.getSku())){
                        int salesPrice = salesRuleGoodsService.price(memberCart.getSku(),null,null,cartCountVO.getSalesId());
                        memberCart.setPrice(salesPrice);
                        memberCart.setSalesId(cartCountVO.getSalesId());
                    }
                }
            }
        }
        Map<String,List<Member_cart>> storeCartMap = OrderUtil.storeCart(memberCartList);

        //加载商品信息
        loadCart(storeCartMap,cartMap,accountId,true, memberAddress != null ? memberAddress.getCounty() : null);

        req.setAttribute("storeCart",cartMap);
        req.setAttribute("area",shopAreaService);
        //查询会员
        Account_user accountUser = accountUserService.fetch(Cnd.where("accountId","=",accountId));
        req.setAttribute("accountUser",accountUser);
        return "pages/front/pc/order/count";
    }




}
