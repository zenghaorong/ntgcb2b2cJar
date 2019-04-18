package com.aebiz.app.order.commons.vo;

import com.aebiz.app.sales.modules.commons.vo.StoreSalesVO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ThinkPad on 2017/7/24.
 */
public class OrderStoreVO implements Serializable{

    private String id;

    private List<OrderGoodsVO> goodsList;

    private StoreSalesVO storeSales;

    //商户订单买家留言（下单时，会员填写）
    private String orderNote;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public List<OrderGoodsVO> getGoodsList() {
        return goodsList;
    }
    public void setGoodsList(List<OrderGoodsVO> goodsList) {
        this.goodsList = goodsList;
    }

    public StoreSalesVO getStoreSales() {
        return storeSales;
    }
    public void setStoreSales(StoreSalesVO storeSales) {
        this.storeSales = storeSales;
    }

    public String getOrderNote() { return orderNote; }
    public void setOrderNote(String orderNote) { this.orderNote = orderNote; }
}
