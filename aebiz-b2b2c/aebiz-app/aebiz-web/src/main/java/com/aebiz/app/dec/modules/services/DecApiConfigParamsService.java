package com.aebiz.app.dec.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.dec.modules.models.Dec_api_config_params;
import org.nutz.dao.Dao;

import javax.annotation.Resource;


public interface DecApiConfigParamsService extends BaseService<Dec_api_config_params>{
    //根据接口uuid删除接口数据
    public void deleteModel(String interfaceUuid);

}
