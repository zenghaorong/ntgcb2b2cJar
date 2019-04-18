package com.aebiz.app.wx.modules.services.impl;

import com.aebiz.app.web.commons.base.Globals;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.wx.modules.models.Wx_config;
import com.aebiz.app.wx.modules.services.WxConfigService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.weixin.at.impl.DaoAccessTokenStore;
import org.nutz.weixin.at.impl.RedisAccessTokenStore;
import org.nutz.weixin.impl.WxApi2Impl;
import org.nutz.weixin.spi.WxApi2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "wxCache")
public class WxConfigServiceImpl extends BaseServiceImpl<Wx_config> implements WxConfigService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private JedisPool jedisPool;

    public synchronized WxApi2 getWxApi2(String wxid) {
        WxApi2Impl wxApi2 = Globals.WxMap.get(wxid);
        if (wxApi2 == null) {
            Wx_config appInfo = this.fetch(Cnd.where("id", "=", wxid));
            RedisAccessTokenStore redisAccessTokenStore = new RedisAccessTokenStore();
            redisAccessTokenStore.setTokenKey("data:wx_api_token_" + wxid);
            redisAccessTokenStore.setJedisPool(jedisPool);
            wxApi2 = new WxApi2Impl();
            wxApi2.setAppid(appInfo.getAppid());
            wxApi2.setAppsecret(appInfo.getAppsecret());
            wxApi2.setEncodingAesKey(appInfo.getEncodingAESKey());
            wxApi2.setToken(appInfo.getToken());
            wxApi2.setAccessTokenStore(redisAccessTokenStore);
            Globals.WxMap.put(wxid, wxApi2);
        }
        return wxApi2;
    }
}
