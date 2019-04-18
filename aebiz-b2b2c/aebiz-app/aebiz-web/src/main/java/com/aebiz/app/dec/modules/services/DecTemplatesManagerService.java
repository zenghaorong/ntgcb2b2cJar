package com.aebiz.app.dec.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.dec.modules.models.Dec_templates_manager;
import org.springframework.beans.BeanUtils;

import java.util.List;

public interface DecTemplatesManagerService extends BaseService<Dec_templates_manager>{
    /**
     * 创建模板的同时创建页面、版本、文件
     * @param manager
     */
    public boolean createAll(Dec_templates_manager manager);
    /**
     * 删除模板时同时删除对应的页面、版本、文件、以及redis数据
     */
    public boolean deleteAllByTemplateUuid(String id);

    /**
     *
     * 获取启用的模板的id
     */
    public String getIsUsingTemplate();
    /**
     * 套用现有模板
     */
    public void adoptTemplate(String templateUuid, Dec_templates_manager m);
}
