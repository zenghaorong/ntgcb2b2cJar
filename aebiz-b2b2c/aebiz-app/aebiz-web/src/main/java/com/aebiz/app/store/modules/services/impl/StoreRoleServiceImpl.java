package com.aebiz.app.store.modules.services.impl;

import com.aebiz.app.store.modules.models.Store_menu;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.store.modules.models.Store_role;
import com.aebiz.app.store.modules.services.StoreRoleService;
import com.aebiz.commons.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoreRoleServiceImpl extends BaseServiceImpl<Store_role> implements StoreRoleService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    /**
     * 添加店铺角色,若是默认角色则更新其他角色默认值为false
     *
     * @param storeRole
     */
    @Transactional
    public void add(Store_role storeRole) {
        storeRole.setCode(R.UU32());
        if (storeRole.isDefaultValue()) {
            this.update(Chain.make("defaultValue", false), Cnd.NEW());
        }
        this.insert(storeRole);
    }

    /**
     * 修改店铺角色,若是默认角色则更新其他角色默认值为false
     *
     * @param storeRole
     */
    @Transactional
    public void edit(Store_role storeRole) {
        storeRole.setOpBy(StringUtil.getUid());
        storeRole.setOpAt((int) (System.currentTimeMillis() / 1000));
        if (storeRole.isDefaultValue()) {
            this.update(Chain.make("defaultValue", false), Cnd.NEW());
        }
        this.updateIgnoreNull(storeRole);
    }

    /**
     * 删除店铺角色,删除关联表
     *
     * @param roleid
     */
    @Transactional
    public void del(String roleid) {
        this.dao().clear("store_user_role", Cnd.where("roleId", "=", roleid));
        this.dao().clear("store_role_menu", Cnd.where("roleId", "=", roleid));
        this.delete(roleid);
    }

    /**
     * 查询权限
     *
     * @param role
     * @return
     */
    public List<String> getPermissionNameList(Store_role role) {
        dao().fetchLinks(role, "menus");
        List<String> list = new ArrayList<String>();
        for (Store_menu menu : role.getMenus()) {
            if (!Strings.isEmpty(menu.getPermission())&&!menu.isDisabled()) {
                list.add(menu.getPermission());
            }
        }
        return list;
    }

    public List<Store_menu> getMenusAndButtons(String roleId) {
        Sql sql = Sqls.create("select distinct a.* from store_menu a,store_role_menu b where a.system='store' and a.id=b.menuId and" +
                " b.roleId=@roleId and a.disabled=0 order by a.location ASC,a.path asc");
        sql.params().set("roleId", roleId);
        Entity<Store_menu> entity = dao().getEntity(Store_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Store_menu.class);
    }

    public List<Store_menu> getDatas(String id) {
        if (!Strings.isBlank(id)) {
            Sql sql = Sqls.create("select distinct a.* from store_menu a,store_role_menu b where a.system='store' and a.id=b.menuId and" +
                    " b.roleId=@roleId and a.type='data' and  a.disabled=0 order by a.location ASC,a.path asc");
            sql.params().set("roleId", id);
            Entity<Store_menu> entity = dao().getEntity(Store_menu.class);
            sql.setEntity(entity);
            sql.setCallback(Sqls.callback.entities());
            dao().execute(sql);
            return sql.getList(Store_menu.class);
        }

        Sql sql = Sqls.create("select distinct a.* from store_menu a,store_role_menu b where a.system='store' and a.id=b.menuId and a.type='data' order by a.location ASC,a.path asc");
        Entity<Store_menu> entity = dao().getEntity(Store_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Store_menu.class);
    }

    @Override
    public void editMenuDo(String ids, String roleid) {
        String[] idss = StringUtils.split(ids, ",");
        this.dao().clear("store_role_menu", Cnd.where("roleid", "=", roleid));
        for (String s : idss) {
            if (!Strings.isEmpty(s)) {
                this.insert("store_role_menu", Chain.make("roleId", roleid).add("menuId", s));
            }
        }
    }

}
