package com.aebiz.app.store.modules.commons.utils;

import org.springframework.stereotype.Component;

/**
 * Created by ThinkPad on 2017/7/5.
 */
@Component
public class StoreUtil {


    //根据店铺的审核状态渲染
    public static String getStatus(Integer status){
        String statusName = "";
        switch (status){
            case 0:
                statusName = "待审核";
                break;
            case 1:
                statusName = "审核通过";
                break;
            case 2:
                statusName = "审核不通过";
                break;
        }
        return  statusName;
    }

}
