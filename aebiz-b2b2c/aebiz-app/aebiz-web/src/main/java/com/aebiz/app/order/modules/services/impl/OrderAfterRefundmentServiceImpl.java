package com.aebiz.app.order.modules.services.impl;

import com.aebiz.app.order.modules.models.Order_after_log;
import com.aebiz.app.order.modules.models.Order_after_main;
import com.aebiz.app.order.modules.models.Order_after_refundment;
import com.aebiz.app.order.modules.models.em.OrderAfterBackMoneyStyleEnum;
import com.aebiz.app.order.modules.models.em.OrderAfterHandleTypeEnum;
import com.aebiz.app.order.modules.models.em.OrderAfterRefundStateEnum;
import com.aebiz.app.order.modules.models.em.OrderAfterStateEnum;
import com.aebiz.app.order.modules.services.OrderAfterLogService;
import com.aebiz.app.order.modules.services.OrderAfterMainService;
import com.aebiz.app.order.modules.services.OrderAfterRefundmentService;
import com.aebiz.baseframework.base.Message;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.FormatFlagsConversionMismatchException;

@Service
public class OrderAfterRefundmentServiceImpl extends BaseServiceImpl<Order_after_refundment> implements OrderAfterRefundmentService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private OrderAfterLogService afterLogService;

    @Autowired
    private OrderAfterMainService orderAfterMainService;

    /**
     * 财务处理
     *
     * @param refundmentId 售后退款单号
     * @param flag 自定义标识，用于区分具体的处理操作
     * @param refundStyle 退款方式
     * @param note 卖家填写的备注说明
     * @param vouchers 凭证
     * @return
     */
    @Override
    @Transactional
    public void financeHandle(String refundmentId, int flag, String refundStyle, String note, String[] vouchers, String opBy, Integer handleType, String handleMan) {
        //退款单
        Order_after_refundment refundment = this.fetch(refundmentId);
        this.fetchLinks(refundment, "orderAfterMain");
        //售后申请单
        Order_after_main afterMain = refundment.getOrderAfterMain();

        int opAt = (int) (System.currentTimeMillis() / 1000);

        // 添加日志
        Order_after_log afterLog = new Order_after_log();
        afterLog.setOpAt(opAt);
        afterLog.setOpBy(opBy);
        afterLog.setHandleTime(opAt);
        afterLog.setHandleType(handleType);
        afterLog.setHandleMan(handleMan);
        afterLog.setAfterSaleId(afterMain.getId());
        switch (flag){
            case 1:
                note = Message.getMessage("order.after.refundment.finance.checkPass") + note;
                break;
            case 2:
                note = Message.getMessage("order.after.refundment.finance.checkNotPass") + note;
                break;
            case 3:
                note = Message.getMessage("order.after.refundment.finance.backMoneyOk") + note;
                break;
            default:
                break;
        }
        afterLog.setDescription(note);

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

        //售后申请主表更新
        Chain mainChain = Chain.make("opAt", opAt).add("opBy", opBy);
        //审核通过更新退款方式
        if(Strings.isNotBlank(refundStyle) && flag == OrderAfterBackMoneyStyleEnum.OFFLINE_TRANSFER.getKey()){
            mainChain.add("returnMoneyStyle", OrderAfterBackMoneyStyleEnum.getValue(Integer.parseInt(refundStyle)));
        }
        Cnd mainCnd = Cnd.where("id", "=", afterMain.getId());

        //售后退款单更新
        Chain refundmentChain = Chain.make("opAt", opAt).add("opBy", opBy).add("refundNote", note);
        if(Strings.isNotBlank(refundStyle) && flag == OrderAfterBackMoneyStyleEnum.OFFLINE_TRANSFER.getKey()){
            refundmentChain.add("refundStyle", Integer.parseInt(refundStyle))
                    .add("refundMoneyWay", Message.getMessage("order.after.refundment.refundMoneyWay.bankCard"));
        }
        //若确认打款更新退款时间
        if(flag == 3){
            refundmentChain.add("refundTime", opAt);
        }
        Cnd refundmentCnd = Cnd.where("id", "=", refundment.getId());

        //默认同意
        Integer stateKey = 0;
        Integer refundState = 0;
        switch (flag) {
            //财务审核通过
            case 1:
                //财务确认(待退款)
                if(Strings.isNotBlank(refundStyle) &&
                        OrderAfterBackMoneyStyleEnum.OFFLINE_TRANSFER.getKey() == Integer.parseInt(refundStyle)){
                    stateKey = OrderAfterStateEnum.WAIT_BUYER_ACCOUNT.getKey();
                }else{
                    stateKey = OrderAfterStateEnum.FINANCE_CONFIRM.getKey();
                }
                refundState = OrderAfterRefundStateEnum.REFUND_CONFIRM.getKey();
                break;
            //财务审核不通过
            case 2:
                //财务审核不通过
                stateKey = OrderAfterStateEnum.FINANCE_REFUSE.getKey();
                refundState = OrderAfterRefundStateEnum.REFUND_FAIL.getKey();
                break;
            //财务确认已打款
            case 3:
                //售后成功(已退款)
                stateKey = OrderAfterStateEnum.AFTER_SUCCESS.getKey();
                refundState = OrderAfterRefundStateEnum.REFUND_SUCCESS.getKey();
                break;
            default:
                break;
        }
        mainChain.add("afterSaleState", stateKey);
        //更新售后主表
        orderAfterMainService.update(mainChain, mainCnd);
        //退款单更新
        refundmentChain.add("refundState", refundState);
        this.update(refundmentChain, refundmentCnd);
        //插入日志
        afterLog.setAfterSaleState(stateKey);
        afterLogService.insert(afterLog);
    }

    /**
     * 填写售后退款银行卡信息
     *
     * @param afterSaleId
     * @param bankCard
     * @param bankName
     */
    @Override
    @Transactional
    public void addBankCardDo(String afterSaleId, String bankCard, String bankName, String name) {
        int opAt = (int) (System.currentTimeMillis() / 1000);
        String opBy = StringUtil.getMemberUid();
        //售后申请单
        Order_after_main orderAfterMain = orderAfterMainService.fetch(afterSaleId);
        Order_after_refundment refundment = this.fetch(Cnd.where("afterSaleId", "=", afterSaleId));

        //更新售后申请状态
        Chain mainchain = Chain.make("opAt", opAt).add("opBy", opBy).add("afterSaleState", OrderAfterStateEnum.FINANCE_CONFIRM.getKey());
        Cnd mainCnd = Cnd.where("id", "=", afterSaleId);
        orderAfterMainService.update(mainchain, mainCnd);

        //售后退款单更新
        Chain refundmentChain = Chain.make("opAt", opAt).add("opBy", opBy).add("refundMoneyAccount", bankCard).add("refundMoneyWay", bankName).add("name", name);
        Cnd refundmentCnd = Cnd.where("afterSaleId", "=", afterSaleId);
        this.update(refundmentChain, refundmentCnd);

        // 添加日志
        Order_after_log afterLog = new Order_after_log();
        afterLog.setOpAt(opAt);
        afterLog.setOpBy(StringUtil.getMemberUid());
        afterLog.setHandleTime(opAt);
        afterLog.setHandleType(OrderAfterHandleTypeEnum.MEMBER.getKey());
        afterLog.setHandleMan(StringUtil.getMemberUsername());
        afterLog.setAfterSaleId(afterSaleId);
        afterLog.setDescription(Message.getMessage("order.after.refundment.member.addBankCard"));
        afterLog.setAfterSaleState(OrderAfterStateEnum.FINANCE_CONFIRM.getKey());
        afterLogService.insert(afterLog);
    }

}
