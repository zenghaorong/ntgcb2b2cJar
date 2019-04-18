package com.aebiz.app.web.modules.controllers.front.pc.cart;

import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.models.em.GoodsSaleClientEnum;
import com.aebiz.app.goods.modules.services.GoodsPriceService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.member.modules.models.Member_cart;
import com.aebiz.app.member.modules.services.MemberCartService;
import com.aebiz.app.order.commons.constant.OrderConsts;
import com.aebiz.app.order.commons.utils.OrderUtil;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.CookieUtil;
import com.aebiz.commons.utils.StringUtil;

import org.apache.commons.lang3.ArrayUtils;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ThinkPad on 2017/6/29.
 */
@Controller
@RequestMapping("/cart")
public class PcCartController {

    private static final Log log = Logs.get();

    @Autowired
    private MemberCartService memberCartService;

    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private GoodsPriceService goodsPriceService;

    /**
     * 加载购物车数据接口
     * @param req
     * @return
     */
    @RequestMapping(value = "/data",method = RequestMethod.POST)
    @SJson
    public Object cartData(HttpServletRequest req){

        try {
            String accountId = StringUtil.getMemberUid();
            Map<String,Object> result = new HashMap<>();
            int cartNum = 0;
            //购物车的合计金额
            int totalMoney = 0;
            List<Member_cart> memberCartList  = new ArrayList<>();
            if(Strings.isNotBlank(accountId)) {
                //查询购物车的信息
                memberCartList = memberCartService.query(Cnd.where("delFlag","=",false).and("accountId","=",accountId));
                if(memberCartList != null){
                    cartNum = memberCartList.size();
                    for(Member_cart memberCart:memberCartList){
                        Integer price = goodsPriceService.getSalePrice(memberCart.getSku(),null,null,null, GoodsSaleClientEnum.PC.getKey(),0,null);
                        totalMoney += (price*memberCart.getNum());
                    }
                }
            }else{
                String [] cookieCartSku = null;
                //获取cartSku
                String cookieSkuStr = CookieUtil.getCookie(req, OrderConsts.CART_COOKIE_SKU);
                if(Strings.isNotBlank(cookieSkuStr)){
                    cookieCartSku = Json.fromJsonAsArray(String.class,cookieSkuStr);
                }
                Integer [] cookieCartNum = null;
                //获取cartNum
                String cookieNumStr = CookieUtil.getCookie(req,OrderConsts.CART_COOKIE_NUM);
                if(Strings.isNotBlank(cookieNumStr)){
                    cookieCartNum = Json.fromJsonAsArray(Integer.class,cookieNumStr);
                }
                if(cookieCartSku != null || cookieCartSku.length > 0){
                    for(String sku:cookieCartSku){
                        Goods_product product = goodsProductService.getBySku(sku);
                        if(product != null){
                            Member_cart member_cart = new Member_cart();
                            Integer price = goodsPriceService.getSalePrice(sku,null,null,null, GoodsSaleClientEnum.PC.getKey(),0,null);
                            String imageUrl = goodsProductService.getProductImage(sku);
                            member_cart.setSku(sku);
                            int index = ArrayUtils.indexOf(cookieCartSku,sku);
                            member_cart.setNum(cookieCartNum[index]);
                            member_cart.setPrice(price);
                            member_cart.setImgurl(imageUrl);
                            member_cart.setStoreMain(product.getStoreMain());
                            member_cart.setGoodsMain(product.getGoodsMain());
                            member_cart.setGoodsProduct(product);
                            memberCartList.add(member_cart);
                            totalMoney += (price*cookieCartNum[index]);
                        }

                    }
                }
                cartNum = memberCartList.size();
            }
            result.put("cartNum",cartNum);
            Map<String,List<Member_cart>> map = OrderUtil.storeCart(memberCartList);
            result.put("obj",map);
            result.put("totalMoney",totalMoney);
            return Result.success("globals.result.success",result);
        }catch (Exception e){
            log.debug(e.getMessage(),e);
            return Result.success("globals.result.error");
        }
    }

}
