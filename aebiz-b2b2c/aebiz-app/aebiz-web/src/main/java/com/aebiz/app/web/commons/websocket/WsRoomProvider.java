package com.aebiz.app.web.commons.websocket;

import java.util.Set;
/**
 * Created by wizzer on 2017/3/8.
 */
public interface WsRoomProvider {

    Set<String> wsids(String room);

    void join(String room, String wsid);

    void left(String room, String wsid);
}
