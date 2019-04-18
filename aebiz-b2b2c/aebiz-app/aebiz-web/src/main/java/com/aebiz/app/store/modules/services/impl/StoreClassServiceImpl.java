package com.aebiz.app.store.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.store.modules.models.Store_class;
import com.aebiz.app.store.modules.services.StoreClassService;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StoreClassServiceImpl extends BaseServiceImpl<Store_class> implements StoreClassService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    /**
     * 新增分类
     *
     * @param c
     * @param pid
     */
    @Transactional
    public void save(Store_class c, String pid) {
        String path = "";
        if (!Strings.isEmpty(pid)) {
            Store_class pp = this.fetch(pid);
            path = pp.getPath();
        }
        c.setPath(getSubPath("store_class", "path", path));
        c.setParentId(pid);
        dao().insert(c);
        if (!Strings.isEmpty(pid)) {
            this.update(Chain.make("hasChildren", true), Cnd.where("id", "=", pid));
        }
    }

    /**
     * 级联删除分类
     *
     * @param c
     */
    @Transactional
    public void deleteAndChild(Store_class c) {
        dao().execute(Sqls.create("delete from store_class where path like @path").setParam("path", c.getPath() + "%"));
        if (!Strings.isEmpty(c.getParentId())) {
            int count = count(Cnd.where("parentId", "=", c.getParentId()));
            if (count < 1) {
                dao().execute(Sqls.create("update store_class set hasChildren=0 where id=@pid").setParam("pid", c.getParentId()));
            }
        }
    }

    /**
     * 获取分类的下拉选项
     * @return
     */
    public String getClassOption() {
        String option = "";
        List<Store_class> list = super.query(Cnd.where("parentId", "=", ""));
        for (Store_class store_class : list) {
            option = option + "<option value='"+store_class.getId()+"'>"+store_class.getName()+"(保证金："+store_class.getDeposit()+"元)</option>";
            if (store_class.isHasChildren()) {
                Store_class child = fetch(Cnd.where("parentId", "=", store_class.getId()));
                option = option + "&nbsp;&nbsp;&nbsp;&nbsp;" + "<option value='"+child.getId()+"'>&nbsp;&nbsp;&nbsp;&nbsp;"+child.getName()+"(保证金："+child.getDeposit()+"元)</option>";
            }
        }
        return option;
    }
}
