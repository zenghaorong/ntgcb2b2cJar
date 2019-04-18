package com.aebiz.app.dec.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.dec.modules.models.Dec_api_class;
import com.aebiz.app.dec.modules.services.DecApiClassService;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DecApiClassServiceImpl extends BaseServiceImpl<Dec_api_class> implements DecApiClassService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
}
