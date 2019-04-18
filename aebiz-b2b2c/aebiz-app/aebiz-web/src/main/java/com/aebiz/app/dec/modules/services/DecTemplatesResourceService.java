package com.aebiz.app.dec.modules.services;

import com.aebiz.app.dec.modules.models.Dec_component_resource;
import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.dec.modules.models.Dec_templates_resource;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DecTemplatesResourceService extends BaseService<Dec_templates_resource>{

    /**
     * DataTable Page
     *@param folderUuid
     * @param length   页大小
     * @param start    start
     * @param draw     draw
     * @param orders   排序
     * @param columns  字段
     * @param cnd      查询条件
     * @param linkName 关联查询
     * @return
     */
    public NutMap getData(String folderUuid, int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName);
    /**
     * 获取文件夹下所有资源文件
     */
    public List<Dec_templates_resource> getFileResourcesByFolderUuid(
            String folderUuid);
    /**
     * 根据资源key集合删除redis里保存的资源信息
     */
    public void deleteResourcesOfRedis(byte[]... bytesArr);
    /**
     * 删除文件夹下所有资源文件
     */
    public void deleteFileResources(String folderUuid);

    /**
     * 根据redis里保存的资源key获取二进制数组内容
     */
    public byte[] getRedisResoureBytesByKey(String resourceKey);

    /**
     * 上传模板资源文件
     */
    public void uploadFileResources(String folderUuid,
                                    MultipartFile[] resourceFiles);
    /**
     * 检查文件夹下文件名是否已存在
     */
    public boolean checkFolderFileNameExisted(String fileName, String folderUuid);
    /**
     * 将资源信息存入redis
     */
    public void saveResourceBytesToRedis(byte[] resourceKey,
                                         byte[] resourceBytes);

    /**
     * 获取模板资源文件版本号
     * @param templateUuid
     */
    public String getTemplateResourceVersion(String templateUuid);

    /**
     * 获取模板下所有的资源key集合
     */
    public List<String> getTemplateResourceKeysByTemplateUuid(String templateUuid);
    /**
     * 删除模板下的所有资源文件
     */
    public void deleteTemplateFileResourcesByTemplateUuid(String templateUuid);

    /**
     * 删除该id对应对的model的redis里的文件
     */
    public void deleteByResouceId(String id);
}
