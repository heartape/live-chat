package com.heartape.connect;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final Map<Long, Connection> cache = new ConcurrentHashMap<>();

    private final static ConnectionManager connectionManager = new ConnectionManager();

    public static ConnectionManager getInstance(){
        return connectionManager;
    }

    public void register(long uid, Connection connection){
        cache.putIfAbsent(uid, connection);
    }

    public Connection pick(long uid){
        return cache.get(uid);
    }

    public Collection<Connection> pickAll(){
        return cache.values();
    }

    public void logout(long uid){
        cache.remove(uid);
    }
}
