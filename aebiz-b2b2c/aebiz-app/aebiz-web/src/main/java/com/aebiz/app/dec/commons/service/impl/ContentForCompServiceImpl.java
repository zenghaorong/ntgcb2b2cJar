package com.aebiz.app.dec.commons.service.impl;

import com.aebiz.app.cms.modules.models.Cms_article;
import com.aebiz.app.cms.modules.models.Cms_channel;
import com.aebiz.app.cms.modules.services.CmsArticleService;
import com.aebiz.app.cms.modules.services.CmsChannelService;
import com.aebiz.app.dec.commons.service.ContentForCompService;
import com.aebiz.app.web.modules.controllers.open.dec.dto.content.ContentCategoryDTO;
import com.aebiz.app.web.modules.controllers.open.dec.dto.content.ContentDTO;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 前台内容相关业务实现
 *
 * Created by yewei on 2017/5/19.
 */
@Service
public class ContentForCompServiceImpl implements ContentForCompService {

    private CmsChannelService channelService;

    private CmsArticleService cmsArticleService;


    @Autowired
    public ContentForCompServiceImpl(CmsArticleService cmsArticleService,CmsChannelService channelService
                                    ){
        this.channelService = channelService;
        this.cmsArticleService = cmsArticleService;
    }

    /**
     * 根据栏目的id获取该栏目下的所有的子栏目集合和文章列表信息
     *
     * @param uuid 父级分类uuid
     * @return 分类及内容集合
     */
    @Override
    public Map<ContentCategoryDTO, List<ContentDTO>> getContentCategoryByUuid(String uuid) {
        /*1、根据栏目的id获取该栏目下的所有的子栏目集合*/
        List<String> channelIdList=new ArrayList<String>();
        List<Cms_channel> channelList =  channelService.query(Cnd.where("parentId","=",uuid).and("disabled","=",0));
        if(channelList !=null && channelList.size()>0){
            for(int i=0;i<channelList.size();i++){
                String channelId=channelList.get(i).getId();
                channelIdList.add(channelId);
            }
        }

        /*2、返回分类及内容集合*/
        return getContCateAndContByUuidList(channelIdList);

    }

    /**
     * 获取平台内容分类
     *
     * @return 返回平台内容分类
     */
    @Override
    public List<ContentCategoryDTO> getContentCategory() {

        /*1、获取平台内容分类*/
        List<Cms_channel> channelList = channelService.query(Cnd.NEW().andNot("parentId","!=",""));

        /*2、将分类Model转化为DTO*/
        List<ContentCategoryDTO> categoryDTOs = new ArrayList<>();
        ContentCategoryDTO contentCategoryDTO;
        for(Cms_channel model : channelList){
            contentCategoryDTO = new ContentCategoryDTO(model.getId(), model.getName(), model.getLocation(), "");
            categoryDTOs.add(contentCategoryDTO);
        }

        /*3、返回分类DTO列表*/
        return categoryDTOs;
    }

    /**
     * 根据栏目id集合获取对应栏目以及栏目下的文章列表信息。
     *
     * @param channelIdList
     * @return 返回栏目下的文章
     */
    @Override
    public Map<ContentCategoryDTO, List<ContentDTO>> getContCateAndContByUuidList(List<String> channelIdList) {
    	
        

        /*1、获取文章列表的标题*/
        Map<Cms_channel, List<Cms_article>> ccModelListMap = getCateAndContByUuid(channelIdList);


        Cms_channel ccModel;
        List<Cms_article> cModelList;
        List<ContentDTO> cDtoList;
        ContentDTO cDto ;
        ContentCategoryDTO ccDto;

        /*2、创建最终内容分类DTO以及内容DTO列表的Map集合*/
        Map<ContentCategoryDTO, List<ContentDTO>> cateAndContMap = new TreeMap<>(new Comparator<ContentCategoryDTO>() {
            /*2.1、根据Map中内容分类的位置进行排序*/
            @Override
            public int compare(ContentCategoryDTO o1, ContentCategoryDTO o2) {
                if (o1.getPosition() > o2.getPosition())
                    return 1;
                if (o1.getPosition() == o2.getPosition())
                    return 0;
                else
                    return -1;
            }
        });

        /*3、迭代获取分类以及分类下内容Model集合，并转化为DTO添加到cateAndContMap集合*/
        Iterator<Cms_channel> it = ccModelListMap.keySet().iterator();
        while(it.hasNext()){
            /*3.1、获取map的键--内容分类对象，创建对应的DTO*/
            ccModel = it.next();
            ccDto = new ContentCategoryDTO(ccModel.getId(), ccModel.getName(), ccModel.getLocation(),"");
            cDtoList = new ArrayList<>();
            /*3.2、获取map的值--分类下内容List，创建对应的DTO List并添加DTO对象到List中*/
            cModelList = ccModelListMap.get(ccModel);
            if(cModelList !=null && cModelList.size()>0){
                /*3.2.1、迭代内容列表，为内容DTO赋值*/
                for(Cms_article model : cModelList){
                    String contValue ;
                    /*3.2.2、判断内容类型：如果是链接类型的，则将链接设为DTO的内容，否则将原内容作为DTO的内容*/
                    if(!Strings.isEmpty(ccModel.getUrl())){
                        contValue = ccModel.getUrl();
                    }else{
                        contValue = model.getContent();
                    }
                    /*3.2.3、创建DTO*/
                    cDto = new ContentDTO(ccModel.getId(), model.getTitle(), contValue,ccModel.getType());
                    /*3.2.4、将DTO添加到内容DTO列表中去*/
                    cDtoList.add(cDto);
                }
            }
            cateAndContMap.put(ccDto, cDtoList);
        }
        return cateAndContMap;
    }

