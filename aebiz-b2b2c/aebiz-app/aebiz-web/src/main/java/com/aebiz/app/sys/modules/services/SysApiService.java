package com.aebiz.app.sys.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.sys.modules.models.Sys_api;

import java.io.IOException;
import java.util.Date;

public interface SysApiService extends BaseService<Sys_api> {
    String generateToken(Date date, String appId)throws IOException, ClassNotFoundException;

    boolean verifyToken(String appId, String token);
}
