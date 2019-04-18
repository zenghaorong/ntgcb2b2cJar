package com.aebiz.app.dec.modules.services.impl;

import com.aebiz.app.dec.modules.models.Dec_api_config_params;
import com.aebiz.app.dec.modules.services.DecApiConfigParamsService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.dec.modules.models.Dec_api;
import com.aebiz.app.dec.modules.services.DecApiService;
import com.aebiz.baseframework.page.OffsetPager;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DecApiServiceImpl extends BaseServiceImpl<Dec_api> implements DecApiService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    @Autowired
    private DecApiConfigParamsService decApiConfigParamsService;



    /**
     * DataTable Page
     *
     * @param length   页大小
     * @param start    start
     * @param draw     draw
     * @param orders   排序
     * @param columns  字段
     * @param cnd      查询条件
     * @param linkName 关联查询
     * @return
     */
    @Override
    public NutMap data(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName) {
        NutMap re = new NutMap();
        if (orders != null && orders.size() > 0) {
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
        }
        Pager pager = new OffsetPager(start, length);
        re.put("recordsFiltered", this.dao().count(this.getEntityClass(), cnd));
        List<Dec_api> list = this.dao().query(this.getEntityClass(), cnd, pager);
        if (!Strings.isBlank(linkName)) {
            this.dao().fetchLinks(list, linkName);
        }
            for (int i=0;i<list.size();i++){
                Dec_api obj= list.get(i);
                String id= obj.getId();
                List<Dec_api_config_params>  paramsList=decApiConfigParamsService.query(cnd.where("interfaceUuid","=",id).asc("opAt"));
                obj.setParamList(paramsList);
            }

        re.put("data", list);
        re.put("draw", draw);
        re.put("recordsTotal", length);
        return re;
    }
}