    /**
     * 根据分类uuid查询子分类集合
     *
     * @param cateUuid 分类uuid
     * @return 返回子分类集合
     */
    @Override
    public List<ContentCategoryDTO> getSubCategoryByUuid(String cateUuid) {
        /*1、获取平台内容分类*/
        List<Cms_channel> categoryModelList = channelService.query(Cnd.where("parentId","=",cateUuid));

        /*2、将分类Model转化为DTO*/
        List<ContentCategoryDTO> categoryDTOList = new ArrayList<>();
        ContentCategoryDTO contentCategoryDTO;
        for(Cms_channel model : categoryModelList){
            contentCategoryDTO = new ContentCategoryDTO(model.getId(), model.getName(), model.getLocation(), "");
            categoryDTOList.add(contentCategoryDTO);
        }

        /*3、返回分类DTO列表*/
        return categoryDTOList;
    }

    /**
     *
     * @param channelIdList
     * @return
     */
    public Map<Cms_channel, List<Cms_article>> getCateAndContByUuid(List<String> channelIdList) {
        List<Cms_channel> channelList=null;
        Map<Cms_channel, List<Cms_article>> returnMap = new HashMap<>();
        if(!channelIdList.isEmpty()){
            for(String id : channelIdList){
                List<Cms_article> articleList = new ArrayList<Cms_article>();
                //增加缓存
                Cms_channel channelModel = channelService.fetch(id);
                channelList=channelService.query(Cnd.where("parentId","=",id));
                if(channelList!=null && channelList.size()>0){
                    List<Cms_article> cmsArticlesList=null;
                    for(int x=0;x<channelList.size();x++){
                        String channeld=channelList.get(x).getId();
                        cmsArticlesList=cmsArticleService.query(Cnd.where("channelId","=",channeld));
                    }
                    articleList.addAll(cmsArticlesList);
                }else{
                    articleList=null;
                }
                if(channelModel != null) {
                    returnMap.put(channelModel, articleList);
                }
            }
        }
        return returnMap;
    }

    /**
     *  根据分类的uuid获取该分类下的所有内容
     *
     * @param contentCateUuid 内容分类uuid
     * @return 获取到的内容列表
     */
    @Override
    public List<ContentDTO> getContListByCateId (String contentCateUuid) {
        //
        Cms_channel channel=channelService.fetch(Cnd.where("id","=",contentCateUuid));
        /*1、根据uuid获取内容列表*/
        List<Cms_article> contentList = cmsArticleService.list(contentCateUuid);

        /*2、将获取到的内容列表转化为内容DTO列表*/
        List<ContentDTO> contentDTOs = new ArrayList<>();
        ContentDTO contentDTO;
        String contValue;
        //获取内容的值，如果是链接类型的，则返回内容链接。如果是文本类型的则返回具体的文本内容
        for(Cms_article model : contentList){
            if(!Strings.isEmpty(channel.getUrl())){
                contValue = channel.getUrl();
            }else{
                contValue = model.getContent();
            }
            contentDTO = new ContentDTO(model.getId(), model.getTitle(), contValue, channel.getType());
            contentDTOs.add(contentDTO);
        }

        /*3、返回获取到的内容DTO列表*/
        return contentDTOs;
    }

}
