package com.aebiz.app.order.modules.models.em;

import com.aebiz.baseframework.base.Message;

/**
 * @Auther: zenghaorong
 * @Date: 2019/4/13  12:21
 * @Description: 订单类型枚举
 */
public enum OrderTypeEnum {

    product_order_type("1", "商品订单"),
    video_order_type("2", "视频订单");

    private String key;
    private String value;

    private OrderTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String getValue(String key) {
        for (OrderTypeEnum ft : OrderTypeEnum.values()) {
            if (key == ft.key) {
                return ft.value;
            }
        }
        return "";
    }


}
