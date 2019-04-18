package com.aebiz.app.dec.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.dec.modules.models.Dec_templates_sub;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;

import java.util.List;

public interface DecTemplatesSubService extends BaseService<Dec_templates_sub>{
    public NutMap data(String pageUuid, int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName);

    /**
     * 获取当前正在使用的页面保存的key和版本号
     */
    public Dec_templates_sub getResourceOfCurrentPage(String pageUuid);
    /**
     * 获取当前正在使用的页面保存的key和版本号
     */
    public Dec_templates_sub getResourceArrOfCurrentPage(String pageUuid);
    /**
     * 从缓存中获取当前正在使用的页面保存的key和版本号
     */
    public Dec_templates_sub getCurrentUsingPageFromCache(String pageUuid);
    /**
     * 根据页面id(pageUuid)获取版本信息
     */
    public List<Dec_templates_sub> getSubPagesByPageUuid(String pageUuid);

    /**
     * 切换页面版本
     */
    public void switchPageVersion(String pageUuid, String subPageUuid);

    /**
     * 将页面保存为新版本
     */
    public void saveAsNewVersion(String templateUuid, String pageUuid, String versionDescribe);

    /**
     * 获取模板下所有页面资源key集合
     */
    public List<String> getSubPageResourceKeysByTemplateUuid(String templateUuid);


    /**
     * 删除模板下的所有页面资源
     *
     * @param templateUuid
     */
    public void deleteSubPagesByTemplateUuid(String templateUuid);
    /**
     * 根据对应的page页面的id去删除版本号
     *
     * @param
     */
    public void deleteByPageUuid(String pageUuid);
}


