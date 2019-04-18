package com.aebiz.app.web.commons.websocket;

import com.aebiz.commons.utils.SpringUtil;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * Created by wizzer on 2017/3/3.
 */
public class MyWebSocketConfigurator extends ServerEndpointConfig.Configurator {

    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return SpringUtil.getBean(endpointClass);
    }

    public void modifyHandshake(ServerEndpointConfig sec,
                                HandshakeRequest request,
                                HandshakeResponse response) {
        super.modifyHandshake(sec, request, response);
        javax.servlet.http.HttpSession session = (javax.servlet.http.HttpSession)request.getHttpSession();
        if (session != null)
            sec.getUserProperties().put("HttpSession", session);
    }
}