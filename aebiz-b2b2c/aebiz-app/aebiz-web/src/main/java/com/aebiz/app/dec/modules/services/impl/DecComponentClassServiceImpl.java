package com.aebiz.app.dec.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.dec.modules.models.Dec_component_class;
import com.aebiz.app.dec.modules.services.DecComponentClassService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DecComponentClassServiceImpl extends BaseServiceImpl<Dec_component_class> implements DecComponentClassService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
