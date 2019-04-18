package com.aebiz.app.dec.modules.services;

import com.aebiz.app.dec.commons.utils.TreeElement;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.dec.modules.models.Dec_templates_files;

import java.util.List;

public interface DecTemplatesFilesService extends BaseService<Dec_templates_files>{
    public void createTemplatePageFile(String templateUuid, String pageUuid, String pageFileName);
    /**
     * 根据父文件夹uuid得到树
     *
     * @param parenUuid
     * @return
     */
    public List<TreeElement> getRootList(String parenUuid, String templateUuid);

    /**
     * 获取父文件夹下的所有子文件夹集合
     */
    public List<Dec_templates_files> getSubFoldersByParentUuid(
            String parentUuid, String templateUuid);
    /**
     * 根据父文件夹uuid查询父文件夹下是否存在子文件夹
     *
     * @param parentUuid
     * @return
     */
    public boolean checkSubFoldersByParentUuid(String parentUuid);
    /**
     * 获取模板下所有的页面文件集合
     */
    public List<Dec_templates_files> getTemplatePageFiles(String templateUuid);
    /**
     * 根据pageUuid删除对应的模板页面文件
     */
    public void deletePageFileByPageUuid(String pageUuid);
    /**
     * 删除文件夹
     */
    public void deleteFolder(String folderUuid);
    /**
     * 创建模板默认文件夹
     */
    public void createTemplateSystemFolder(String templateUuid);

    /**
     * 检查子文件夹名称是否重复
     */
    public boolean checkSubFolderNameExisted(String parentUuid,
                                             String folderName);

    /**
     * 新建模板文件夹
     *
     * @param m
     */
    public void createTemplateFolder(Dec_templates_files m);

    /**
     * 根据模板id删除文件
     *
     */

    public void deleteTemplateFilesByTemplateUuid(String id);

    /**
     * 根据页面uuid删除文件
     */
    public void deleteFileByPageUuid(String pageUuid);

    /**
     * 复制模板资源文件信息
     *
     * @param adoptTemplateUuid
     * @param templateUuid
     */
    public void copyTemplateFileResources(String adoptTemplateUuid, String templateUuid);
}
