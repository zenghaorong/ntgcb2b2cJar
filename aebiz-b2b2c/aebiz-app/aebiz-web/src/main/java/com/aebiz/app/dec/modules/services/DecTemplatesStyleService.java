package com.aebiz.app.dec.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.dec.modules.models.Dec_templates_style;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;

import java.util.List;

public interface DecTemplatesStyleService extends BaseService<Dec_templates_style>{

    public NutMap data(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName);
    //根绝templateUuid获取皮肤列表
    public NutMap getData(String templateUuid,int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName);

    public Dec_templates_style getCurrentStyleOfTemplate(String templateUuid);

    /**
     * 根据模板id去删除皮肤
     */
    public void deleteTemplateStylesByTemplateUuid(String id);


    /**
     * 拷贝模板下所有的皮肤
     *
     * @param adoptTemlateUuid
     * @param templateUuid
     */
    public void copyTemplateStyles(String adoptTemlateUuid, String templateUuid);

    /**
     * 获取模板所有皮肤集合
     */
    public List<Dec_templates_style> getTemplateSkinList(
            String templateUuid);
}
