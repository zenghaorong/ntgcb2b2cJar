package com.aebiz.app.sys.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.sys.modules.models.Sys_api;
import com.aebiz.app.sys.modules.services.SysApiService;
import com.aebiz.baseframework.redis.RedisService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.io.*;
import java.security.Key;
import java.util.Date;

@Service
public class SysApiServiceImpl extends BaseServiceImpl<Sys_api> implements SysApiService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private RedisService redisService;

    private Key getKey(String appId) throws IOException, ClassNotFoundException {
        Key key;
        try (Jedis jedis = redisService.jedis()) {
            byte[] obj = jedis.get(("apitoken:" + appId).getBytes());
            if (obj != null) {
                ObjectInputStream keyIn = new ObjectInputStream(new ByteArrayInputStream(obj));
                key = (Key) keyIn.readObject();
                keyIn.close();
            } else {
                key = MacProvider.generateKey();
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bao);
                oos.writeObject(key);
                obj = bao.toByteArray();
                jedis.set(("apitoken:" + appId).getBytes(), obj);
            }
        }
        return key;
    }

    /**
     * 生成token
     *
     * @param date  失效时间
     * @param appId appId
     * @return
     */
    public String generateToken(Date date, String appId) throws IOException, ClassNotFoundException {
        return Jwts.builder()
                .setSubject(appId)
                .signWith(SignatureAlgorithm.HS512, getKey(appId))
                .setExpiration(date)
                .compact();
    }

    /**
     * 验证token
     *
     * @param appId AppId
     * @param token token
     * @return
     */
    public boolean verifyToken(String appId, String token) {
        try {
            return Jwts.parser().setSigningKey(getKey(appId)).parseClaimsJws(token).getBody().getSubject().equals(appId);
        } catch (Exception e) {
            return false;
        }
    }
}
