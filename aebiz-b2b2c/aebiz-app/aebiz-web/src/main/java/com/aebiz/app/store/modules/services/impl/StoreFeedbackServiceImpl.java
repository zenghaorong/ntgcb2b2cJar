package com.aebiz.app.store.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.store.modules.models.Store_feedback;
import com.aebiz.app.store.modules.services.StoreFeedbackService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StoreFeedbackServiceImpl extends BaseServiceImpl<Store_feedback> implements StoreFeedbackService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    /**
     * 根据商户id获取店铺评分
     * @param storeUuid
     * @return
     */
    public  Store_feedback getAverageScore(String storeUuid){
        return  this.fetch(Cnd.where("storeId","=",storeUuid));
    }
}
