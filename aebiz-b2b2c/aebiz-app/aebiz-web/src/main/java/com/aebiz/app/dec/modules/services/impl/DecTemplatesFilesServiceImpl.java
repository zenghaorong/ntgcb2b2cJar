package com.aebiz.app.dec.modules.services.impl;

import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.commons.utils.TreeElement;
import com.aebiz.app.dec.modules.models.Dec_templates_resource;
import com.aebiz.app.dec.modules.services.DecTemplatesPagesService;
import com.aebiz.app.dec.modules.services.DecTemplatesResourceService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.dec.modules.models.Dec_templates_files;
import com.aebiz.app.dec.modules.services.DecTemplatesFilesService;
import com.aebiz.baseframework.shiro.filter.AebizShiroFilter;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DecTemplatesFilesServiceImpl extends BaseServiceImpl<Dec_templates_files> implements DecTemplatesFilesService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    @Autowired
    DecTemplatesPagesService pagesService;
    @Autowired
    DecTemplatesResourceService decTemplatesResourceService;

    private static final Logger log = LoggerFactory.getLogger(AebizShiroFilter.class);
    /**
     * 新建模板页面文件
     */
    @Override
    public void createTemplatePageFile(String templateUuid, String pageUuid, String pageFileName) {
        Dec_templates_files m = new Dec_templates_files();
        m.setFileType(DecorateCommonConstant.TEMPLAE_FILE_TYPE_HTML);
        m.setTemplateUuid(templateUuid);
        m.setPageUuid(pageUuid);
        m.setShowName(pageFileName);
        m.setParentUuid("");
        this.insert(m);
    }

    /**
     * 根据父文件夹uuid得到树
     */
    @Override
    public List<TreeElement> getRootList(String parenUuid, String templateUuid) {
        List<TreeElement> nodes = new ArrayList<TreeElement>();
        List<Dec_templates_files> list = this.getSubFoldersByParentUuid(parenUuid, templateUuid);
        if (list == null || list.size() == 0) {
            return nodes;
        }
        for (int i = 0; i < list.size(); i++) {
            Dec_templates_files model = list.get(i);
            TreeElement ele = new TreeElement();
            ele.setKey(model.getId());
            ele.setTitle(model.getShowName());
            ele.setNote(model.getNote());
            boolean subFolderExisted = this.checkSubFoldersByParentUuid(model.getId());
            if (subFolderExisted) {
                ele.setIsLazy(true);
            }
            ele.setIsFolder(true);
            ele.setExpand(false);
            nodes.add(ele);
        }
        if (Strings.isEmpty(parenUuid)) {
            // 获致模板下所有的页面文件
            List<Dec_templates_files> pageFileList = this.getTemplatePageFiles(templateUuid);
            if (pageFileList != null && pageFileList.size() > 0) {
                for (int i = 0; i < pageFileList.size(); i++) {
                    Dec_templates_files model = pageFileList.get(i);
                    TreeElement ele = new TreeElement();
                    ele.setKey(model.getPageUuid());
                    ele.setTitle(model.getShowName());
                    ele.setIsFolder(false);
                    ele.setExpand(false);
                    nodes.add(ele);
                }
            }
        }

        return nodes;
    }

    /**
     * 获取父文件夹下的所有子文件夹集合
     */
    @Override
    public List<Dec_templates_files> getSubFoldersByParentUuid(
            String parentUuid, String templateUuid) {
        List<Dec_templates_files> list  =this.query(Cnd.where("parentUuid","=",parentUuid).and("fileType","=",DecorateCommonConstant.TEMPLAE_FILE_TYPE_FOLDER).and("templateUuid","=",templateUuid));
        return list;
    }
    /**
     * 根据父文件夹uuid查询父文件夹下是否存在子文件夹
     *
     * @param parentUuid
     * @return
     */
    @Override
    public boolean checkSubFoldersByParentUuid(String parentUuid) {
       List<Dec_templates_files> mmList=this.query(Cnd.where("parentUuid","=",parentUuid));
        if (mmList != null && mmList.size() > 0) {
            return true;
        }

        return false;
    }

    /**
     * 获取模板下所有的页面文件集合
     */
    @Override
    public List<Dec_templates_files> getTemplatePageFiles(String templateUuid) {
        List<Dec_templates_files> list=this.query(Cnd.where("fileType","=",DecorateCommonConstant.TEMPLAE_FILE_TYPE_HTML).and("templateUuid","=",templateUuid));
        return list;
    }


    /**
     * 根据pageUuid删除对应的模板页面文件
     */
    @Override
    public void deletePageFileByPageUuid(String pageUuid) {
        if (Strings.isEmpty(pageUuid)) {
            return;
        }
        this.clear(Cnd.where("pageUuid","=",pageUuid));
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("pageUuid", pageUuid);
        pagesService.delete(pageUuid);
    }

    /**
     * 删除文件夹
     */
    @Override
    public void deleteFolder(String folderUuid) {
        Dec_templates_files model = this.fetch(folderUuid);

        // 获取文件夹下所有资源文件
        List<Dec_templates_resource> fileResourceList = decTemplatesResourceService
                .getFileResourcesByFolderUuid(folderUuid);

        if (fileResourceList != null && fileResourceList.size() > 0) {
            byte[][] resourceKeyBytesArr = new byte[fileResourceList.size()][];
            for (int i = 0; i < fileResourceList.size(); i++) {
                Dec_templates_resource resourceModel = fileResourceList.get(i);
                String resourceKey = resourceModel.getResourceKey();
                resourceKeyBytesArr[i] = resourceKey.getBytes();
            }
            // 删除redis里页面保存的资源信息
            decTemplatesResourceService.deleteResourcesOfRedis(resourceKeyBytesArr);
        }

        decTemplatesResourceService.deleteFileResources(folderUuid);

        this.delete(model.getId());

    }
    /**
     * 创建模板默认文件夹
     */
    @Override
    public void createTemplateSystemFolder(String templateUuid) {
        String[] folderNamesArr = new String[]{"images", "fonts", "css", "js"};
        for (int i = 0; i < folderNamesArr.length; i++) {
            String folderName = folderNamesArr[i];
            Dec_templates_files m = new Dec_templates_files();
            m.setFileType(DecorateCommonConstant.TEMPLAE_FILE_TYPE_FOLDER);
            m.setShowName(folderName);
            m.setTemplateUuid(templateUuid);
            m.setIsDefault(DecorateCommonConstant.TEMPLATE_FOLDER_DEFAULT_YES);
            m.setParentUuid("");
            if ("images".equals(folderName)) {
                m.setNote("图片文件夹");
            } else if ("fonts".equals(folderName)) {
                m.setNote("字体文件夹");
            } else if ("css".equals(folderName)) {
                m.setNote("样式文件夹");
            } else if ("js".equals(folderName)) {
                m.setNote("脚本文件夹");
            }

            this.insert(m);
        }
    }


    /**
            * 检查子文件夹名称是否重复
     */
    @Override
    public boolean checkSubFolderNameExisted(String parentUuid,
                                             String folderName){
        List<Dec_templates_files> list= this.query(Cnd.where("parentUuid","=",parentUuid).and("showName","=",folderName));
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 新建模板文件夹
     *
     * @param m
     */
    @Override
    public void createTemplateFolder(Dec_templates_files m) {
        m.setFileType(DecorateCommonConstant.TEMPLAE_FILE_TYPE_FOLDER);

        this.insert(m);
    }

    /**
     * 根据模板id删除文件
     *
     */

    public void deleteTemplateFilesByTemplateUuid(String id) {
        try {
            List<Dec_templates_files> filesList = this.query(Cnd.where("templateUuid", "=", id));
            if (filesList != null && filesList.size() > 0) {
                for (int i = 0; i < filesList.size(); i++) {
                    this.delete(filesList.get(i).getId());
                }
            }
        } catch (Exception e) {
            log.error(e + "");
        }
        List<String> resourceKeyList = decTemplatesResourceService.getTemplateResourceKeysByTemplateUuid(id);
        if (resourceKeyList != null && resourceKeyList.size() > 0) {
            byte[][] resourceKeyBytesArr = new byte[resourceKeyList.size()][];
            for (int i = 0; i < resourceKeyList.size(); i++) {
                String resoureKey = resourceKeyList.get(i);
                resourceKeyBytesArr[i] = resoureKey.getBytes();
            }
            decTemplatesResourceService.deleteResourcesOfRedis(resourceKeyBytesArr);

            decTemplatesResourceService.deleteTemplateFileResourcesByTemplateUuid(id);
        }
    }

    /**
     * 根据页面uuid删除文件
     */
    public void deleteFileByPageUuid(String pageUuid){
        String str="delete from Dec_templates_files where pageUuid =@pageUuid";
        Sql sql= Sqls.create(str).setParam("pageUuid",pageUuid);
        dao().execute(sql);
    }


    /**
     * 复制模板资源文件信息
     */
    @Override
    public void copyTemplateFileResources(String adoptTemplateUuid, String templateUuid) {
        List<Dec_templates_files> templateFolderList = this.getTemplateFiles(adoptTemplateUuid, DecorateCommonConstant.TEMPLAE_FILE_TYPE_FOLDER);

        if (templateFolderList != null && templateFolderList.size() > 0) {
            for (Dec_templates_files templateFolder : templateFolderList) {
                String folderName = templateFolder.getShowName();
                String[] ignoreProperties = new String[] { "uuid", "oper", "opeTime", "templateUuid" };
                Dec_templates_files m = new Dec_templates_files();
                m.setTemplateUuid(templateUuid);
                BeanUtils.copyProperties(templateFolder, m, ignoreProperties);

                this.insert(m);

                List<Dec_templates_resource> fileResourceList = decTemplatesResourceService
                        .getFileResourcesByFolderUuid(templateFolder.getId());
                if (fileResourceList != null && fileResourceList.size() > 0) {
                    for (Dec_templates_resource adoptFileResource : fileResourceList) {
                        String fileName = adoptFileResource.getFileName();
                        String adoptKey = adoptFileResource.getResourceKey();
                        String[] ignoreProps = new String[] { "uuid", "oper", "opeTime", "folderUuid", "resourceKey" };
                        Dec_templates_resource fileResource = new Dec_templates_resource();
                        String resourceKey = templateUuid + "_" + folderName + "_" + fileName;
                        fileResource.setFolderUuid(m.getId());
                        fileResource.setResourceKey(resourceKey);
                        BeanUtils.copyProperties(adoptFileResource, fileResource, ignoreProps);

                        decTemplatesResourceService.insert(fileResource);

                        this.copyFileResouceByKey(adoptKey, resourceKey);
                    }
                }
            }
        }
    }

    /**
     * 获取模板下所有的文件夹/页面文件集合
     */
    public List<Dec_templates_files> getTemplateFiles(String templateUuid, int fileType) {
        return this.query(Cnd.where("fileType","=",fileType).and("templateUuid","=",templateUuid));
    }
    /**
     * 拷贝一份新的资源数据
     *
     * @param adoptKey
     * @param key
     */
    private void copyFileResouceByKey(String adoptKey, String key) {
        byte[] fileBytes = decTemplatesResourceService.getRedisResoureBytesByKey(adoptKey);
        if (fileBytes != null) {
            decTemplatesResourceService.saveResourceBytesToRedis(key.getBytes(), fileBytes);
        }
    }
}
