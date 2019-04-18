package com.aebiz.app.wx.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.wx.modules.models.Wx_menu;
import com.aebiz.app.wx.modules.services.WxMenuService;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WxMenuServiceImpl extends BaseServiceImpl<Wx_menu> implements WxMenuService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Override
    public void deleteAndChild(Wx_menu menu) {
        dao().execute(Sqls.create("delete from wx_menu where path like @path").setParam("path", menu.getPath() + "%"));
        if (!Strings.isBlank(menu.getParentId())) {
            int count = count(Cnd.where("parentId", "=", menu.getParentId()));
            if (count < 1) {
                dao().execute(Sqls.create("update wx_menu set hasChildren=0 where id=@pid").setParam("pid", menu.getParentId()));
            }
        }
    }

    @Override
    public void save(Wx_menu menu, String pid) {
        String path = "";

        if (!Strings.isEmpty(pid)) {
            Wx_menu pp = this.fetch(pid);
            path = pp.getPath();
        } else pid = "";
        menu.setPath(getSubPath("wx_menu", "path", path));
        menu.setParentId(pid);
        dao().insert(menu);
        if (!Strings.isEmpty(pid)) {
            this.update(Chain.make("hasChildren", true), Cnd.where("id", "=", pid));
        }
    }
}
