package com.aebiz.app.sys.modules.services;

import com.aebiz.app.sys.modules.models.Sys_unit;
import com.aebiz.baseframework.base.service.BaseService;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface SysUnitService extends BaseService<Sys_unit> {
	void save(Sys_unit dict, String pid);

	void deleteAndChild(Sys_unit dict);
}
