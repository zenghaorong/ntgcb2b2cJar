package com.aebiz.app.store.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.store.modules.models.Store_feedback;

public interface StoreFeedbackService extends BaseService<Store_feedback>{
    /**
     * 根据店铺id获取店铺的评分
     * @param storeUuid
     * @return
     */
    Store_feedback getAverageScore(String storeUuid);
}
