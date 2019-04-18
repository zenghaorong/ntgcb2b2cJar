package com.aebiz.app.dec.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.dec.modules.models.Dec_component_resource;

import java.util.List;

public interface DecComponentResourceService extends BaseService<Dec_component_resource>{

    /**
     * 保存组件资源的js文件信息
     *
     * @param compUuid
     * @param compId
     * @param fileBytes
     */
    public void saveComponentJsResource(String compUuid, String compId, byte[] fileBytes);
    /**
     * 保存组件资源的jsp文件信息
     *
     * @param compUuid
     * @param compId
     * @param fileBytes
     */
    public void saveComponentJspResource(String compUuid, String compId, byte[] fileBytes);

    /**
     * 保存组件资源的html文件信息
     */
    public void saveComponentHtmlResource(String compUuid, String compId, byte[] fileBytes);

    public String getRedisResourceBytesByKey(String resourceKey);
    /**
     * 获取JSP类型文件组件资源对象
     *
     * @param compUuid
     * @param resourceType
     * @return
     */
    public Dec_component_resource getComponentResource(String compUuid, String resourceType );
    /**
     * 根据组件的compId去获取用户自定义组件的参数设置页面
     */
    public String getUserDefinedParamHtml(String compId);

    /**
     * 根据组件资源文件保存的key获取文件内容
     */
    public String getRedisResourceByKey(String resourceKey);
    /**
     * 根据组件资源文件保存的key获取文件内容
     */
    public String getRedisResourceByKey(String resourceKey, boolean flag);
    /**
     * 根据组件uuid删除所有组件资源信息
     *
     * @param compUuids
     * @return
     */
    public void deleteResources(List<String> compUuids);
}
