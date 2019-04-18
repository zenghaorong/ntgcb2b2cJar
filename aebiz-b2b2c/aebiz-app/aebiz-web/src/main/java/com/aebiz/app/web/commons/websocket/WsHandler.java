package com.aebiz.app.web.commons.websocket;

import javax.websocket.MessageHandler;
import javax.websocket.Session;
/**
 * Created by wizzer on 2017/3/8.
 */

public interface WsHandler extends MessageHandler {

    void setSession(Session session);

    void setRoomProvider(WsRoomProvider roomProvider);

    void onMessage(String msg);

    void depose();
}
