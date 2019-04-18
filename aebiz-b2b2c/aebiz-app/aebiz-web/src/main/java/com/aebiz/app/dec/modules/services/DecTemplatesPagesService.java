package com.aebiz.app.dec.modules.services;

import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.dec.modules.models.Dec_templates_sub;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.dec.modules.models.Dec_templates_pages;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;

import java.util.List;

public interface DecTemplatesPagesService extends BaseService<Dec_templates_pages>{
    public NutMap data(String id,int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName);
    /**
     * 验证模板下的文件名称是否重复
     *
     * @param pageFileName
     * @return
     */
    public boolean checkTemplatePageFileName(String templateUuid, String pageFileName);
    public String[] getPageRedisResourceByPageUuid(String pageUuid, String[] preKeyArr);
    public void createTemplatePages(String templateUuid);
    public Dec_templates_sub addPage(Dec_templates_pages m);
    public String getRedisResourceByKey(String resourceKey, boolean flag);

    /**
     * 保存为新页面
     */
    public String saveAsNewPage(Dec_templates_pages m, String pageUuid);
    /**
     * 将PageModel序列化后存入redis
     */
    public void savePageModelBytesToRedis(byte[] resourceKey, byte[] resourceBytes);
    /**
     * 保存设计器页面
     */
    public void saveDesingerPage(List<String[]> resourceArrList);

    public WebPageModel getPageModelByKey(String key);
    /**
     * 根据页面key获取redis里保存的页面资源信息
     * @return
     */
    public String[] getPageRedisResourceByResourceKey(String key, String[] preKeyArr);

    /**
     * 根据子页面保存的资源key复制资源信息
     */
    public void saveResourceToRedisByKeys(String originalResourceKey, String newResourceKey);

    /**
     * 根据模板id删除该模板下面的所有页面
     * @param id
     */
    public void deletePagesByTemplateUuid(String id);


    /**
     * 根据页面uuid去删除对应的版本和文件、redis信息
     */
    public void deleteAllByPage(String id);

    /**
     * 根据页面请求过来的模板id和页面文件名称去找页面id
     */
    public String getPageId(String templateUuid,String pageFileName);
}

