package com.aebiz.app.store.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.store.modules.models.Store_menu;
import com.aebiz.app.store.modules.services.StoreMenuService;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StoreMenuServiceImpl extends BaseServiceImpl<Store_menu> implements StoreMenuService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    public Store_menu save(Store_menu sysMenu) {
        String path = "";
        String pid = sysMenu.getParentId();
        if (!Strings.isEmpty(pid)) {
            Store_menu tempParentMenu = this.fetch(pid);
            path = tempParentMenu.getPath();
        }
        sysMenu.setPath(getSubPath("store_menu", "path", path));
        sysMenu.setParentId(pid);
        Store_menu result = dao().insert(sysMenu);
        if (!Strings.isEmpty(pid)) {
            this.update(Chain.make("hasChildren", true), Cnd.where("id", "=", pid));
        }
        return result;
    }

    public Store_menu saveData(Store_menu sysMenu) {
        String path = "";
        String pid = sysMenu.getParentId();
        if (!Strings.isEmpty(pid)) {
            Store_menu tempParentMenu = this.fetch(pid);
            path = tempParentMenu.getPath();
        }
        sysMenu.setPath(getSubPath("store_menu", "path", path));
        sysMenu.setParentId(pid);
        return dao().insert(sysMenu);
    }

    /**
     * 级联删除菜单
     *
     * @param sysMenu
     */
    public void deleteAndChild(Store_menu sysMenu) {
        dao().execute(Sqls.create("delete from store_menu where path like @path").setParam("path", sysMenu.getPath() + "%"));
        dao().execute(Sqls.create("delete from store_role_menu where menuId=@id or menuId in(SELECT id FROM store_menu WHERE path like @path)").setParam("id", sysMenu.getId()).setParam("path", sysMenu.getPath() + "%"));
        if (!Strings.isEmpty(sysMenu.getParentId())) {
            int count = count(Cnd.where("parentId", "=", sysMenu.getParentId()));
            if (count < 1) {
                dao().execute(Sqls.create("update store_menu set hasChildren=0 where id=@pid").setParam("pid", sysMenu.getParentId()));
            }
        }
    }

    public Store_menu addChildData(String pid, String name, String permission,String system) {
        Store_menu tempParentMenu = this.fetch(pid);
        Store_menu sysMenu = new Store_menu();
        sysMenu.setParentId(tempParentMenu.getId());
        sysMenu.setName(name);
        sysMenu.setPath(getSubPath("store_menu", "path", tempParentMenu.getPath()));
        sysMenu.setPermission(permission);
        sysMenu.setSystem(system);
        sysMenu.setType("data");
        return dao().insert(sysMenu);
    }
}
