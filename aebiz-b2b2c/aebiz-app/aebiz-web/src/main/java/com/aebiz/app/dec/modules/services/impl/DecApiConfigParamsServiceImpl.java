package com.aebiz.app.dec.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.dec.modules.models.Dec_api_config_params;
import com.aebiz.app.dec.modules.services.DecApiConfigParamsService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class DecApiConfigParamsServiceImpl extends BaseServiceImpl<Dec_api_config_params> implements DecApiConfigParamsService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    //根据接口uuid删除接口数据
    public void deleteModel(String interfaceUuid){
        List<Dec_api_config_params> list=this.query(Cnd.where("interfaceUuid","=",interfaceUuid));
        if(list !=null && list.size()>0){
            for(int i=0;i<list.size();i++){
                Dec_api_config_params dec=list.get(i);
                super.delete(dec.getId());
            }
        }
    }
}
