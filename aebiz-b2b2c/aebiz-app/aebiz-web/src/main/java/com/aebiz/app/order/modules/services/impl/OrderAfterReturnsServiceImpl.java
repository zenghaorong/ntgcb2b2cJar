package com.aebiz.app.order.modules.services.impl;

import com.aebiz.app.order.modules.models.Order_after_log;
import com.aebiz.app.order.modules.models.Order_after_main;
import com.aebiz.app.order.modules.models.em.OrderAfterHandleTypeEnum;
import com.aebiz.app.order.modules.models.em.OrderAfterStateEnum;
import com.aebiz.app.order.modules.services.OrderAfterLogService;
import com.aebiz.app.order.modules.services.OrderAfterMainService;
import com.aebiz.baseframework.base.Message;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.order.modules.models.Order_after_returns;
import com.aebiz.app.order.modules.services.OrderAfterReturnsService;
import com.aebiz.commons.utils.StringUtil;
import org.apache.logging.log4j.util.Strings;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class OrderAfterReturnsServiceImpl extends BaseServiceImpl<Order_after_returns> implements OrderAfterReturnsService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private OrderAfterMainService orderAfterMainService;

    @Autowired
    private OrderAfterLogService afterLogService;

    /**
     * 上传会员寄回商品物流信息
     *
     * @param afterSaleId 售后单号
     * @param logisticsCompany 物流公司
     * @param logisticsSheetId 物流单号
     * @param note 备注说明
     * @param vouchers 凭证图片
     */
    @Override
    @Transactional
    public void addRefundLogistics(String afterSaleId, String logisticsCompany, String logisticsSheetId, String note, String[] vouchers) {
        int opAt = (int) (System.currentTimeMillis() / 1000);
        String opBy = StringUtil.getMemberUid();
        //售后申请单
        Order_after_main orderAfterMain = orderAfterMainService.fetch(afterSaleId);
        Order_after_returns orderAfterReturns = this.fetch(Cnd.where("afterSaleId", "=", afterSaleId));

        //更新售后申请主表售后状态
        Chain mainChain = Chain.make("opAt", opAt).add("opBy", opBy).add("afterSaleState", OrderAfterStateEnum.BUYER_DELIVERY.getKey());
        Cnd mainCnd = Cnd.where("id", "=", afterSaleId);
        orderAfterMainService.update(mainChain, mainCnd);

        //更新退货单
        orderAfterReturns.setOpAt(opAt);
        orderAfterReturns.setOpBy(opBy);
        orderAfterReturns.setLogisticsCompany(logisticsCompany);
        orderAfterReturns.setLogisticsSheetId(logisticsSheetId);
        orderAfterReturns.setReturnsTime(opAt);
        orderAfterReturns.setNote(Message.getMessage("order.after.returns.member.sentGoods") + note);
        for (int i = 0; i < vouchers.length; i++) {
            if (i > 2) {
                break;
            }
            switch (i + 1) {
                case 1:
                    orderAfterReturns.setEvidence1(vouchers[i]);
                    break;
                case 2:
                    orderAfterReturns.setEvidence2(vouchers[i]);
                    break;
                case 3:
                    orderAfterReturns.setEvidence3(vouchers[i]);
                    break;
                default:
                    break;
            }
        }
        this.updateIgnoreNull(orderAfterReturns);


        //添加日志
        Order_after_log afterLog = new Order_after_log();
        afterLog.setOpAt(opAt);
        afterLog.setOpBy(StringUtil.getMemberUid());
        afterLog.setHandleTime(opAt);
        afterLog.setHandleType(OrderAfterHandleTypeEnum.MEMBER.getKey());
        afterLog.setHandleMan(StringUtil.getMemberUsername());
        afterLog.setAfterSaleId(orderAfterMain.getId());
        afterLog.setDescription(Message.getMessage("order.after.returns.member.sentGoods") + note);
        afterLog.setAfterSaleState(OrderAfterStateEnum.BUYER_DELIVERY.getKey());
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
        afterLogService.insert(afterLog);
    }


}
