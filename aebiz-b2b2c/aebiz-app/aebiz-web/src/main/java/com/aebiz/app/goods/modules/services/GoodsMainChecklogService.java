package com.aebiz.app.goods.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.goods.modules.models.Goods_main_checklog;

public interface GoodsMainChecklogService extends BaseService<Goods_main_checklog>{

    /**
     *  保存审核日志
     * @param goodsId 商品ID
     * @param checkStatus 审核状态GoodsCheckStatusEnum
     * @param reason 备注
     * @param checkerId 审核人ID
     */
    void addCheckLog(String goodsId, Integer checkStatus, String reason, String checkerId);

    /**
     * 保存申请日志
     * @param goodsId 商品ID
     * @param storeId 申请商家ID
     * @param applicantId 申请人ID
     */
    void addApplyLog(String goodsId, String storeId, String applicantId);

}
