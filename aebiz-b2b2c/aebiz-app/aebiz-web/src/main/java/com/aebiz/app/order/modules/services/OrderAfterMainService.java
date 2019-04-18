package com.aebiz.app.order.modules.services;

import com.aebiz.app.order.modules.models.Order_after_detail;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.order.modules.models.Order_after_main;
import org.nutz.lang.util.NutMap;

public interface OrderAfterMainService extends BaseService<Order_after_main> {
    /**
     * 获取售后详情（包含商品信息，订单信息，售后信息，日志信息等）
     *
     * @param afterSaleId 售后单号
     * @param storeId 店铺id
     * @return
     */
    NutMap getAfterSaleDetail(String afterSaleId, String storeId);

    /**
     * 卖家处理
     *
     * @param afterSaleId 售后单号
     * @param flag 自定义标识，用于区分具体的处理操作
     * @param note 卖家填写的备注说明
     * @param vouchers 凭证
     * @param opBy 操作人
     * @param handleMan 处理人
     * @param handleType 处理人类型
     * @return
     */
    public void sellerHandle(String afterSaleId , int applyType, int flag, int returnMoney, String note,String[] vouchers, String opBy, Integer handleType, String handleMan);

    /**
     * 平台处理
     *
     * @param afterSaleId 售后单号
     * @param flag 自定义标识，用于区分具体的处理操作
     * @param note 卖家填写的备注说明
     * @param vouchers 凭证
     * @return
     */
    void platformHandle(String afterSaleId , int applyType, int flag,String note,String[] vouchers);

    /**
     * 创建售后申请
     * @param orderAfterMain 售后申请主表
     * @param orderAfterDetail 售后申请商品详情
     */
    public void addApplyOrderAfter(Order_after_main orderAfterMain, Order_after_detail orderAfterDetail);

    /**
     * 取消售后
     *
     * @param afterSaleId 售后申请单号
     * @param cancelReason 取消售后原因
     */
    public void cancelOrderAfter(String afterSaleId, String cancelReason);

    /**
     * 检查是否可以申请售后
     *
     * @param orderId
     * @param orderGoodsId
     * @return true 可以申请，false不可申请
     */
    public NutMap checkCanApply(String orderId, String orderGoodsId);
}
