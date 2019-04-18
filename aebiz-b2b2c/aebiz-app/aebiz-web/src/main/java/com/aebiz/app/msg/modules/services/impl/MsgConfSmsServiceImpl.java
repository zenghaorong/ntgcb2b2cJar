package com.aebiz.app.msg.modules.services.impl;

import com.aebiz.app.msg.modules.models.Msg_conf_sms_tpl;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.msg.modules.models.Msg_conf_sms;
import com.aebiz.app.msg.modules.services.MsgConfSmsService;

import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.springframework.cache.annotation.CacheConfig;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "msgCache")
public class MsgConfSmsServiceImpl extends BaseServiceImpl<Msg_conf_sms> implements MsgConfSmsService {

    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Override
    @Transactional
    public Msg_conf_sms insertWith(Msg_conf_sms sms, String many) {
        return dao().insertWith(sms, many);
    }

    @Override
    @Transactional
    public void save(Msg_conf_sms conf) {
        conf.setOpBy(StringUtil.getUid());
        conf.setOpAt((int) (System.currentTimeMillis() / 1000));
        this.updateIgnoreNull(conf);
        if (conf.isHasTpl()) {
            List<String> ids = new LinkedList<>();
            for (Msg_conf_sms_tpl tpl : conf.getSmsTpls()) {
                if(Strings.isNotBlank(tpl.getId())){
                    dao().updateIgnoreNull(tpl);
                } else {
                    tpl.setSmsId(conf.getId());
                    dao().insert(tpl);
                }
                ids.add(tpl.getId());
            }

            Cnd cnd = Cnd.where("smsId", "=", conf.getId());
            if (ids.size() > 0) {
                cnd.and("id","not in",ids);
            }
            dao().clear(Msg_conf_sms_tpl.class, cnd);
        }
    }

    @Override
    @Transactional
    public void deleteWith(String id, String many) {
        dao().deleteWith(this.fetch(id), many);
    }
}
