package com.aebiz.app.order.modules.services;

import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.member.modules.models.Member_address;
import com.aebiz.app.order.commons.vo.OrderStoreVO;
import com.aebiz.app.order.modules.models.Order_goods;
import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.baseframework.base.service.BaseService;

import java.util.List;

public interface OrderMainService extends BaseService<Order_main>{

    /**
     * 前台生成订单
     * @param memberAddress 会员地址
     * @param storeVOList 购物的信息
     * @param payType 支付方式
     * 返回创建订单组的ID
     */
    String createOrder(Member_address memberAddress, List<OrderStoreVO> storeVOList, Integer payType,String memberLevelId,boolean isNowBuy);

    /**
     * 手工录单
     * @param orderMain 主订单
     * @param goodsProductList  货品详情
     * @param payments 支付详情
     * @param uploadInfo 凭证详情
     */
    void insertOrderByManual(Order_main orderMain, List<Goods_product> goodsProductList, String payments, String uploadInfo);

    /**
     * 清除缓存
     */
    void clearCache();

    /**
     * 删除订单（逻辑删除）
     * @param orderMain
     * @return
     */
    void delOrder(Order_main orderMain);

    /**
     * 关闭订单
     * @param orderMain
     * @return
     */
    void closeOrder(Order_main orderMain);

    /**
     * 订单审核
     * @param orderMain
     */
    void audit(Order_main orderMain, Integer checkStatus, String comment);

    /**
     * 订单改地址
     * @param orderMain
     */
    void changeAddress(Order_main orderMain);

    /**
     * 订单改价
     * @param orderMain
     */
    void changePrice(Order_main orderMain, List<Order_goods> orderGoodsList);


    /**
     * 确认收款
     * @param orderMain
     * @param accountInfo
     * @param uploadInfo
     */
    void confirmReceive(Order_main orderMain,String accountInfo,String uploadInfo);

    /**
     * 验证订单是否能删除
     * @param orderMain
     * @return
     */
    boolean checkDelOrder(Order_main orderMain);

    /**
     * 验证订单是否能关闭
     * @param orderMain
     * @return
     */
    boolean checkCloseOrder(Order_main orderMain);

    /**
     * 验证订单是否能审核
     * @param orderMain
     * @return
     */
    boolean checkAuditOrder(Order_main orderMain);

    /**
     * 凭证上传或跳过
     * @param orderMain
     * @param uploadInfo
     */
    void uploadProof(Order_main orderMain,String uploadInfo);

    /**
     * 批量保存支付凭证
     * @param groupId
     * @param mainId
     * @param accountId
     * @param storeId
     * @param uploadInfo
     */
    void saveUploadInfo(String groupId,String mainId,String accountId,String storeId,String uploadInfo);

    void updateOrderGoodsStock(String orderGroupId);


}
