package com.aebiz.app.order.commons.utils;

import com.aebiz.app.member.modules.models.Member_cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ThinkPad on 2017/6/21.
 */
public class OrderUtil {

    //转成分不同商铺的购物车数据
    public static Map<String,List<Member_cart>> storeCart(List<Member_cart> memberCartList){
        Map<String,List<Member_cart>> map = new HashMap<>();
        if(memberCartList != null){
            for(Member_cart memberCart : memberCartList){
                if(map.containsKey(memberCart.getStoreId())){
                    map.get(memberCart.getStoreId()).add(memberCart);
                }else{
                    List<Member_cart> memberCarts = new ArrayList<>();
                    memberCarts.add(memberCart);
                    map.put(memberCart.getStoreId(),memberCarts);
                }
            }
        }
        return map;
    }

    //获取6位随机数
    public static int getRandNum(int min, int max) {
        int randNum = min + (int)(Math.random() * ((max - min) + 1));
        return randNum;
    }

}
