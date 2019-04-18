package com.aebiz.app.sys.modules.services.impl;

import com.aebiz.app.sys.modules.models.Sys_menu;
import com.aebiz.app.sys.modules.services.SysMenuService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by wizzer on 2016/12/22.
 */
@Service
public class SysMenuServiceImpl extends BaseServiceImpl<Sys_menu> implements SysMenuService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    public Sys_menu save(Sys_menu sysMenu) {
        String path = "";
        String pid = sysMenu.getParentId();
        if (!Strings.isEmpty(pid)) {
            Sys_menu tempParentMenu = this.fetch(pid);
            path = tempParentMenu.getPath();
        }
        sysMenu.setPath(getSubPath("sys_menu", "path", path));
        sysMenu.setParentId(pid);
        Sys_menu result = dao().insert(sysMenu);
        if (!Strings.isEmpty(pid)) {
            this.update(Chain.make("hasChildren", true), Cnd.where("id", "=", pid));
        }
        return result;
    }

    public Sys_menu saveData(Sys_menu sysMenu) {
        String path = "";
        String pid = sysMenu.getParentId();
        if (!Strings.isEmpty(pid)) {
            Sys_menu tempParentMenu = this.fetch(pid);
            path = tempParentMenu.getPath();
        }
        sysMenu.setPath(getSubPath("sys_menu", "path", path));
        sysMenu.setParentId(pid);
        return dao().insert(sysMenu);
    }

    /**
     * 级联删除菜单
     *
     * @param sysMenu
     */
    public void deleteAndChild(Sys_menu sysMenu) {
        dao().execute(Sqls.create("delete from sys_menu where path like @path").setParam("path", sysMenu.getPath() + "%"));
        dao().execute(Sqls.create("delete from sys_role_menu where menuId=@id or menuId in(SELECT id FROM sys_menu WHERE path like @path)").setParam("id", sysMenu.getId()).setParam("path", sysMenu.getPath() + "%"));
        if (!Strings.isEmpty(sysMenu.getParentId())) {
            int count = count(Cnd.where("parentId", "=", sysMenu.getParentId()));
            if (count < 1) {
                dao().execute(Sqls.create("update sys_menu set hasChildren=0 where id=@pid").setParam("pid", sysMenu.getParentId()));
            }
        }
    }

    public Sys_menu addChildData(String pid, String name, String permission,String system) {
        Sys_menu tempParentMenu = this.fetch(pid);
        Sys_menu sysMenu = new Sys_menu();
        sysMenu.setParentId(tempParentMenu.getId());
        sysMenu.setName(name);
        sysMenu.setPath(getSubPath("sys_menu", "path", tempParentMenu.getPath()));
        sysMenu.setPermission(permission);
        sysMenu.setSystem(system);
        sysMenu.setType("data");
        return dao().insert(sysMenu);
    }
}
