package com.aebiz.app.member.modules.services.impl;

import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.models.em.GoodsSaleClientEnum;
import com.aebiz.app.goods.modules.services.GoodsPriceService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.member.modules.models.Member_cart;
import com.aebiz.app.member.modules.services.MemberCartService;
import com.aebiz.app.order.commons.constant.OrderConsts;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.commons.utils.CookieUtil;
import com.aebiz.commons.utils.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class MemberCartServiceImpl extends BaseServiceImpl<Member_cart> implements MemberCartService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }


    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private GoodsPriceService goodsPriceService;

    /**
     * (调用方法前要先验证accountId、goods_product对应的记录是否存在)
     *
     * @param accountId     会员id
     * @param goods_product 货品
     * @param price         加入购物车的价格
     * @param num           购物车数量
     * @param imageUrl      商品图片地址
     * @return
     */
    @Override
    public Member_cart saveCart(String accountId, Goods_product goods_product, Integer price, Integer num, String imageUrl) {
        Member_cart member_cart = new Member_cart();
        member_cart.setAccountId(accountId);
        member_cart.setStoreId(goods_product.getStoreId());
        member_cart.setGoodsId(goods_product.getGoodsId());
        member_cart.setProductId(goods_product.getId());
        member_cart.setSku(goods_product.getSku());
        member_cart.setNum(num);
        member_cart.setPrice(price);
        member_cart.setImgurl(imageUrl);
        member_cart.setOpAt((int) (System.currentTimeMillis() / 1000));
        member_cart.setOpBy(StringUtil.getUid());
        return this.dao().insert(member_cart);
    }

    /**
     * 同步cookie数据到购物车
     */
    @Override
    @Transactional
    public void synchronizeCart(String cookieSkuStr, String cookieNumStr, String accountId) {
        String[] cookieCartSku = null;
        String[] cookieCartNum = null;
        if (Strings.isNotBlank(cookieSkuStr) && Strings.isNotBlank(cookieNumStr)) {
            //获取cartSku
            cookieCartSku = Json.fromJsonAsArray(String.class, cookieSkuStr);
            cookieCartNum = Json.fromJsonAsArray(String.class, cookieNumStr);
            if (cookieCartSku != null || cookieCartSku.length > 0) {
                for (String sku : cookieCartSku) {
                    Member_cart memberCart = this.fetch(Cnd.where("delFlag", "=", false).and("sku", "=", sku).and("accountId","=",accountId));
                    if (memberCart == null) {
                        //购物车中不存在
                        Goods_product product = goodsProductService.getBySku(sku);
                        if (product != null) {
                            Member_cart member_cart = new Member_cart();
                            Integer price = goodsPriceService.getSalePrice(sku, null, null, null, GoodsSaleClientEnum.PC.getKey(), 0, null);
                            String imageUrl = goodsProductService.getProductImage(sku);
                            member_cart.setSku(sku);
                            int index = ArrayUtils.indexOf(cookieCartSku, sku);
                            String skuNum=cookieCartNum[index].split("_")[0];
                            member_cart.setNum(Integer.parseInt(skuNum));
                            member_cart.setPrice(price);
                            member_cart.setImgurl(imageUrl);
                            member_cart.setAccountId(accountId);
                            member_cart.setStoreId(product.getStoreId());
                            member_cart.setGoodsId(product.getGoodsId());
                            member_cart.setProductId(product.getId());
                            member_cart.setOpBy(accountId);
                            this.insert(member_cart);
                        }
                    }else{
                        //更新数量
                        Goods_product product = goodsProductService.getBySku(sku);
                        if (product != null) {
                            Integer price = goodsPriceService.getSalePrice(sku, null, null, null, GoodsSaleClientEnum.PC.getKey(), 0, null);
                            int index = ArrayUtils.indexOf(cookieCartSku, sku);
                            String skuNum=cookieCartNum[index].split("_")[0];
                            this.update(Chain.make("num",Integer.parseInt(skuNum)+memberCart.getNum()).add("price",price),Cnd.where("sku","=",sku).and("accountId","=",accountId));
                        }

                    }
                }
            }




        }
    }

}
