package com.aebiz.app.web.modules.controllers.front.pc;

import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.models.Goods_tag;
import com.aebiz.app.goods.modules.models.em.GoodsSaleClientEnum;
import com.aebiz.app.goods.modules.services.GoodsPriceService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.goods.modules.services.GoodsService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.nutz.dao.Cnd;
import org.nutz.lang.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ThinkPad on 2017/8/24.
 */
@Controller
@RequestMapping("/goods/guessFavoorite")
public class GuessFavoriteController {
    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsPriceService goodsPriceService;

    @RequestMapping("")
    @SJson
    private List<Map> getFavorite(){
        List<Goods_main> D = goodsService.query(Cnd.NEW());
        goodsService.fetchLinks(D,"tagList");
        List<Map> productss=new ArrayList<>();
        if(!Lang.isEmpty(D)){
            for(Goods_main main:D){
               List<Goods_tag> tags=main.getTagList();
               if(!Lang.isEmpty(tags)){
                   for(Goods_tag tag:tags){
                       if("2017060000000001".equals(tag.getId())){
                           List<Goods_product> products=goodsProductService.query(Cnd.where("goodsId","=",main.getId()));
                           for(Goods_product product:products){
                            if(product.isDefaultValue()){
                                Map map=new HashMap<>();
                                String img= goodsProductService.getProductImage(product.getSku(),GoodsSaleClientEnum.PC);
                                Integer price = goodsPriceService.getSalePrice(product.getSku(),null,null,null, GoodsSaleClientEnum.PC.getKey(),0,null);
                                product.setSalePrice(price);
                                map.put("img",img);
                                map.put("product",product);
                                productss.add(map);
                            }
                           }
                       }
                   }
               }
            }
        }
        return productss;
    }
}
