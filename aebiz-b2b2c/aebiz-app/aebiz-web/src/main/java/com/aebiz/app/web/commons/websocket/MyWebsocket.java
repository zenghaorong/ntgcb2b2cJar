package com.aebiz.app.web.commons.websocket;

import com.aebiz.app.web.commons.websocket.room.JedisRoomProvider;
import com.aebiz.baseframework.redis.RedisService;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.websocket.server.ServerEndpoint;

/**
 * @ Autowired
 * protect MyWebsocket myWebsocket;

 public void sayhi(String room) {
     myWebsocket.each(room, new Each<Session>() {
     public void invoke(int index, Session ele, int length) {
        myWebsocket.sendJson(ele.getId(), new NutMap("action", "layer").setv("msg", "hi"));
     }
     });
 }

 public void sayhiJson(String wsid) {
     myWebsocket.sendJson(wsid, new NutMap("action", "layer").setv("msg", "hi"));
 }
 */
@ServerEndpoint(value = "/websocket", configurator = MyWebSocketConfigurator.class)
@Component
public class MyWebsocket extends AbstractWsEndpoint {

    @Autowired
    protected RedisService redisService;

    @PostConstruct
    public void init() {
        super.setRoomPrefix("wsroom:");
        super.setRoomProvider(new JedisRoomProvider(redisService.getJedisAgent()));
    }

    @PreDestroy
    public void depose() {

    }
}
