package com.aebiz.app.goods.modules.services.impl;

import com.aebiz.app.goods.modules.models.em.GoodsCheckStatusEnum;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.goods.modules.models.Goods_main_checklog;
import com.aebiz.app.goods.modules.services.GoodsMainChecklogService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GoodsMainChecklogServiceImpl extends BaseServiceImpl<Goods_main_checklog> implements GoodsMainChecklogService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Override
    public void addCheckLog(String goodsId, Integer checkStatus, String reason, String checkerId) {
        Goods_main_checklog checklog = new Goods_main_checklog();
        checklog.setGoodsId(goodsId);
        checklog.setOpType(1);//审核
        checklog.setCheckStatus(checkStatus);
        checklog.setReason(reason);
        checklog.setCheckerId(checkerId);
        checklog.setOpAt((int) (System.currentTimeMillis() / 1000));
        this.insert(checklog);
    }

    @Override
    public void addApplyLog(String goodsId, String storeId, String applicantId) {
        Goods_main_checklog checklog = new Goods_main_checklog();
        checklog.setGoodsId(goodsId);
        checklog.setOpType(0);//提交申请
        checklog.setCheckStatus(GoodsCheckStatusEnum.WAIT.getKey());
        checklog.setReason("");
        checklog.setStoreId(storeId);
        checklog.setApplicantId(applicantId);
        checklog.setOpAt((int) (System.currentTimeMillis() / 1000));
        this.insert(checklog);
    }
}
