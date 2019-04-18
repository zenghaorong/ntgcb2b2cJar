package com.aebiz.app.shop.modules.services;

import java.util.List;
import java.util.Map;

import com.aebiz.app.shop.modules.models.Shop_area;
import com.aebiz.baseframework.base.service.BaseService;
import org.nutz.dao.entity.Record;

public interface ShopAreaService extends BaseService<Shop_area> {
    void clearCache();

    String getNameByCode(String code);

    String getNameById(String id);

    String getParentCode(String childCode);

    Map getArea(String code);

    Shop_area getByCode(String code);

    List<Shop_area> getAreaNodeList(String code);

    void save(Shop_area shoparea, String pid);

    void deleteAndChild(Shop_area shoparea);

    /**
     * 获取列表
     * @return
     */
    List<Shop_area> getShopAreaList();

}
