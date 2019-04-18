package com.aebiz.app.dec.modules.services.impl;

import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.modules.models.Dec_templates_manager;
import com.aebiz.app.dec.modules.services.DecTemplatesManagerService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.dec.modules.models.Dec_templates_style;
import com.aebiz.app.dec.modules.services.DecTemplatesStyleService;
import com.aebiz.baseframework.page.OffsetPager;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.shiro.filter.AebizShiroFilter;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DecTemplatesStyleServiceImpl extends BaseServiceImpl<Dec_templates_style> implements DecTemplatesStyleService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    @Autowired
    DecTemplatesManagerService decTemplatesManagerService;

    private static final Logger log = LoggerFactory.getLogger(AebizShiroFilter.class);
    /**
     * DataTable Page
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
    public NutMap data(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName){
        NutMap re = new NutMap();
        if (orders != null && orders.size() > 0) {
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
        }
        Pager pager = new OffsetPager(start, length);
        re.put("recordsFiltered", this.dao().count(this.getEntityClass(), cnd));
        List<Dec_templates_style>  list= this.query(Cnd.NEW());
        if(list !=null && list.size()>0){
            for(int i=0;i<list.size();i++){
                Dec_templates_style styleModel=list.get(i);
                String templateUuid=styleModel.getTemplateUuid();
                Dec_templates_manager managerModel=decTemplatesManagerService.fetch(templateUuid);
                String templateZhName=managerModel.getTemplateZhName();
                styleModel.setTemplateUuid(templateZhName);
                list.set(i,styleModel);
            }
        }
        if (!Strings.isBlank(linkName)) {
            this.dao().fetchLinks(list, linkName);
        }
        re.put("data", list);
        re.put("draw", draw);
        re.put("recordsTotal", length);
        return re;
    }
    //根绝templateUuid获取皮肤列表
    public NutMap getData(String templateUuid,int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName){
        NutMap re = new NutMap();
        if (orders != null && orders.size() > 0) {
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
        }
        Pager pager = new OffsetPager(start, length);
        re.put("recordsFiltered", this.dao().count(this.getEntityClass(), cnd));
        List<Dec_templates_style>  list= this.query(cnd.where("templateUuid","=",templateUuid));
        if (!Strings.isBlank(linkName)) {
            this.dao().fetchLinks(list, linkName);
        }
        re.put("data", list);
        re.put("draw", draw);
        re.put("recordsTotal", length);
        return re;
    }
    /**
     * 获取当前正在使用的模板样式
     */
    @Override
    public Dec_templates_style getCurrentStyleOfTemplate(String templateUuid) {
        List<Dec_templates_style> styleList= this.query(Cnd.where("templateUuid","=",templateUuid).and("disabled","=", DecorateCommonConstant.DECORATE_USING_YES));
        if (styleList != null && styleList.size() > 0) {
            return styleList.get(0);
        }
        return null;
    }

    /**
     * 根据模板id去删除皮肤
     */
    public void deleteTemplateStylesByTemplateUuid(String id){
        try {
            List<Dec_templates_style> styleList=this.query(Cnd.where("templateUuid","=",id));
            if(styleList !=null && styleList.size()>0){
                for(int i=0;i<styleList.size();i++){
                    this.delete(styleList.get(i).getId());
                }
            }
        }catch (Exception e){
            log.error(e+"");
        }

    }

    /**
     * 拷贝模板下所有的皮肤
     */
    @Override
    public void copyTemplateStyles(String adoptTemlateUuid, String templateUuid) {
        List<Dec_templates_style> adoptStyleList = this.getTemplateSkinList(adoptTemlateUuid);

        List<Dec_templates_style> list = null;
        if (adoptStyleList != null && adoptStyleList.size() > 0) {
            list = new ArrayList<Dec_templates_style>();
            for (Dec_templates_style adoptStyleModel : adoptStyleList) {
                Dec_templates_style styleModel = new Dec_templates_style();
                styleModel.setTemplateUuid(templateUuid);
                list.add(styleModel);
            }
            //myDao.batchSave(list);
        }
    }

    /**
     * 获取模板所有皮肤集合
     */
    @Override
    public List<Dec_templates_style> getTemplateSkinList(
            String templateUuid) {
        return this.query(Cnd.where("templateUuid","=",templateUuid));
    }

}
