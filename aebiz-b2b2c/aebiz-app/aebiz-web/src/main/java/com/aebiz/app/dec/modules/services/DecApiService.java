package com.aebiz.app.dec.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.dec.modules.models.Dec_api;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;

import java.util.List;

public interface DecApiService extends BaseService<Dec_api>{
    public NutMap data(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName);



}
