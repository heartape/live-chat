package com.heartape.connect;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryConnectionManager implements ConnectionManager<SseConnection> {
    private final Map<Long, SseConnection> cache = new ConcurrentHashMap<>();

    private final static MemoryConnectionManager MEMORY_CONNECTION_MANAGER = new MemoryConnectionManager();

    public static MemoryConnectionManager getInstance(){
        return MEMORY_CONNECTION_MANAGER;
    }

    @Override
    public SseConnection register(long uid, SseConnection connection){
        return cache.putIfAbsent(uid, connection);
    }

    @Override
    public SseConnection pick(long uid){
        return cache.get(uid);
    }

    @Override
    public Collection<SseConnection> pickAll(){
        return cache.values();
    }

    @Override
    public void upgrade(long uid) {

    }

    @Override
    public void relegation(long uid) {

    }

    @Override
    public SseConnection logout(long uid){
        return cache.remove(uid);
    }
}
