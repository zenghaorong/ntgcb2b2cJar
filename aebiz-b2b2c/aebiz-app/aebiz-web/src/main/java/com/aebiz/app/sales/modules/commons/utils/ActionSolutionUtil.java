package com.aebiz.app.sales.modules.commons.utils;

import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;

/**
 * Created by Hope on 2017/7/25.
 */
public class ActionSolutionUtil {


    /**
     * 取差价
     * @param originalPrice 原价
     * @param actionSolution 优惠方案
     * @return
     */
    public static int getDiffPrice(int originalPrice, String actionSolution) {
        return originalPrice - getSalesPrice(originalPrice, actionSolution);
    }

    /**
     * 取优惠价
     * @param originalPrice 原价
     * @param actionSolution 优惠方案
     * @return
     */
    public static int getSalesPrice(int originalPrice, String actionSolution) {
        int totalAmount = originalPrice;
        NutMap map = Json.fromJson(NutMap.class, actionSolution);
        if (map.containsKey("tpl_sale_sol_subfixed")) {
            NutMap subfixed = map.getAs("tpl_sale_sol_subfixed", NutMap.class);
            totalAmount = originalPrice - subfixed.getInt("total_amount");
        } else if (map.containsKey("tpl_sale_sol_fixed")) {
            NutMap subfixed = map.getAs("tpl_sale_sol_fixed", NutMap.class);
            totalAmount = subfixed.getInt("total_amount");
        } else if (map.containsKey("tpl_sale_sol_subpercent")) {
            NutMap subpercent = map.getAs("tpl_sale_sol_subpercent", NutMap.class);
            totalAmount = originalPrice - originalPrice * subpercent.getInt("persent")/100;
        } else if (map.containsKey("tpl_sale_sol_percent")) {
            NutMap subpercent = map.getAs("tpl_sale_sol_percent", NutMap.class);
            totalAmount = originalPrice * subpercent.getInt("percent")/100;
        }
        return totalAmount;
    }
}
