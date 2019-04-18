package com.aebiz.app.dec.modules.services.impl;

import com.aebiz.app.dec.commons.utils.DecorateCacheConstant;
import com.aebiz.app.dec.commons.utils.DecorateCommonConstant;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.dec.modules.models.Dec_component_resource;
import com.aebiz.app.dec.modules.services.DecComponentResourceService;
import com.aebiz.baseframework.redis.RedisService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DecComponentResourceServiceImpl extends BaseServiceImpl<Dec_component_resource> implements DecComponentResourceService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private RedisService redisService;

    /**
     * 获取JSP类型文件组件资源对象
     *
     * @param compUuid
     * @param resourceType
     * @return
     */
    public Dec_component_resource getComponentResource(String compUuid, String resourceType ){
            Cnd cnd= Cnd.NEW();
        List<Dec_component_resource> list=(List<Dec_component_resource>) this.query(cnd.where("compUuid","=",compUuid).and("resourceType","=",resourceType));
            if(list !=null && list.size()>0){
                Dec_component_resource model=list.get(0);
                return model;
            }
        return null;
    }
    /**
     * 保存组件资源jsp文件信息
     */
    public void saveComponentJspResource(String compUuid, String compId, byte[] fileBytes) {
        if (!Strings.isEmpty(compUuid) && !Strings.isEmpty(compId)) {
            String resourceKey = this.generateComponentResouceKey(
                    DecorateCacheConstant.COMPONENTS_JSP_REDIS_KEY, compId);
            String resourceType = DecorateCommonConstant.COMPONENT_RESOURCETYE_JSP;
            String generateVersionNo = "1.0";

            Dec_component_resource componentResource = this
                    .getComponentResource(compUuid, DecorateCommonConstant.COMPONENT_RESOURCETYE_JSP);
            if (componentResource != null) {
                byte[] jspFileBytes = this.getRedisResoureBytesByKey(resourceKey);
                generateVersionNo = componentResource.getVersionNo();
                if (jspFileBytes != null && !Arrays.equals(jspFileBytes, fileBytes)) {
                    // 将jsp文件以二进制数组形式存入redis
                    this.saveFileResourceToRedis(resourceKey.getBytes(), fileBytes);
                    double versionNo = Double.parseDouble(generateVersionNo) + 1;
                    generateVersionNo = String.valueOf(versionNo);
                    componentResource.setVersionNo(generateVersionNo);
                    this.update(componentResource);
                }

            } else {
                // 将jsp文件以二进制数组形式存入redis
                this.saveFileResourceToRedis(resourceKey.getBytes(), fileBytes);
                this.saveComponentResource(compUuid, resourceKey, resourceType, generateVersionNo);
            }

        }
    }
   /**
     * 保存组件资源的html文件信息
     **/
    public void saveComponentHtmlResource(String compUuid, String compId, byte[] fileBytes) {
        if (!Strings.isEmpty(compUuid) && !Strings.isEmpty(compId)) {
            String resourceKey = this.generateComponentResouceKey(
                    DecorateCacheConstant.COMPONENTS_HTML_REDIS_KEY, compId);
            String resourceType = DecorateCommonConstant.COMPONENT_RESOURCETYE_HTML;

            // 将html文件以二进制数组形式存入redis
            this.saveFileResourceToRedis(resourceKey.getBytes(), fileBytes);

            boolean isSaved = this.isComponetResourceSaved(compUuid, resourceType);
            if (!isSaved) {
                this.saveComponentResource(compUuid, resourceKey, resourceType, "");
            }
        }
    }
    /**
     * 保存组件资源的js文件信息
     */
    public void saveComponentJsResource(String compUuid, String compId, byte[] fileBytes) {
        if (!Strings.isEmpty(compUuid) && !Strings.isEmpty(compId)) {
            String resourceKey = this.generateComponentResouceKey(
                    DecorateCacheConstant.COMPONENTS_JS_REDIS_KEY, compId);
            String resourceType = DecorateCommonConstant.COMPONENT_RESOURCETYE_JS;

            // 将js文件以二进制数组形式存入redis
            this.saveFileResourceToRedis(resourceKey.getBytes(), fileBytes);

            boolean isSaved = this.isComponetResourceSaved(compUuid, resourceType);
            if (!isSaved) {
                this.saveComponentResource(compUuid, resourceKey, resourceType, "");
            }
        }
    }
    /**
     * 生成组件资源保存的key
     *
     * @param preKey
     * @param compId
     * @return
     */
    private String generateComponentResouceKey(String preKey, String compId) {
        return preKey + compId;
    }

   /**
     * 根据组件资源文件保存的key获取二进制数组内容
     */
    public byte[] getRedisResoureBytesByKey(String resourceKey) {
        byte[] resourceBytes = null;
        try (Jedis jedis = redisService.jedis()) {
            resourceBytes=jedis.get(resourceKey.getBytes());
        }
        return resourceBytes;
    }
    /**
     * 根据组件资源文件保存的key获取文件内容
     */
    @Override
    public String getRedisResourceBytesByKey(String resourceKey) {
        String resourceStr = "";
        boolean flag=false;
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
     * 将组件资源文件信息存入redis
     */
    public void saveFileResourceToRedis(byte[] resourceKey, byte[] resourceBytes) {
        try (Jedis jedis = redisService.jedis()) {
            jedis.set(resourceKey, resourceBytes);
        }
    }

    /**
     * 检查组件的资源信息是否已保存
     */
    public boolean isComponetResourceSaved(String compUuid, String resourceType) {
        Dec_component_resource componentResourceModel = this.getComponentResource(compUuid, resourceType);
        if (componentResourceModel != null) {
            return true;
        }
        return false;
    }

    /**
     * 保存组件注册文件的资源信息
     *
     * @param compUuid
     */
    private void saveComponentResource(String compUuid, String resourceKey, String resourceType, String versionNo) {
        Dec_component_resource m = new Dec_component_resource();
        m.setCompUuid(compUuid);
        m.setResourceKey(resourceKey);
        m.setResourceType(resourceType);
        m.setDisabled(DecorateCommonConstant.DECORATE_USING_YES);
        m.setVersionNo(versionNo);
        // 保存新数据
        this.insert(m);
    }
    /**
     * 根据组件的compId去获取用户自定义组件的参数设置页面
     */
    @Override
    public String getUserDefinedParamHtml(String compId){
        String resourceKey = this.generateComponentResouceKey(
                DecorateCacheConstant.COMPONENTS_JSP_REDIS_KEY, compId);
        boolean flag=true;
        return getRedisResourceByKey(resourceKey,true);
    }

    /**
     * 根据组件资源文件保存的key获取文件内容
     */
    @Override
    public String getRedisResourceByKey(String resourceKey) {
        return this.getRedisResourceByKey(resourceKey, false);
    }


    /**
     * 根据组件资源文件保存的key获取文件内容
     */
    @Override
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
     * 根据组件uuid删除所有组件资源信息
     *
     * @param compUuids
     * @return
     */
    @Override
    public void deleteResources(List<String> compUuids) {
        // 从redis里删除对应的资源信息
        List<String> resourceKeyList = getResourcesListOfRedisByCompUuids(compUuids);
        if (resourceKeyList != null && resourceKeyList.size() > 0) {
          //  byte[][] resourceKeyBytesArr = new byte[resourceKeyList.size()][];
            for (int i = 0; i < resourceKeyList.size(); i++) {
                String resoureKey = resourceKeyList.get(i);
                byte[] resourceKeyBytesArr = resoureKey.getBytes();
                deleteResourcesOfRedis(resourceKeyBytesArr);
            }

        }

        // 从数据库里删除资源数据
        deleteResourcesByCompUuids(compUuids);
    }

    /**
     * 根据组件uuid集合得到对应保存的资源key集合
     *
     * @param compUuids
     * @return
     */
    public List<String> getResourcesListOfRedisByCompUuids(List<String> compUuids) {
        List<String> resourceKeyList=new ArrayList<String>();
        for(int i=0;i<compUuids.size();i++){
            List<Dec_component_resource> resouceList=this.query(Cnd.NEW().where("compUuid","=",compUuids.get(i)));
            if(resouceList !=null && resouceList.size()>0){
                for (Dec_component_resource resourceModel:resouceList) {
                    resourceKeyList.add(resourceModel.getResourceKey());
                }
            }
        }
        return resourceKeyList;
    }

    /**
     * 根据组件uuid集合删除redis里保存的资源信息
     */
    public void deleteResourcesOfRedis(byte[]... bytesArr) {
        try (Jedis jedis = redisService.jedis()) {
             jedis.del(bytesArr);
            }
    }

    /**
     * 根据组件uuid删除所有组件资源信息
     */
    public void deleteResourcesByCompUuids(List<String> compUuids) {
        if (compUuids == null || compUuids.size() == 0) {
            return;
        }
        for (String uuid:compUuids) {
            List<Dec_component_resource> resourceList=this.query(Cnd.NEW().where("compUuid","=",uuid));
            if(resourceList !=null && resourceList.size()>0){
                for (Dec_component_resource resourceModel:resourceList) {
                    this.delete(resourceModel.getId());
                }
            }
        }
    }
}
