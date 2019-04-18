package com.aebiz.app.dec.modules.services.impl;

import com.aebiz.app.dec.commons.vo.WebPageModel;
import com.aebiz.app.dec.commons.utils.DecorateCacheConstant;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.app.dec.modules.models.Dec_templates_manager;
import com.aebiz.app.dec.modules.models.Dec_templates_sub;
import com.aebiz.app.dec.modules.models.em.PageTypeEnum;
import com.aebiz.app.dec.modules.services.DecTemplatesFilesService;
import com.aebiz.app.dec.modules.services.DecTemplatesManagerService;
import com.aebiz.app.dec.modules.services.DecTemplatesSubService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.dec.modules.models.Dec_templates_pages;
import com.aebiz.app.dec.modules.services.DecTemplatesPagesService;
import com.aebiz.baseframework.page.OffsetPager;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.redis.RedisService;
import com.aebiz.baseframework.shiro.filter.AebizShiroFilter;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

@Service
public class DecTemplatesPagesServiceImpl extends BaseServiceImpl<Dec_templates_pages> implements DecTemplatesPagesService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    @Autowired
    DecTemplatesManagerService decTemplatesManagerService;

    @Autowired
    DecTemplatesSubService decTemplatesSubService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DecTemplatesFilesService decTemplatesFilesService;

    private static final Logger log = LoggerFactory.getLogger(AebizShiroFilter.class);
    /**
     * DataTable Page
     *@param id
     * @param length   页大小
     * @param start    start
     * @param draw     draw
     * @param orders   排序
     * @param columns  字段
     * @param cnd      查询条件
     * @param linkName 关联查询
     * @return
     */
    public NutMap data(String id,int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName){
        NutMap re = new NutMap();
        if (orders != null && orders.size() > 0) {
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
        }
        Pager pager = new OffsetPager(start, length);
        re.put("recordsFiltered", this.dao().count(this.getEntityClass(), cnd));
        List<Dec_templates_pages>  list=this.query(cnd.where("templateUuid","=",id));
        if(list !=null && list.size()>0){
            for(int i=0;i<list.size();i++){
                Dec_templates_pages pageModel=list.get(i);
                String templateUuid=pageModel.getTemplateUuid();
                Dec_templates_manager managerModel=decTemplatesManagerService.fetch(templateUuid);
                String templateZhName=managerModel.getTemplateZhName();
                pageModel.setTemplateUuid(templateZhName);
                list.set(i,pageModel);
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
    /**
     * 验证模板下的文件名称是否重复
     */
    public boolean checkTemplatePageFileName(String templateUuid,
                                             String pageFileName) {
        if (Strings.isEmpty(templateUuid) && Strings.isEmpty(pageFileName)) {
            return false;
        }
        List<Dec_templates_pages> list =this.query(Cnd.where("templateUuid","=",templateUuid).and("pageFileName","=",pageFileName));
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据页面uuid和key获取redis里保存的页面资源信息
     */
    @Override
    public String[] getPageRedisResourceByPageUuid(String pageUuid, String[] preKeyArr) {
        Dec_templates_sub subPagesModel = decTemplatesSubService.getResourceOfCurrentPage(pageUuid);

        String[] resourceArr = new String[2];
        if (subPagesModel != null && preKeyArr.length == 2) {
            for (int i = 0; i < preKeyArr.length; i++) {
                String resourceKey = preKeyArr[i] + subPagesModel.getResourceKey();
                resourceArr[i] = this.getRedisResourceByKey(resourceKey, true);
            }
        }

        return resourceArr;
    }

    /**
     * 根据页面资源文件保存的key获取文件内容
     */
    public String getRedisResourceByKey(String resourceKey, boolean flag) {
        String resourceStr = "";
        try (Jedis jedis = redisService.jedis()) {
            if (flag) {
                resourceStr = jedis.get(resourceKey);
            } else {
                byte[] resourceBytes =jedis.get(resourceKey.getBytes());
                if (resourceBytes != null) {
                    resourceStr = new String(resourceBytes);
                }
            }
            return resourceStr;
        }
    }

    /**
     * 新增模板页面
     */
    @Override
    public void createTemplatePages(String templateUuid) {
        int[] pageTypeArr = new int[]{1, 2, 3};
        for (int i = 0; i < pageTypeArr.length; i++) {
            int pageType = pageTypeArr[i];

            Dec_templates_pages m = new Dec_templates_pages();
            m.setPageType(pageType);
            m.setTemplateUuid(templateUuid);
            if (PageTypeEnum.BASE.getKey()==pageType) {
                m.setPageName(PageTypeEnum.BASE.getValue());
                m.setPageFileName("index.html");
                m.setDescription(PageTypeEnum.BASE.getValue());
            } else if (PageTypeEnum.LIST.getKey()==pageType) {
                m.setPageName(PageTypeEnum.LIST.getValue());
                m.setPageFileName("list.html");
                m.setDescription(PageTypeEnum.LIST.getValue());
            } else if (PageTypeEnum.DETAIL.getKey()==pageType) {
                m.setPageName(PageTypeEnum.DETAIL.getValue());
                m.setPageFileName("detail.html");
                m.setDescription(PageTypeEnum.DETAIL.getValue());
            }
            this.addPage(m);
        }
    }
    /**
     * 保存设计器页面
     */
    public void saveDesingerPage(List<String[]> resourceArrList) {
        if (resourceArrList != null && resourceArrList.size() > 0) {
            for (Iterator<String[]> iterator = resourceArrList.iterator(); iterator
                    .hasNext();) {
                String[] resourceArr = iterator.next();
                String resourceKey = resourceArr[0];
                String resourceStr = resourceArr[1];
                this.saveFileResourceToRedis(resourceKey, resourceStr);
            }
        }
    }
    @Override
    @Transactional
    public Dec_templates_sub addPage(Dec_templates_pages m) {
        if (Strings.isEmpty(m.getId())) {
            this.insert(m);
        }

        // 保存子页面
        Dec_templates_sub subPagesModel = new Dec_templates_sub();
        String resourceKey = m.getTemplateUuid() + "_" + m.getId() + "_"
                + DecorateCommonConstant.FILE_DEFAULT_VERSIONNO;

        subPagesModel.setPageUuid(m.getId());
        subPagesModel.setVersionNo(DecorateCommonConstant.FILE_DEFAULT_VERSIONNO);
        subPagesModel.setDescription(DecorateCommonConstant.FILE_DEFAULT_VERSIONDESCRIBE);
        subPagesModel.setDisabled(DecorateCommonConstant.DECORATE_USING_YES);
        subPagesModel.setResourceKey(resourceKey);
        subPagesModel.setCreateTime((int)(System.currentTimeMillis() / 1000));
        decTemplatesSubService.insert(subPagesModel);
        // 创建模板页面资源文件
        decTemplatesFilesService.createTemplatePageFile(m.getTemplateUuid(), m.getId(), m.getPageFileName());

        return subPagesModel;
    }

    /**
     * 将PageModel序列化后存入redis
     */
    @Override
    public void savePageModelBytesToRedis(byte[] resourceKey, byte[] resourceBytes) {
        this.saveResourceBytesToRedis(resourceKey, resourceBytes);
    }
    /**
     * 将PageModel序列化后存入redis
     */
    public void saveResourceBytesToRedis(byte[] resourceKey,
                                         byte[] resourceBytes) {
        try (Jedis jedis = redisService.jedis()) {
            jedis.set(resourceKey,resourceBytes);
        }
    }

    /**
     * 将页面资源文件信息存入redis
     */
    public void saveFileResourceToRedis(String resourceKey, String resourceStr) {
        try (Jedis jedis = redisService.jedis()) {
            jedis.set(resourceKey, resourceStr);
        }
    }
    /**
     * 根据页面key获取redis里保存的页面资源信息
     *
     * @return
     */
    public String[] getPageRedisResourceByResourceKey(String key, String[] preKeyArr) {
        String[] resourceArr = new String[2];
        if ( preKeyArr.length == 2) {
            for (int i = 0; i < preKeyArr.length; i++) {
                String resourceKey = preKeyArr[i] + key;
                resourceArr[i] = this.getRedisResourceByKey(resourceKey, true);
            }
        }
        return resourceArr;
    }

    public WebPageModel getPageModelByKey(String key) {
        WebPageModel wpm = new WebPageModel();
        String pageModelKey = DecorateCacheConstant.DESIGNER_PAGEMODEL + key;
        byte[] value = this.getRedisResoureBytesByKey(pageModelKey);
        if(value !=null){
            wpm=Lang.fromBytes(value,WebPageModel.class);
        }
        if(wpm != null){
            return wpm ;
        }
        return null;
    }

    /**
     * 根据页保存的资源key获取二进制数组内容
     */
    public byte[] getRedisResoureBytesByKey(String resourceKey) {
        byte[] resourceBytes = null;
        try (Jedis jedis = redisService.jedis()) {
            resourceBytes = jedis.get(resourceKey.getBytes());
        }
        return resourceBytes;
    }

    /**
     * 保存为新页面
     */
    @Override
    public String saveAsNewPage(Dec_templates_pages m, String pageUuid) {
        Dec_templates_sub subPagesModel = this.addPage(m);
        String resourceKey = subPagesModel.getResourceKey();

        // 获取原有页面的资源信息
        Dec_templates_sub originalSubPagesModel = decTemplatesSubService.getResourceOfCurrentPage(pageUuid);
        if (originalSubPagesModel != null) {
            String originalResourceKey = originalSubPagesModel.getResourceKey();
            this.saveResourceToRedisByKeys(originalResourceKey, resourceKey);
        }
        return subPagesModel.getPageUuid();
    }

    /**
     * 根据子页面保存的资源key复制资源信息
     */
    @Override
    public void saveResourceToRedisByKeys(String originalResourceKey, String newResourceKey) {
        String pageModelKey = DecorateCacheConstant.DESIGNER_PAGEMODEL + originalResourceKey;
        String pageModelJsonKey = DecorateCacheConstant.DESIGNER_PAGEMODELJSON + originalResourceKey;
        String pageViewHtmlKey = DecorateCacheConstant.DESIGNER_PAGEVEIWHTML + originalResourceKey;
        String pageJsKey = DecorateCacheConstant.DESIGNER_PAGEJS + originalResourceKey;

        byte[] pageModelBytes = getRedisResoureBytesByKey(pageModelKey);
        String pageModelJsonStr =getRedisResourceByKey(pageModelJsonKey, true);
        String pageViewHtmlStr = getRedisResourceByKey(pageViewHtmlKey, true);
        String pageJsStr = getRedisResourceByKey(pageJsKey, true);

        saveResourceBytesToRedis((DecorateCacheConstant.DESIGNER_PAGEMODEL + newResourceKey).getBytes(), pageModelBytes);
        saveFileResourceToRedis(DecorateCacheConstant.DESIGNER_PAGEMODELJSON + newResourceKey, pageModelJsonStr);
        saveFileResourceToRedis(DecorateCacheConstant.DESIGNER_PAGEVEIWHTML + newResourceKey, pageViewHtmlStr);
        saveFileResourceToRedis(DecorateCacheConstant.DESIGNER_PAGEJS + newResourceKey, pageJsStr);
    }

    /**
     * 根据模板id删除该模板下面的所有页面
     * @param id
     */
    public void deletePagesByTemplateUuid(String id){
        try {
            List<Dec_templates_pages> pagesList=this.query(Cnd.where("templateUuid","=",id));
            if(pagesList !=null && pagesList.size()>0){
                for(int i=0;i<pagesList.size();i++){
                    this.delete(pagesList.get(i).getId());
                }
            }
        }catch (Exception e){
            log.error(e+"");
        }

        List<String> resourceKeyList = decTemplatesSubService.getSubPageResourceKeysByTemplateUuid(id);
        if (resourceKeyList != null && resourceKeyList.size() > 0) {
            for (String resourceKey : resourceKeyList) {
                String[] keysArr = new String[3];

                String pageModelKey = DecorateCacheConstant.DESIGNER_PAGEMODEL + resourceKey;
                keysArr[0]  = DecorateCacheConstant.DESIGNER_PAGEMODELJSON + resourceKey;
                keysArr[1] = DecorateCacheConstant.DESIGNER_PAGEVEIWHTML + resourceKey;
                keysArr[2] = DecorateCacheConstant.DESIGNER_PAGEJS + resourceKey;

                // 删除redis里页面保存的资源信息
                deleteRedisResourceByKeys(keysArr);

                deleteRedisResourcesByKey(pageModelKey, false);
            }
        }

        decTemplatesSubService.deleteSubPagesByTemplateUuid(id);
    }


    /**
     * 根据资源key集合删除redis里保存的资源信息
     */
    public void deleteRedisResourceByKeys(String... keys) {
        try (Jedis jedis = redisService.jedis()) {
            jedis.del(keys);
        }
    }
    /**
     * 根据资源key删除redis里保存的资源信息
     */
    public void deleteRedisResourcesByKey(String resourceKey, boolean flag) {
        try (Jedis jedis = redisService.jedis()) {
            if (flag) {
                jedis.del(resourceKey);
            } else {
                jedis.del(resourceKey.getBytes());
            }
        }
    }

    /**
     * 根据页面uuid去删除对应的版本和文件、redis信息
     */
    public void deleteAllByPage(String id){
        this.delete(id);
        decTemplatesSubService.deleteByPageUuid(id);
        decTemplatesFilesService.deleteFileByPageUuid(id);
    }

    /**
     * 根据页面请求过来的模板id和页面文件名称去找页面id
     */
    public String getPageId(String templateUuid,String pageFileName){
        if(templateUuid !="" || pageFileName !=""){
            Dec_templates_pages pageModel=this.fetch(Cnd.where("templateUuid","=",templateUuid).and("pageFileName","=",pageFileName));
           if(pageModel !=null){
               return  pageModel.getId();
           }else{
               return "";
           }
        }
        return "";
    }
}
