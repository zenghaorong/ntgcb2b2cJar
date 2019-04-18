package com.aebiz.app.web.commons.websocket.room;

import com.aebiz.app.web.commons.websocket.WsRoomProvider;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
/**
 * 单机版的WsRoom实现
 * Created by wizzer,wendal on 2017/3/8.
 */
public class MemoryRoomProvider implements WsRoomProvider {

    private ConcurrentHashMap<String, ConcurrentSkipListSet<String>> rooms = new ConcurrentHashMap<>();

    public Set<String> wsids(String room) {
        return getRoom(room);
    }

    public void join(String room, String wsid) {
        getRoom(room).add(wsid);
    }

    public void left(String room, String wsid) {
        getRoom(room).remove(wsid);
    }

    public Set<String> getRoom(String room) {
        ConcurrentSkipListSet<String> _room = rooms.get(room);
        if (_room == null) {
            _room = new ConcurrentSkipListSet<String>();
            ConcurrentSkipListSet<String> prev = rooms.putIfAbsent(room, _room);
            if (prev != null)
                _room = prev;
        }
        return _room;
    }

}