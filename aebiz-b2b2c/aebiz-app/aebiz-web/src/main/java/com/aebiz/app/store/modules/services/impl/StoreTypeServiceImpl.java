package com.aebiz.app.store.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.store.modules.models.Store_type;
import com.aebiz.app.store.modules.services.StoreTypeService;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class StoreTypeServiceImpl extends BaseServiceImpl<Store_type> implements StoreTypeService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    /**
     * 添加店铺类型,若是默认类型则更新其他类型默认值为false
     *
     * @param storeRole
     */
    @Transactional
    public void add(Store_type storeRole) {
        if (storeRole.isDefaultValue()) {
            this.update(Chain.make("defaultValue", false), Cnd.NEW());
        }
        this.insert(storeRole);
    }

    /**
     * 修改店铺类型,若是默认类型则更新其他类型默认值为false
     *
     * @param storeRole
     */
    @Transactional
    public void edit(Store_type storeRole) {
        storeRole.setOpBy(StringUtil.getUid());
        storeRole.setOpAt((int) (System.currentTimeMillis() / 1000));
        if (storeRole.isDefaultValue()) {
            this.update(Chain.make("defaultValue", false), Cnd.NEW());
        }
        this.updateIgnoreNull(storeRole);
    }

    public Store_type getStore_type() {
        return dao().fetch(Store_type.class,Cnd.where("defaultValue","=",1));
    }
}
