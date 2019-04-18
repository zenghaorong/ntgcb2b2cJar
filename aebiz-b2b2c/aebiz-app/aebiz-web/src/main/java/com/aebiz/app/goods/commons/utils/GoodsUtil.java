package com.aebiz.app.goods.commons.utils;

import com.aebiz.app.goods.modules.models.Goods_image;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GoodsUtil {

    public static boolean isDefaultSpec(List<Map<String,String>> goodsSpecList, Map<String,String> spec){
        boolean flag = false;//是否默认货品规格标识
            if(goodsSpecList != null){
            if(goodsSpecList.contains(spec)){
                flag =true;
            }
        }
        return  flag;
    }

    /**
     * 根据终端的key过滤图片数据
     * @param goodsImageList
     * @param client
     */
    public  static void getClientGoodsImage(List<Goods_image> goodsImageList, int client){
        if(goodsImageList != null){
            Iterator<Goods_image> iterator = goodsImageList.iterator();
            while (iterator.hasNext()){
                if(client !=  iterator.next().getSaleClient()){
                    iterator.remove();//移除非PC端的数据
                }
            }
        }
    }
}
