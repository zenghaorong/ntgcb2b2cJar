package com.aebiz.app.dec.modules.services.impl;

import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.modules.models.Dec_templates_pages;
import com.aebiz.app.dec.modules.services.*;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.dec.modules.models.Dec_templates_manager;
import com.mchange.v2.beans.BeansUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DecTemplatesManagerServiceImpl extends BaseServiceImpl<Dec_templates_manager> implements DecTemplatesManagerService {
   @Autowired
   private DecTemplatesManagerService decTemplatesManagerService;

    @Autowired
    private DecTemplatesPagesService decTemplatesPagesService;

    @Autowired
    private DecTemplatesFilesService decTemplatesFilesService;

    @Autowired
    private DecTemplatesStyleService decTemplatesStyleService;
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    /**
     * 创建模板的同时创建页面、版本、文件
     * @param manager
     */
    @Transactional
    public boolean createAll(Dec_templates_manager manager){
        try {
            //创建模板的同时创建三个页面和版本号
            decTemplatesManagerService.insert(manager);
            String templateUuid=manager.getId();
            decTemplatesPagesService.createTemplatePages(templateUuid);
            // 创建模板的同时会创建默认四个系统默认的文件夹（images、fonts、css、js）
            decTemplatesFilesService.createTemplateSystemFolder(templateUuid);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    /**
     * 删除模板时同时删除对应的页面、版本、文件、以及redis数据
     */
    @Transactional
    public boolean deleteAllByTemplateUuid(String id){
        try {
            this.delete(id);
            decTemplatesStyleService.deleteTemplateStylesByTemplateUuid(id);

            decTemplatesFilesService.deleteTemplateFilesByTemplateUuid(id);

            decTemplatesPagesService.deletePagesByTemplateUuid(id);
            return true;
        }catch (Exception e){
            return false;
        }

    }
    /**
     *
     * 获取启用的模板的id
     */
    public String getIsUsingTemplate(){
        Dec_templates_manager managerModel=this.fetch(Cnd.where("disabled","=", DecorateCommonConstant.DECORATE_USING_YES));
        String templateUuid="";
        if(managerModel !=null){
            templateUuid=managerModel.getId();
            return templateUuid;
        }
        return templateUuid;
    }

    /**
     * 套用现有模板
     */
    @Override
    public void adoptTemplate(String templateUuid, Dec_templates_manager m) {
        this.insert(m);
        // 查询要套用的模板下所有的页面
        // 创建模板的同时会创建默认四个系统默认的文件夹（images、fonts、css、js）
        decTemplatesFilesService.createTemplateSystemFolder(templateUuid);
        List<Dec_templates_pages> pageList = decTemplatesPagesService.query(Cnd.where("templateUuid","=",templateUuid));
        if (pageList != null && pageList.size() > 0) {
            for (Dec_templates_pages adoptPage : pageList) {
                Dec_templates_pages pagesModel = new Dec_templates_pages();
                String[] ignoreProperties = new String[] { "id", "opBy", "opAt", "templateUuid" };
                BeanUtils.copyProperties(adoptPage, pagesModel, ignoreProperties);
                // 新增模板页面
                decTemplatesPagesService.insert(pagesModel);
                // 保存新的页面资源信息
                decTemplatesPagesService.saveAsNewPage(pagesModel, adoptPage.getId());
            }
        }

        // 拷贝要套用的模板下的所有皮肤数据
        decTemplatesStyleService.copyTemplateStyles(templateUuid, m.getId());

        // 拷贝要套用的模板下的所有资源文件
        decTemplatesFilesService.copyTemplateFileResources(templateUuid, m.getId());
    }
}
