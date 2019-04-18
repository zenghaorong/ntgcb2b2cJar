package com.aebiz.app.wx.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.wx.modules.models.Wx_menu;

public interface WxMenuService extends BaseService<Wx_menu>{
    /**
     * 新增菜单
     * @param menu
     * @param parentId
     */
    void save(Wx_menu menu, String parentId);

    /**
     * 删除菜单
     * @param menu
     */
    void deleteAndChild(Wx_menu menu);
}
