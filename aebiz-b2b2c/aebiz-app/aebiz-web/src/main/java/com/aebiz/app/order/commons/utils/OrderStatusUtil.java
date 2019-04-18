package com.aebiz.app.order.commons.utils;

import org.springframework.stereotype.Component;

@Component
public class OrderStatusUtil {


    //根据订单主表的一些状态进行判断
    public static String getOrderStatus(Integer orderStatus,Integer payStatus,Integer deliveryStatus,Integer getStatus,Integer feedStatus){
        String statusName = "";
        switch (orderStatus){
            case  0:
                statusName = "待审核订单";
                break;
            case  1:
                switch (payStatus){
                    case 0:
                        statusName = "待支付";
                        break;
                    case 1:
                        statusName = "待确认";
                        break;
                    case 2:
                        statusName = "部分支付";
                        break;
                    case 3:
                        switch (deliveryStatus){
                            case 0:
                                statusName = "待发货";
                                break;
                            case 1:
                                statusName = "配货中";
                                break;
                            case 2:
                                statusName = "部分发货";
                                break;
                            case 3:
                                switch (getStatus){
                                    case 0:
                                        statusName = "待收货";
                                        break;
                                    case 1:
                                        switch (feedStatus){
                                            case 1:
                                                statusName = "待评价";
                                                break;
                                            case 2:
                                                statusName = "已评价";
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case 4:
                                statusName = "申请退货";
                                break;
                            case 5:
                                statusName = "部分退货";
                                break;
                            case 6:
                                statusName = "已退货";
                                break;
                            default:
                        }
                        break;
                    case 4:
                        statusName = "待退款";
                        break;
                    case 5:
                        statusName = "部分退款";
                        break;
                    case 6:
                        statusName = "已退款";
                        break;
                    default:
                }
                break;
            case  2:
                statusName = "无效订单";
                break;
            case  3:
                statusName = "取消订单";
                break;
            case  4:
                statusName = "关闭订单";
                break;
            case  5:
                statusName = "完成订单";
                break;
        }
        return statusName;
    }
}
