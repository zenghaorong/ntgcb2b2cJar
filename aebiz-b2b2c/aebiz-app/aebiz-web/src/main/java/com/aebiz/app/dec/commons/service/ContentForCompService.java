package com.aebiz.app.dec.commons.service;


import com.aebiz.app.cms.modules.models.Cms_article;
import com.aebiz.app.cms.modules.models.Cms_channel;
import com.aebiz.app.web.modules.controllers.open.dec.dto.content.ContentCategoryDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.content.ContentDTO;

import java.util.List;
import java.util.Map;

/**
 *前台内容相关业务
 *
 * Created by Aebiz_yjq on 2016/12/19.
 */
public interface ContentForCompService {


    /**
     * 根据分类的uuid获取该分类下的所有内容
     *
     * @param contentCateUuid 内容分类uuid
     * @return 获取到的内容列表
     */
    List<ContentDTO> getContListByCateId(String contentCateUuid);

    /**
     * 根据分类UUID获取子分类名称以及内容集合
     *
     * @param uuid
     * @return
     */
    Map<ContentCategoryDTO, List<ContentDTO>> getContentCategoryByUuid(String uuid);

    /**
     * 获取平台内容分类
     *
     * @return 返回平台内容分类
     */
     List<ContentCategoryDTO> getContentCategory();

    /**
     * 根据分类uuid集合获取对应分类以及分类下的内容。（NoJson）
     *
     * @param uuidList 分类集合 <code>List<String> uuidList</code>
     * @return 返回内容分类以及分类下内容集合
     */
    Map<ContentCategoryDTO, List<ContentDTO>> getContCateAndContByUuidList(List<String> uuidList);

    /**
     * 根据分类uuid查询子分类集合
     *
     * @param cateUuid 分类uuid
     * @return 返回子分类集合
     */
   List<ContentCategoryDTO> getSubCategoryByUuid(String cateUuid);
    
    
    
    
    
    
    
	/*CompWebModel getPageContentData(int nowPage, int pageShow, String categoryUuid, String contentTitle);*/

    public Map<Cms_channel, List<Cms_article>> getCateAndContByUuid(List<String> uuidList);

}
