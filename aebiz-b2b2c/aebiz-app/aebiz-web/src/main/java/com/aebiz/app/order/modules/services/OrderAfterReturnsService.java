package com.aebiz.app.order.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.order.modules.models.Order_after_returns;

public interface OrderAfterReturnsService extends BaseService<Order_after_returns>{

    /**
     * 上传会员寄回商品物流信息
     *
     * @param afterSaleId 售后单号
     * @param logisticsCompany 物流公司
     * @param logisticsSheetId 物流单号
     * @param note 备注说明
     * @param vouchers 凭证图片
     */
    public void addRefundLogistics(String afterSaleId, String logisticsCompany, String logisticsSheetId, String note, String[] vouchers);

}
