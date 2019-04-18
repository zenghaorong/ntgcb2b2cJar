package com.aebiz.app.dec.modules.services;

import com.aebiz.baseframework.base.service.BaseService;
import com.aebiz.app.dec.modules.models.Dec_page__layout;

import java.util.List;

public interface DecPageLayoutService extends BaseService<Dec_page__layout>{
    /**
     * 保存页面布局的内容到redis
     * @param layoutId
     * @param htmlBytes
     */
    public void saveLayoutHtmlResource(String layoutId, byte[] htmlBytes);

    /**
     * 根据页面布局资源文件保存的key获取布局内容
     *
     * @param resourceKey
     * @return
     */
    public String getRedisResourceByKey(String resourceKey);

    /**
     * 根据布局uuids删除所有布局资源信息
     * @param layoutUuids
     */
    public void deletesResourcesByPageLayoutUuids(List<String> layoutUuids);

    /**
     * 获取所有页面布局
     * @param contextPath
     * @param versionType 终端类型
     * @return
     */
    public List<Dec_page__layout> getPageLayouts(String contextPath, String versionType);

    /**
     * 检查页面布局编号是否重复
     *
     * @param layoutId
     * @return
     */
    public boolean isLayoutIdExist(String layoutId);
}
