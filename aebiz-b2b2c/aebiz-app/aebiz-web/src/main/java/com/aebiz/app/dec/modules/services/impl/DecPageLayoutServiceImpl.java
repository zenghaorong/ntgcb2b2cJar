package com.aebiz.app.dec.modules.services.impl;

import com.aebiz.app.dec.commons.utils.DecorateCacheConstant;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.dec.modules.models.Dec_page__layout;
import com.aebiz.app.dec.modules.services.DecPageLayoutService;
import com.aebiz.baseframework.redis.RedisService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DecPageLayoutServiceImpl extends BaseServiceImpl<Dec_page__layout> implements DecPageLayoutService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private RedisService redisService;
    /**
     * 保存页面布局的内容到redis
     * @param layoutId
     * @param htmlBytes
     */
    @Override
    public void saveLayoutHtmlResource(String layoutId, byte[] htmlBytes) {
        if(!Strings.isEmpty(layoutId)){
            String resourceKey = DecorateCacheConstant.LAYOUT_HTML_REDIS_KEY + layoutId;
            // 将html文件以二进制数组形式存入redis
            saveFileResourceToRedis(resourceKey.getBytes(), htmlBytes);
        }
    }


    /**
     * 根据页面布局内容保存的key获取页面内容
     *
     * @param resourceKey
     * @return
     */
    @Override
    public String getRedisResourceByKey(String resourceKey) {
        String resourceStr = "";
        try(Jedis jedis = redisService.jedis()){
            resourceStr = jedis.get(resourceKey);
            byte[] resourceBytes = jedis.get(resourceKey.getBytes());
            if(resourceBytes !=null){
                resourceStr = new String(resourceBytes);
            }
        }
        return resourceStr;
    }


    /**
     * 获取所有页面布局
     * @param contextPath
     * @param versionType 终端类型
     * @return
     */
    @Override
    public List<Dec_page__layout> getPageLayouts(String contextPath ,String versionType) {
        //获取所有页面布局库里数据
        List<Dec_page__layout> mList = this.query(Cnd.where("versionType","=",versionType));
        if(mList !=null && mList.size()>0){
            for(Dec_page__layout m : mList){
                //获取布局内容存redis中的key
                String resourceKey = m.getResourceKey();
                if(!Strings.isEmpty(resourceKey)){
                    //根据key获取redis中页面布局内容
                    String redisLayoutHtml = getRedisResourceByKey(resourceKey);
                    m.setLayoutContent(redisLayoutHtml);
                }
            }
        }

        return mList;
    }
    /**
     * 检查页面布局编号是否重复
     *
     * @param layoutId
     * @return
     */
    @Override
    public boolean isLayoutIdExist(String layoutId) {
        List<Dec_page__layout> layouts= this.query(Cnd.where("layoutId","=",layoutId));
        if(layouts!=null && layouts.size()>0){
            return true;
        }
        return false;
    }

    /**
     * 根据布局uuids删除所有布局资源信息
     * @param layoutUuids
     */
    @Override
    public void deletesResourcesByPageLayoutUuids(List<String> layoutUuids) {
        //获取布局uuids对应的keys
        List<String> resourceKeyList =getResourcesKeyListByLayoutUuids(layoutUuids);
        if (resourceKeyList != null && resourceKeyList.size() > 0) {
            byte[][] resourceKeyBytesArr = new byte[resourceKeyList.size()][];
            for (int i = 0; i < resourceKeyList.size(); i++) {
                String resoureKey = resourceKeyList.get(i);
                resourceKeyBytesArr[i] = resoureKey.getBytes();
            }
            deleteResourcesOfRedis(resourceKeyBytesArr);
        }
        //根据uuids删除表中页面布局对应的数据
       // this.deletes(layoutUuids);
    }
    /**
     * 将页面布局资源文件信息存入redis
     *
     * @param resourceKey
     * @param resourceBytes
     */
    public void saveFileResourceToRedis(byte[] resourceKey, byte[] resourceBytes) {
        try (Jedis jedis = redisService.jedis()) {
            jedis.set(resourceKey,resourceBytes);
        }
    }

    /**
     * 根据Uuids获取redis保存资源的key
     * @param layoutUuids
     * @return
     */
    public List<String> getResourcesKeyListByLayoutUuids(List<String> layoutUuids) {
        List<String> layouts=new ArrayList<>();
        if(layoutUuids !=null && layoutUuids.size()>0){
            for(String layoutId:layoutUuids){
                if(this.fetch(layoutId)!=null){
                    layouts.add(this.fetch(layoutId).getResourceKey());
                }
            }
        }
        return layouts;
    }

    //根据resouceKey删除redis数据
    public void deleteResourcesOfRedis(byte[]... bytesArr) {
        try(Jedis jedis=redisService.jedis()){
            jedis.del(bytesArr);
        }
    }
}
