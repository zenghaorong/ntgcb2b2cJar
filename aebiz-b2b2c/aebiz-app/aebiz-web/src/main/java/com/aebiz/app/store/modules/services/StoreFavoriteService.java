package com.aebiz.app.store.modules.services;

import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.store.modules.models.Store_favorite;

import java.util.Map;

/**
 * 店铺收藏Service
 */
public interface StoreFavoriteService extends BaseService<Store_favorite>{

    /**
     * 保存店铺收藏信息(调用方法前要先验证accountId、store_main对应的记录是否存在)
     * @param accountId 会员id
     * @param store_main 店铺
     * @return
     */
    Store_favorite saveData(String accountId, Store_main store_main);

    Map<String,Object> selectDataAll(String accountId, Integer page, Integer rows, Integer status);

    Map<String,Object> selectData(String content, String accountId, Integer page, Integer rows, Integer status);
}
