package com.aebiz.app.msg.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.msg.modules.models.Msg_conf_email;
import com.aebiz.app.msg.modules.services.MsgConfEmailService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@CacheConfig(cacheNames = "msgCache")
public class MsgConfEmailServiceImpl extends BaseServiceImpl<Msg_conf_email> implements MsgConfEmailService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @CacheEvict(key = "#root.targetClass.getName()+'*'")
    @Async
    public void clearCache() {

    }

    @Cacheable
    public Msg_conf_email getConfEmail(){
        return this.fetch(Cnd.NEW().limit(1,1));
    }
}
