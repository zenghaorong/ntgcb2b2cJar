package com.aebiz.app.dec.commons.service.impl;

import com.aebiz.app.dec.commons.service.ShoppingCartService;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.models.em.GoodsSaleClientEnum;
import com.aebiz.app.goods.modules.services.GoodsPriceService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.member.modules.models.Member_cart;
import com.aebiz.app.member.modules.services.MemberCartService;
import com.aebiz.app.order.commons.constant.OrderConsts;
import com.aebiz.app.order.commons.utils.OrderUtil;
import com.aebiz.app.web.modules.controllers.open.dec.dto.cart.CartProductDTO;
import com.aebiz.app.web.modules.controllers.open.dec.exception.ShoppingCartValidateException;
import com.aebiz.baseframework.base.Result;
import com.aebiz.commons.utils.CookieUtil;
import com.aebiz.commons.utils.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.Transient;
import java.util.*;

/**
 * Created by yewei on 2017/6/17.
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private MemberCartService memberCartService;
    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private GoodsPriceService goodsPriceService;

    //为了防止与其他的网站的Cookie名字冲突,所以加上工程名
    private static final String CART_COOKIE_NAME = "aebizCart";//项目的购物车cookie名称
    private static final Log log = Logs.get();
    /**
     * 获取当前用户的购物车数据
     * @return
     */
    public Map getCart(){
        //获取request对象
        HttpServletRequest req =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map cartMap=new HashMap();
        String accountId=StringUtil.getMemberUid();
        List<Member_cart> memberCartList = new ArrayList<>();
        Map<String,List<Member_cart>> map = new HashMap<>();
        if(Strings.isEmpty(accountId)){
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
                cartMap.put("cartProduct",memberCartList);
                return cartMap;
            }
        }else{
            memberCartList = memberCartService.query(Cnd.where("delFlag","=",false).and("accountId","=",accountId),"^(storeMain|goodsMain|goodsProduct)$");
            if(!Lang.isEmpty(memberCartList)){
                for (Member_cart cart:memberCartList) {
                    Integer price = goodsPriceService.price(cart.getSku(),null, GoodsSaleClientEnum.PC.getKey(),null);
                    cart.setSalesPrice(price);
                    cart.setPrice(price);
                }
            }
            cartMap.put("cartProduct",memberCartList);
            return cartMap;
        }
        return cartMap;
    }
    //删除购物车数据  cookies是用户未登陆的时候从cookies里面删除数据
    @Transient
    public Map removeCart(HttpServletResponse res,HttpServletRequest req,String sku,Cookie[] cookies) throws ShoppingCartValidateException{
        //从cookie里取当前会员的accountId。如果null就是未登陆
        Map cartMap=new HashMap();
        String accountId=StringUtil.getMemberUid();
        List<Member_cart> memberCartList=new ArrayList<>();
        if(Strings.isEmpty(accountId)){
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



            //会员未登陆,购物车取cookie数据
            String [] cookieCartSku = null;
            //获取cartSku
            String cookieSkuStr =Json.toJson(cartSkus);
            if(Strings.isNotBlank(cookieSkuStr)){
                cookieCartSku = Json.fromJsonAsArray(String.class,cookieSkuStr);
            }
            String [] cookieCartNum = null;
            //获取cartNum
            String cookieNumStr = Json.toJson(numbers);
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
            cartMap.put("cartProduct",memberCartList);
           return cartMap;
        }else{
            if(Strings.isEmpty(sku)){
                throw new ShoppingCartValidateException("商品skuUuid不能为空！");
            }

            Member_cart membercart=memberCartService.fetch(Cnd.where("sku","=",sku).and("accountId","=",accountId));
            if(membercart!=null){
                try {
                    memberCartService.delete(membercart.getId());
                }catch (Exception e){
                    log.error(e);
                }
            }
            List<Member_cart> cartList=memberCartService.query(Cnd.where("accountId","=",accountId).desc("opAt"));
            if(cartList !=null && cartList.size()>0){
                List<CartProductDTO> products=new ArrayList<CartProductDTO>();
                int totNum=0;
                int totPrice=0;
                for (Member_cart cartModel: cartList) {
                    Goods_product product=goodsProductService.fetch(cartModel.getProductId());
                    if(product !=null){
                        totNum+=cartModel.getNum();
                        totPrice +=cartModel.getPrice();
                    }
                }
                cartMap.put("cartProduct",cartList);
                cartMap.put("totNum",totNum);
                cartMap.put("totPrice",totPrice);
                return cartMap;
            }else{
                cartMap.put("cartProduct",null);
                cartMap.put("totNum",0);
                cartMap.put("totPrice",0);
                return cartMap;
            }
        }
    }

    /**
     * 拆分操作类型以及商品以及属性值
     *
     * @param productUuidAndAttrId
     * @return
     */
    private Map<String, String> splitProductIdAndAttrId(
            String productUuidAndAttrId) {
        Map<String, String> paramMap = new HashMap<String, String>();

        // 拆分productUuidAndAttrId,
        String[] operIds = productUuidAndAttrId.split("_");

        if (operIds.length >= 1) {
            paramMap.put("operType", operIds[0]);
        }
        if (operIds.length >= 2) {
            paramMap.put("productId", operIds[1]);
        }
        if (operIds.length >= 3) {
            paramMap.put("attrId", operIds[2]);
        }

        return paramMap;
    }

}
