package com.aebiz.app.sys.modules.services;

import com.aebiz.app.sys.modules.models.Sys_log;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface SysLogService extends BaseService<Sys_log> {

    void dropLogTable();
}
