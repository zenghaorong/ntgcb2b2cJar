package com.aebiz.app.web.commons.websocket.room;

import com.aebiz.app.web.commons.websocket.WsRoomProvider;
import com.aebiz.baseframework.redis.JedisAgent;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * 基于Redis/Jedis的Websocket房间实现
 * Created by wizzer,wendal on 2017/3/8.
 */
public class JedisRoomProvider implements WsRoomProvider {

    protected JedisAgent jedisAgent;

    public JedisRoomProvider(JedisAgent jedisAgent) {
        this.jedisAgent = jedisAgent;
    }

    public Set<String> wsids(String room) {
        try (Jedis jedis = jedisAgent.getResource()) {
            return jedis.smembers(room);
        }
    }

    public void join(String room, String wsid) {
        try (Jedis jedis = jedisAgent.getResource()) {
            jedis.sadd(room, wsid);
        }
    }

    public void left(String room, String wsid) {
        try (Jedis jedis = jedisAgent.getResource()) {
            jedis.srem(room, wsid);
        }
    }

}