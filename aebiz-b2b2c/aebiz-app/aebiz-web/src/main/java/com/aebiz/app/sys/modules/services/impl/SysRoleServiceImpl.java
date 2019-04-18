package com.aebiz.app.sys.modules.services.impl;

import com.aebiz.app.sys.modules.models.Sys_menu;
import com.aebiz.app.sys.modules.models.Sys_role;
import com.aebiz.app.sys.modules.services.SysRoleService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<Sys_role> implements SysRoleService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    /**
     * 查询权限
     *
     * @param role
     * @return
     */
    public List<String> getPermissionNameList(Sys_role role) {
        dao().fetchLinks(role, "menus");
        List<String> list = new ArrayList<String>();
        for (Sys_menu menu : role.getMenus()) {
            if (!Strings.isEmpty(menu.getPermission())&&!menu.isDisabled()) {
                list.add(menu.getPermission());
            }
        }
        return list;
    }

    public List<Sys_menu> getMenusAndButtons(String roleId) {
        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.system='platform' and a.id=b.menuId and" +
                " b.roleId=@roleId and a.disabled=0 order by a.location ASC,a.path asc");
        sql.params().set("roleId", roleId);
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_menu.class);
    }

    public List<Sys_menu> getDatas(String id) {
        if (!Strings.isBlank(id)) {
            Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.system='platform' and a.id=b.menuId and" +
                    " b.roleId=@roleId and a.type='data' and  a.disabled=0 order by a.location ASC,a.path asc");
            sql.params().set("roleId", id);
            Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
            sql.setEntity(entity);
            sql.setCallback(Sqls.callback.entities());
            dao().execute(sql);
            return sql.getList(Sys_menu.class);
        }

        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.system='platform' and a.id=b.menuId and a.type='data' order by a.location ASC,a.path asc");
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_menu.class);

    }


    @Transactional
    public void del(String roleid) {
        this.dao().clear("sys_user_role", Cnd.where("roleId", "=", roleid));
        this.dao().clear("sys_role_menu", Cnd.where("roleId", "=", roleid));
        this.delete(roleid);
    }

    @Transactional
    public void del(String[] roleids) {
        this.dao().clear("sys_user_role", Cnd.where("roleId", "in", roleids));
        this.dao().clear("sys_role_menu", Cnd.where("roleId", "in", roleids));
        this.delete(roleids);
    }
}
