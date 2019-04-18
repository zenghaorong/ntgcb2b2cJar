package com.aebiz.app.order.modules.services.impl;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.goods.modules.services.GoodsService;
import com.aebiz.app.order.modules.models.*;
import com.aebiz.app.order.modules.models.em.*;
import com.aebiz.app.order.modules.services.*;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.app.store.modules.services.StoreMainService;
import com.aebiz.baseframework.base.Message;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderAfterMainServiceImpl extends BaseServiceImpl<Order_after_main> implements OrderAfterMainService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private OrderAfterDetailService afterDetailService;
    @Autowired
    private OrderAfterLogService afterLogService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderAfterRefundmentService refundmentService;

    @Autowired
    private OrderMainService orderMainService;

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private StoreMainService storeMainService;

    @Autowired
    private OrderAfterDetailService orderAfterDetailService;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private OrderAfterReturnsService orderAfterReturnsService;

    @Autowired
    private GoodsProductService goodsProductService;

    /**
     * 获取售后详情（包含商品信息，订单信息，售后信息，日志信息等）
     *
     * @param afterSaleId 售后单号
     * @param storeId 店铺id
     * @return nutMap 售后详情相关内容
     */
    @Override
    public NutMap getAfterSaleDetail(String afterSaleId, String storeId) {
        NutMap nutMap = new NutMap();
        // 详情页要展示售后信息，此处通过id查询相关的售后信息
        Cnd cnd = Cnd.where("id", "=", afterSaleId);
        if(Strings.isNotBlank(storeId)){
            cnd.and("storeId", "=", storeId);
        }
        Order_after_main afterMain = this.fetch(cnd);
        if (afterMain != null) {
            Integer state = afterMain.getAfterSaleState();
            afterMain.setAfterSaleStateName(OrderAfterStateEnum.getValue(state));
            afterMain.setApplyTypeName(OrderAfterTypeEnum.getValue(afterMain.getApplyType()));
            if (null != state) {
                nutMap.put("afterSaleState", OrderAfterStateEnum.getValue(state));
            }

            Integer type = afterMain.getApplyType();
            if (null != type) {
                nutMap.put("applyType",  OrderAfterTypeEnum.getValue(type));

            }
        }
        nutMap.put("afterMain", afterMain);
        //售后商品详情
        ArrayList<NutMap> goodsList = null;
        List<Order_after_detail> afterDetails = afterDetailService.query(Cnd.where("afterSaleId", "=", afterSaleId));

        if (afterDetails != null && afterDetails.size() > 0) {
            goodsList = new ArrayList<>();
            for (Order_after_detail detail : afterDetails) {
                String sku = detail.getSku();
                String orderGoodsId = detail.getOrderGoodsId();
                Order_goods orderGoods = orderGoodsService.fetch(Cnd.where("id", "=", orderGoodsId).and("sku", "=", sku));
                orderGoods.setImgUrl(goodsProductService.getProductImage(orderGoods.getSku()));
                String goodsId = orderGoods.getGoodsId();
                Goods_main goodsMain = goodsService.fetch(Cnd.where("id", "=", goodsId));
                NutMap goods = new NutMap();
                goods.put("goodsName", orderGoods.getGoodsName());//商品名称
                goods.put("buyPrice", orderGoods.getBuyPrice());//单件购买价
                goods.put("buyNum", orderGoods.getBuyNum());//购买数量
                goods.put("freeMoney", orderGoods.getFreeMoney());//优惠金额
                goods.put("payMoney", orderGoods.getPayMoney());//应付金额
                goods.put("afterSaleNum", detail.getAfterSaleNum());//售后数量
                goods.put("imgUrl", orderGoods.getImgUrl());//货品图片
                goodsList.add(goods);
            }
        }
        nutMap.put("goodsList", goodsList);

        //若退款退货售后状态是买家已寄回则返回退货物流信息
        if(afterMain.getAfterSaleState().equals(OrderAfterStateEnum.BUYER_DELIVERY.getKey())){
            Order_after_returns orderAfterReturns = orderAfterReturnsService.fetch(Cnd.where("afterSaleId", "=", afterSaleId));
            if(null != orderAfterReturns)
                nutMap.put("orderAfterReturns", orderAfterReturns);
        }


        //订单详情
        Order_main _orderMain = orderMainService.fetch(afterMain.getOrderId());
            NutMap orderMain = new NutMap();
            orderMain.put("id", _orderMain.getId());
            orderMain.put("orderAt", _orderMain.getOrderAt());//下单时间
            orderMain.put("payAt", _orderMain.getPayAt());//支付时间
            orderMain.put("payStatus", _orderMain.getPayStatus());//支付状态
            orderMain.put("payStatusName", OrderPayStatusEnum.getValue(_orderMain.getPayStatus()));//支付状态
            orderMain.put("orderStatusName", OrderStatusEnum.getValue(_orderMain.getOrderStatus()));//订单状态
            orderMain.put("payTypeName", OrderPayTypeEnum.getValue(_orderMain.getPayType()));//支付方式
            orderMain.put("payName", _orderMain.getPayName());//网上支付名称
            orderMain.put("accountId", _orderMain.getAccountId());//会员账户
            orderMain.put("getStatus", _orderMain.getGetStatus());//收货状态
            orderMain.put("getStatusName", OrderGetStatusEnum.getValue(_orderMain.getGetStatus()));//收货状态名称
            orderMain.put("freightMoney", _orderMain.getFreightMoney());//订单运费
            orderMain.put("payMoney", _orderMain.getPayMoney());//订单支付金额
        nutMap.put("orderMain", orderMain);

        List<Order_after_log> afterLogs = afterLogService.query(Cnd.where("afterSaleId", "=", afterSaleId));
        if (afterLogs != null && afterLogs.size() > 0) {
            for (Order_after_log afterLog : afterLogs) {
                Integer handleType = afterLog.getHandleType();
                if (null != handleType) {
                    afterLog.setHandleTypeName(OrderAfterHandleTypeEnum.getValue(handleType));
                }
                String stateName = OrderAfterStateEnum.getValue(afterLog.getAfterSaleState());
                afterLog.setAfterSaleStateName(stateName);
            }
        }
        nutMap.put("afterLogs", afterLogs);

        return nutMap;
    }

    /**
     * 卖家处理
     *
     * @param afterSaleId 售后单号
     * @param applyType   申请类型（0仅退款、1退货退款）
     * @param flag        表示位
     * @param note        卖家填写的备注说明
     * @param vouchers    凭证
     * @return
     */
    @Override
    @Transactional
    public void sellerHandle(String afterSaleId, int applyType, int flag, int returnMoney, String note, String[] vouchers, String opBy, Integer handleType, String handleMan) {
        int opAt = (int) (System.currentTimeMillis() / 1000);

        // 添加日志
        Order_after_log afterLog = new Order_after_log();
        afterLog.setOpAt(opAt);
        afterLog.setOpBy(opBy);
        afterLog.setHandleTime(Integer.valueOf(opAt));
        afterLog.setHandleType(handleType);
        afterLog.setHandleMan(handleMan);
        afterLog.setAfterSaleId(afterSaleId);

        for (int i = 0; i < vouchers.length; i++) {
            if (i > 2) {
                break;
            }
            switch (i + 1) {
                case 1:
                    afterLog.setEvidence1(vouchers[i]);
                    break;
                case 2:
                    afterLog.setEvidence2(vouchers[i]);
                    break;
                case 3:
                    afterLog.setEvidence3(vouchers[i]);
                    break;
                default:
                    break;
            }
        }

        if (!Strings.isEmpty(note)) {
            afterLog.setDescription(note);
        }

        Chain chain = Chain.make("opAt", opAt).add("opBy", opBy);
        if(0 != returnMoney){
            chain.add("returnMoney", returnMoney);
        }
        Cnd cnd = Cnd.where("id", "=", afterSaleId);

        Order_after_main afterMain = this.fetch(afterSaleId);
        //默认同意
        Integer stateKey = 0;
        switch (flag) {
            case 1:
                if (applyType == OrderAfterTypeEnum.ONLY_REFUNDMENT.getKey()) {
                    // 卖家同意后提交财务审核退款
                    stateKey = OrderAfterStateEnum.WAIT_FINANCE_CHECK.getKey();
                    //如果是仅退款，当双方协议达成，生成退款单，即向退款表插入一条记录
                    Order_after_refundment refundment = new Order_after_refundment();
                    refundment.setAfterSaleId(afterSaleId);
                    refundment.setOpBy(opBy);
                    // 设置退款状态为等待退款
                    refundment.setRefundState(OrderAfterRefundStateEnum.REFUND_WAIT.getKey());
                    if (afterMain != null) {
                        refundment.setStoreId(afterMain.getStoreId());
                        refundment.setApplyMan(afterMain.getApplyMan());
                        refundment.setApplyTime(afterMain.getApplyTime());
                        refundment.setOrderId(afterMain.getOrderId());
                        refundment.setRefundReason(afterMain.getReturnReason());
                        if(0 != returnMoney){
                            refundment.setRefundMoney(returnMoney);
                        }
                    }
                    refundmentService.insert(refundment);
                }else if(applyType == OrderAfterTypeEnum.REFUNDMENT_AND_GOODS.getKey()){
                    // 卖家同意退货，则更新状态为等待买家寄回
                    stateKey = OrderAfterStateEnum.WAIT_BUYER_SEND_BACK.getKey();
                    //如果是退款退货，当同意时，生成退货单，向退货表中插入一条数据
                    Order_after_returns oaReturns = new Order_after_returns();
                    oaReturns.setOpBy(opBy);
                    oaReturns.setAfterSaleId(afterSaleId);
                    oaReturns.setReturnsManId(afterMain.getApplyManId());
                    oaReturns.setReturnsMan(afterMain.getApplyMan());
                    orderAfterReturnsService.insert(oaReturns);
                }
                break;
            case 2:
                // 卖家拒绝
                stateKey = OrderAfterStateEnum.SELLER_REFUSE.getKey();
                break;
            case 3:
                //卖家验货通过
                stateKey = OrderAfterStateEnum.WAIT_FINANCE_CHECK.getKey();
                if (applyType == OrderAfterTypeEnum.REFUNDMENT_AND_GOODS.getKey()) {
                    //如果是退款退货，并且验货通过，则生成退款单，向退款表中插入一条数据
                    Order_after_refundment refundment = new Order_after_refundment();
                    refundment.setAfterSaleId(afterSaleId);
                    refundment.setOpBy(opBy);
                    // 设置退款状态为等待退款
                    refundment.setRefundState(OrderAfterRefundStateEnum.REFUND_WAIT.getKey());
                    if (afterMain != null) {
                        refundment.setStoreId(afterMain.getStoreId());
                        refundment.setApplyMan(afterMain.getApplyMan());
                        refundment.setApplyTime(afterMain.getApplyTime());
                        refundment.setOrderId(afterMain.getOrderId());
                        refundment.setRefundReason(afterMain.getReturnReason());
                        if(0 != returnMoney){
                            refundment.setRefundMoney(returnMoney);
                        }
                    }

                    refundmentService.insert(refundment);
                }
                break;
            case 4:
                //卖家验货不通过
                stateKey = OrderAfterStateEnum.SELLER_RECEIVE_REFUSE.getKey();
                break;
            default:
                break;
        }
        chain.add("afterSaleState", stateKey);
        this.update(chain, cnd);
        afterLog.setAfterSaleState(stateKey);
        afterLogService.insert(afterLog);
    }

    /**
     * 平台处理
     *
     * @param afterSaleId 售后单号
     * @param flag 自定义标识，用于区分具体的处理操作
     * @param note 卖家填写的备注说明
     * @param vouchers 凭证
     * @return
     */
    @Override
    @Transactional
    public void platformHandle(String afterSaleId, int applyType, int flag, String note, String[] vouchers) {
        // TODO: 2017/6/21
    }

    /**
     * 创建售后申请
     *
     * @param orderAfterMain 售后申请主表
     * @param orderAfterDetail 售后申请商品详情
     */
    @Override
    @Transactional
    public void addApplyOrderAfter(Order_after_main orderAfterMain, Order_after_detail orderAfterDetail) {
        //查询订单和订单货品详情
        Order_main orderMain = orderMainService.fetch(orderAfterMain.getOrderId());
        Order_goods orderGoods = orderGoodsService.fetch(orderAfterDetail.getOrderGoodsId());
        //查询会员信息
        String accountId = orderMain.getAccountId();
        Account_info accountInfo = accountInfoService.fetch(accountId);
        Account_user accountUser = accountUserService.fetch(Cnd.where("accountId", "=", accountId));
        //查询店铺信息
        Store_main storeMain = storeMainService.fetch(orderMain.getStoreId());

        //完善售后申请主表信息
        orderAfterMain.setApplyTime((int) (System.currentTimeMillis() / 1000));
        orderAfterMain.setApplyMan(StringUtil.getMemberUsername());
        orderAfterMain.setApplyManId(accountInfo.getId());
        orderAfterMain.setStoreId(orderMain.getStoreId());
        orderAfterMain.setStoreName(storeMain.getStoreName());
        orderAfterMain.setAfterSaleState(OrderAfterStateEnum.WAIT_SELLER_TREAT.getKey());
        orderAfterMain.setApplyManMobile(accountUser.getMobile());
        Order_after_main orderAfterMainData = this.insert(orderAfterMain);

        //完善售后申请详情信息
        orderAfterDetail.setAfterSaleId(orderAfterMainData.getId());
        orderAfterDetail.setGoodsId(orderGoods.getGoodsId());
        orderAfterDetail.setSku(orderGoods.getSku());
        orderAfterDetailService.insert(orderAfterDetail);

        //售后日志
        Order_after_log orderAfterLog = new Order_after_log();
        orderAfterLog.setAfterSaleId(orderAfterMain.getId());
        orderAfterLog.setDescription(OrderAfterTypeEnum.getValue(orderAfterMain.getApplyType()) + "," + orderAfterMain.getDescription());
        orderAfterLog.setHandleTime((int) (System.currentTimeMillis() / 1000));
        orderAfterLog.setHandleMan(StringUtil.getMemberUsername());
        orderAfterLog.setHandleType(OrderAfterHandleTypeEnum.MEMBER.getKey());
        orderAfterLog.setAfterSaleState(orderAfterMain.getAfterSaleState());
        if(Strings.isNotBlank(orderAfterMain.getEvidence1())){
            orderAfterLog.setEvidence1(orderAfterMain.getEvidence1());
        }
        if(Strings.isNotBlank(orderAfterMain.getEvidence2())){
            orderAfterLog.setEvidence2(orderAfterMain.getEvidence2());
        }
        if(Strings.isNotBlank(orderAfterMain.getEvidence3())){
            orderAfterLog.setEvidence3(orderAfterMain.getEvidence3());
        }
        afterLogService.insert(orderAfterLog);

    }

    /**
     * 取消售后
     *
     * @param afterSaleId 售后申请单号
     * @param cancelReason 取消售后原因
     */
    @Override
    @Transactional
    public void cancelOrderAfter(String afterSaleId, String cancelReason) {
        int opAt = (int) (System.currentTimeMillis() / 1000);
        String opBy = StringUtil.getMemberUid();
        //售后申请单
        Order_after_main orderAfterMain = this.fetch(afterSaleId);
        Order_after_refundment refundment = refundmentService.fetch(Cnd.where("afterSaleId", "=", afterSaleId));

        //更新售后申请主表售后状态
        Chain mainChain = Chain.make("opAt", opAt).add("opBy", opBy).add("afterSaleState", OrderAfterStateEnum.AFTER_SALE_CANCEL.getKey());
        Cnd mainCnd = Cnd.where("id", "=", afterSaleId);
        this.update(mainChain, mainCnd);

        //若退款单已生成则更新退款单退款状态
        if(null != refundment){
            Chain refundmentChain = Chain.make("opAt", opAt).add("opBy", opBy).add("refundState", OrderAfterRefundStateEnum.REFUND_CLOSE.getKey());
            Cnd refundmentCnd = Cnd.where("id","=", refundment.getId());
            refundmentService.update(refundmentChain, refundmentCnd);
        }

        //添加日志
        Order_after_log afterLog = new Order_after_log();
        afterLog.setOpAt(opAt);
        afterLog.setOpBy(StringUtil.getMemberUid());
        afterLog.setHandleTime(opAt);
        afterLog.setHandleType(OrderAfterHandleTypeEnum.MEMBER.getKey());
        afterLog.setHandleMan(StringUtil.getMemberUsername());
        afterLog.setAfterSaleId(orderAfterMain.getId());
        afterLog.setDescription(Message.getMessage("order.after.enum.state.afterSaleCancel") + cancelReason);
        afterLog.setAfterSaleState(OrderAfterStateEnum.AFTER_SALE_CANCEL.getKey());
        afterLogService.insert(afterLog);
    }

    /**
     * 检查是否可以申请售后
     *
     * @param orderId 订单id
     * @param orderGoodsId 订单商品详情id
     * @return true 可以申请，false不可申请
     */
    @Override
    public NutMap checkCanApply(String orderId, String orderGoodsId) {
        NutMap nutMap = new NutMap();
        String accountId = StringUtil.getMemberUid();
        Order_main orderMain = orderMainService.fetch(Cnd.where("accountId", "=", accountId).and("id", "=", orderId));
        if(null == orderMain){
            //订单不存在
            nutMap.put("code", 1);
            nutMap.put("result", Result.error("order.after.result.orderInexistenceError"));
            return nutMap;
        }
        Integer orderStatus = orderMain.getOrderStatus();//订单状态
        Integer deliveryStatus = orderMain.getDeliveryStatus();//发货状态
        Integer getStatus = orderMain.getGetStatus();//收货状态
        if( !((orderStatus == 1 && deliveryStatus == 3)||(orderStatus == 1 && getStatus == 1)) ){
            // TODO: 2017/6/27 补充订单不可申请的条件
            nutMap.put("code", 1);
            nutMap.put("result", Result.error("order.after.result.orderCanNotApplyError"));
            return nutMap;
        }
        List<Order_after_detail> orderAfterDetails = afterDetailService.query(Cnd.where("orderGoodsId", "=", orderGoodsId));
        if(orderAfterDetails.size() == 0){
            //未申请过
            nutMap.put("code", 0);
            return nutMap;
        }
        List<String> afterSaleIds = orderAfterDetails.stream().map(e -> e.getAfterSaleId()).collect(Collectors.toList());
        List<Order_after_main> orderAfterMains = this.query(Cnd.where("id", "in", afterSaleIds).and("orderId", "=", orderId));
        int count = 0;
        for(Order_after_main o :orderAfterMains){
            Integer state = o.getAfterSaleState();
            // 不同意、财务审核不通过、售后关闭、售后已取消 可再次申请
            if(state == 2 || state == 5 || state == 11 || state == 12){
                count++;
            }
        }
        if(count == orderAfterMains.size()){
            nutMap.put("code", 0);
            return nutMap;
        }
        nutMap.put("code", 1);
        nutMap.put("result", Result.error("order.after.result.stateDoingError"));
        return nutMap;
    }

}
